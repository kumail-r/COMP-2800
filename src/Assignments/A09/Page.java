package Assignments.A09;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Page extends JPanel {
    private String path = "Client\\";
    private final String ip;
    public Page(String ip) {
        this.ip = ip;
        setName("Page");
        setDefaultDisplay();
    }
    public void setDefaultDisplay(){
        removeAll();
        repaint();
        setLayout(new FlowLayout());
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JTextField studentNameField = new JTextField();
        studentNameField.setColumns(20);
        JTextField fileNameField = new JTextField();
        fileNameField.setColumns(20);
        c.gridx = 0;
        c.gridy = 0;
        textFieldPanel.add(new JLabel("student name"), c);
        c.gridx = 1;
        textFieldPanel.add(studentNameField, c);
        c.gridx = 0;
        c.gridy = 1;
        textFieldPanel.add(new JLabel("file name         "), c);
        c.gridx = 1;
        textFieldPanel.add(fileNameField, c);
        add(textFieldPanel);
        JButton uploadButton = new JButton("upload");
        uploadButton.addActionListener(e -> upload(studentNameField.getText(),fileNameField.getText()));
        add(uploadButton);
        validate();
    }

    public void showOutput(String output){ // will display the provided string
        removeAll();
        repaint();
        setLayout(new FlowLayout());
        add(new JTextArea(output));
        validate();
    }
    private void upload(String studentName, String fileName){ // will be run when upload button is pressed
        try {
            Socket socket = new Socket(ip, 5575);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            File file = new File(path + fileName);
            dataOutputStream.writeUTF(fileName);
            dataOutputStream.writeUTF(studentName);
            int size = (int) file.length(); // file size
            byte[] buffer = new byte[size];
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer);
            dataOutputStream.writeUTF(size + "");
            dataOutputStream.write(buffer);


            String response = dataInputStream.readUTF();
            showOutput(response);

            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Page");
        jFrame.add(new Page("localhost"));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(360,180);
        jFrame.setVisible(true);
    }
}
