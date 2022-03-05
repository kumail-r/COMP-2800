package Labs.L5;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static DataInputStream input;
    private static JFrame clientFrame;
    public static void main(String[] args){
        generate(0);
        try {
            socket = new Socket(InetAddress.getByName("137.207.82.52"), 5575);
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            int value = Integer.parseInt(input.readLine());
            generate(value);

            while(socket.isConnected()){
                socket.close();
                socket = new Socket(InetAddress.getByName("137.207.82.52"), 5575);
                input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                value = Integer.parseInt(input.readLine());
                System.out.println(value);
                generate(value);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public static void generate(int k){
        if (clientFrame != null) clientFrame.dispose();
        clientFrame = new JFrame("Client");
        clientFrame.setSize(500,500);
        clientFrame.setLayout(new BorderLayout());
        DotPanel dotPanel = new DotPanel(k);
        clientFrame.add(dotPanel, BorderLayout.CENTER);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setVisible(true);
    }
}

class DotPanel extends JPanel {
    private Point[] points;
    private int k;
    public DotPanel(int k) {
        this.k = k;
        points = new Point[k];
        for (int i = 0; i < k; i++){
            points[i] = new Point((int) (Math.random() * 500),(int) (Math.random() * 500));
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < k; i++){
            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            g.drawOval(points[i].x, points[i].y, 5, 5);
        }
    }
}