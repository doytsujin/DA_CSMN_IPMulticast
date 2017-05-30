/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.Datagram;
import model.DatagramCollection;

/**
 *
 * @author Admin
 */
public class MultiCast {

    /*Ip Multicast*/
    protected InetAddress ipAdress;
    protected String ip_Multicast = "224.0.0.2";
    protected int port = 12345;
    protected MulticastSocket ms;

    private static int DATAGRAM_RECEIVE_MAX_SIZE = 35507;
    public static int HEADER_SIZE = 14;
    public static int DATAGRAM_SEND_MAX_SIZE = DATAGRAM_RECEIVE_MAX_SIZE - HEADER_SIZE;
    /* Buffer gói tin nhận */
    byte[] buffer = new byte[DATAGRAM_RECEIVE_MAX_SIZE];
    /* Kích thước và cờ */

    public static int SESSION_START = 128;
    public static int SESSION_END = 64;

    /*Biến hiệu chỉnh*/
    int currentSession = -1;
    int slicesStored = 0;
    int[] slicesCol = null;
    byte[] dataReceive = null;
    boolean sessionAvailable = false;

    int sessionNumber = 0;
    public static int MAX_PACKETS = 255;
    public static int MAX_SESSION_NUMBER = 255;

    private HashMap<Long, DatagramCollection> datagramCollection;

    public HashMap<Long, DatagramCollection> getDatagramCollection() {
        return datagramCollection;
    }

    public void setDatagramCollection(HashMap<Long, DatagramCollection> datagramCollection) {
        this.datagramCollection = datagramCollection;
    }

    public MultiCast() throws IOException {
        datagramCollection = new HashMap<>();
        ms = new MulticastSocket();
    }

    public MultiCast(int port, HashMap<Long, DatagramCollection> datagramCollection) throws IOException {
        this.port = port;
        this.datagramCollection = datagramCollection;
        this.ms = new MulticastSocket(port);
        System.out.println("contructor");
        if (this.ms == null) {
            System.out.println("null");
        }
    }

    public boolean Sender(byte[] dataSend, long currentTime) {
        int packets = (int) Math.ceil(dataSend.length / (float) DATAGRAM_SEND_MAX_SIZE);

        /* If image has more than MAX_PACKETS slices -> error */
        if (packets > MAX_PACKETS) {
            System.out.println("Image is too large to be transmitted!");
            return false;
        }

        /* Loop through slices */
        for (int i = 0; i <= packets; i++) {
            int flags = 0;
            flags = i == 0 ? flags | SESSION_START : flags;
            flags = (i + 1) * DATAGRAM_SEND_MAX_SIZE > dataSend.length ? flags | SESSION_END : flags;

            int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_SEND_MAX_SIZE : dataSend.length - i * DATAGRAM_SEND_MAX_SIZE;

            /* Set additional header */
            byte[] data = new byte[HEADER_SIZE + size];
            data[0] = (byte) flags;
            data[1] = (byte) sessionNumber;
            data[2] = (byte) packets;
            data[3] = (byte) (DATAGRAM_SEND_MAX_SIZE >> 8);
            data[4] = (byte) DATAGRAM_SEND_MAX_SIZE;
            data[5] = (byte) i;
            data[6] = (byte) (size >> 8);
            data[7] = (byte) size;
            data[8] = (byte) (currentTime >> 8);
            data[9] = (byte) (currentTime);

            /* Copy current slice to byte array */
            System.arraycopy(dataSend, i * DATAGRAM_SEND_MAX_SIZE, data, HEADER_SIZE, size);
            /* Send multicast packet */
            sendData(data);

            /* Leave loop if last slice has been sent */
            if ((flags & SESSION_END) == SESSION_END) {
                break;
            }
        }
        /* Increase session number */
        sessionNumber = sessionNumber < MAX_SESSION_NUMBER ? ++sessionNumber : 0;
        return true;
    }

    public boolean Sender(byte[] dataSend, long currentTime, InetAddress address, int port) {
        //create a hashmap data
        int packets = (int) Math.ceil(dataSend.length / (float) DATAGRAM_SEND_MAX_SIZE);
        if (packets > MAX_PACKETS) {
            System.out.println("Image is too large to be transmitted!");
            return false;
        }
        // user paralel push data
        HashMap<Integer, Datagram> datagram = new HashMap<>();
        /* Loop through slices */
        for (int i = 0; i <= packets; i++) {
            int flags = 0;
            flags = i == 0 ? flags | SESSION_START : flags;
            flags = (i + 1) * DATAGRAM_SEND_MAX_SIZE > dataSend.length ? flags | SESSION_END : flags;

            int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_SEND_MAX_SIZE : dataSend.length - i * DATAGRAM_SEND_MAX_SIZE;

            /* Set additional header */
            byte[] data = new byte[HEADER_SIZE + size];
            data[0] = (byte) flags;
            data[1] = (byte) sessionNumber;
            data[2] = (byte) packets;
            data[3] = (byte) (DATAGRAM_SEND_MAX_SIZE >> 8);
            data[4] = (byte) DATAGRAM_SEND_MAX_SIZE;
            data[5] = (byte) i;
            data[6] = (byte) (size >> 8);
            data[7] = (byte) size;
            data[8] = (byte) (currentTime >> 8);
            data[9] = (byte) (currentTime);

            /* Copy current slice to byte array */
            System.arraycopy(dataSend, i * DATAGRAM_SEND_MAX_SIZE, data, HEADER_SIZE, size);
            /* Send multicast packet */
            //sendData(data);
            datagram.put(i, new Datagram(i, data));
            /* Leave loop if last slice has been sent */
            if ((flags & SESSION_END) == SESSION_END) {
                break;
            }
        }
        // send multi port
        long timeStart = System.currentTimeMillis();
        IntStream s = IntStream.range(0, datagram.size());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        s.parallel().forEach(i -> {
            //send datagram i
            new Thread(() -> {
                try {
                    ms.setTimeToLive(2);
                    byte send[] = datagram.get(i).getData();
                    DatagramPacket dp = new DatagramPacket(send, send.length,
                            address, port);
                    ms.send(dp);
                    System.out.println("Sended:" + Arrays.toString(send));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        // System.out.println("Time:"+(System.currentTimeMillis() - timeStart));
//        datagram.entrySet()
//                .parallelStream()
//                .forEach(entry -> {
//                    System.out.println(entry.getKey() + ":" + new String(entry.getValue().getData()).length());
//                    sendData(entry.getValue().getData());
//                });

        /* If image has more than MAX_PACKETS slices -> error */
 /* Increase session number */
        sessionNumber = sessionNumber < MAX_SESSION_NUMBER ? ++sessionNumber : 0;

        return true;
    }

    private boolean sendData(byte[] data) {
        boolean ret = false;
        int ttl = 2;

        try {
            ms.setTimeToLive(ttl);
            DatagramPacket dp = new DatagramPacket(data, data.length, ipAdress, port);
            ms.send(dp);
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public byte[] Receiver() {
        try {
            /* Receive a UDP packet */
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ms.receive(dp);
            byte[] data = dp.getData();

            /* Read header infomation */
            short session = (short) (data[1] & 0xff);
            short slices = (short) (data[2] & 0xff);
            int maxPacketSize = (int) ((data[3] & 0xff) << 8 | (data[4] & 0xff)); // mask
            // the
            // sign
            // bit
            short slice = (short) (data[5] & 0xff);
            int size = (int) ((data[6] & 0xff) << 8 | (data[7] & 0xff)); // mask
            long currentTime = (int) ((data[8] & 0xff) << 8 | (data[9] & 0xff));
            // the
            // sign
            // bit
            // push tất cả vào datagram
            Long timeCurrent = new Long(currentTime);
            /* If SESSION_START falg is set, setup start values */

            //add new if not have
            if (!datagramCollection.containsKey(timeCurrent)) {
                datagramCollection.put(timeCurrent, new DatagramCollection(currentTime, new ArrayList<Datagram>()));
            }
            DatagramCollection dataCollectionItem = datagramCollection.get(timeCurrent);
            //push data
            byte[] dataPush = new byte[size];
            System.arraycopy(data, HEADER_SIZE, dataPush, 0, size);
            dataCollectionItem.addDatagram(new Datagram(slice, dataPush));
            //check data is full
            if (dataCollectionItem.getDatagram().size() == slices) {
                ArrayList<Datagram> datagram = dataCollectionItem.getDatagram();
                Collections.sort(datagram);
                byte[] result = new byte[(int) dataCollectionItem.getTotalSize()];
                ByteBuffer target = ByteBuffer.wrap(result);
                for (Datagram item : datagram) {
                    item.getData();
                    target.put(item.getData());
                }
                datagramCollection.remove(currentTime);
                return result;
            }

//            /* If SESSION_START falg is set, setup start values */
//            if ((data[0] & SESSION_START) == SESSION_START) {
//                if (session != currentSession) {
//                    currentSession = session;
//                    slicesStored = 0;
//                    /* Consturct a appropreately sized byte array */
//                    dataReceive = new byte[slices * maxPacketSize];
//                    slicesCol = new int[slices];
//                    sessionAvailable = true;
//                    if (!datagramCollection.containsKey(timeCurrent)) {
//                        datagramCollection.put(timeCurrent, new DatagramCollection(currentTime, new ArrayList<Datagram>(), DatagramCollection.TypeData.VIDEO));
//                    }
//
//                }
//            }
//
//            /* If package belogs to current session */
//            if (sessionAvailable && session == currentSession) {
//                if (slicesCol != null && slicesCol[slice] == 0) {
//                    slicesCol[slice] = 1;
//                    System.arraycopy(data, HEADER_SIZE, dataReceive, slice
//                            * maxPacketSize, size);
//                    slicesStored++;
//
//                    DatagramCollection dataCollectionItem = datagramCollection.get(timeCurrent);
//                    byte[] dataPush = new byte[size];
//                    System.arraycopy(data, HEADER_SIZE, dataPush, 0, size);
//                    dataCollectionItem.addDatagram(new Datagram(slice, dataPush));
//                }
//            }
//            /* If image is complete dispay it */
//            if (slicesStored == slices) {
//                //end of collection
//                DatagramCollection datagramColl = datagramCollection.get(timeCurrent);
//                if (datagramColl == null) {
//                    return null;
//                }
//                ArrayList<Datagram> datagram = datagramColl.getDatagram();
//                Collections.sort(datagram);
//                byte[] result = new byte[(int) datagramColl.getTotalSize()];
//                ByteBuffer target = ByteBuffer.wrap(result);
//                for (Datagram item : datagram) {
//                    item.getData();
//                    target.put(item.getData());
//                }
//                datagramCollection.remove(currentTime);
//                return result;
//
//            }
        } catch (IOException ex) {
            Logger.getLogger(MultiCast.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void sendMessage(byte[] dataSend, long currentTime, InetAddress address, int port) {
        //sử lý dâta
        HashMap<Integer, Datagram> datagram = createDatagram(dataSend, currentTime, TypeData.Message);
        //phan packet
        long timeStart = System.currentTimeMillis();
        IntStream s = IntStream.range(0, datagram.size());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "500");
        s.parallel().forEach((int i) -> {
            //send datagram i
            new Thread(() -> {
                try {
                    ms.setTimeToLive(2);
                    byte send[] = datagram.get(i).getData();
                    DatagramPacket dp = new DatagramPacket(send, send.length,
                            address, port);
                    ms.send(dp);
                    System.out.println("Sended:" + new String(send));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        System.out.println(System.currentTimeMillis() - timeStart);
        //send
    }

    public void sendVideo(byte[] dataSend, long currentTime, InetAddress address, int port) {
        //sử lý dâta
        HashMap<Integer, Datagram> datagram = createDatagram(dataSend, currentTime, TypeData.Video);
        //phan packet
        long timeStart = System.currentTimeMillis();
        IntStream s = IntStream.range(0, datagram.size());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "500");
        s.parallel().forEach((int i) -> {
            //send datagram i
            new Thread(() -> {
                try {
                    ms.setTimeToLive(2);
                    byte send[] = datagram.get(i).getData();
                    DatagramPacket dp = new DatagramPacket(send, send.length,
                            address, port);
                    ms.send(dp);
                    //System.out.println("Sended:" + Arrays.toString(send));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        System.out.println(System.currentTimeMillis() - timeStart);
        //send
    }
    
    public void sendVoice(byte[] dataSend, long currentTime, InetAddress address, int port) {
        //sử lý dâta
        HashMap<Integer, Datagram> datagram = createDatagram(dataSend, currentTime, TypeData.Sound);
        //phan packet
        long timeStart = System.currentTimeMillis();
        IntStream s = IntStream.range(0, datagram.size());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "500");
        s.parallel().forEach((int i) -> {
            //send datagram i
            new Thread(() -> {
                try {
                    ms.setTimeToLive(2);
                    byte send[] = datagram.get(i).getData();
                    DatagramPacket dp = new DatagramPacket(send, send.length,
                            address, port);
                    ms.send(dp);
                    //System.out.println("Sended:" + Arrays.toString(send));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        System.out.println(System.currentTimeMillis() - timeStart);
        //send
    }

    private HashMap<Integer, Datagram> createDatagram(byte[] dataSend, long currentTime, TypeData type) {
        //create a hashmap data
        int packets = (int) Math.ceil(dataSend.length / (float) DATAGRAM_SEND_MAX_SIZE);
        if (packets > MAX_PACKETS) {
            System.out.println("Image is too large to be transmitted!");
            return null;
        }
        // user paralel push data
        HashMap<Integer, Datagram> datagram = new HashMap<>();
        /* Loop through slices */
        for (int i = 0; i <= packets; i++) {
            int flags = 0;
            flags = i == 0 ? flags | SESSION_START : flags;
            flags = (i + 1) * DATAGRAM_SEND_MAX_SIZE > dataSend.length ? flags | SESSION_END : flags;

            int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_SEND_MAX_SIZE : dataSend.length - i * DATAGRAM_SEND_MAX_SIZE;

            /* Set additional header */
            byte[] data = new byte[HEADER_SIZE + size];
            data[0] = (byte) flags;
            data[1] = (byte) sessionNumber;
            data[2] = (byte) packets;
            data[3] = (byte) (DATAGRAM_SEND_MAX_SIZE >> 8);
            data[4] = (byte) DATAGRAM_SEND_MAX_SIZE;
            data[5] = (byte) i;
            data[6] = (byte) (size >> 8);
            data[7] = (byte) size;
            data[8] = (byte) (currentTime >> 8);
            data[9] = (byte) (currentTime);

            //add type 
            switch (type) {
                case Message:
                    data[10] = (byte) 1;//message
                    break;
                case File:
                    data[10] = (byte) 2;//message
                    break;
                case Sound:
                    data[10] = (byte) 3;//message
                    break;
                case Video:
                    data[10] = (byte) 4;//message
                    break;
            }

            /* Copy current slice to byte array */
            System.arraycopy(dataSend, i * DATAGRAM_SEND_MAX_SIZE, data, HEADER_SIZE, size);
            /* Send multicast packet */
            //sendData(data);
            datagram.put(i, new Datagram(i, data));
            /* Leave loop if last slice has been sent */
            if ((flags & SESSION_END) == SESSION_END) {
                break;
            }
        }
        return datagram;
    }

    private enum TypeData {
        Message, Video, Sound, File;
    }

    public void Join(InetAddress address) throws IOException {

        this.ms.joinGroup(address);

    }

    public void Leave(InetAddress address) throws IOException {
        this.ms.leaveGroup(address);
    }

    public byte[] ReceiveData(int type) throws IOException {
        /* Receive a UDP packet */
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ms.receive(dp);
        byte[] data = dp.getData();

        /* Read header infomation */
        short session = (short) (data[1] & 0xff);
        short slices = (short) (data[2] & 0xff);
        int maxPacketSize = (int) ((data[3] & 0xff) << 8 | (data[4] & 0xff)); // mask
        // the
        // sign
        // bit
        short slice = (short) (data[5] & 0xff);
        int size = (int) ((data[6] & 0xff) << 8 | (data[7] & 0xff)); // mask
        long currentTime = (int) ((data[8] & 0xff) << 8 | (data[9] & 0xff));
        byte typeData = data[10];
        if (typeData != type) {
            return null;
        }
        byte[] dataPush = new byte[size];
        System.arraycopy(data, HEADER_SIZE, dataPush, 0, size);
        synchronized (datagramCollection) {
            if (!datagramCollection.containsKey(currentTime)) {
                datagramCollection.put(currentTime, new DatagramCollection(currentTime, new ArrayList<Datagram>()));
            }
            DatagramCollection dataCollectionItem = datagramCollection.get(currentTime);
            //push datas
            dataCollectionItem.addDatagram(new Datagram(slice, dataPush));
            //check data is full
            if (dataCollectionItem.getDatagram().size() == slices) {
                ArrayList<Datagram> datagram = dataCollectionItem.getDatagram();
                Collections.sort(datagram);
                byte[] result = new byte[(int) dataCollectionItem.getTotalSize()];
                ByteBuffer target = ByteBuffer.wrap(result);
                for (Datagram item : datagram) {
                    item.getData();
                    target.put(item.getData());
                }
                datagramCollection.remove(currentTime);
                return result;
            }
        }
        //IF NOT HAVE COLLECTION , WE ADD NEW

        return null;
    }
    
   
}
