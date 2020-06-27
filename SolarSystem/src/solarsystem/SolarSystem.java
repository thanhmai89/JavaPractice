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
 * Main entry into program. Creates GUI and repaints it in infinite loop.
 */
public class SolarSystem {

    /**
     * The time increment for numerical integration of differential equations
     * governing motion in the solar system.
     */
    private static double dt = 0.001;

    /**
     * Default constructor.
     */
    public SolarSystem() {
    }

    /**
     * Main entry point into program. User may input number of planets on
     * command line.
     */
    public static void main(String[] arguments) {
        SkyFrame skyFrame;
        int numOfPlanets = 9;

        if (arguments.length == 0) {
            skyFrame = new SkyFrame(numOfPlanets, dt);
//            skyFrame = new SkyFrame(numOfPlanets, dt);
        } else {
            if (arguments[0].equals("h")) {
                usage();
                System.exit(1);
                skyFrame = new SkyFrame(0, 0);
            }
            try {
                numOfPlanets = Integer.parseInt(arguments[0]);
                numOfPlanets = (int) Math.abs(numOfPlanets);
                skyFrame = new SkyFrame(numOfPlanets, 0);
            } catch (Exception e) {
                System.err.println(e.toString());
                usage();
                System.exit(0);
                skyFrame = new SkyFrame(0, 0);
            }
        }

        while (true) {
            skyFrame.getPanel().repaint();
        }
    }

    /**
     * The method getColor is used by the sattelites of the solar system to get
     * their color.
     *
     * @param i Integer that determines the color that is returned.
     * @return Color
     */
    public static Color getColor(int i) {
        Color color;
        int j = i % 8;

        switch (j) {
            case 0:
                color = Color.red;
                break;
            case 1:
                color = Color.blue;
                break;
            case 2:
                color = Color.green;
                break;
            case 3:
                color = Color.pink;
                break;
            case 4:
                color = Color.orange;
                break;
            case 5:
                color = Color.cyan;
                break;
            case 6:
                color = Color.magenta;
                break;
            case 7:
                color = Color.lightGray;
                break;
            default:
                color = Color.gray;
        }
        return color;
    }

    /**
     * The method usage prints a message to the console instructing the use on
     * how to use the program.
     */
    private static void usage() {
        String explaination
                = "This program displays a simulated solar system in 2 dimensions.\n"
                + "Buttons along the top of the GUI allow the user to show or hide\n"
                + "various physical properties of the planets. These properties are\n"
                + "the acceleration and velocity vectors, the percent kinetic and potential\n"
                + "energy, and the trajectory the planet has taken. In addition, two\n"
                + "buttons allow the user to zoom in or out for better viewing. Finally,\n"
                + "there is a sliding scale on the right of the GUI that allows the user\n"
                + "to alter the mass of the sun.";

        System.out.println("Usage :\n"
                + "java SolarSystem [options] <number of planets>\n"
                + "\n"
                + "options :\n"
                + "h\t\tprint this message\n\n"
                + explaination);
    }
}
