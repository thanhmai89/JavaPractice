/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solarsystem;

/**
 *
 * @author Tước Thức
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.Vector;
import javax.swing.JPanel;

/**
 * Contains the sun, planets and moons of the simulated solar system.
 *
 * @see JPanel
 */
class SkyPanel extends JPanel {

    /**
     * The initial size of the panel in pixels
     */
    private int xMaxPixels, xMinPixels, yMaxPixels, yMinPixels;
    /**
     * Minimum and maximum planet diameter in pixels.
     */
    private double minPlanetDia = 20, maxPlanetDia = 60;
    /**
     * The maximun number of orbits allowed in solar system.
     */
    private final int numberOfPlanets;
    /**
     * Number of stars in the background.
     */
    private final int numStars = 30;
    /**
     * The maximum number of orbits (i.e. planets) allowed in the solar system.
     */
    private final int maxNumberOfOrbits;
    /**
     * A boolean array indicating if orbit indexed is occupied by a planet.
     */
    private boolean[] orbitOccupied;
    /**
     * An array of planet objects. @see Planet
     */
    private Planet[] planet;
    /**
     * Boolean variables indicating whether or not to display energy, velocity,
     * acceleration or position. Variable freeze is set to true to stop the
     * display from updating.
     */
    private boolean displayEnergy = false, displayVel = false, displayAcc = false,
            displayPos = false, freeze = false;
    /**
     * A vector containing Stars objects.
     *
     * @see Stars
     */
    private Vector starGroup;
    /**
     * Sun object.
     *
     * @see Sun
     */
    private Sun sun;

    /**
     * SkyPanel constructor. The constructor calculates the maximum number of
     * planets that will be visible given mMaxXPosition, and then determines the
     * maximum number of orbits that are needed. It then creates an array of
     * planets and places each planet at random within the orbits. Finally, it
     * creates a star group which is displayed for the default view of the solar
     * system (more star groups are created as needed when/if the user zooms out
     * for a wider view of the solar system).
     *
     * @param dt Time increment for numerical integration of differential
     * equations of motion.
     * @param mMaxXPosition Maximum value of X coordinate allowed (in pixels).
     * @param mMaxYPosition Maximum value of Y coordinate allowed (in pixels).
     * @param mNumberOfPlanets Number of planets in solar system.
     * @param mSun Reference to Sun object of solar system.
     * @see Sun
     */
    public SkyPanel(double dt,
            int mMaxXPosition,
            int mMaxYPosition,
            int mNumberOfPlanets,
            Sun mSun) {
        xMinPixels = 0;
        xMaxPixels = mMaxXPosition;
        yMinPixels = 0;
        yMaxPixels = mMaxYPosition;
        numberOfPlanets = mNumberOfPlanets;
        sun = mSun;

        int numVisibleOrbits = (int) (((double) xMaxPixels
                / sun.getAbsPos(Coord.X)
                - sun.getDiameter() / 2) / maxPlanetDia);

        maxNumberOfOrbits = numVisibleOrbits > numberOfPlanets
                ? numVisibleOrbits
                : numberOfPlanets;

        orbitOccupied = new boolean[maxNumberOfOrbits];
        for (int i = 0; i < maxNumberOfOrbits; ++i) {
            orbitOccupied[i] = false;
        }

        planet = new Planet[numberOfPlanets];

        for (int i = 0; i < numberOfPlanets; ++i) {
            double diameter = minPlanetDia + Math.random()
                    * (maxPlanetDia - minPlanetDia);
            planet[i] = new Planet(dt, // Time increment for calculating motion.
                    diameter, // Diameter of planet
                    getUnoccupiedOrbit(), // Radius of orbit for planet
                    SolarSystem.getColor(i), // Color of planet
                    diameter / maxPlanetDia, // Mass of planet
                    this, // Host panel
                    sun);
        }

        starGroup = new Vector(1);
        populateStarGroup(1);
    }

    /**
     * Creates a new star group if needed for the given lengthScaleFactor.
     *
     * @param mLengthScaleFactor The scale factor which determines sizing and
     * distances for positioning objects in display. This allows user to zoom in
     * or out.
     */
    public void populateStarGroup(int mLengthScaleFactor) {
        //
        // Check to see if we have already made a star group for
        // this zoom factor.
        //
        if (starGroup.size() >= mLengthScaleFactor) {
            return;
        }
        //
        // If not, make a new star group.
        //

        Stars stars = new Stars(numStars / SkyFrame.getScaleFactor(), // number of stars
                SkyFrame.getScaleFactor() * (xMaxPixels
                - sun.getAbsPos(Coord.X)), // maximum distance from sun
                (SkyFrame.getScaleFactor() - 1) * (xMaxPixels
                - sun.getAbsPos(Coord.X)), // minimum distance from sun

                sun);
        starGroup.addElement(stars);
    }

    /**
     * Searches randomly among the allowed orbits until it finds an unoccupied
     * orbit.
     *
     * @return double The intial radius of the unoccupied orbit.
     */
    private double getUnoccupiedOrbit() {
        int trialOrbitNum = (int) (Math.random() * maxNumberOfOrbits);

        while (true) {
            if (!orbitOccupied[trialOrbitNum]) {
                orbitOccupied[trialOrbitNum] = true;
                break;
            } else {
                trialOrbitNum = (int) (Math.random() * maxNumberOfOrbits);
            }
        }

        return 1.5 * sun.getDiameter() + trialOrbitNum * maxPlanetDia;
    }

    /**
     * Repaints the display.
     *
     * @param comp A Graphics object.
     * @see Graphics
     */
    public void paintComponent(Graphics comp) {
        super.paintComponent(comp);
        Graphics2D comp2D = (Graphics2D) comp;

        sun.draw(comp2D);
        //
        // Only draw the star groups that are being displayed (i.e.
        // are within the current scale factor).
        //
        for (int i = 0; i < SkyFrame.getScaleFactor(); ++i) {
            ((Stars) starGroup.get(i)).draw(comp2D);
        }

        for (int i = 0; i < numberOfPlanets; ++i) {
            if (!freeze) {
                planet[i].translate();
            }
            planet[i].draw(comp2D);
            if (displayVel) {
                drawVelocityVector(comp2D, i);
            }
            if (displayAcc) {
                drawAccelerationVector(comp2D, i);
            }
            if (displayEnergy) {
                drawEnergy(comp2D, i);
            }
        }
    }

    /**
     * Prints the kinetic energy of the planet indicated by the parameter i as a
     * percent of the total energy (kinetic + potential) of the planet.
     *
     * @param comp2D A 2D graphics object.
     * @see Graphics2D
     * @param i Index of planet for which to display the kinetic energy.
     */
    private void drawEnergy(Graphics2D comp2D, int i) {
        comp2D.setColor(Color.white);

        //
        // Print kinetic energy as percent of total energy.
        //
        String percentKE = Integer.toString((int) (100 * planet[i].getKE()
                / (planet[i].getKE()
                + planet[i].getPE())));
        comp2D.drawString("KE% " + percentKE,
                (int) planet[i].getAbsPos(Coord.X) + 50,
                (int) planet[i].getAbsPos(Coord.Y) + 70);
        //
        // Print potential energy as percent of total energy.
        //
        String percentPE = Integer.toString((int) (100 * planet[i].getPE()
                / (planet[i].getKE()
                + planet[i].getPE())));
        comp2D.drawString("PE% " + percentPE,
                (int) planet[i].getAbsPos(Coord.X) + 50,
                (int) planet[i].getAbsPos(Coord.Y) + 82);
    }

    /**
     * Displays the velocity vector of the planet indexed by the parameter i.
     *
     * @param comp2D A 2D graphics object.
     * @see Graphics2D
     * @param i Index of planet for which to display the kinetic energy.
     */
    private void drawVelocityVector(Graphics2D comp2D, int i) {
        float xPos = (float) planet[i].getAbsPos(Coord.X);
        float yPos = (float) planet[i].getAbsPos(Coord.Y);
        float xVel = xPos + 25 * (float) planet[i].getVel(Coord.X)
                / (float) SkyFrame.getScaleFactor();
        float yVel = yPos + 25 * (float) planet[i].getVel(Coord.Y)
                / (float) SkyFrame.getScaleFactor();

        comp2D.setColor(Color.white);
        comp2D.draw(new Line2D.Float(xPos, yPos, xVel, yVel));
        drawArrow(comp2D, xPos, yPos, xVel, yVel);
    }

    /**
     * Displays the acceleration vector for the planet indexed by the parameter
     * i.
     *
     * @param comp2D A 2D graphics object.
     * @see Graphics2D
     * @param i Index of planet for which to display the kinetic energy.
     */
    private void drawAccelerationVector(Graphics2D comp2D, int i) {
        double xPos = planet[i].getAbsPos(Coord.X);
        double yPos = planet[i].getAbsPos(Coord.Y);
        double xAcc = xPos + 2000 * planet[i].getAcc(Coord.X)
                / SkyFrame.getScaleFactor();
        double yAcc = yPos + 2000 * planet[i].getAcc(Coord.Y)
                / SkyFrame.getScaleFactor();

        comp2D.setColor(Color.white);
        comp2D.draw(new Line2D.Float((float) xPos,
                (float) yPos,
                (float) xAcc,
                (float) yAcc));
        drawArrow(comp2D,
                (float) xPos,
                (float) yPos,
                (float) xAcc,
                (float) yAcc);
    }

    /**
     * Draws an arrow (vector) from (x0, y0) to (x1, y1).
     *
     * @param comp2D A 2D graphics object.
     * @see Graphics2D
     * @param x0 X coordinate of base of arrow.
     * @param y0 Y coordinate of base of arrow.
     * @param x1 X coordinate of tip of arrow.
     * @param y1 Y coordinate of tip of arrow.
     */
    private void drawArrow(Graphics2D comp2D,
            float x0,
            float y0,
            float x1,
            float y1) {
        float dx = x1 - x0, dy = y1 - y0;
        float r = (float) Math.sqrt(dx * dx + dy * dy);
        float len = 15 / (int) SkyFrame.getScaleFactor();
        int[] x, y;
        int numPts = 4;

        x = new int[numPts];
        y = new int[numPts];
        x[0] = (int) x1;
        y[0] = (int) y1;
        x[1] = (int) (x1 - len * dy / (2 * r));
        y[1] = (int) (y1 + len * dx / (2 * r));
        x[2] = (int) (x1 + len * dx / r);
        y[2] = (int) (y1 + len * dy / r);
        x[3] = (int) (x1 + len * dy / (2 * r));
        y[3] = (int) (y1 - len * dx / (2 * r));

        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, numPts);
        path.moveTo(x[0], y[0]);
        for (int i = 1; i < numPts; ++i) {
            path.lineTo(x[i], y[i]);
        }
        path.closePath();
        comp2D.fill(path);
    }

    /**
     * Set method to set boolean variable displayAcc.
     *
     * @param value Boolean value, true = display acceleration vector, false =
     * do not display acceleration vector.
     */
    public void displayAcc(boolean value) {
        displayAcc = value;
    }

    /**
     * Set method to set boolean variable displayVel.
     *
     * @param value Boolean value, true = display velocity vector, false = do
     * not display velocity vector.
     */
    public void displayVel(boolean value) {
        displayVel = value;
    }

    /**
     * Set method to set boolean variable displayEnergy.
     *
     * @param value Boolean value, true = display kinetic energy, false = do not
     * display kinetic energy.
     */
    public void displayEnergy(boolean value) {
        displayEnergy = value;
    }

    /**
     * Set method to set boolean variable displayPos.
     *
     * @param value Boolean value, true = display trace of spatial position,
     * false = do not display trace of spatial position.
     */
    public void displayPos(boolean value) {
        displayPos = value;
    }

    /**
     * Get method to get the value of the boolean variable displayPos.
     *
     * @return displayPos Boolean value. true = diplay trace of previous spatial
     * positions of planets. false = do not display trace of previous spatial
     * positions of planets.
     */
    public boolean showPos() {
        return displayPos;
    }

    /**
     * Erases traces of previous spatial positions of planets.
     */
    public void resetPos() {
        for (int i = 0; i < numberOfPlanets; ++i) {
            planet[i].erasePrevPositions();
        }
    }

    /**
     * A set method to set the boolean variable freeze. true = do not translate
     * planets. false = translate planets according to normal physical laws of
     * motion.
     */
    public void setFreeze(boolean value) {
        freeze = value;
    }

    /**
     * Get method to retrieve the maximum diameter allowed for planets. return
     * double Maximum planet diameter (in pixels).
     */
    public double getMaxPlanetDiameter() {
        return maxPlanetDia;
    }
}
