package Labs.L9;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class L9 {
    public static JPanel imagePanel = new JPanel();
    public static void main(String[] arg){
        String fileName = String.join(" ", arg);
        JFrame jFrame = new JFrame("Image with Dropped Color Component");
        jFrame.setSize(1000,500);
        jFrame.setLayout(new BorderLayout());
        GridLayout gridLayout = new GridLayout(1,4);
        gridLayout.setHgap(20);
        imagePanel.setLayout(gridLayout);
        JLabel label = new JLabel(createImages(fileName));
        jFrame.add(label,BorderLayout.NORTH);
        jFrame.add(imagePanel, BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    public static String createImages(String name){
        String extension = name.substring(name.length()-4); // assumes that file extension is 3 chars (4 including .)
        String nameWithoutExtension = name.substring(0,name.length()-4); // assumes that file extension is 3 chars
        BufferedImage raw;
        try{
            raw = ImageIO.read(new File(name));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        BufferedImage noRed = copyImage(raw);
        BufferedImage noGreen = copyImage(raw);
        BufferedImage noBlue = copyImage(raw);
        for (int i = 0; i < raw.getHeight(); i++){
            for (int j = 0; j < raw.getWidth(); j++){
                Color originalColor = new Color(raw.getRGB(j,i));
                noRed.setRGB(j,i,(new Color(0,originalColor.getGreen(),originalColor.getBlue())).getRGB());
                noGreen.setRGB(j,i,(new Color(originalColor.getRed(), 0,originalColor.getBlue())).getRGB());
                noBlue.setRGB(j,i,(new Color(originalColor.getRed(),originalColor.getGreen(),0)).getRGB());
            }
        }
        imagePanel.add(new JLabel(new ImageIcon(raw)));
        imagePanel.add(new JLabel(new ImageIcon(noBlue)));
        imagePanel.add(new JLabel(new ImageIcon(noGreen)));
        imagePanel.add(new JLabel(new ImageIcon(noRed)));

        File redOutput = new File(nameWithoutExtension + "-r" + extension);
        File greenOutput = new File(nameWithoutExtension + "-g" + extension);
        File blueOutput = new File(nameWithoutExtension + "-b" + extension);
        try {
            ImageIO.write(noRed, extension.substring(1), redOutput);
            ImageIO.write(noGreen, extension.substring(1), greenOutput);
            ImageIO.write(noBlue, extension.substring(1), blueOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        {HashMap<Integer, String> values = new HashMap<>();
        values.put(5, "TYPE_3BYTE_BGR");
        values.put(6, "TYPE_4BYTE_ABGR");
        values.put(7, "TYPE_4BYTE_ABGR_PRE");
        values.put(12, "TYPE_BYTE_BINARY");
        values.put(10, "TYPE_BYTE_GRAY");
        values.put(13, "TYPE_BYTE_INDEXED");
        values.put(0, "TYPE_CUSTOM");
        values.put(2, "TYPE_INT_ARGB");
        values.put(3, "TYPE_INT_ARGB_PRE");
        values.put(4, "TYPE_INT_BGR");
        values.put(1, "TYPE_INT_RGB");
        values.put(9, "TYPE_USHORT_555_RGB");
        values.put(8, "TYPE_USHORT_565_RGB");
        values.put(11, "TYPE_USHORT_GRAY");
        return values.get(raw.getType());}
    }
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
