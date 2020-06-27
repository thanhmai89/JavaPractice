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
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Class Planet extends Satellite @see Satellite and implements the planets that
 * revolve about the Sun @see Sun. A Planet is given an initial position and
 * velocity and follows Newton's laws of motion.
 */
class Planet extends Satellite {

    /**
     * An array of int's used to indicate the previous positions of the planet.
     * Used to plot the trajectory of the planet. The array is filled and the
     * index of the current position is tracked.
     */
    private int[] prevXPos, prevYPos;
    /**
     * Used to index into the position arrays to indicate the current position
     * of the planet.
     */
    private int currPosition = 0;
    /**
     * Counts the number of transaltions the Planet undergoes.
     */
    private int count = 0;
    /**
     * Determines the number of translations between which the position of the
     * Planet is recorded.
     */
    private int recordPosition = 5;
    /**
     * Indicates when we must loop back to the beginning of the previous
     * position array
     */
    private boolean looped = false;
    /**
     * The maximum number of previous positions that are recorded.
     */
    private final int maxPrevPositions = 128;
    /**
     * Total energy of the planet.
     */
    private final double TE;
    /**
     * The actual number of moons orbiting around the planet.
     */
    private final byte numOfMoons;
    /**
     * The maximum number of moons that may orbit around the planet.
     */
    private final byte maxNumOfMoons = 4;
    /**
     * The array of moons that orbit around the planet.
     */
    private Moon[] moon;
    /**
     * The panel in which the planet is displayed.
     */
    private SkyPanel hostPanel;

    /**
     * Constructor.
     *
     * @param mDt The time increment for calculting the motion of the planet.
     * @param mDiameter The diameter of the planet in pixels.
     * @param mRadius The radius of the planet's orbit in pixels.
     * @param mColor The color of the planet. @see Color
     * @param mMass The planet's mass in arbitrary units.
     * @param mHostPanel The panel in which the planet is displayed.
     * @param mHost The Host object about which the planet orbits. @see Host
     */
    public Planet(double mDt,
            double mDiameter,
            double mRadius,
            Color mColor,
            double mMass,
            SkyPanel mHostPanel,
            Host mHost) {
        super(mDt, mDiameter, mRadius, mColor, mMass, mHost);
        hostPanel = mHostPanel;
        prevXPos = new int[maxPrevPositions];
        prevYPos = new int[maxPrevPositions];

        for (int i = 0; i < maxPrevPositions; ++i) {
            prevXPos[i] = prevYPos[i] = 0;
        }

        numOfMoons = (byte) (Math.random() * maxNumOfMoons);

        if (numOfMoons > 0) {
            moon = new Moon[numOfMoons];
            for (int i = 0; i < numOfMoons; ++i) {
                moon[i] = new Moon(dt, // Time increment for calculating motion.
                        Math.random() * 6 + 3, // Moon's diameter.
                        diameter * (1 + 0.2 * Math.random()),// Radius of orbit.
                        this); // Host about which moon orbits.
            }
        }

        double distToHost = Math.sqrt(getRelPosUnscaled(Coord.X)
                * getRelPosUnscaled(Coord.X)
                + getRelPosUnscaled(Coord.Y)
                * getRelPosUnscaled(Coord.Y));
        //
        // For calculating the force due to gravity, we set the universal
        // gravitational constant to one (or incorporate it into the sun's
        // mass).
        //
        double forceDueToGravity = host.getMass() * mass
                / (distToHost * distToHost);
        double theta = getTheta();
        //
        // The following formula for the velocity ensures that the
        // planet's initial motion about the sun is circular.
        //
        yVel = Math.sqrt(host.getMass() / distToHost)
                * Math.sin(theta + Math.PI / 2);
        xVel = Math.sqrt(host.getMass() / distToHost)
                * Math.cos(theta + Math.PI / 2);

        xAcc = forceDueToGravity * Math.cos(theta + Math.PI) / mass;
        yAcc = forceDueToGravity * Math.sin(theta + Math.PI) / mass;
        //
        // Record the initial total energy of planet.
        //
        TE = mass * (xVel * xVel + yVel * yVel) / 2
                + mass * Math.sqrt(xAcc * xAcc + yAcc * yAcc) * distToHost;
    }

    /**
     * The method translate calculates the motion of the planet by numerically
     * integrating Newton's laws of motion.
     */
    public void translate() {
        double force, distToHost, theta;

        for (int i = 0; i < 1 / dt; ++i) {
            distToHost = Math.sqrt(getRelPosUnscaled(Coord.X)
                    * getRelPosUnscaled(Coord.X)
                    + getRelPosUnscaled(Coord.Y)
                    * getRelPosUnscaled(Coord.Y));

            force = mass * host.getMass() / (distToHost * distToHost);
            theta = getTheta();
            //
            // Calculate new acceleration.
            //
            xAcc = force * Math.cos(theta + Math.PI) / mass;
            yAcc = force * Math.sin(theta + Math.PI) / mass;
            //
            // Calculate new position using the original velocity.
            //
            xPos += xVel * dt + dt * dt * xAcc / 2;
            yPos += yVel * dt + dt * dt * yAcc / 2;
            //
            // Calculate new velocity.
            //
            xVel += xAcc * dt;
            yVel += yAcc * dt;

            if (hostPanel.showPos()) {
                recordPosition();
            }
            //
            // Translate the moons orbiting around the planet.
            //
            for (int j = 0; j < numOfMoons; ++j) {
                moon[j].translate();
            }
        }
    }

    /**
     * The method recordPosition records the current position of the planet in
     * the arrays prevXPos and prevYPos.
     */
    private void recordPosition() {
        if (++count > (int) (recordPosition / dt)) {
            count = 0;
            if (currPosition == maxPrevPositions) {
                currPosition = 0;
                looped = true;
            }

            prevXPos[currPosition] = (int) getRelPosUnscaled(Coord.X);
            prevYPos[currPosition++] = (int) getRelPosUnscaled(Coord.Y);
        }
    }

    /**
     * The method getKE returns the kinetic energy of the planet in arbitrary
     * units.
     */
    public double getKE() {
        return mass * (xVel * xVel + yVel * yVel) / 2;
    }

    /**
     * The method getPE returns the potential energy of the planet in arbitrary
     * units.
     */
    public double getPE() {
        return mass * Math.sqrt(xAcc * xAcc + yAcc * yAcc)
                * Math.sqrt(xPos * xPos + yPos * yPos);
    }

    /**
     * The method getTE returns the total energy of the planet in arbitrary
     * units.
     */
    public double getTE() {
        return TE;
    }

    /**
     * The method draw displays the Planet in scaled units of pixels.
     *
     * @param comp2D The Graphics2D object in which to display the planet.
     * @see Graphics2D
     */
    public void draw(Graphics2D comp2D) {
        double zoomedDiameter = diameter / SkyFrame.getScaleFactor();
        //
        // Draw the night-time semi-circle of the planet.
        //
        comp2D.setColor(Color.darkGray);
        comp2D.fill(semiCircle(getAbsPos(Coord.X),
                getAbsPos(Coord.Y),
                zoomedDiameter,
                getTheta() + 3 * Math.PI / 2));
        //
        // Draw the day-time semi-circle of the planet.
        //
        comp2D.setColor(color);
        comp2D.fill(semiCircle(getAbsPos(Coord.X),
                getAbsPos(Coord.Y),
                zoomedDiameter,
                getTheta() + Math.PI / 2));
        //
        // Show previous positions of the planet if requested by hostPanel.
        //
        if (hostPanel.showPos()) {
            drawPrevPositions(comp2D);
        }
        //
        // Draw the moons orbiting around the planet.
        //
        for (int i = 0; i < numOfMoons; ++i) {
            moon[i].draw(comp2D);
        }
    }

    /**
     * The method drawPrevPositions draws the previous positions of the planet
     * by placing dots to indicate it's path.
     *
     * @param comp2D The Graphics2D object used for the display.
     */
    private void drawPrevPositions(Graphics2D comp2D) {
        int end;
        int x, y;
        if (looped) {
            end = maxPrevPositions;
        } else {
            end = currPosition;
        }

        for (int i = 0; i < end; ++i) {
            x = prevXPos[i] / SkyFrame.getScaleFactor()
                    + (int) host.getAbsPos(Coord.X);
            y = prevYPos[i] / SkyFrame.getScaleFactor()
                    + (int) host.getAbsPos(Coord.Y);
            comp2D.fill(new Ellipse2D.Float(x, y, 1, 1));
        }
    }

    /**
     * The method erasePrevPostions removes the dots indicating the previous
     * position of the planet from the screen.
     */
    public void erasePrevPositions() {
        looped = false;
        currPosition = 0;
        count = 0;
    }

    /**
     * The method semiCircle is used to draw a day-time and night-time
     * semi-circle of the planet with respect to the Host about which the planet
     * orbits.
     *
     * @param x The scaled horizontal positon of the planet with respect to the
     * Host about which the planet orbits.
     * @param y The scaled vertical positon of the planet with respect to the
     * Host about which the planet orbits.
     * @param dia The scaled diameter of the planet.
     * @param theta The angular position of the planet.
     * @return Area A semi-circular area used to draw the day-time or night-time
     * part of the planet.
     */
    private Area semiCircle(double x, double y, double dia, double theta) {
        double xp = x - dia / 2;
        double yp = y - dia / 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(xp, yp, dia, dia);
        Rectangle2D.Double rect = new Rectangle2D.Double(xp, yp, dia, dia / 2);
        Area semiCircleArea = new Area(circle);
        Area rectArea = new Area(rect);
        semiCircleArea.subtract(rectArea);
        semiCircleArea.transform(AffineTransform.
                getRotateInstance(theta, x, y));
        return semiCircleArea;
    }
}
