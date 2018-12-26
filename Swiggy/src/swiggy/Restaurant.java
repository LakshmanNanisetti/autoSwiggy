/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swiggy;
import java.util.*;
/**
 *
 * @author lakshman-pt2712
 */
public class Restaurant {
    private int name,address, busy;
    private HashMap<Integer,Integer> items;

    public Restaurant(int name, int address) {
        this.name = name;
        this.address = address;
    }

    public Restaurant(int name, int address, HashMap<Integer, Integer> items) {
        this.name = name;
        this.address = address;
        this.items = items;
    }

    public int getName() {
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

    public HashMap<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Integer, Integer> items) {
        this.items = items;
    }
    
    public ArrayList<Integer> getItemList(){
        return new ArrayList<Integer>(items.keySet());
    }

    public synchronized boolean makeOrder(HashMap<Integer, Integer> orders) {
        HashMap<Integer, Integer> tempItems = new HashMap<>(items);
        for(Map.Entry<Integer, Integer> me : orders.entrySet()){
            if(me.getValue()>tempItems.get(me.getKey())){
                return false;
            }
            else{
                tempItems.put(me.getKey(),tempItems.get(me.getKey()) - me.getValue());
            }
        }
        items = tempItems;
        return true;
    }
}
