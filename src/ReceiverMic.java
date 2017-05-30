
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.sound.sampled.*;

public class ReceiverMic {

    static AudioFormat format = new AudioFormat(44000.0f, 16, 1, true, true);
    static SourceDataLine speakers;

    public static void main(String[] args) throws LineUnavailableException, SocketException, UnknownHostException, IOException {
        TargetDataLine line;

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }
        line = (TargetDataLine) AudioSystem.getLine(info);

        //TOTALLY missed this.
        int buffsize = line.getBufferSize() / 5;
        buffsize += 512;
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        speakers.open(format);
        speakers.start();
        DatagramSocket socket = new DatagramSocket(50005);
        
        InetAddress ia = InetAddress.getByName("127.0.0.1");
        while (true) {
            byte[] bytes = new byte[buffsize];
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, ia, 50005);
            socket.receive(dp);
            speakers.write(dp.getData(), 0, buffsize);

        }
    }
}
