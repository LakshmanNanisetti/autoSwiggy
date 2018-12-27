/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swiggy;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lakshman-pt2712
 */
public class Order extends Thread{
    int cName,rName,cAdd,rAdd;
    boolean delivered = false;

    public Order(int cName, int rName, int cAdd, int rAdd) {
        this.cName = cName;
        this.rName = rName;
        this.cAdd = cAdd;
        this.rAdd = rAdd;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public int getcName() {
        return cName;
    }

    public void setcName(int cName) {
        this.cName = cName;
    }

    public int getrName() {
        return rName;
    }

    public void setrName(int rName) {
        this.rName = rName;
    }

    public int getcAdd() {
        return cAdd;
    }

    public void setcAdd(int cAdd) {
        this.cAdd = cAdd;
    }

    public int getrAdd() {
        return rAdd;
    }

    public void setrAdd(int rAdd) {
        this.rAdd = rAdd;
    }
    public String toString(){
        return "cust" + cName + " cust add:" + cAdd + " rest" + rName + " rest Add:" + rAdd;
    }
    public void run(){
        int dName = 0;
        while(dName == 0){
            
            dName = Swiggy.findDE(cAdd,rAdd);
            if(dName==0){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
        System.out.println(this + " is being delivered by de" + dName);
//        try {
//            wait(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
//        }
        Swiggy.changeDeAddress(dName,cAdd);
    }
}
