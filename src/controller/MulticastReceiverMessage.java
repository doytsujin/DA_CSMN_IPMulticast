/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.DatagramCollection;

/**
 *
 * @author Admin
 */
public class MulticastReceiverMessage extends Thread {

    private MultiCast multiCast;
    private JTextArea txtOutput;
    private HashMap<Long, DatagramCollection> datagramCollection;

    public MulticastReceiverMessage(JTextArea txtOut, int port) throws IOException {
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
                byte[] ReceiveData = multiCast.ReceiveData(1);
                if (ReceiveData != null) {
                    txtOutput.append("\n-Receive:" + new String(ReceiveData));
                    //System.out.println(new String(ReceiveData));
                }
            } catch (IOException ex) {
                Logger.getLogger(MultiCast.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("...");
        }

    }
    public static void main(String[] args) throws IOException {
        MulticastReceiverMessage multicastReceiverMessage = new MulticastReceiverMessage(null,12345);
        multicastReceiverMessage.join( InetAddress.getByName("224.0.0.2"));
        multicastReceiverMessage.start();
    }
}
