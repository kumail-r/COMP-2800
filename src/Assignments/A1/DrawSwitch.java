//by Kumail Raza
//105225432
//1/30/2022

package Assignments.A1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawSwitch extends JFrame {

    public static void main(String[] args) {
        DrawSwitch f = new DrawSwitch(); // main frame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit program on closing
        f.setLayout(new BorderLayout()); // border layout so I can later set the JComboBox to be on the north edge of it
        f.setTitle("Rectangle/Oval Generator"); // title bar
        DrawingPanel p = new DrawingPanel(); // DrawingPanel object

        JComboBox<String> options = new JComboBox<String>(new String[]{"Rectangle", "Oval"}); // JComboBox with options
        options.setMaximumRowCount(2); // 2 options
        options.addItemListener( // listen for selection
                new ItemListener(){ // anonymous inner class
                    // handle JComboBox event
                    @Override
                    public void itemStateChanged(ItemEvent event) {
                        // determine whether item selected
                        if (event.getStateChange() == ItemEvent.SELECTED) {
                            // System.out.println(event.getItem()); // for debug
                            if (event.getItem().equals("Rectangle")){ // if rectangle is selected
                                p.setDrawRect(true); // set p to draw rectangles
                            }
                            else{ // if rectangle is not selected
                                p.setDrawRect(false); // set p to draw ovals
                            }
                        }
                    }
                } // end anonymous inner class
        ); // end call to addItemListener
        f.add(options, BorderLayout.NORTH); // add JComboBox to the North edge of the JFrame

        f.add(p); // add DrawPanel
        f.setSize(300, 200); // default size
        f.setVisible(true); // make visible
    }
}

// DrawingPanel class - this can be its own file, but in order to make the submission easier (only one file) I combined them.
class DrawingPanel extends JPanel {
    private int startX, startY;
    private int x,y;
    private boolean drawRect = true; // true = rectangle, false = oval

    public DrawingPanel(){
        addMouseListener(
                new MouseAdapter() {
                    public void mousePressed(MouseEvent event){ // this will be called when the mouse is pressed
                        startX = event.getX();
                        startY = event.getY();
                        //System.out.println("clicked!(" + startX + ", " + startY + ")"); // for debug
                        //repaint();
                    }
                    public void mouseReleased(MouseEvent event){ // this will be called when the mouse is released
                        x = event.getX();
                        y = event.getY();
                        //System.out.println("released!(" + x + ", " + y + ")"); // for debug
                        repaint(); // repaint since we should now have both values
                    }
                }
        );
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        // these two if statements will handle the case where user is drawing opposite the intended way (such as starting bottom right and moving top left)
        if (startX > x){ // handles the X coordinates by swapping them if the start point is larger than the end point
            int temp = x; // temp variable used to swap values
            x = startX;
            startX = temp;
        }
        if (startY > y){ // handles the Y coordinates by swapping them if the start point is larger than the end point
            int temp = y; // temp variable used to swap values
            y = startY;
            startY = temp;
        }
        if (drawRect) {
            g.drawRect(startX, startY, x - startX, y - startY); // draw rectangle according to provided coordinates
        }
        else{
            g.drawOval(startX, startY, x - startX, y - startY); // draw oval according to provided coordinates
        }
        clearValues(); // clear the values of X/Y/startX/startY after they are drawn
    }
    public void setDrawRect(boolean drawRect){ // sets the value of drawRect based on passed through value
        this.drawRect = drawRect;
        clearValues();
        repaint();
    }
    private void clearValues(){ // this function is used to clear the values of the 4 global variables
        x = 0;
        y = 0;
        startX = 0;
        startY = 0;
    }
}
