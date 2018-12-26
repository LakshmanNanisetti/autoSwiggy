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
public class User extends Thread{
    private int name, address, nio, nr, restnum, q;
    private boolean done = false;
    private Random rand;
    private ArrayList<Integer> restItems;
    private Restaurant OrderingRestaurant;
    HashMap<Integer, Integer> orders;
    public User(int name, int address) {
        this.name = name;
        this.address = address;
        this.nr = Swiggy.getNr();
        rand = new Random();
    }

    public int getname() {
        return name;
    }

    public void setname(int name) {
        this.name = name;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    public void run(){
        int tolerance = 10;
        while(true && tolerance > 0){
            nio = rand.nextInt(2) + 1;
            restnum = rand.nextInt(nr) + 1;
            restItems = Swiggy.getRestaurantItems(restnum);
            orders = initOrder(restItems);
            OrderingRestaurant = Swiggy.getRestaurant(restnum);
            if(OrderingRestaurant.makeOrder(orders)){
                break;
            }
            tolerance--;
        }
        if(tolerance > 0){
            printOrder();
        }
        else{
            System.out.println("user" + name + "says: I hate swiggy");
        }
        
        
    }
    public synchronized  void printOrder(){
        System.out.println("User"+ name +" made an order at rest" + restnum + " and items are:" );
        for(Map.Entry<Integer,Integer> me: orders.entrySet()){
            System.out.println("item" + me.getKey() + " - quantity: " + me.getValue());
        }
    }
    public HashMap<Integer,Integer> initOrder(ArrayList<Integer> restItems){
        int itemno;
        HashMap<Integer,Integer> orders = new HashMap<>();
        for(int j = 0; j < nio; j ++){
                int ind = rand.nextInt(restItems.size() - j);
                itemno = restItems.remove(ind);                
                int q = rand.nextInt(2) + 1;
                orders.put(itemno, q);
        }
        return orders;
    }
}
