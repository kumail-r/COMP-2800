package Assignments.A10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceManager extends JPanel {
    private final Participant hare;
    private final Participant turtle;
    private final String path = "C:\\Users\\robot\\IdeaProjects\\COMP2800\\src\\Assignments\\A10\\";
    private final ImageIcon backgroundImage = new ImageIcon(path + "background.png");
    private final ImageIcon turtleImage = new ImageIcon(path + "turtle.png");
    private final ImageIcon hareImage = new ImageIcon(path + "hare.png");

    public RaceManager(int hareSpeed, int turtleSpeed, int sleepTime) {
        hare = new Participant(sleepTime, hareSpeed, 0);
        turtle = new Participant(0, turtleSpeed, 1);
        repaint();

        ExecutorService executorService = Executors.newCachedThreadPool(); // based off of given example from textbook
        executorService.execute(hare); // for hare
        executorService.execute(turtle); // for turtle
        executorService.execute(new Runnable() { // dedicated for painting
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            @Override
            public void run() {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repaint();
                if (hare.getX() >= 15 * 64 || turtle.getX() >= 15 * 64) executor.shutdownNow();
                else executor.execute(this);
            }
        });
        executorService.shutdown();

        JLabel sleepLabel = new JLabel("You have used 0 sleeps so far (2 maximum)");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hare.addSleep();
                sleepLabel.setText("You have used " + hare.getSleeps() + " sleeps so far (2 maximum)");
            }
        });
        sleepLabel.setBounds(430,10, (int) sleepLabel.getPreferredSize().getWidth() + 80, (int) sleepLabel.getPreferredSize().getHeight());
        add(sleepLabel);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        backgroundImage.paintIcon(this,g,0,0);
        g.drawLine(960,0,960,300);
        turtleImage.paintIcon(this, g, turtle.getX(),30);
        hareImage.paintIcon(this, g, hare.getX(), 150);
    }
}
