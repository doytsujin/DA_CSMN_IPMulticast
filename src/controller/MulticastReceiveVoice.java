/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import model.DatagramCollection;

/**
 *
 * @author Admin
 */
public class MulticastReceiveVoice extends Thread {

    ByteArrayOutputStream byteOutputStream;
    AudioFormat adFormat;
    TargetDataLine targetDataLine;
    AudioInputStream InputStream;
    SourceDataLine sourceLine;
    private MultiCast multiCast;
    private HashMap<Long, DatagramCollection> datagramCollection;

    SourceDataLine speakers;

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

    public MulticastReceiveVoice(int port) throws IOException, LineUnavailableException {

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
                byte[] audioData = multiCast.ReceiveData(3);
                if (audioData != null) {
                    InputStream byteInputStream = new ByteArrayInputStream(audioData);
                    AudioFormat adFormat = getAudioFormat();
                    InputStream = new AudioInputStream(byteInputStream, adFormat, audioData.length / adFormat.getFrameSize());
                    DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, adFormat);
                    sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                    sourceLine.open(adFormat);
                    sourceLine.start();
                    Thread playThread = new Thread(new PlayThread());
                    playThread.start();
                }
            } catch (IOException ex) {
                // Logger.getLogger(MultiCast.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(MulticastReceiveVoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) throws IOException, LineUnavailableException {
        MulticastReceiveVoice multicastReceiverMessage = new MulticastReceiveVoice(12345);
        multicastReceiverMessage.join(InetAddress.getByName("224.0.0.2"));
        multicastReceiverMessage.start();
    }

    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[10000];

        public void run() {
            try {
                int cnt;
                while ((cnt = InputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        sourceLine.write(tempBuffer, 0, cnt);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }
}
