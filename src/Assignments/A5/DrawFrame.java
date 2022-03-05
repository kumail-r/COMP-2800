package Assignments.A5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class DrawFrame extends JFrame {
    public static DrawPanel drawPanel;
    public DrawFrame(){
        // initialize size, position, title, and dispose setting
        super("Graph Frame");
        setSize(500,500);
        setLocationRelativeTo(null);
        setLocation(getX(), getY() - 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create new DrawPanel
        drawPanel = new DrawPanel();
        add(drawPanel);

        // make visible
        setVisible(true);

    }
}

class DrawPanel extends JPanel{
    private static String mode;
    public static ArrayList<Node> nodes;
    public static ArrayList<Arc> arcs;
    private static Node buffer;
    public static JLabel instructionLabel;

    public DrawPanel(){
        // initialize nodes and arcs lists
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();

        mode = "addNode";

        // add instructional label
        instructionLabel = new JLabel("Click somewhere to add a node.");
        add(instructionLabel);

        // initialize buffer just in case
        buffer = null;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(mode){
                    case "addNode":
                        addNodeClick(e);
                        break;
                    case "removeNode":
                        removeNodeClick(e);
                        break;
                    case "moveNode":
                        moveNodeClick(e);
                        break;
                    case "addArc":
                        addArcClick(e);
                        break;
                    case "removeArc":
                        removeArcClick(e);
                        break;
                }
                repaint();
            }
        });
    }

    private void addNodeClick(MouseEvent e){
        nodes.add(new Node(e.getX(), e.getY()));
        instructionLabel.setText("Node added at " + (new Node(e.getX(), e.getY())));
    }
    private void removeNodeClick(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        for (Node i : nodes){
            // check if node was clicked on
            if (Math.abs(x - i.x) < 5 && Math.abs(y - i.y) < 5){
                //if it was clicked on
                Iterator<Arc> it = arcs.iterator();
                while (it.hasNext()){
                    if (it.next().contains(i) > 0) it.remove(); // if arc contains node, delete arc
                }
                nodes.remove(i);
                instructionLabel.setText("Node at " + i + " and all associated arc(s) deleted.");
                break;
            }
        }
    }
    private void moveNodeClick(MouseEvent e){
        int x = e.getX();
        int y = e.getY();

        if (buffer == null){
            if (getNode(x,y) != null){
                buffer = getNode(x,y);
                instructionLabel.setText("Node at " + buffer + " selected. Click on where you would like to move it.");
            }
            else{
                instructionLabel.setText("You did not click on a node. Click on a node in order to move it.");
            }
        }
        else{
            Node temp = new Node(x,y);
            for (Arc i : arcs){
                i.move(buffer, temp);
            }
            nodes.remove(buffer);
            nodes.add(temp);
            instructionLabel.setText("Node at " + buffer + " moved to " + temp + " alongside any associated arc(s).");
            buffer = null;
        }
    }
    private void addArcClick(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        if (getNode(x,y) != null){
            if (buffer == null){
                buffer = getNode(x,y);
                instructionLabel.setText("Node at " + buffer + " selected. Select another node to draw an arc.");
            }
            else{ // buffer is not null...
                arcs.add(new Arc(buffer, getNode(x,y)));
                instructionLabel.setText("Arc drawn from " + buffer + " to " + getNode(x,y) + ".");
                buffer = null;
            }
        }
        else{
            instructionLabel.setText("You did not click on a node.");
        }
    }
    private void removeArcClick(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        if (getNode(x,y) != null){
            if (buffer == null){
                buffer = getNode(x,y);
                instructionLabel.setText("You have selected " + buffer + ". Click another node to delete any arc between them.");
            }
            else{ // buffer is not null, thus we have two nodes selected
                for (Arc i : arcs){
                    if ((i.contains(buffer) > 0) && (i.contains(getNode(x,y)) > 0)){
                        arcs.remove(i);
                        instructionLabel.setText("Arc going from " + buffer + " to " + getNode(x,y) + " deleted.");
                        buffer = null;
                        return;
                    }
                }
                instructionLabel.setText("There is no arc between these two nodes.");
                buffer = null;
            }
        }
        else{
            instructionLabel.setText("You did not click on a node.");
        }
    }
    public void setMode(String x){
        mode = x;
        buffer = null;
        switch(mode) {
            case "addNode":
                instructionLabel.setText("Click somewhere to add a node.");
                break;
            case "removeNode":
                instructionLabel.setText("Click a node to delete it and its associated arcs.");
                break;
            case "moveNode":
                instructionLabel.setText("Click a node and its new position in order to move it.");
                break;
            case "addArc":
                instructionLabel.setText("Click on two nodes in order to draw an arc.");
                break;
            case "removeArc":
                instructionLabel.setText("Click on two nodes connected by an arc in order to delete the arc.");
                break;
            default:
                System.out.println("INVALID MODE NAME...");
        }
    }
    private Node getNode(int x, int y){
        for (Node i : nodes){
            if(Math.abs(x - i.x) < 5 && Math.abs(y - i.y) < 5){
                return i;
            }
        }
        return null;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Node i : nodes){
            g.setColor(Color.blue);
            g.fillOval(i.x - 5, i.y - 5, 10, 10);
        }
        for (Arc i : arcs){
            //g.drawLine(i.source.x, i.source.y, i.destination.x, i.destination.y);
            g.setColor(Color.red);
            drawArrow(g,i.source.x, i.source.y, i.destination.x, i.destination.y);
        }
    }

    // rather than drawing a line, this method will draw an arrow
    private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        // changes the size of the arrowhead
        int ARR_SIZE = 6;
        g.fillPolygon(new int[] {len, len- ARR_SIZE, len- ARR_SIZE, len},
                new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
}

class Node {
    public int x;
    public int y;
    public Node(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}

class Arc {
    public Node source;
    public Node destination;

    public Arc(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
    }

    // returns 1 if x matches with source, returns 2 if it matches with destination, and returns 0 if it doesn't match
    public int contains(Node x){
        if (x.equals(source)) return 1;
        else if (x.equals(destination)) return 2;
        else return 0;
    }

    public void move(Node oldNode, Node newNode){
        switch(contains(oldNode)){
            case 0:
                break;
            case 1:
                source.x = newNode.x;
                source.y = newNode.y;
                break;
            case 2:
                destination.x = newNode.x;
                destination.y = newNode.y;
                break;
        }
    }

    @Override
    public String toString(){
        return "(" + source + ", " + destination + ")";
    }
}