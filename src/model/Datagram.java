/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;

/**
 *
 * @author Admin
 */
public class Datagram implements Comparable<Datagram> {

   
    int offset;
    byte[] data;

    @Override
    public int compareTo(Datagram t) {
        if (offset == t.offset) {
            return 0;
        } else if (offset > t.offset) {
            return 1;
        } else {
            return -1;
        }
    }

    public Datagram(int offset, byte[] data) {
        this.offset = offset;
        this.data = data;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Datagram() {
    }

    
}
