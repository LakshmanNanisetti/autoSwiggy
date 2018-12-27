/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swiggy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lakshman-pt2712
 */
public class DE extends Thread{
    private int name, address, btime, noOfOrders, deliveryTime;

    DE(DE remove) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    private boolean busy;
    public void incAvlTime(int inc){
        noOfOrders += inc;
    }

    public int getAvlTime() {
        return noOfOrders;
    }
    public DE(int name, int address) {
        this.name = name;
        this.address = address;
        busy = false;
        btime = 0;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public int getBtime() {
        return btime;
    }

    public void setBtime(int btime) {
        this.btime = btime;
    }

    public int getname() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    public void run(){
        try {
//            System.out.println("dname:"+name+" dtime:"+deliveryTime);
            sleep(deliveryTime*100);
            System.out.println("de:"+name+" is available now at "+address);
        } catch (InterruptedException ex) {
            Logger.getLogger(DE.class.getName()).log(Level.SEVERE, null, ex);
        }
        Swiggy.addToAvailable(name);
    }
    public String toString(){
        return "de"+name+" ,add:"+address;
    }
}
