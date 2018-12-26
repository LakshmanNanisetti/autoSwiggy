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
    private static ArrayList<User> des;
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
        des = new ArrayList<User>();
        
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
//        User u = new User(11,11);
//        
//        
//        u.start();
//        try{
//        u.join();
//        }catch(Exception e){
//            System.out.println("thread u problem. " + e);
//        }
        startOrdering();
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
        User de;
        for(int i = 1; i <= nde; i ++){
            areaNo = rand.nextInt(nareas) + 1;
            de = new User(i, areaNo);
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
        for(User de: des){
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

}
