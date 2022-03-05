package Assignments.A5;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;

public class OptionFrame extends JFrame {
    private static JRadioButton addNode;
    private static JRadioButton deleteNode;
    private static JRadioButton moveNode;
    private static JRadioButton addArc;
    private static JRadioButton removeArc;
    private static ButtonGroup optionGroup;
    private static JButton resetButton;
    private static JButton saveButton;
    private static JButton loadButton;

    private static DrawFrame drawFrame;

    public OptionFrame(){
        // default size/layout settings for OptionFrame
        super("Options");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(470,110);

        // initialize position
        setLocationRelativeTo(null);
        setLocation(getX(), getY() + 110);

        // create initial DrawFrame
        drawFrame = new DrawFrame();

        // initialize RadioButtons
        addNode = new JRadioButton("Add Node", true);
        deleteNode = new JRadioButton("Delete Node", false);
        moveNode = new JRadioButton("Move Node", false);
        addArc = new JRadioButton("Add Arc", false);
        removeArc = new JRadioButton("Remove Arc", false);

        // add RadioButtons to OptionFrame
        add(addNode);
        add(deleteNode);
        add(moveNode);
        add(addArc);
        add(removeArc);

        // create logical relationship between JRadioButtons
        optionGroup = new ButtonGroup();
        optionGroup.add(addNode);
        optionGroup.add(deleteNode);
        optionGroup.add(moveNode);
        optionGroup.add(addArc);
        optionGroup.add(removeArc);

        // register events for JRadioButtons
        addNode.addItemListener(new RadioButtonHandler("addNode"));
        deleteNode.addItemListener(new RadioButtonHandler("removeNode"));
        moveNode.addItemListener(new RadioButtonHandler("moveNode"));
        addArc.addItemListener(new RadioButtonHandler("addArc"));
        removeArc.addItemListener(new RadioButtonHandler("removeArc"));

        // reset button
        resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.addActionListener(e -> { // activates on button press
            reset();
        });

        // save button
        saveButton = new JButton("Save to File");
        add(saveButton);
        saveButton.addActionListener(e -> {
            save(); // save to file
        });

        // load button
        loadButton = new JButton("Load From File");
        add(loadButton);
        loadButton.addActionListener(e -> {
            load(); // load from file
        });

        // make visible
        setVisible(true);
    }

    public static void reset(){
        optionGroup.clearSelection(); // clear selected button
        addNode.setSelected(true); // set addNode as the selected button
        drawFrame.dispose(); // dispose current drawFrame
        drawFrame = new DrawFrame(); // create new DrawFrame
    }

    public static void load() {
        reset(); // call reset
        try {
            File file = new File("src\\Assignments\\A5\\data.txt");
            // if the file doesn't exist
            if (!file.exists()) JOptionPane.showMessageDialog(null, "data.txt not found.");

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            DrawPanel.nodes = new ArrayList<>();
            DrawPanel.arcs = new ArrayList<>();
            int nodeCount = Integer.parseInt(br.readLine());
            int arcCount = Integer.parseInt(br.readLine());
            for (int i = 0; i < nodeCount; i++){
                DrawPanel.nodes.add(new Node(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())));
            }
            for (int i = 0; i < arcCount; i++){
                DrawPanel.arcs.add(new Arc(new Node(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine())),
                        new Node(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()))));
            }
            fr.close();
            JOptionPane.showMessageDialog(null, "You have successfully loaded the data from data.txt!");
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void save(){
        try {
            FileWriter fw = new FileWriter("src\\Assignments\\A5\\data.txt");

            fw.write(DrawPanel.nodes.size() + "\n");
            fw.write(DrawPanel.arcs.size() + "\n");

            for (Node node : DrawPanel.nodes){
                fw.write(node.x + "\n");
                fw.write(node.y + "\n");
            }
            for (Arc arc : DrawPanel.arcs){
                fw.write(arc.source.x + "\n");
                fw.write(arc.source.y + "\n");
                fw.write(arc.destination.x + "\n");
                fw.write(arc.destination.y + "\n");
            }

            fw.close();
            JOptionPane.showMessageDialog(null, "You have successfully saved the data to data.txt!");
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private static class RadioButtonHandler implements ItemListener { // handles pressing of buttons
        private final String option;
        public RadioButtonHandler(String x){
            option = x;
        }
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) DrawFrame.drawPanel.setMode(option);
        }
    }
}
