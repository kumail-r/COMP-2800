package Assignments.A08;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main (String[] args){
        if (args.length == 0){
            System.out.println("No file names provided.\nExiting program.");
            return;
        }
        try {
            while (true){
                System.out.println("Waiting for connection...");
                ServerSocket ss = new ServerSocket(5575);
                Socket s = ss.accept();
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(args.length + ""); // send the number of files

                for (String arg : args) {
                    dos.writeUTF(arg); // send file length
                    File file = new File(arg);
                    System.out.println("file length: " + file.length()); // print file size
                    dos.writeUTF(file.length() + ""); // send file size
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[(int) file.length()];
                    fis.read(buffer);
                    dos.write(buffer);
                    fis.close();
                }
                dos.close(); // close data output stream
                s.close(); // close socket
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
