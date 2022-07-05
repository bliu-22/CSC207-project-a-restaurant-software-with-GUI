package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class Restaurant that simulates a restaurant with
 * menu, inventory, tables and staffs. */
public class Restaurant {

    // HashMap of all Servers in this Restaurant with Server's Id as key and Server as value.
    public HashMap<Integer, Server> servers = new HashMap<>();

    // HashMap of all Chefs in this Restaurant with Chef's Id as key and Chef as value.
    public HashMap<Integer, Chef> chefs = new HashMap<>();

    // The Manager of this Restaurant.
    private Manager manager;

    // HashMap of all Courses in this Restaurant with Course Name as key and Course as value.
    public HashMap<String, Course> menu = new HashMap<>();

    // HashMap of all Tables in this Restaurant with Table number as key and each Table as value.
    public HashMap<Integer, Table> tables = new HashMap<>();

    // This Restaurant's revenue (unset).
    private double revenue = 0.00;

    /* HashMap of all the orders in this Restaurant with
       Table number as key and ArrayList<String> of bill's information as value. */
    public HashMap<Integer, ArrayList<String>> orderHistory = new HashMap<>();

    // The current Employee user.
    public Employee activeEmployee;

    // Create a new Single static Restaurant.
    private static Restaurant restaurant = new Restaurant();

    /**
     * Use of Singleton Design Pattern to get this restaurant.
     *
     * @return this restaurant.
     */
    public static Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * @return a double which is the revenue of this Restaurant.
     */
    public double getRevenue() {
        return this.revenue;
    }

    public HashMap<String, Course> getMenu() {
        return menu;
    }

    /**
     * @return the Inventory of this Restaurant.
     */
    public Inventory getInventory(){
        return Inventory.getInventory();
    }

    /**
     * Add the amount received to the revenue of the restaurant.
     *
     * @param payment the payment it received.
     */
    void addToRevenue(double payment) {
        this.revenue += payment;
    }


    /**
     * A method called at the end to update
     * the configuration files and generate a request file.
     */
    public void start() {
        ConfigReader reader = new ConfigReader();
        reader.generateRestaurant();
    }

    /**
     * A method called at the end to update
     * the configuration files and generate a request file.
     */
    public boolean end() {
        if (this.isVacant()) {
            new ConfigWriter().writeToConfig();
            return true;
        } else {
            Logger.getGlobal().log(Level.WARNING,"There're customers still eating.");
            return false;
        }
    }

    public Manager getManager() {
        return this.manager;
    }

    /**
     * Add a Manager with given name and id to the Restaurant.
     * Nothing will be added and a message will be printed
     * when there exists a duplicate id.
     *
     * @param name the name of the Manager to be added
     * @param id   the id of the Manager to be added
     */
    void setManager(String name, int id) {
        this.manager = new Manager(name, id);
    }

    /**
     * Return true if there is no costumers in this Table,
     * otherwise return false.
     *
     * @return true if there is no costumers in this Table, else false.
     */
    private boolean isVacant() {
        for (Map.Entry<Integer, Table> entry : this.tables.entrySet()) {
            if (entry.getValue().checkStatus()) {
                return false;
            }
        }
        return true;
    }
}
