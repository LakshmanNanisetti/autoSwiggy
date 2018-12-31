/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swiggy;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lakshman-pt2712
 */
public class Swiggy implements Values {


    public static class Ordering implements Runnable {

        private boolean ordersSuspended;

        public Ordering() {
            ordersSuspended = false;
            pendingOrders = 0;
        }

        public void run() {

            for (User c : customers) {
                c.start();
                ++pendingOrders;
                sd.resume();
                while (ordersSuspended) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Swiggy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        public void suspend() {
            ordersSuspended = true;
        }

        public void resume() {
            ordersSuspended = false;
            notify();
        }
    }

    public static class Delivering implements Runnable {

        private boolean noOrders;

        public Delivering() {
            noOrders = false;
        }

        public void run() {
            while (true) {
                while (noOrders && (pendingOrders != nc)) {
                    try {
                        wait(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Swiggy.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                while (newOrders.size() != 0) {
//                                            System.out.println("i am waiting here");
                    ArrayList<Order> tempOrders = new ArrayList<>(newOrders);
                    for (Order o : tempOrders) {
                        o.start();
                        System.out.println("order: " + o + " is started");
                        orders.add(o);
//                        newOrders.remove(o);
                    }
                    newOrders.removeAll(tempOrders);
                }
                if (pendingOrders == nc && orders.size() == nc) {
                        System.out.println("entered waiting "+ orders.size());
                      int i=0;
                        for(Order o: orders){
                            i++;
                          try {
                              System.out.println("before join of");
                              o.join();
                              System.out.println("after join of "+i);
                          } catch (InterruptedException ex) {
                              Logger.getLogger(Swiggy.class.getName()).log(Level.SEVERE, null, ex);
                          }
                      }
                      break;
                }
            }
        }

        public void suspend() {
            noOrders = true;
        }

        public void resume() {
            noOrders = false;
            notify();
        }
    }
    /**
     * @param args the command line arguments
     */
    private static Thread so, sd;
    private static int pendingOrders;
    private static int nc, nr, nde;
    private static Scanner sc;
    private static ArrayList<User> customers;
    private static HashMap<Integer, DE> des;
    private static HashMap<Integer, DE> bdes;
    private static ArrayList<Restaurant> restaurants;
    private static ArrayList<Order> newOrders, orders;
    private static Random rand;
    private static ArrayList<Integer> ual[], ral[], deal[];

    public static int getNr() {
        return nr;
    }

    public static int scanInt() {
        /*
        To take input only an integer.
         */
        int scanVal;
        while (true) {
            try {
                scanVal = sc.nextInt();
                while (scanVal == 0) {
                    System.out.println("Input must be > 0");
                    scanVal = sc.nextInt();
                }
                break;
            } catch (InputMismatchException ime) {
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
        des = new HashMap<Integer, DE>();

        //busy delivery boys
        bdes = new HashMap<Integer, DE>();
        // arraylist to hold all res names, addresses and items
        restaurants = new ArrayList<Restaurant>();

        //arraylist of all orders
        newOrders = new ArrayList<>();
        orders = new ArrayList<>();
        
        //des
        System.out.println("enter the no of customers:");
        nc = scanInt();
        System.out.println("enter the no of delivery executives:");
        nde = scanInt();
        System.out.println("enter the no of restaurants:");
        nr = scanInt();

        pendingOrders = 0;
        initDes();
        initRestaurants();
        initCustomers();
        initAreaLists();
        printUsers();
//        startOrdering();
//        startDelivering();
        so = new Thread(new Ordering());
        sd = new Thread(new Delivering());
        so.start();
        sd.start();
        try {
            so.join();
            System.out.println("stopped starting orders");
            sd.join();
            System.out.println("stopped asking deliversie");
        } catch (InterruptedException ex) {
            Logger.getLogger(Swiggy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void initCustomers() {
        int areaNo;
        User cust;
        for (int i = 1; i <= nc; i++) {
            areaNo = rand.nextInt(nareas) + 1;
            cust = new User(i, areaNo);
            customers.add(cust);
        }
    }

    private static void initDes() {
        int areaNo;
        DE de;
        for (int i = 1; i <= nde; i++) {
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
        for (int i = 1; i <= nr; i++) {
            areaNo = rand.nextInt(nareas) + 1;
            r = new Restaurant(i, areaNo);
            r.setItems(initItems());
            restaurants.add(r);
        }
    }

    private static HashMap<Integer, Integer> initItems() {
        /*
        This method will be called from initRestaurants method.
        it will select "nritems" no of items from "nitems" no of total items
        and assigns each of the "nritems" items a quantity of either "1" or "2".
         */
        int itemno;
        ArrayList<Integer> itemnos = new ArrayList<Integer>();
        HashMap<Integer, Integer> items = new HashMap<>();
        for (int i = 1; i <= nitems; i++) {
            itemnos.add(i);
        }
        for (int j = 0; j < nritems; j++) {
            int ind = rand.nextInt(nitems - j);
            itemno = itemnos.remove(ind);
            int q = rand.nextInt(2) + 1;
            items.put(itemno, q);
        }
        return items;
    }

    public static void initAreaLists() {
        ual = new ArrayList[nareas];
        ral = new ArrayList[nareas];
        deal = new ArrayList[nareas];
        for (int i = 0; i < 10; i++) {
            ual[i] = new ArrayList<>();
            ral[i] = new ArrayList<>();
            deal[i] = new ArrayList<>();
        }
        for (User c : customers) {
            ual[c.getAddress() - 1].add(c.getname());
        }
        for (Map.Entry<Integer, DE> de : des.entrySet()) {
            deal[de.getValue().getAddress() - 1].add(de.getKey());
        }
        for (Restaurant r : restaurants) {
            ral[r.getAddress() - 1].add(r.getName());
        }
    }

    private static void printUsers() {

        System.out.println("Customers:");
        for (User c : customers) {
            System.out.println("cust" + c.getname() + "\t" + "area" + c.getAddress());
        }
        System.out.println("des:\n" + des);
//        for(DE de: des){
//            System.out.println("de" + de.getname() + "\t" + "area" + de.getAddress());
//        }

        System.out.println("Restaurants:");
        for (Restaurant r : restaurants) {
            System.out.println("res" + r.getName() + "\t" + "area" + r.getAddress());
            System.out.println(r.getItems());
        }

        System.out.format("%5s %10s %10s %10s\n", "area", "cust", "res", "de");
        for (int i = 0; i < 10; i++) {
            System.out.format("%5s %10s %10s %10s\n", (i + 1), ual[i], ral[i], deal[i]);
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
        for (User c : customers) {
            c.start();

        }
        for (User c : customers) {
            try {
                c.join();
            } catch (Exception e) {
                System.out.println("thread c problem. " + e);
            }
        }
    }

    private static void startDelivering() {
        System.out.println("orders:" + orders);
        for (Order o : newOrders) {
            o.start();
        }
        for (Order o : newOrders) {
            try {
                o.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Swiggy.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public synchronized static void addOrder(Order o) {
        newOrders.add(o);
    }

    public static int getRestaurantAddress(int restNum) {
        return restaurants.get(restNum - 1).getAddress();
    }

    public synchronized static int findDE(int cAdd, int rAdd) {
        int min = 100000;
        int dName = 0;
        for (Map.Entry<Integer, DE> de : des.entrySet()) {
            int tempTime = Math.abs(de.getValue().getAddress() - rAdd) + Math.abs(cAdd - rAdd);
            if (min > tempTime) {
                dName = de.getKey();
                min = tempTime;
            }
        }
        if (dName != 0) {
            des.get(dName).setAddress(cAdd);
            des.get(dName).setDeliveryTime(min);
            bdes.put(dName, des.remove(dName));
//            System.out.println("des:" + des);
//            System.out.println("bdes:" + bdes);
            Thread t = new Thread(bdes.get(dName));
            System.out.println("de" + dName + " is delivering for customer");
            t.start();
            
        }
        if (bdes.size() == nde) {
            so.suspend();
        }
        return dName;
    }

    public static void addToAvailable(int name) {
        des.put(name, bdes.remove(name));
        so.resume();
    }

}
