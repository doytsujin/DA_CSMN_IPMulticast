/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import javax.imageio.ImageIO;
import library.LibImage;
import static library.LibImage.bufferedImageToByteArray;
import static library.LibImage.shrink;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author Admin
 */
public class MulticastSendVideo extends Thread {
    // definitions

    public static String OUTPUT_FORMAT = "jpg";
    private DaemonThread myThread = null;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();

    // multicast
    MultiCast multiCast;
    InetAddress address;
    int port;

    public MulticastSendVideo(InetAddress address, int port) throws IOException {
        this.address = address;
        this.port = port;
        multiCast = new MultiCast();
    }

    // variable for video
    @Override
    public void run() {
        webSource = new VideoCapture(0);
        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();

    }

    public void destop() {
        myThread.runnable = false;
        webSource.release();
    }

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            while (runnable) {
                if (webSource.grab()) {
                    try {
                        webSource.retrieve(frame);
                        Highgui.imencode(".bmp", frame, mem);

                        byte[] data = mem.toArray();
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
                        image = shrink(image, 0.75);
                        byte[] imageByteArray = bufferedImageToByteArray(image, OUTPUT_FORMAT);
                        System.out.println(data.length/imageByteArray.length);
                        multiCast.sendVideo(imageByteArray, System.currentTimeMillis(), address, port);
                    } catch (Exception ex) {
                        System.out.println("Error");
                    }
                }
            }
        }
    }
}
