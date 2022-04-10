package Assignments.A10;

import javax.swing.*;
import java.awt.*;

public class OptionPanel extends JPanel {
    private final JSlider turtleSpeedSlider;
    private final JSlider hareSpeedSlider;
    private final JSlider sleepTimeSlider;
    private JLabel turtleValue;
    private JLabel hareValue;
    private JLabel sleepTimeValue;
    public OptionPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> start()); // when start button is pressed
        turtleSpeedSlider = new JSlider(0,300,100);
        hareSpeedSlider = new JSlider(0,300,50);
        sleepTimeSlider = new JSlider(0,9999,2000);

        turtleSpeedSlider.addChangeListener(e -> turtleValue.setText(turtleSpeedSlider.getValue() + ""));
        hareSpeedSlider.addChangeListener(e -> hareValue.setText(hareSpeedSlider.getValue() + ""));
        sleepTimeSlider.addChangeListener(e -> sleepTimeValue.setText(sleepTimeSlider.getValue() + ""));

        turtleValue = new JLabel(turtleSpeedSlider.getValue() + "");
        hareValue = new JLabel(hareSpeedSlider.getValue() + "");
        sleepTimeValue = new JLabel(sleepTimeSlider.getValue() + "");

        addSlider(turtleSpeedSlider, "Turtle Speed (ms)", turtleValue);
        addSlider(hareSpeedSlider, "Hare Speed (ms)  ", hareValue);
        addSlider(sleepTimeSlider, "Sleep Time (ms)   ", sleepTimeValue);

        add(Box.createRigidArea(new Dimension(140,10))); // used for centering the start button
        add(startButton);

    }

    private void addSlider(JSlider s, String description, JLabel label) { // helper function to add slider
        JPanel panel = new JPanel();
        panel.add(new JLabel(description, JLabel.LEFT));
        panel.add(s);
        panel.add(label);
        add(panel);
    }

    private static JFrame raceFrame;
    public static RaceManager raceManager;
    private void start() { // on start button press
        frame.setVisible(false); // make option frame invisible since options are locked in
        raceFrame = new JFrame("Race");
        raceFrame.setSize(1040,300);
        raceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        raceFrame.setLocationRelativeTo(null);
        raceFrame.setResizable(false);
        raceFrame.setLayout(new BorderLayout());
        raceManager = new RaceManager(hareSpeedSlider.getValue(), turtleSpeedSlider.getValue(), sleepTimeSlider.getValue());
        raceFrame.add(raceManager, BorderLayout.CENTER);
        raceFrame.setVisible(true);
    }

    public static void onConclusion(int winnerID) { // when race finishes
        String winnerName = winnerID == 0 ? "Hare" : "Turtle";
        int i = JOptionPane.showConfirmDialog(raceFrame, winnerName + " won! Would you like to restart?", "Race Ended", JOptionPane.YES_NO_OPTION);
        if (i == 1){
            System.exit(0);
        }
        else{
            raceFrame.dispose();
            frame.setVisible(true); // make option frame visible for next run
        }
    }

    private static JFrame frame;
    public static void main(String[] args) {
        frame = new JFrame("Options");
        frame.setSize(370,170);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new OptionPanel());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
