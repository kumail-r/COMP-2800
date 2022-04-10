package Assignments.A06;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Properties extends JFrame {
    public Properties(Field[] fields){
        super("Properties");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300,300);
        ArrayList<String> values = new ArrayList<>();
        for (Field field : fields){
            values.add("Field Name: " + field.getName());
            values.add("Field Type: " + field.getType());
            values.add(" ");
        }
        JList<Object> list = new JList<>(values.toArray());
        JScrollPane scrollPane = new JScrollPane(list);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        setVisible(true);
    }
}
