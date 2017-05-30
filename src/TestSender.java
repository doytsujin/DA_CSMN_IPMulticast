/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import controller.MultiCast;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class TestSender {
    public static void main(String[] args) throws IOException {
        MultiCast test = new MultiCast();
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "10");
        //test.Join();
        System.out.println("Start Send");
            new Thread(new ScreenSender()).start();
            
    }
}
