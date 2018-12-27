/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swiggy;

/**
 *
 * @author lakshman-pt2712
 */
public class DE extends Thread{
    private int name, address, btime, noOfOrders;
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
//    public void run(){
//        while(Swiggy.assignOrder(name, address, btime) != btime){
//            try{
//            wait(btime);
//            }catch(Exception e){}
//        }
//        
//    }
    public String toString(){
        return "de"+name+" ,add:"+address;
    }
}
