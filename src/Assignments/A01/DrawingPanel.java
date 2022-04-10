//package Assignments.A1;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//public class DrawingPanel extends JPanel {
//    private int startX, startY;
//    private int x,y;
//    private boolean drawRect = true; // true = rectangle, false = oval
//
//    public DrawingPanel(){
//        addMouseListener(
//            new MouseAdapter() {
//                public void mousePressed(MouseEvent event){ // this will be called when the mouse is pressed
//                    startX = event.getX();
//                    startY = event.getY();
//                    //System.out.println("clicked!(" + startX + ", " + startY + ")"); // for debug
//                    //repaint();
//                }
//                public void mouseReleased(MouseEvent event){ // this will be called when the mouse is released
//                    x = event.getX();
//                    y = event.getY();
//                    //System.out.println("released!(" + x + ", " + y + ")"); // for debug
//                    repaint(); // repaint since we should now have both values
//                }
//            }
//        );
//    }
//    public void paintComponent(Graphics g){
//        super.paintComponent(g);
//        // these two if statements will handle the case where user is drawing opposite the intended way (such as starting bottom right and moving top left)
//        if (startX > x){ // handles the X coordinates by swapping them if the start point is larger than the end point
//            int temp = x; // temp variable used to swap values
//            x = startX;
//            startX = temp;
//        }
//        if (startY > y){ // handles the Y coordinates by swapping them if the start point is larger than the end point
//            int temp = y; // temp variable used to swap values
//            y = startY;
//            startY = temp;
//        }
//        if (drawRect) {
//            g.drawRect(startX, startY, x - startX, y - startY); // draw rectangle according to provided coordinates
//        }
//        else{
//            g.drawOval(startX, startY, x - startX, y - startY); // draw oval according to provided coordinates
//        }
//        clearValues(); // clear the values of X/Y/startX/startY after they are drawn
//    }
//    public void setDrawRect(boolean drawRect){ // sets the value of drawRect based on passed through value
//        this.drawRect = drawRect;
//        clearValues();
//        repaint();
//    }
//    private void clearValues(){ // this function is used to clear the values of the 4 global variables
//        x = 0;
//        y = 0;
//        startX = 0;
//        startY = 0;
//    }
//}
