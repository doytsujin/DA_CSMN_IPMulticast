package model;

import java.util.ArrayList;

public class DatagramCollection implements Comparable<DatagramCollection>{

    long time;
    long totalSize;

    public long getTotalSize() {
        return totalSize;
    }
    ArrayList<Datagram> datagram;
    
    //Thời gian nhận data
    public DatagramCollection(){
        datagram = new ArrayList<>();
        totalSize = 0;
    }
    
    public void addDatagram(Datagram datagram){
        //Nếu như data rỗng thì addd
        if (this.datagram.isEmpty()) {
            this.datagram.add(datagram);
        } //caso o pacote a adicionar seja diferente dos presentes, adicona o pacote
        if(!this.datagram.contains(datagram)){
            this.datagram.add(datagram);
        }
        this.totalSize += datagram.getData().length;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Datagram> getDatagram() {
        return datagram;
    }

    public void setDatagram(ArrayList<Datagram> datagram) {
        this.datagram = datagram;
    }

    public DatagramCollection(long time, ArrayList<Datagram> datagram) {
        this.time = time;
        this.datagram = datagram;
        this.totalSize =0;
        for (Datagram data : datagram) {
            this.totalSize += data.getData().length;
        }
    }
    @Override
    public int compareTo(DatagramCollection t) {
         if (this.time == t.time) {
            return 0;
        } else if (this.time > t.time) {
            return 1;
        } else {
            return -1;
        }
    }
}
