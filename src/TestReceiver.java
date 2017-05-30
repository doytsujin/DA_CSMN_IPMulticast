/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import controller.MultiCast;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DatagramCollection;

/**
 *
 * @author Admin
 */
public class TestReceiver {

    public static void main(String[] args) throws IOException {
        MultiCast test = new MultiCast();
       // test.Join();
        HashMap<Long, DatagramCollection> datagramCollection = test.getDatagramCollection();
        System.out.println("Strat Receive...");

        (new Thread(() -> {
            MultiCast testa;
            try {
                testa = new MultiCast();
               // testa.Join();
                testa.setDatagramCollection(datagramCollection);

                int pts = 0;
                long start = System.currentTimeMillis();
                while (true) {
                    byte[] Receiver = test.Receiver();
                    if (System.currentTimeMillis() - start > 1000) {
                        System.out.println(pts);
                        pts = 0;
                        start = System.currentTimeMillis();
                    }
                    if (Receiver != null) {
                        System.out.println(Receiver.length);
                        pts++;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(TestReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }

        })).start();

    }

}
