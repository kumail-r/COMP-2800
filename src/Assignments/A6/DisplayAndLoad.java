package Assignments.A6;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;

public class DisplayAndLoad extends JFrame{
    private JPanel content = new JPanel();
    private Properties properties = null;

    public DisplayAndLoad(){
        super("Reflection Exercise");
        setLayout(new BorderLayout());
        JTextField textField = new JTextField(36);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocation(this.getX() - 500, this.getY() - 200);
        add(content, BorderLayout.NORTH);
        {
            JPanel temp = new JPanel();
            temp.setLayout(new FlowLayout());
            temp.add(new JLabel("Class name: "));
            temp.add(textField);
            setSize(500,300);
            textField.addActionListener(e -> generate(textField.getText()));
            add(temp, BorderLayout.NORTH);
        }
        setVisible(true);
    }

    public void generate(String str){
        try{
            Class<?> temp = Class.forName(str);
            if (properties != null) properties.dispose(); // kill properties if it already exists
            properties = new Properties(temp.getFields());
            properties.setLocationRelativeTo(this);
            properties.setLocation(properties.getX() + 386,properties.getY());
            Constructor<?> constructor = temp.getConstructor();
            Object object = constructor.newInstance();
            this.remove(content);
            content = (JPanel)object;
            this.add(content, BorderLayout.CENTER);
            this.setVisible(true);
        }
        catch(ClassNotFoundException exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, str + " is not a valid class name.");
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not find constructor.");
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws ClassNotFoundException {
        new DisplayAndLoad();
    }
}
