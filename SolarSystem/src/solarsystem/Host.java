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


/**
 * The interface host defines the interface needed for an object to serve as a
 * host about which a satellite may orbit.
 */
interface Host {

    /**
     * The method getAbsPos calculates the absolute position of the Host. This
     * is the position used to display the Host and is taken with respect to the
     * upper left corner of the display.
     *
     * @param mAxis The Cartesian axis with respect to which the position is to
     * be calculated.
     * @return double The absolute position of the Satellite with respect to the
     * given axis.
     */
    public double getAbsPos(byte mAxis);

    /**
     * The method getMass returns the mass of the Host in arbitrary units.
     *
     * @return double
     */
    public double getMass();
}
