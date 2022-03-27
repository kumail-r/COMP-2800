package Assignments.A8;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Client extends JFrame {
    public static JPanel content = new JPanel();
    public Client(){
        super("my simple applet");
        setLayout(new BorderLayout());
        JTextField textField = new JTextField(36);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocation(this.getX() - 500, this.getY() - 200);
        add(content, BorderLayout.NORTH);
        {
            JPanel temp = new JPanel();
            temp.setLayout(new FlowLayout());
            temp.add(new JLabel("Server IP: "));
            temp.add(textField);
            setSize(500,300);
            textField.addActionListener(e -> callServer(textField.getText()));
            add(temp, BorderLayout.NORTH);
        }
        setVisible(true);
    }
    public void generate(String str){
        try{
            Class<?> temp = Class.forName(str);
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
    public void callServer(String ip){
        try {
            Socket s = new Socket(ip, 5575);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String fileCount = dis.readUTF();
            System.out.println("Number of files: " + fileCount);
            for (int i = 0; i < Integer.parseInt(fileCount); i++){
                String name = dis.readUTF();
                System.out.println("filename: " + name);
                int size = Integer.parseInt(dis.readUTF());
                System.out.println("filelength: " + size);
                String nameWithoutClass = name.replace(".class", "");
                System.out.println("filename without .class: " + nameWithoutClass);
                byte[] buffer = new byte[size];
                dis.read(buffer);
                FileOutputStream fos = new FileOutputStream(name);
                fos.write(buffer);
                if (i + 1 == Integer.parseInt(fileCount)) generate(nameWithoutClass); // only use final file as jpanel
                fos.close();
            }
            dis.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        new Client();
    }
}