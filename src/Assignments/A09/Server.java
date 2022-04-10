package Assignments.A09;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static String path = "Server\\";
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(5575);
        ServerSocket serverSocket1 = new ServerSocket(5576);
        while(true){
            {
                Socket socket1 = serverSocket1.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket1.getOutputStream());
                File file = new File(path + "Page.class");
                dataOutputStream.writeUTF(file.length() + ""); // send file size
                dataOutputStream.writeUTF("Page.class"); // send name
                byte[] buffer = new byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(buffer);
                dataOutputStream.write(buffer);
                dataOutputStream.close();
                fis.close();
                socket1.close();
            }

            Socket socket = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String fileName = dataInputStream.readUTF();
            String studentName = dataInputStream.readUTF();
            int fileSize = Integer.parseInt(dataInputStream.readUTF());

            byte[] buffer = new byte[fileSize];
            dataInputStream.read(buffer);

            new File(path + studentName).mkdirs(); // create student folder

            FileOutputStream fos = new FileOutputStream(new File(path + studentName +"\\"+ fileName));
            fos.write(buffer);

            fos.close();

            initialize(args[0], studentName, fileName);
            {
                BufferedReader br = new BufferedReader(new FileReader(path + studentName + "\\" + "results.txt"));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = br.readLine())!= null) builder.append(line + "\n");
                dataOutputStream.writeUTF(builder.toString());
                br.close();
                dataOutputStream.close();
                dataInputStream.close();
            }
            socket.close();
            if (studentName.equals("end")) break;
        }
        serverSocket.close();
    }
    public static void initialize(String fileName, String studentName, String sentFileName){
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + fileName));
            String line = null;
            StringBuilder temp = new StringBuilder();
            int i = 0;
            while((line = br.readLine()) != null){
                temp.append(line + '\n');
            }
            String total = temp.toString();
            String[] cases = total.split("#\n");
            StringBuilder finalized = new StringBuilder("Compiled successfully.\n");
            int caseCount = 1;
            for (String j : cases){
                String[] values = j.split("\n");
                boolean flag = false;
                String inputString = "";
                String outputString = "";
                for (int num = 0; num < values.length; num++){

                    if (values[num].equals("*")){
                        flag = true;
                    }
                    else if (flag){
                        outputString += values[num] + "\n";
                    }
                    else{
                        inputString += values[num] + "\n";
                    }
                }

                String result = compileAndRun(studentName, sentFileName, inputString);

                if (result.equals("error")){
                    finalized = new StringBuilder("Failed to compile.\n");
                    break;
                }
                else if (result.equals(outputString)) {
                    finalized.append("test ").append(caseCount).append(": Pass\n");
                }
                else{
                    finalized.append("test ").append(caseCount).append(": Fail\n");
                }
                caseCount++;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(path + studentName + "\\results.txt"));
            bw.write(finalized.toString());
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static String compileAndRun(String studentName, String fileName, String toWrite) throws IOException, InterruptedException {

        String command = "cmd.exe /c cd " + path + studentName + " && gcc " + fileName + " -o compiled && compiled";
        Process p = Runtime.getRuntime().exec(command);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
        bw.write(toWrite);
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String output = "";

        int i;
        while((i=br.read())!=-1){
            output += (char)i;
        }

        br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String error = "";
        i = 0;
        while((i=br.read())!=-1){
            error += (char)i;
        }
        if (error.contains("error")){
            return "error";
        }

        return output;
    }
}
