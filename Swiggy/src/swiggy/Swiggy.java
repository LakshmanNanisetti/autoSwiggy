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
public class Swiggy implements Values{

    /**
     * @param args the command line arguments
     */
    private static int nc, nr, nde;
    private static Scanner sc;
    private static ArrayList<User> customers;
    private static ArrayList<DE> des;
    private static ArrayList<Restaurant> restaurants;
    private static Random rand;

    public static int getNr() {
        return nr;
    }
    public static int scanInt(){
        /*
        To take input only an integer.
        */
        int scanVal;
        while(true){
            try{
                scanVal = sc.nextInt();
                break;
            }
            catch(InputMismatchException ime){
                sc.next();
                System.out.println("Input must be an integer");
            }
        }
        return scanVal;
    }
    public static void main(String[] args) {
        // TODO code application logic here
        sc = new Scanner(System.in);
        
        // to create random values
        rand = new Random();
        
        // arraylist to hold all customer names and addresses
        customers = new ArrayList<User>();
        
        // arraylist to hold all de names and addresses
        des = new ArrayList<DE>();
        
        // arraylist to hold all res names, addresses and items
        restaurants = new ArrayList<Restaurant>();
        
        System.out.println("enter the no of customers:");
        nc = scanInt();
        System.out.println("enter the no of delivery executives:");
        nde = scanInt();
        System.out.println("enter the no of restaurants:");
        nr = scanInt();
        
        initDes();
        initRestaurants();
        initCustomers();
        printUsers();
        startOrdering();
        startDelivering();
    }

    private static void initCustomers() {
        int areaNo;
        User cust;
        for(int i = 1; i <= nc; i ++){
            areaNo = rand.nextInt(nareas) + 1;
            cust = new User(i, areaNo);
            customers.add(cust);
        }
    }
    
    private static void initDes() {
        int areaNo;
        DE de;
        for(int i = 1; i <= nde; i ++){
            areaNo = rand.nextInt(nareas) + 1;
            de = new DE(i, areaNo);
            des.add(de);
        }
    }
    
    private static void initRestaurants() {
        /*
        This method initializes "nr" no of  restaurants with a name, area and 3
        items each of quantity either "1" or "2"
        */
        int areaNo;
        Restaurant r;
        for(int i = 1; i <= nr; i ++){
            areaNo = rand.nextInt(nareas) + 1;
            r = new Restaurant(i,areaNo);            
            r.setItems(initItems());
            restaurants.add(r);
        }
    }
    private static HashMap<Integer,Integer> initItems(){
        /*
        This method will be called from initRestaurants method.
        it will select "nritems" no of items from "nitems" no of total items
        and assigns each of the "nritems" items a quantity of either "1" or "2".
        */
        int itemno;
        ArrayList<Integer> itemnos = new ArrayList<Integer>();
        HashMap<Integer,Integer> items = new HashMap<>();
        for(int i = 1; i <= nitems; i ++){
            itemnos.add(i);
        }
        for(int j = 0; j < nritems; j ++){
            int ind = rand.nextInt(nitems - j);
            itemno = itemnos.remove(ind);                
            int q = rand.nextInt(2) + 1;
            items.put(itemno, q);
        }
        return items;
    }
    private static void printUsers() {
        System.out.println("Customers:");
        for(User c: customers){
            System.out.println("cust" + c.getname() + "\t" + "area" + c.getAddress());
        }
        System.out.println("des:");
        for(DE de: des){
            System.out.println("de" + de.getname() + "\t" + "area" + de.getAddress());
        }
        System.out.println("Restaurants:");
        for(Restaurant r: restaurants){
            System.out.println("res" + r.getName() + "\t" + "area" + r.getAddress());
            System.out.println(r.getItems());
        }
    }

    static ArrayList<Integer> getRestaurantItems(int restnum) {
        return new ArrayList<Integer>(restaurants.get(restnum - 1).getItemList());
    }

    static Restaurant getRestaurant(int restnum) {
        return restaurants.get(restnum - 1);
    }

    private static void startOrdering() {
        for(User c: customers){
            c.start();
            try{
            c.join();
            }catch(Exception e){
                System.out.println("thread c problem. " + e);
            }
        }
    }

    private static void startDelivering() {
        for(DE de: des){
            de.start();
        }
        for(DE de: des){
        try{
                de.join();
            }
            catch(Exception e){
                System.out.println("de error:" + e);
            }
        }
    }

    public synchronized static int assignOrder(int name, int a,int btime) {
        int time = 10000000;
        int cName = 1;
        for(User c: customers){
            if(c.isDone()&&!c.isDelivered()){
                int a1 = c.getAddress();
                int a2 = restaurants.get(c.getRestnum()-1).getAddress();
                int tempTime = 10*(Math.abs(a-a2)+Math.abs(a1-a2));
                if(tempTime<time){
                    time = tempTime;
                    cName = c.getname();
                }
            }
        }
        if(time == 10000000){
            return btime;
        }
        customers.get(cName-1).setDelivered(true);
        System.out.println("de" + name + " is delivering from rest" + 
                customers.get(cName-1).getRestnum() + " to cust" + cName 
                + " in " + (time+btime) + " minutes.");
        return time+btime;
    }

}
