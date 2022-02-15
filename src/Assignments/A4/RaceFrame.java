package Assignments.A4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RaceFrame extends JFrame {
    private int sleepTimeCurrent;

    public final RacePanel racePanel;
    public RaceFrame(){
        super("Race Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040,300);
        setResizable(false);
        racePanel = new RacePanel();
        setLayout(new BorderLayout());
        add(racePanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public boolean moveTurtle(){
        return racePanel.moveTurtle();
    }

    public boolean moveHare(){
        return racePanel.moveHare();
    }

    public void sleepUsed(){
        racePanel.sleepsRemaining--;
        racePanel.sleepsRemainingLabel.setText("Hare will sleep " + racePanel.sleepsRemaining + " more time(s).");
    }

    public boolean isSleeping(){
        return racePanel.sleeping;
    }

    public void setSleeping(boolean sleeping){
        racePanel.sleeping = sleeping;
    }

    public int getSleepTimeCurrent() {
        return sleepTimeCurrent;
    }

    public void setSleepTimeCurrent(int sleepTimeCurrent) {
        this.sleepTimeCurrent = sleepTimeCurrent;
    }

    private class RacePanel extends JPanel{
        private static final String path = "C:\\Users\\robot\\IdeaProjects\\COMP2800\\src\\Assignments\\A4\\"; // file path for images
        private final ImageIcon backgroundImage = new ImageIcon(path + "background.png");
        private final ImageIcon turtleImage = new ImageIcon(path + "turtle.png");
        private final ImageIcon hareImage = new ImageIcon(path + "hare.png");

        private int turtlePos = 0;
        private int harePos = 0;

        private int sleepCounter = 0;
        private final JLabel sleepCounterLabel = new JLabel("Click to make the rabbit sleep!");
        public int sleepsRemaining = 0;
        private final JLabel sleepsRemainingLabel = new JLabel("Hare will sleep " + sleepsRemaining + " more time(s).");

        public boolean sleeping = false;

        public RacePanel(){
            repaint();
            setLayout(null);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (sleepCounter < 2){
                        sleepCounter++;
                        sleepsRemaining++;
                        sleepCounterLabel.setText("Hare will sleep " + sleepCounter + " time(s) during the race.");
                        sleepsRemainingLabel.setText("Hare will sleep " + sleepsRemaining + " more time(s).");
                    }
                }
            });
            sleepCounterLabel.setBounds(430,10, (int) sleepCounterLabel.getPreferredSize().getWidth() + 80, (int) sleepCounterLabel.getPreferredSize().getHeight());
            sleepsRemainingLabel.setBounds(430, 235, (int) sleepsRemainingLabel.getPreferredSize().getWidth(), (int) sleepsRemainingLabel.getPreferredSize().getHeight());
            add(sleepCounterLabel);
            add(sleepsRemainingLabel);
        }
        public boolean moveTurtle(){
            turtlePos = turtlePos >= 15 * 64 ? 15 * 64: turtlePos + 3;
            repaint();
            return turtlePos >= 15 * 64;
        }

        public boolean moveHare(){
            if (sleeping) return harePos >= 15 * 64;
            else if (sleepsRemaining > 0){ sleeping = true; }
            harePos = harePos >= 15 * 64? 15 * 64 : harePos + 2;
            repaint();
            return harePos >= 15 * 64;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            backgroundImage.paintIcon(this,g,0,0);
            g.drawLine(960,0,960,300);
            turtleImage.paintIcon(this,g,turtlePos,30);
            hareImage.paintIcon(this, g, harePos, 150);
        }
    }
}
