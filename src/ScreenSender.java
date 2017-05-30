/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import controller.MultiCast;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.DatagramCollection;
import sun.awt.image.ImageFormatException;

public class ScreenSender implements Runnable {

    /* Default parameters */
    public static double SCALING = 0.75;
    public static int SLEEP_MILLIS = 30;
    public static int PORT = 4444;
    public static boolean SHOW_MOUSEPOINTER = true;
    public volatile boolean isRunning = true;
    long timeStamp = 0;
    int pts = 60;
    public static String OUTPUT_FORMAT = "jpg";

    public static BufferedImage getScreenshot() throws AWTException,
            ImageFormatException, IOException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);

        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRect);

        return image;
    }
    private JPanel view;

    public ScreenSender() {
    }

    public ScreenSender(JPanel view) {
        this.view = view;
    }

    public static void main(String[] args) {
        ScreenSender tmp = new ScreenSender();

        Thread thread = new Thread(tmp);
        thread.start();
    }
    int index = 0;
    long time;

    @Override
    public void run() {
        try {
            /* Continuously send images */
            MultiCast testa = new MultiCast();
           // testa.Join();
            long timepts = 0;
            int numofpts = 0;
            while (true) {

               // if (System.currentTimeMillis() - timeStamp > 1000 / pts) {
                    if (System.currentTimeMillis() - timepts > 1000) {
                        System.out.println("PTS: " + numofpts);
                        numofpts = 0;
                        timepts = System.currentTimeMillis();
                    }
                    time = System.currentTimeMillis();
                    numofpts++;
                    BufferedImage image;
                    image = getScreenshot();
                    byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);
                  
//                    long timstart = System.currentTimeMillis();
                    IntStream s = IntStream.range(0, 500);
                    //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
                    s.parallel().forEach(i -> {
                        testa.Sender(imageByteArray, time);
                    });

                    //System.out.println((System.currentTimeMillis() - timstart)+"|"+i++);
            //    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

}
