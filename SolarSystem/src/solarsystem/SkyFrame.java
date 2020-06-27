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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class provides the frame that contains the GUI interface and the
 * display.
 */
class SkyFrame extends JFrame implements ChangeListener, ActionListener {

    /**
     * The panel that displays the solar system.
     */
    private SkyPanel skyPanel;

    int a[] = new int[10];
    int b = a.length;
    /**
     * The range within which the user may modify the sun's mass.
     */
    private static double maxSunMassMultiplier = 1.5;
    private static double minSunMassMultiplier = 0.5;
    /**
     * The initial mass of the sun (arbitrary units).
     */
    private static int massOfSun = 1000;
    /**
     * The initial size of the frame in pixels.
     */
    private static int xPixels = 1000, yPixels = 1000;
    /**
     * The scale factor used to zoom in and out.
     */
    private static int lengthScaleFactor = 1;
    /**
     * The maximum scale factor allowed for zooming out.
     */
    private static int maxScaleFactor = 5;
    /**
     * The sun for the solar system.
     *
     * @see Sun
     */
    private Sun sun;
    /**
     * The slider that allows the user to modify the sun's mass.
     *
     * @see JSlider
     */
    private JSlider slider;
    /**
     * Button that allows the user to zoom in (i.e. view the solar system in
     * more detail).
     */
    private JButton zoomIn;
    /**
     * Button that allows the user to zoom out (i.e. view the solar system in
     * less detail.
     */
    private JButton zoomOut;
    /**
     * Button that allows the user to display the acceleration vectors of each
     * planet in the solar system.
     */
    private JButton accButton;
    /**
     * Button that allows the user to display the velocity vectors of each
     * planet in the solar system.
     */
    private JButton velButton;
    /**
     * Button that allows the user to display the energy of each planet in the
     * solar system.
     */
    private JButton energyButton;
    /**
     * Button that allows the user to display the previous positions of each
     * planet in the solar system.
     */
    private JButton positionButton;
    /**
     * Button that allows the user to freeze the display of the solar system
     * (i.e. stop the motion of the planets).
     */
    private JButton freezeButton;
    /**
     * String to label button.
     */
    private final String zoomInString = "Zoom In", zoomOutString = "Zoom Out";
    /**
     * String to label button.
     */
    private final String showAcc = "Show Acceleration",
            hideAcc = "Hide Acceleration", showVel = "Show Velocity",
            hideVel = "Hide Velocity";
    /**
     * String to label button.
     */
    private final String showEnergy = "Show Energy",
            hideEnergy = "Hide Energy", showPos = "Trace Position",
            hidePos = "Hide Trace", freeze = "Freeze", unFreeze = "Unfreeze";
    /**
     * Panel in which to put the buttons.
     *
     * @see JPanel
     */
    private JPanel buttonPanel;

    /**
     * Constructs a frame containing the specified number of planets.
     *
     * @param mNumberOfPlanets The number of planets in the solar system.
     * @param dt The time differential for integrating the equations of motion.
     */
    public SkyFrame(int mNumberOfPlanets, double dt) {
        super("Solar System");

        sun = new Sun(75, 1000, xPixels / 2, yPixels / 2);

        setSize(xPixels, yPixels);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        skyPanel = new SkyPanel(dt,
                xPixels,
                yPixels,
                mNumberOfPlanets,
                sun);
        skyPanel.setBackground(Color.black);

        BorderLayout MasterPanelLayout = new BorderLayout();
        Container pane = getContentPane();
        pane.setLayout(MasterPanelLayout);
        pane.add(skyPanel, MasterPanelLayout.CENTER);

        slider = new JSlider(JSlider.VERTICAL,
                (int) (sun.getMass() * minSunMassMultiplier),
                (int) (sun.getMass() * maxSunMassMultiplier),
                (int) sun.getMass());
        slider.setMajorTickSpacing((int) (sun.getMass()
                * (maxSunMassMultiplier
                - minSunMassMultiplier) / 20));
        slider.setMinorTickSpacing((int) (sun.getMass()
                * (maxSunMassMultiplier
                - minSunMassMultiplier) / 40));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        pane.add(slider, MasterPanelLayout.EAST);

        buttonPanel = new JPanel();
        zoomIn = new JButton(zoomInString);
        zoomIn.addActionListener(this);
        zoomIn.setEnabled(false);
        buttonPanel.add(zoomIn);

        zoomOut = new JButton(zoomOutString);
        zoomOut.addActionListener(this);
        zoomOut.setEnabled(true);
        buttonPanel.add(zoomOut);

        accButton = new JButton(showAcc);
        accButton.addActionListener(this);
        buttonPanel.add(accButton);

        velButton = new JButton(showVel);
        velButton.addActionListener(this);
        buttonPanel.add(velButton);

        energyButton = new JButton(showEnergy);
        energyButton.addActionListener(this);
        buttonPanel.add(energyButton);

        positionButton = new JButton(showPos);
        positionButton.addActionListener(this);
        buttonPanel.add(positionButton);

        freezeButton = new JButton(freeze);
        freezeButton.addActionListener(this);
        buttonPanel.add(freezeButton);

        pane.add(buttonPanel, MasterPanelLayout.NORTH);

        setContentPane(pane);
        setVisible(true);
    }

    /**
     * The method actionPerformed implements the action called for by the user.
     *
     * @param event An ActionEvent.
     * @see ActionEvent
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals(zoomOutString)) {
            zoomIn.setEnabled(true);
            ++lengthScaleFactor;
            energyButton.setEnabled(false);
            skyPanel.displayEnergy(false);
            skyPanel.populateStarGroup(lengthScaleFactor);
            if (lengthScaleFactor > maxScaleFactor) {
                zoomOut.setEnabled(false);
            }
        } else if (command.equals(zoomInString)) {
            --lengthScaleFactor;
            if (lengthScaleFactor == 1) {
                zoomIn.setEnabled(false);
                energyButton.setEnabled(true);
                if (energyButton.getText().equals(hideEnergy)) {
                    skyPanel.displayEnergy(true);
                }
            }
            zoomOut.setEnabled(true);
        } else if (command.equals(showAcc)) {
            skyPanel.displayAcc(true);
            accButton.setText(hideAcc);
        } else if (command.equals(showVel)) {
            skyPanel.displayVel(true);
            velButton.setText(hideVel);
        } else if (command.equals(showEnergy)) {
            skyPanel.displayEnergy(true);
            energyButton.setText(hideEnergy);
        } else if (command.equals(showPos)) {
            skyPanel.displayPos(true);
            positionButton.setText(hidePos);
        } else if (command.equals(freeze)) {
            skyPanel.setFreeze(true);
            freezeButton.setText(unFreeze);
        } else if (command.equals(hideAcc)) {
            skyPanel.displayAcc(false);
            accButton.setText(showAcc);
        } else if (command.equals(hideVel)) {
            skyPanel.displayVel(false);
            velButton.setText(showVel);
        } else if (command.equals(hideEnergy)) {
            skyPanel.displayEnergy(false);
            energyButton.setText(showEnergy);
        } else if (command.equals(hidePos)) {
            skyPanel.displayPos(false);
            skyPanel.resetPos();
            positionButton.setText(showPos);
        } else if (command.equals(unFreeze)) {
            skyPanel.setFreeze(false);
            freezeButton.setText(freeze);
        }
    }

    /**
     * Gets the current value of lengthScaleFactor used to calculate position of
     * planets within the display.
     */
    public static int getScaleFactor() {
        return lengthScaleFactor;
    }

    /**
     * Gets the SkyPanel that displays the solar system.
     *
     * @see SkyPanel
     */
    public SkyPanel getPanel() {
        return skyPanel;
    }

    /**
     * Changes the sun's mass according to the input from the slider.
     *
     * @param event ChangeEvent
     * @see ChangeEvent
     */
    public void stateChanged(ChangeEvent event) {
        JSlider source = (JSlider) event.getSource();
        if (source.getValueIsAdjusting() != true) {
            sun.setMass((double) slider.getValue());
        }
    }
}
