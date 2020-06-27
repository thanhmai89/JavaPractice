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
import java.awt.geom.Ellipse2D;


/**
 * Class Stars defines a group of stars that are displayed at random locations.
 * The stars are yellow circles that change diameter to simulate twinlking.
 *
 */
class Stars {

    /**
     * Coordinates of the stars relative to the Sun's position.
     */
    private double[] xPos, yPos;
    /**
     * Initial diameter of the stars.
     */
    private double diameter = 5;
    /**
     * Number of stars.
     */
    private int numOfStars;
    /**
     * The Sun at the center of the solar system.
     */
    private Sun sun;

    /**
     * Constructor for creating a group of stars.
     *
     * @param mNumOfStars The number of stars to create.
     * @param maxRadius The maximum distance from the sun that a star may be
     * positioned.
     * @param minRadius The minimum distance from the sun that a star may be
     * positioned.
     * @param mSun The Sun object which defines the center of the solar system.
     */
    public Stars(int mNumOfStars,
            double maxRadius,
            double minRadius,
            Sun mSun) {
        numOfStars = (int) Math.abs(mNumOfStars);
        xPos = new double[numOfStars];
        yPos = new double[numOfStars];
        maxRadius = Math.abs(maxRadius);
        minRadius = Math.abs(minRadius);
        sun = mSun;

        if (minRadius == 0) {
            minRadius += sun.getDiameter();
        }
        //
        // Chose positions for each star at random between max and min radius.
        //
        for (int i = 0; i < numOfStars; ++i) {
            xPos[i] = (float) Math.random() * maxRadius;
            float yMin = xPos[i] > minRadius
                    ? 0
                    : (float) Math.sqrt(minRadius * minRadius - xPos[i] * xPos[i]);
            float yMax = (float) Math.sqrt(maxRadius * maxRadius
                    - xPos[i] * xPos[i]);
            yPos[i] = (float) Math.random() * (yMax - yMin) + yMin;
            if (Math.random() > 0.5) {
                xPos[i] *= -1;
            }
            if (Math.random() > 0.5) {
                yPos[i] *= -1;
            }
        }
    }

    /**
     * The method draw displays the stars.
     *
     * @param comp2D The Graphics2D object in which to display the stars.
     */
    public void draw(Graphics2D comp2D) {
        comp2D.setColor(Color.yellow);

        //
        // Make stars twinkle by chosing random diameter for each star.
        //
        for (int i = 0; i < numOfStars; ++i) {
            double actualDia = diameter * Math.random();

            Ellipse2D.Double starShape
                    = new Ellipse2D.Double(xPos[i]
                            / SkyFrame.getScaleFactor()
                            + sun.getAbsPos(Coord.X),
                            yPos[i] / SkyFrame.getScaleFactor()
                            + sun.getAbsPos(Coord.Y),
                            actualDia,
                            actualDia);
            comp2D.fill(starShape);
        }
    }
}
