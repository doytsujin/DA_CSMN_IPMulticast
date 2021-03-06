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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.DatagramCollection;

/**
 *
 * @author Admin
 */
public class MulticastReceiverVideo extends Thread {

    private MultiCast multiCast;
    private JPanel txtOutput;
    private HashMap<Long, DatagramCollection> datagramCollection;

    public MulticastReceiverVideo(JPanel txtOut, int port) throws IOException {
        this.txtOutput = txtOut;
        datagramCollection = new HashMap<Long, DatagramCollection>();
        multiCast = new MultiCast(port, datagramCollection);
    }

    public void join(InetAddress address) throws IOException {
        this.multiCast.Join(address);
    }

    public void leave(InetAddress address) throws IOException {
        this.multiCast.Leave(address);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] ReceiveData = multiCast.ReceiveData(4);
                if (ReceiveData != null) {
                    Image im = ImageIO.read(new ByteArrayInputStream(ReceiveData));
                    BufferedImage buff = (BufferedImage) im;
                    Graphics g = txtOutput.getGraphics();
                    g.drawImage(buff, 0, 0, txtOutput.getWidth(), txtOutput.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null);
                    g.dispose();
                    txtOutput.validate();
                    //System.out.println(new String(ReceiveData));
                }
            } catch (IOException ex) {
               // Logger.getLogger(MultiCast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        MulticastReceiverVideo multicastReceiverMessage = new MulticastReceiverVideo(null, 12345);
        multicastReceiverMessage.join(InetAddress.getByName("224.0.0.2"));
        multicastReceiverMessage.start();
    }
}
