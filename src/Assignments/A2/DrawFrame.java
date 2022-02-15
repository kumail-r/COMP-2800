// by Kumail Raza
// raza11g, 105225432
// 2/6/2022

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class DrawFrame extends JFrame {
    // mode constants
    public final int ADD = 0;
    public final int DELETE = 1;
    public final int MOVE = 2;
    private int mode; // holds current mode
    private JLabel instructionalLabel;

    private Point cache;
    private Line2D moveCache;

    private DrawPanel drawPanel;
    public DrawFrame(int inputMode){
        super("Draw Frame");
        this.mode = inputMode; // make sure mode is set
        setLayout(new BorderLayout()); // set borderlayout

        drawPanel = new DrawPanel();
        add(drawPanel,BorderLayout.CENTER); // add drawing panel to center
        add(drawPanel);
        instructionalLabel = new JLabel("Waiting for actions...");
        add(instructionalLabel, BorderLayout.SOUTH); // add instructional label to south edge

        // cache variables will hold line/point for use in add/delete (since two points are needed for add/delete and 2 points + a line are needed for move).
        cache = null; // initialize as null
        moveCache = null;

        addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent event){ // this will be called when the mouse is pressed
                        int x = event.getX() - 7;
                        int y = event.getY() - 30;
                        int margin = 10; // how much room for error to give the clicks
                        // ADD MODE
                        System.out.println("current mode: " + mode);
                        if (mode == ADD) {
                            for (Point p : drawPanel.getPoints()) {
                                if (Math.abs(p.x - x) <= margin && Math.abs(p.y - y) <= margin) { // point was clicked!
                                    System.out.println("Clicked on " + p);
                                    if (cache != null) {
                                        drawPanel.addLine(cache, p);
                                        System.out.println("Drawing line...");
                                        instructionalLabel.setText("Line Drawn from (" + cache.x + ", " + cache.y + ") to (" + p.x + ", " + p.y + ")");
                                        cache = null;
                                    }
                                    else {
                                        cache = p;
                                        instructionalLabel.setText("(" + p.x + ", " + p.y + ") selected... click one more to draw line.");
                                    }
                                    return;
                                }
                            }
                            instructionalLabel.setText("No point found at clicked location.");
                        }
                        else if (mode == DELETE){
                            for (Point p : drawPanel.getPoints()) {
                                if (Math.abs(p.x - x) <= margin && Math.abs(p.y - y) <= margin) { // point was clicked!
                                    System.out.println("Clicked on " + p);
                                    if (cache != null) {
                                        for (Line2D l : drawPanel.getLines()){
                                            if ((l.getP1().equals(cache) && l.getP2().equals(p)) || (l.getP1().equals(p) && l.getP2().equals(cache))){
                                                drawPanel.removeLine(l);
                                                instructionalLabel.setText("Line from (" + cache.x + ", " + cache.y + ") to (" + p.x + ", " + p.y + ") deleted");
                                                cache = null;
                                                return;
                                            }
                                        }
                                    }
                                    else {
                                        cache = p;
                                        instructionalLabel.setText("(" + p.x + ", " + p.y + ") selected... click one more to delete line.");
                                    }
                                    return;
                                }
                            }
                            instructionalLabel.setText("No point found at clicked location.");
                        }
                        else if (mode == MOVE){
                            for (Point p : drawPanel.getPoints()) {
                                if (Math.abs(p.x - x) <= margin && Math.abs(p.y - y) <= margin) { // point was clicked!
                                    System.out.println("Clicked on " + p);
                                    if (cache != null) {
                                        if (moveCache == null) {
                                            for (Line2D l : drawPanel.getLines()) {
                                                if ((l.getP1().equals(cache) && l.getP2().equals(p)) || (l.getP1().equals(p) && l.getP2().equals(cache))) {
                                                    instructionalLabel.setText("Line from (" + cache.x + ", " + cache.y + ") to (" + p.x + ", " + p.y + ") selected");
                                                    moveCache = l;
                                                    cache = null;
                                                    return;
                                                }
                                            }
                                        }
                                        else{
                                            drawPanel.removeLine(moveCache);
                                            drawPanel.addLine(cache,p);
                                            instructionalLabel.setText("Line moved to (" + cache.x + ", " + cache.y + ") to (" + p.x + ", " + p.y + ")");
                                            moveCache = null;
                                            cache = null;
                                        }
                                    }
                                    else {
                                        cache = p;
                                        instructionalLabel.setText("(" + p.x + ", " + p.y + ") selected... click one more to move line.");
                                    }
                                    return;
                                }
                            }
                            instructionalLabel.setText("No point found at clicked location.");
                        }
                    }
                }
        );
    }
    public void addMode(){ // sets the panel to add mode
        mode = ADD;
        cache = null;
        moveCache = null;
    }
    public void deleteMode(){ // sets the panel to delete mode
        mode = DELETE;
        cache = null;
        moveCache = null;
    }
    public void moveMode(){ // sets the panel to move mode
        mode = MOVE;
        cache = null;
        moveCache = null;
    }
    public int getMode(){ // returns the current mode
        return mode;
    }
    private class DrawPanel extends JPanel {
        private Point[] points; // holds the nodes (randomly generated)
        private ArrayList<Line2D> lines = new ArrayList<>();
        public DrawPanel(){
            generatePoints();
        }

        private void generatePoints(){ // randomly generates nodes
            int nodeCount = 8; // sets the total number of points
            points = new Point[nodeCount];
            for (int i = 0; i < nodeCount; i++){
                points[i] = new Point((int)(Math.random()*(201)+50),(int)(Math.random()*(201)+50)); // generate new point between 50 and 250
                System.out.println(points[i]); // for debug
            }
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Point point : points){ // paint the points
                g.fillOval(point.x, point.y, 4, 4);
            }
            if (!lines.isEmpty()){ // paint lines IF there are lines
                System.out.println("Drawing lines...");
                for (Line2D line : lines){
                    g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
                }
            }
        }
        public void addLine(Point first, Point second){ // adds line to arraylist
            lines.add(new Line2D.Double(first,second));
            repaint();
        }
        public Point[] getPoints() { // return points array
            return points;
        }

        public ArrayList<Line2D> getLines() { // return lines array
            return lines;
        }

        public void removeLine(Line2D line){ // remove line from array
            System.out.println("Index" + lines.indexOf(line));
            if (lines.indexOf(line) > -1) lines.remove(lines.indexOf(line));
            repaint();
        }
    }
}

class OptionPanel extends JFrame {
    private DrawFrame drawFrame; // the actual DrawFrame

    // JRadioButtons for the three modes
    private JRadioButton add;
    private JRadioButton delete;
    private JRadioButton move;
    private ButtonGroup optionGroup; // buttongroup to hold option buttons
    private JButton resetButton; // reset button
    private JLabel instructionLabel; // instructionLabel

    public OptionPanel(){
        super("Options Panel");
        newDrawFrame(0); // initialize with default mode (add)
        setLayout(new FlowLayout());

        // declare JRadioButtons
        add = new JRadioButton("Add", true);
        delete = new JRadioButton("Delete", false);
        move = new JRadioButton("Move", false);

        // add JRadioButtons
        add(add);
        add(delete);
        add(move);

        // create logical relationship between JRadioButtons
        optionGroup = new ButtonGroup();
        optionGroup.add(add);
        optionGroup.add(delete);
        optionGroup.add(move);

        // register events for JRadioButtons
        add.addItemListener(
                new RadioButtonHandler(0));
        delete.addItemListener(
                new RadioButtonHandler(1));
        move.addItemListener(
                new RadioButtonHandler(2));

        // set up instructional JLabel
        instructionLabel = new JLabel("Draw a new graph edge by clicking and dragging");
        add(instructionLabel);
        // set up reset button
        resetButton = new JButton("Full Reset"); // this button will fully reset the DrawFrame if necessary
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // dispose and rebuild on pressing reset
                int mode = drawFrame.getMode();
                drawFrame.dispose();
                newDrawFrame(mode);
            }
        });
        add(resetButton);
    }

    private class RadioButtonHandler implements ItemListener { // handles pressing of buttons
        private int option;
        public RadioButtonHandler(int x){
            option = x;
        }
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (option == 0) { // add
                drawFrame.addMode();
                instructionLabel.setText("Draw a new graph edge by clicking and dragging it");
            }
            else if (option == 1) { // delete
                drawFrame.deleteMode();
                instructionLabel.setText("Delete a graph edge by clicking on its nodes");
            }
            else {
                drawFrame.moveMode(); // move
                instructionLabel.setText("Move a graph edge by clicking original then new nodes");
            }
        }
    }
    public void newDrawFrame(int mode){ // create new frame and remove old frame
        drawFrame = new DrawFrame(mode);
        drawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        drawFrame.setSize(300,300);
        drawFrame.setVisible(true);
    }
    public static void main(String[] args){
        OptionPanel optionPanel = new OptionPanel();
        optionPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionPanel.setSize(330,130);
        optionPanel.setVisible(true);
    }
}