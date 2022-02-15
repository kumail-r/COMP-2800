package Labs.L3;

// Fig. 13.32: Shapes2.java
// Demonstrating a general path.
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Shapes2
{
    // execute application
    public static void main(String[] args)
    {
        // create frame for Shapes2JPanel
        JFrame frame = new JFrame("Star Moving with Internal Event");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Shapes2JPanel shapes2JPanel = new Shapes2JPanel();
        frame.add(shapes2JPanel, BorderLayout.CENTER);

        frame.setBackground(Color.WHITE);
        frame.setSize(315, 370);
        frame.setVisible(true);

        JLabel howLongLabel = new JLabel("Delay: " + 1000 + " ms");
        frame.add(howLongLabel,BorderLayout.NORTH);

        Timer timer = new Timer(1000, shapes2JPanel); // initialize timer to use the shapes2JPanel

        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0,2000, 1000);
        slider.setName("Time (ms)");
//        slider.setPaintLabels(true);
//        slider.setLabelTable(slider.createStandardLabels(500,0));
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(500);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);

        frame.add(slider,BorderLayout.SOUTH);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                timer.setDelay(slider.getValue());
                howLongLabel.setText("Delay: " + slider.getValue() + " ms");
            }
        });

        timer.start(); // start

    }
} // end class Shapes2

/**************************************************************************
 * (C) Copyright 1992-2018 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
