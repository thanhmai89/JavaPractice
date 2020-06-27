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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * The class Sun extends Satellite @see Satellite and defines the origin about
 * which all planets orbit in the solar system. It is yellow and has rays of
 * random length eminatting from it.
 */
class Sun extends Satellite {

    /**
     * Constructor.
     *
     * @param mDiameter The Diameter of the sun in pixels.
     * @param mMass The mass of the sun in arbitrary units.
     * @param mXPos The position of the sun on the horizontal axis.
     * @param mYPos The position of the sun on the vertical axis.
     */
    public Sun(double mDiameter,
            double mMass,
            double mXPos,
            double mYPos) {
        super((double) 1,
                mDiameter,
                (double) 0,
                Color.yellow,
                mMass,
                (Host) null);

        xPos = mXPos;
        yPos = mYPos;
    }

    /**
     * The method getAbsPos returns the absolute position of the Sun in pixels.
     *
     * @param mAxis The cartesian axis of interest.
     * @return double The absolute positon of the Sun in pixels.
     */
    public double getAbsPos(byte mAxis) {
        if (mAxis == Coord.X) {
            return xPos;
        } else {
            return yPos;
        }
    }

    /**
     * The methos setMass allows the user to set the mass of the sun in order to
     * observe the effects on planetary motion. The mass is measured in arbitray
     * units.
     *
     * @param newMass The new mass of the sun in arbitrary units.
     */
    public void setMass(double newMass) {
        diameter *= newMass / mass;
        mass = newMass;
    }

    /**
     * The method draw displays the Sun in scaled units of pixels.
     *
     * @param comp2D The Graphics2D object in which to display the Sun.
     * @see Graphics2D
     */
    public void draw(Graphics2D comp2D) {
        super.draw(comp2D);

        BasicStroke pen = new BasicStroke(2F);
        comp2D.setStroke(pen);

        double xf, yf;

        double zoomedDiameter = diameter / SkyFrame.getScaleFactor();
        //
        // Draw rays of sun.
        //
        for (int i = 0; i < 10; ++i) {
            if (probOneHalf()) {
                xf = xPos + Math.random() * zoomedDiameter * 1.5;
            } else {
                xf = xPos - Math.random() * zoomedDiameter * 1.5;
            }

            if (probOneHalf()) {
                yf = yPos + Math.random() * zoomedDiameter * 1.5;
            } else {
                yf = yPos - Math.random() * zoomedDiameter * 1.5;
            }

            Line2D.Double ray = new Line2D.Double(xPos,
                    yPos,
                    xf,
                    yf);
            comp2D.draw(ray);
        }
    }

    public void translate() {
    } // Sun does not move.
}
