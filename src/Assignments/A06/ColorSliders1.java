package Assignments.A06;

import javax.swing.*;
import java.awt.*;

public class ColorSliders1 extends JPanel {
    private final JSlider redSlider; // red JSlider
    private final JSlider greenSlider; // green JSlider
    private final JSlider blueSlider; // blue JSlider
    private final JLabel rVal;
    private final JLabel gVal;
    private final JLabel bVal; // JLabels for the up-to-date R, G, and B values
    ColorPanel colorPanel; //
    public ColorSliders1() {
        colorPanel = new ColorPanel();
        setLayout(new BorderLayout()); // TEMP
        // set up JSliders and JLabels
        redSlider = new JSlider(SwingConstants.HORIZONTAL, 0,255, 0);
        JLabel r = new JLabel("Red", JLabel.LEFT);
        rVal = new JLabel("0", JLabel.RIGHT);
        greenSlider = new JSlider(SwingConstants.HORIZONTAL, 0,255, 0);
        JLabel g = new JLabel("Green", JLabel.LEFT);
        gVal = new JLabel("0", JLabel.RIGHT);
        blueSlider = new JSlider(SwingConstants.HORIZONTAL, 0,255, 0);
        // red, green, and blue names
        JLabel b = new JLabel("Blue", JLabel.LEFT);
        bVal = new JLabel("0", JLabel.RIGHT);

        add(colorPanel);

        // the following lines handle the event where sliders are moved
        // each one updates the color and the text showing the integer value
        redSlider.addChangeListener(
                e -> {
                    colorPanel.setRed(redSlider.getValue());
                    rVal.setText(String.valueOf(redSlider.getValue()));
                }
        );
        greenSlider.addChangeListener(
                e -> {
                    colorPanel.setGreen(greenSlider.getValue());
                    gVal.setText(String.valueOf(greenSlider.getValue()));
                }
        );
        blueSlider.addChangeListener(
                e -> {
                    colorPanel.setBlue(blueSlider.getValue());
                    bVal.setText(String.valueOf(blueSlider.getValue()));
                }
        );

        JPanel sliderPanel = new JPanel(); // a sub-panel used to hold the sliders and slider labels
        sliderPanel.setLayout(new GridBagLayout()); // 9x9 grid layout, using GridBag so that they can be different sizes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        sliderPanel.add(r, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.95; // sliders have weight
        sliderPanel.add(redSlider, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.05;
        sliderPanel.add(rVal, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        sliderPanel.add(g, gbc);
        gbc.gridx = 1;
        sliderPanel.add(greenSlider, gbc);
        gbc.gridx = 2;
        sliderPanel.add(gVal, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        sliderPanel.add(b, gbc);
        gbc.gridx = 1;
        sliderPanel.add(blueSlider, gbc);
        gbc.gridx = 2;
        sliderPanel.add(bVal, gbc);
        sliderPanel.add(Box.createHorizontalStrut(rVal.getPreferredSize().width * 3),gbc); // for padding of Val's

        add(sliderPanel, BorderLayout.SOUTH); // place on south edge
    }
    private static class ColorPanel extends JPanel {
        private int red; // holds red value
        private int blue; // holds blue value
        private int green; // holds green value
        public ColorPanel(){
            red = 0; // initialize red
            blue = 0; // initialize blue
            green = 0; // initialize green
            setColor(); // update with initialized values
        }
        public void setColor(){
            this.setBackground(new Color(red,green,blue)); // update background color based off of variables
        }

        public void setRed(int red) { // update red
            this.red = red;
            setColor(); // set updated red
        }

        public void setBlue(int blue) { // update blue
            this.blue = blue;
            setColor(); // set updated blue
        }

        public void setGreen(int green) { // update green
            this.green = green;
            setColor(); // set updated green
        }
    }
    public static void main(String[] args){ // main method
        JFrame sliderFrame = new JFrame("Slider for Colors");
        sliderFrame.add(new ColorSliders1());
        sliderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sliderFrame.setSize(220,270);
        sliderFrame.setVisible(true);
    }
}