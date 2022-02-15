package Labs.L4;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.security.SecureRandom;

public class CatcherPanel extends JPanel implements ActionListener {
    private static final String path = "C:\\Users\\robot\\IdeaProjects\\COMP2800\\src\\Labs\\L4\\"; // file path for images
    private static final String[] images = {"black.png", "blue.png", "brown.png", "green.png", "purple.png", "red.png", "yellow.png"}; // all of the image names
    private static final SecureRandom generator = new SecureRandom(); // used for generating random numbers

    private ImageIcon image; // actual image icon to be drawn
    private int x = 0;
    private int y = 0;
    private Rectangle rectangle;
    private String imageName = "";
    private final Timer timer = new Timer(1000,this);

    public CatcherPanel(){
        rectangle = new Rectangle(x,y,50,50);
        newImage();
        timer.start();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println(e.getPoint());
                if (rectangle.contains(e.getPoint())){
                    if (timer.isRunning()){
                        newImage();
                        timer.stop();
                        playSound();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (rectangle.contains(e.getPoint())){
                    if (!timer.isRunning()){
                        timer.start();
                        newPosition();
                    }
                }
            }
        });
    }

    private void newPosition(){
        x = generator.nextInt(getWidth() - image.getIconWidth()); // generate random x location based off current panel size
        y = generator.nextInt(getHeight() - image.getIconHeight()); // generate random y location based off current panel size
        rectangle = new Rectangle(x,y,50,50);
        repaint();
    }

    private void newImage(){
        // the purpose of oldImageName and the while loop is to prevent a dupe from being selected
        String oldImageName = imageName;
        while(imageName.equals(oldImageName)){ // it keeps generating until they are different
            imageName = images[generator.nextInt(images.length)];
        }
        image = new ImageIcon(path + imageName); // set image to be the new one
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this,g,x,y);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newPosition();
    }

    private void playSound(){
        try {
            File file = new File(path + "bloop.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
            stream.close();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Catching...");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        CatcherPanel catcherPanel = new CatcherPanel();
        frame.add(catcherPanel);
        frame.setVisible(true);
    }

}
