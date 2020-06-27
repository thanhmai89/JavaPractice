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
 * The abstract class Satellite defines satellites that orbit about a Sun.
 *
 * @see Sun
 */
abstract class Satellite implements Host {

    /**
     * The x and y coordinates of the Satellite's position.
     */
    protected double xPos, yPos;
    /**
     * The x and y components of the Satellite's velocity.
     */
    protected double xVel = 0, yVel = 0;
    /**
     * The x and y components of the Satellite's acceleration.
     */
    protected double xAcc = 0, yAcc = 0, dt;
    /**
     * The diameter of the Satellite.
     */
    protected double diameter;
    /**
     * The mass of the Satellite (arb. units).
     */
    protected double mass;
    /**
     * The color of the Satellite
     */
    protected final Color color;
    /**
     * The Host about which the Satellite orbits.
     *
     * @see Host
     */
    protected Host host;

    /**
     * Constructor for Satellite objects.
     *
     * @param mDt The time increment for calculating the motion.
     * @param mDiameter The diameter of the satellite (arb. units).
     * @param mRadius The initial radius of the Satellite's orbit.
     * @param mMass The mass of the Satellite (arb. units).
     * @param mHost The Host about which the Satellite orbits.
     * @see Host
     */
    public Satellite(double mDt,
            double mDiameter,
            double mRadius,
            Color mColor,
            double mMass,
            Host mHost) {
        dt = mDt;
        diameter = mDiameter;
        mass = mMass;
        color = mColor;
        host = mHost;
        //
        // The satellite's coordinates (xPos, yPos) relative to the
        // coordinates of the host satellite.
        //
        xPos = Math.random() * mRadius;
        yPos = Math.sqrt(mRadius * mRadius - xPos * xPos);
        //
        // Choose at random from the four Cartesian quadrants for the
        // intial position.
        //
        if (probOneHalf()) {
            xPos *= -1;
        }
        if (probOneHalf()) {
            yPos *= -1;
        }
    }

    /**
     * The method probOneHalf returns true with probability 1/2, otherwise
     * returns false.
     *
     * @return boolean
     */
    protected boolean probOneHalf() {
        return Math.random() > 0.5;
    }

    /**
     * The method getAbsPos calculates the absolute position of the Satellite.
     * This is the position used to display the Satellite and is taken with
     * respect to the upper left corner of the display.
     *
     * @param mAxis The cartesian axis with respect to which the position is to
     * be calculated.
     * @return double The absolute position of the Satellite with respect to the
     * given axis.
     */
    public double getAbsPos(byte mAxis) {
        if (mAxis == Coord.X) {
            return xPos / SkyFrame.getScaleFactor() + host.getAbsPos(mAxis);
        } else {
            return yPos / SkyFrame.getScaleFactor() + host.getAbsPos(mAxis);
        }
    }

    /**
     * The getRelPosScaled calculate the relative position of the Satellite
     * scaled by the scale factor of the SkyFrame.
     *
     * @see SkyFrame
     * @param mAxis The cartesian axis with respect to which the position is to
     * be calculated.
     * @return double The relative position of the Satellite scaled and with
     * respect to the given axis.
     */
    protected double getRelPosScaled(byte mAxis) {
        if (mAxis == Coord.X) {
            return xPos / SkyFrame.getScaleFactor();
        } else {
            return yPos / SkyFrame.getScaleFactor();
        }
    }

    /**
     * The method getRelPosUnscaled calculates the relative position of the
     * Satellite without scaling.
     *
     * @param mAxis The cartesian axis with respect to which to calculate the
     * position.
     * @return double The relative position of the Satellite with respect to the
     * given axis.
     */
    protected double getRelPosUnscaled(byte mAxis) {
        if (mAxis == Coord.X) {
            return xPos;
        } else {
            return yPos;
        }
    }

    /**
     * The method getTheta returns the angular position of the Satellite. The
     * angle is measured clockwise from the positive horizontal axis, with the
     * Sun at the origin.
     *
     * @return double The angular position in radians.
     */
    public double getTheta() {
        double theta = Math.atan(yPos / xPos);
        if (xPos < 0) {
            theta = Math.PI + theta;
        } else if (xPos > 0 && yPos < 0) {
            theta = 2 * Math.PI + theta;
        }
        return theta;
    }

    /**
     * The method getVel calculates a Cartesian component of the Satellite's
     * velocity (arb. units).
     *
     * @param mAxis The cartesian component of the velocity to calculate.
     * @return double A cartesian component of the velocity (arb. units).
     */
    public double getVel(byte mAxis) {
        if (mAxis == Coord.X) {
            return xVel;
        } else {
            return yVel;
        }
    }

    /**
     * The method getAcc calculates a Cartesian component of the Satellite's
     * acceleration (arb. units).
     *
     * @param mAxis The cartesian component of the acceleration to calculate.
     * @return double A cartesian component of the acceleration (arb. units).
     */
    public double getAcc(byte mAxis) {
        if (mAxis == Coord.X) {
            return xAcc;
        } else {
            return yAcc;
        }
    }

    /**
     * The abstract method translate calculates the displacement of the
     * Satellite as a function of time and updates the Satellite's coordinates
     * xPos and yPos.
     */
    public abstract void translate();

    /**
     * The method getDiameter returns the Satellite's diameter.
     *
     * @return double.
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * The method getColor returns the Satellite's color.
     *
     * @return Color
     * @see Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * The method getMass returns the Satellite's mass (arb. units).
     *
     * @return double
     */
    public double getMass() {
        return mass;
    }

    /**
     * The method draw displays the Satellite.
     *
     * @param comp2D The Graphics2D object in which to display the Satellite.
     * @see Graphics2D
     */
    public void draw(Graphics2D comp2D) {
        double zoomedDiameter = diameter / SkyFrame.getScaleFactor();
        Ellipse2D.Double form
                = new Ellipse2D.Double((int) (getAbsPos(Coord.X) - zoomedDiameter / 2),
                        (int) (getAbsPos(Coord.Y) - zoomedDiameter / 2),
                        (int) zoomedDiameter,
                        (int) zoomedDiameter);
        comp2D.setColor(color);
        comp2D.fill(form);
    }
}
