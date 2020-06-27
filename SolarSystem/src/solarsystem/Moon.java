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

/**
 * The class Moon defines a moon which executes pure circular motion about it's
 * host, so the formulas for calculating position are simplified compared to
 * those used for calculating planetary motion about the sun.
 *
 * @see Planet
 */
class Moon extends Satellite {

    /**
     * Constructor.
     *
     * @param mDt The time increment used to integrate the equations of motion.
     * @param mDiameter The diameter of the moon in pixels.
     * @param mRadius The radius of the moon's orbit.
     * @param mHost The host about which the moon orbits.
     */
    public Moon(double mDt, double mDiameter, double mRadius,
            Host mHost) {
        super(mDt, mDiameter, mRadius, Color.lightGray, (double) 1, mHost);
    }

    /**
     * The method translate calculates the motion of the moon. Since moons are
     * only allowed to undergo circular motion, the calculation is simplified
     * compared to planetary motion.
     *
     * @see Planet
     */
    public void translate() {
        double r, theta;
        //
        // Find radial position of moon.
        //
        r = Math.sqrt(getRelPosUnscaled(Coord.X) * getRelPosUnscaled(Coord.X)
                + getRelPosUnscaled(Coord.Y) * getRelPosUnscaled(Coord.Y));
        //
        // Calculate new angular position which is old old angular position
        // plus speed multiplied by delta t. The number 1000 is an arbitrary
        // number chosen to give the moons a convenient angular speed of orbit
        // (it is not related to any othe part of the program).
        //
        theta = getTheta() + Math.sqrt(1000 * host.getMass() / (r * r * r)) * dt;
        //
        // Convert angular position to Cartesian coordiantes.
        //
        xPos = r * Math.cos(theta);
        yPos = r * Math.sin(theta);
    }
}
