/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Admin
 */
public class MulticastSendVoice extends Thread {

    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine;
    private DaemonThread myThread = null;
    // multicast
    MultiCast multiCast;
    InetAddress address;
    int port;

    public MulticastSendVoice(InetAddress address, int port) throws IOException, LineUnavailableException {
        this.address = address;
        this.port = port;
        multiCast = new MultiCast();
        adFormat = getAudioFormat();
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, adFormat);
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(adFormat);
        targetDataLine.start();
    }

    // variable for video
    @Override
    public void run() {
        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }

    public void destop() {
        myThread.runnable = false;
    }

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;
        byte tempBuffer[] = new byte[10000];

        @Override
        public void run() {
            while (runnable) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                if (cnt > 0) {
                    multiCast.sendVoice(tempBuffer, System.currentTimeMillis(), address, port);
                }
            }
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

}
