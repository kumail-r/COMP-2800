package Assignments.A7;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Properties extends JFrame {
    private final ArrayList<JTextField> jTextFields = new ArrayList<>(); // arraylist holding JTextFields
    private final ArrayList<item> values = new ArrayList<>(); // arraylist holding item values
    private final Method[] methods; // arraylist holding all methods
    public Properties(Field[] fields, Method[] methods){ // constructor
        super("Properties"); // title
        this.methods = methods;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // dispose when x is pressed
        setSize(300,300); // default size is 300x300
        for (Field field : fields){ // iterate through every field
            // if statement will only display integers and booleans
            if ((field.getType().toString().equals("boolean") || field.getType().toString().equals("int")) && !field.getName().toUpperCase().equals(field.getName())){
                values.add(new item(field.getName(), field.getType().toString()));
            }
        }
        setLayout(new GridLayout(values.size() + 1, 2)); // gridlayout with 2 columns and enough rows
        for (item v : values){ // iterate through all stored values
            add(new JLabel(v.name)); // create JLabel for each
            JTextField textField = new JTextField();
            add(textField); // add JLabel for each
            textField.addActionListener(e -> { // if TextField is updated, call update function with correct index
                try {
                    update(values.indexOf(v));
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            });
            jTextFields.add(textField); // add textField to grid
        }
        JButton button = new JButton("Reset"); // reset button
        add(button); // add button
        button.addActionListener(e -> {
            try {
                reset(); // on button press call reset function
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        });
        setVisible(true);
    }
    private void update(int index) throws InvocationTargetException, IllegalAccessException { // updates variable values
        for (Method m : methods){
            if (m.getName().equals("set" + ("" +values.get(index).name.charAt(0)).toUpperCase() + values.get(index).name.substring(1))){
                if (values.get(index).type.equals("int")) // integer
                    m.invoke(DisplayAndLoad.content, Integer.parseInt(jTextFields.get(index).getText()));
                else // boolean
                    m.invoke(DisplayAndLoad.content, jTextFields.get(index).getText().equals("true"));
            }
        }
    }
    public void reset() throws IllegalAccessException, InvocationTargetException { // resets variable values if needed
        for (int i = 0; i < jTextFields.size(); i++){
            for (Method m : methods){
                if (m.getName().equals("get" + ("" +values.get(i).name.charAt(0)).toUpperCase() + values.get(i).name.substring(1))){
                    jTextFields.get(i).setText(m.invoke(DisplayAndLoad.content) + "");
                }
            }
        }
    }
    private static class item{ // private class used to simplify process of storing name/type
        public final String name;
        public final String type;
        public item(String name, String type) {
            this.name = name;
            this.type = type;
        }
        public String toString(){
            return "Field Name: " + name + "\nField Type: type";
        }
    }
}
