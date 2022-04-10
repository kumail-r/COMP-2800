package Assignments.A04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame {
    private final RaceFrame raceFrame;
    private final JSlider turtleSpeedSlider;
    private final JSlider hareSpeedSlider;
    private final JSlider sleepTimeSlider;
    private JLabel turtleValue;
    private JLabel hareValue;
    private JLabel sleepTimeValue;

    private final Timer hareTimer;
    private final Timer turtleTimer;
    private final Timer sleepTimer;

    public MainFrame(){
        super("Main Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370,140);
        setLocationRelativeTo(null);
        setLocation(getX(),getY()+200);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setResizable(false);
        turtleSpeedSlider = new JSlider(0,300,100);
        hareSpeedSlider = new JSlider(0,300,50);
        sleepTimeSlider = new JSlider(0,10000,2000);

        raceFrame = new RaceFrame();
        raceFrame.setLocationRelativeTo(this);
        raceFrame.setLocation(raceFrame.getX(),raceFrame.getY()-70);

        sleepTimer = new Timer(sleepTimeSlider.getValue(), new SleepListener());

        hareTimer = new Timer(hareSpeedSlider.getValue(), e -> {
            if(raceFrame.isSleeping()){
                if (raceFrame.getSleepTimeCurrent() == 0) raceFrame.setSleepTimeCurrent(sleepTimeSlider.getValue());
                sleepTimer.setDelay(raceFrame.getSleepTimeCurrent());
                sleepTimer.start();
            }
            else {
                if (raceFrame.moveHare()){
                    winnerDecided("Hare");
                }
            }
        });
        turtleTimer = new Timer(turtleSpeedSlider.getValue(), e -> {
            if(raceFrame.moveTurtle()) winnerDecided("Turtle");
        });

        hareTimer.start();
        turtleTimer.start();

        turtleSpeedSlider.addChangeListener(e -> {
            turtleValue.setText(turtleSpeedSlider.getValue() + "");
            turtleTimer.setDelay(turtleSpeedSlider.getValue());
        });

        hareSpeedSlider.addChangeListener(e -> {
            hareValue.setText(hareSpeedSlider.getValue() + "");
            hareTimer.setDelay(hareSpeedSlider.getValue());
        });

        sleepTimeSlider.addChangeListener(e -> sleepTimeValue.setText(sleepTimeSlider.getValue() + ""));

        turtleValue = new JLabel(turtleSpeedSlider.getValue() + "");
        hareValue = new JLabel(hareSpeedSlider.getValue() + "");
        sleepTimeValue = new JLabel(sleepTimeSlider.getValue() + "");

        addSlider(turtleSpeedSlider, "Turtle Speed (ms)", turtleValue);
        addSlider(hareSpeedSlider, "Hare Speed (ms)  ", hareValue);
        addSlider(sleepTimeSlider, "Sleep Time (ms)   ", sleepTimeValue);

        setVisible(true);
    }

    private void addSlider(JSlider s, String description, JLabel label){
        JPanel panel = new JPanel();
        panel.add(new JLabel(description));
        panel.add(s);
        panel.add(label);
        add(panel);
    }

    private void clear(){
        raceFrame.dispose();
        turtleTimer.stop();
        hareTimer.stop();
        sleepTimer.stop();
        dispose();
    }

    public static MainFrame mainFrame;
    public static void winnerDecided(String name){
        if (name.equals("")){
            mainFrame = new MainFrame();
        }
        else{
            mainFrame.clear();
            int i = JOptionPane.showConfirmDialog(null, name + " won! Would you like to restart?", "Race Ended", JOptionPane.YES_NO_OPTION);
            if (i == 1){
                System.exit(0);
            }
            else{
                mainFrame = new MainFrame();
            }
        }
    }

    public static void main(String[] args){
        winnerDecided("");
    }

    private class SleepListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            raceFrame.setSleeping(false);
            raceFrame.sleepUsed();
            sleepTimer.stop();
        }
    }
}
