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
    private static HashMap<Integer,DE> des;
    private static HashMap<Integer,DE> bdes;
    private static ArrayList<Restaurant> restaurants;
    private static ArrayList<Order> orders;
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
                while(scanVal==0){
                    System.out.println("Input must be > 0");
                    scanVal = sc.nextInt();
                }
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
        des = new HashMap<Integer,DE>();
        
        //busy delivery boys
        bdes = new HashMap<Integer,DE>();
        // arraylist to hold all res names, addresses and items
        restaurants = new ArrayList<Restaurant>();
        
        //arraylist of all orders
        orders = new ArrayList<>();
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
            System.out.println(des);
        startOrdering();
            System.out.println(des);
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
            des.put(i, de);
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
        System.out.println("des:\n"+ des);
//        for(DE de: des){
//            System.out.println("de" + de.getname() + "\t" + "area" + de.getAddress());
//        }

        System.out.println("Restaurants:");
        for(Restaurant r: restaurants){
            System.out.println("res" + r.getName() + "\t" + "area" + r.getAddress());
            System.out.println(r.getItems());
        }
        ArrayList<Integer> ual[];
        ual = new ArrayList[nareas];
        ArrayList<Integer> ral[];
        ral = new ArrayList[nareas];
        ArrayList<Integer> deal[];
        deal = new ArrayList[nareas];
        for(int i=0;i<10;i++){
            ual[i] = new ArrayList<>();
            ral[i] = new ArrayList<>();
            deal[i] = new ArrayList<>();
        }
        for(User c: customers){
            ual[c.getAddress() - 1].add(c.getname());
        }
        for(Map.Entry<Integer,DE> de: des.entrySet()){
            deal[de.getValue().getAddress() - 1].add(de.getKey());
        }
        for(Restaurant r: restaurants){
            ral[r.getAddress() - 1].add(r.getName());
        }
        System.out.format("%5s %10s %10s %10s\n","area","cust","res","de");
        for(int i=0;i<10;i++){
            System.out.format("%5s %10s %10s %10s\n",(i+1),ual[i],ral[i],deal[i]);
        }
//        System.out.println("orders:");
//        for(Order o : orders){
//            System.out.println(o);
//        }
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
        for(Order o: orders){
            o.start();
            try{
                o.join();
            }
            catch(Exception e){}
        }
    }
    public static void addOrder(Order o){
        orders.add(o);
    }
    public static int getRestaurantAddress(int restNum){
        return restaurants.get(restNum-1).getAddress();
    }


    public synchronized static int findDE(int cAdd, int rAdd) {
        int min = 100000;
        int dName = 0;
//        int dos = 10000;
//            System.out.println("des:"+des);
//            System.out.println("bdes:"+bdes);
        for(Map.Entry<Integer,DE> de: des.entrySet()){
//            if(((dos > d.getAvlTime()) ||
//                ((min> (Math.abs(d.getAddress() - rAdd)+Math.abs(cAdd - rAdd))) 
//                    && dos == d.getAvlTime()))){
//                min = Math.abs(d.getAddress()-rAdd) + Math.abs(cAdd-rAdd);
//                dName = d.getname();
//                dos = d.getAvlTime();
//            }
            int tempTime = Math.abs(de.getValue().getAddress() - rAdd)+Math.abs(cAdd - rAdd);
            if(min>tempTime){
                dName = de.getKey();
                min = tempTime;
            }
        }
        if(dName !=0){
//            des.get(dName-1).incAvlTime(min);
            des.get(dName).setAddress(cAdd);
            des.get(dName).setDeliveryTime(min);
            bdes.put(dName,des.remove(dName));
            System.out.println("des:"+des);
            System.out.println("bdes:"+bdes);
            Thread t = new Thread(bdes.get(dName));
            t.start();
        }
        return dName;
    }

    static void addToAvailable(int name) {
        des.put(name,bdes.remove(name));
    }

}
