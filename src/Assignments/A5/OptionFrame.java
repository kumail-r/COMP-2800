package Assignments.A5;

import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class OptionFrame extends JFrame {
    private static JRadioButton addNode;
    private static JRadioButton deleteNode;
    private static JRadioButton moveNode;
    private static JRadioButton addArc;
    private static JRadioButton removeArc;
    private static ButtonGroup optionGroup;

    private static DrawFrame drawFrame;

    // popupmenu items
    private final JPopupMenu popupMenu;
    public MenuItemListener menuItemListener;

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
        addNode = new CustomRadioButton("Add Node", true);
        deleteNode = new CustomRadioButton("Delete Node", false);
        moveNode = new CustomRadioButton("Move Node", false);
        addArc = new CustomRadioButton("Add Arc", false);
        removeArc = new CustomRadioButton("Remove Arc", false);

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
        JButton resetButton = new CustomButton("Reset");
        add(resetButton);
        resetButton.addActionListener(e -> { // activates on button press
            reset();
        });

        // save button
        JButton saveButton = new CustomButton("Save to File");
        add(saveButton);
        saveButton.addActionListener(e -> {
            save(); // save to file
        });

        // load button
        JButton loadButton = new CustomButton("Load From File");
        add(loadButton);
        loadButton.addActionListener(e -> {
            load(); // load from file
        });

        // initialize popupmenu
        popupMenu = new JPopupMenu();

        // initialize menuItemListener
        menuItemListener = new MenuItemListener();

        // initialize addNode menu item
        JMenuItem setAddNode = new JMenuItem("Add Node");
        setAddNode.setActionCommand("addNode");
        setAddNode.addActionListener(menuItemListener);
        popupMenu.add(setAddNode);

        // initialize deleteNode menu item
        JMenuItem setDeleteNode = new JMenuItem("Delete Node");
        setDeleteNode.setActionCommand("removeNode");
        setDeleteNode.addActionListener(menuItemListener);
        popupMenu.add(setDeleteNode);

        // initialize moveNode menu item
        JMenuItem setMoveNode = new JMenuItem("Move Node");
        setMoveNode.setActionCommand("moveNode");
        setMoveNode.addActionListener(menuItemListener);
        popupMenu.add(setMoveNode);

        // initialize addArc menu item
        JMenuItem setAddArc = new JMenuItem("Add Arc");
        setAddArc.setActionCommand("addArc");
        setAddArc.addActionListener(menuItemListener);
        popupMenu.add(setAddArc);

        // initialize deleteNode menu item
        JMenuItem setDeleteArc = new JMenuItem("Delete Arc");
        setDeleteArc.setActionCommand("removeArc");
        setDeleteArc.addActionListener(menuItemListener);
        popupMenu.add(setDeleteArc);

        // initialize reset menu item
        JMenuItem resetOption = new JMenuItem("Reset Draw Frame");
        resetOption.setActionCommand("reset");
        resetOption.addActionListener(menuItemListener);
        popupMenu.add(resetOption);

        // initialize save menu item
        JMenuItem saveOption = new JMenuItem("Save Graph to File");
        saveOption.setActionCommand("save");
        saveOption.addActionListener(menuItemListener);
        popupMenu.add(saveOption);

        // initialize load menu item
        JMenuItem loadOption = new JMenuItem("Load Graph from File");
        loadOption.setActionCommand("load");
        loadOption.addActionListener(menuItemListener);
        popupMenu.add(loadOption);

        // show on right click
        OptionFrame temp = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) // make sure it is a right click
                    popupMenu.show(temp, e.getX(),e.getY());
            }
        });

        // add popupmenu to frame
        add(popupMenu);

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
    public static class MenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "addNode":
                    optionGroup.clearSelection();
                    addNode.setSelected(true);
                    DrawFrame.drawPanel.setMode(e.getActionCommand());
                    break;
                case "removeNode":
                    optionGroup.clearSelection();
                    deleteNode.setSelected(true);
                    DrawFrame.drawPanel.setMode(e.getActionCommand());
                    break;
                case "moveNode":
                    optionGroup.clearSelection();
                    moveNode.setSelected(true);
                    DrawFrame.drawPanel.setMode(e.getActionCommand());
                    break;
                case "addArc":
                    optionGroup.clearSelection();
                    addArc.setSelected(true);
                    DrawFrame.drawPanel.setMode(e.getActionCommand());
                    break;
                case "removeArc":
                    optionGroup.clearSelection();
                    removeArc.setSelected(true);
                    DrawFrame.drawPanel.setMode(e.getActionCommand());
                    break;
                case "reset":
                    reset();
                    break;
                case "save":
                    save();
                    break;
                case "load":
                    load();
                    break;
                default:
                    System.out.println("SOMETHING BROKE - the current ActionCommand: " + e.getActionCommand());
            }
        }
    }
}
class CustomButton extends JButton {
    private JPopupMenu popupMenu;
    public CustomButton() {
        generate();
    }
    public CustomButton(String text){
        super(text);
        generate();
    }
    private void generate(){
        // popupmenu
        popupMenu = new JPopupMenu();
        // menuitemlistener
        OptionFrame.MenuItemListener menuItemListener = new OptionFrame.MenuItemListener();

        // reset
        JMenuItem resetOption = new JMenuItem("Reset Draw Frame");
        resetOption.setActionCommand("reset");
        resetOption.addActionListener(menuItemListener);
        popupMenu.add(resetOption);

        // save
        JMenuItem saveOption = new JMenuItem("Save Graph to File");
        saveOption.setActionCommand("save");
        saveOption.addActionListener(menuItemListener);
        popupMenu.add(saveOption);

        // load
        JMenuItem loadOption = new JMenuItem("Load Graph from File");
        loadOption.setActionCommand("load");
        loadOption.addActionListener(menuItemListener);
        popupMenu.add(loadOption);

        // add click registration
        CustomButton temp = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) // make sure it is a right click
                    popupMenu.show(temp, e.getX(),e.getY());
            }
        });
    }
}

class CustomRadioButton extends JRadioButton {
    private JPopupMenu popupMenu;
    public CustomRadioButton(String text, boolean selected){
        super(text, selected);
        generate();
    }
    public void generate(){
        popupMenu = new JPopupMenu();
        OptionFrame.MenuItemListener menuItemListener = new OptionFrame.MenuItemListener();

        // initialize addNode menu item
        JMenuItem setAddNode = new JMenuItem("Add Node");
        setAddNode.setActionCommand("addNode");
        setAddNode.addActionListener(menuItemListener);
        popupMenu.add(setAddNode);

        // initialize deleteNode menu item
        JMenuItem setDeleteNode = new JMenuItem("Delete Node");
        setDeleteNode.setActionCommand("removeNode");
        setDeleteNode.addActionListener(menuItemListener);
        popupMenu.add(setDeleteNode);

        // initialize moveNode menu item
        JMenuItem setMoveNode = new JMenuItem("Move Node");
        setMoveNode.setActionCommand("moveNode");
        setMoveNode.addActionListener(menuItemListener);
        popupMenu.add(setMoveNode);

        // initialize addArc menu item
        JMenuItem setAddArc = new JMenuItem("Add Arc");
        setAddArc.setActionCommand("addArc");
        setAddArc.addActionListener(menuItemListener);
        popupMenu.add(setAddArc);

        // initialize deleteNode menu item
        JMenuItem setDeleteArc = new JMenuItem("Delete Arc");
        setDeleteArc.setActionCommand("removeArc");
        setDeleteArc.addActionListener(menuItemListener);
        popupMenu.add(setDeleteArc);

        // add click registration
        CustomRadioButton temp = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) // make sure it is a right click
                    popupMenu.show(temp, e.getX(),e.getY());
            }
        });
    }
}

