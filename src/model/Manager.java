package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class Manager, inherited from class Person. */
public class Manager extends Employee {

    // This Restaurant.
    private Restaurant restaurant = Restaurant.getRestaurant();

    /**
     * Construct an instance of Manager.
     *
     * @param name String the name of the manager.
     * @param id int the id of the manager.
     */
    public Manager(String name, int id) {
        super(name, id);
    }

    /**
     * Check the inventory and print it.
     */
    public String checkInventory() {
        return Inventory.getInventory().toString();
    }

    /**
     * Generate a request by a specific amount which is 20.
     */
    public void generateRequest() {
        Inventory inventory = Inventory.getInventory();
        try {
            FileWriter fw = new FileWriter("src/requests.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            if (inventory.getLow().size() == 0) {
                bw.write("No ingredient is running low at this moment.");
            } else {
                StringBuilder LowIng = new StringBuilder();
                for (int i = 0; i < inventory.getLow().size(); i++) {
                    if (!(inventory.getRequestedItem().contains(inventory.getLow().get(i)))) {
                        LowIng = LowIng.append("Ingredient name:")
                                .append(inventory.getLow().get(i))
                                .append("          Qty needed: ")
                                .append("20")
                                .append(System.lineSeparator());
                        inventory.addRequestedItem(inventory.getLow().get(i));
                    }
                }
                bw.write(LowIng.toString());
            }
            bw.close();
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING,"Error writing to file 'requests.txt'");
        }
    }

    /**
     * Add a Table to the Restaurant with given tableSize.
     * Nothing will be added and a message will be printed
     * when the size provided is invalid.
     *
     * @param tableSize the specified size of table being added.
     */
    public void addTable(int tableSize) {
        if (tableSize == 2 | tableSize == 4 | tableSize == 8 | tableSize == 10) {
            Table table = new Table(tableSize);
            this.restaurant.orderHistory.put(table.getTableNumber(), new ArrayList<>());
            this.restaurant.tables.put(table.getTableNumber(), table);
        } else {
            Logger.getGlobal().log(Level.WARNING,"Invalid size of table");
        }
    }

    /**
     * @return All the orders that finished cooking by Chefs, but not yet delivered by the Servers.
     */
    public String trackOrder(){
        if (Server.orderCooked.size()!=0) {
            StringBuilder result = new StringBuilder("");
            for (int i = 0; i < Server.orderCooked.size(); i++) {
                Course curCourse = Server.orderCooked.get(i);
                result.append(curCourse.getName()).append(" for Customer ")
                        .append(curCourse.getCustomerNumber()).append(" on Table ").
                        append(curCourse.getTable().getTableNumber()).append(System.lineSeparator());
            }
            return result.toString();
        }
        return "No undelivered order(s) at this moment";
    }

    /**
     * Remove a Table from the Restaurant with given tableNumber.
     * Nothing will be removed and a message will be printed
     * when the tableNumber provided does not exist.
     *
     * @param tableNumber the specified Table being removed.
     */
    public String removeTable(int tableNumber) {
        if (this.restaurant.tables.containsKey(tableNumber)) {

            this.restaurant.tables.remove(tableNumber);
            return "Table " + tableNumber + " has been removed";

        } else {
            return "The Table Number does not exist";
        }
    }

    /**
     * Add a Chef with given name and id to the Restaurant.
     * Nothing will be added and a message will be printed when
     * there exists a duplicate id.
     *
     * @param name the Chef to be added
     * @param id the Chef to be added
     */
    public String addChef(String name, int id) {
        if (restaurant.chefs.containsKey(id)) {
            return "ID already exists";
        } else {
            restaurant.chefs.put(id, new Chef(name, id));
            return "Chef: " + name + " ID: " + id + System.lineSeparator() + " has been added";
        }
    }

    /**
     * Remove a Chef with given id from the Restaurant.
     * Nothing will be removed and a message will be printed
     * when no matching id is found
     *
     * @param id of the Chef to be removed
     */
    public void removeChef(int id) {
        if (restaurant.chefs.containsKey(id)) {
            restaurant.chefs.remove(id);
        }
    }

    /**
     * Add a Server with given name and id to the Restaurant.
     * Nothing will be added and a message will be printed
     * when there exists a duplicate id.
     *
     * @param name of the Server to be added
     * @param id of the Server to be added
     */
    public String addServer(String name, int id) {
        if (restaurant.servers.containsKey(id)) {
            return "ID already exists";
        } else {
            restaurant.servers.put(id, new Server(name, id));
            return "Server: " + name + " ID: " + id + System.lineSeparator() + " has been added";
        }
    }

    /**
     * Remove a Server with given id from the Restaurant.
     * Nothing will be removed and a message will be printed
     * when no matching id is found
     *
     * @param id of the Server to be removed
     */
    public void removeServer(int id) {
        if (restaurant.servers.containsKey(id)) {
            restaurant.servers.remove(id);
        }

    }

    /**
     * Add a course to the menu by passing in a line with the same format as in menu.txt
     * which is specified in README.txt
     *
     * @param line one line with a format of a course.
     */
    public void addToMenu(String line) {
        String[] course = line.split("\\|");
        if (course.length == 3) {
            for (int i = 0; i < course.length; i++) {
                course[i] = course[i].trim();
            }
            String name = course[0];
            double price = Double.valueOf(course[1]);
            String[] ingredients = course[2].split(";");
            HashMap<String, Integer> ing = new HashMap<>();
            for (String thing : ingredients) {
                ing.put(thing.split(":")[0].trim(),
                        Integer.parseInt(thing.split(":")[1].trim()));
            }
            Course entry = new Course(name, price, ing);
            if (this.restaurant.menu.containsKey(name)) {
                Logger.getGlobal().log(Level.WARNING,name + " is already in menu.");
            } else {
                this.restaurant.menu.put(name, entry);
            }
        } else {
            Logger.getGlobal().log(Level.WARNING,"Not valid input for course !!");
        }
    }

    /**
     * Remove a Course from this menu by giving the name of a Course.
     *
     * @param name a Course to be remove.
     */
    public void removeCourse(String name) {
        if (this.restaurant.menu.containsKey(name)) {
            this.restaurant.menu.remove(name);
        } else {
            Logger.getGlobal().log(Level.WARNING,name + " is not in menu.");
        }
    }
    public void setIngPrice(String name, double price) { this.restaurant.getInventory().setPrice(name, price); }
    public void setIngLimit(String name, double unit) { this.restaurant.getInventory().setLowLimit(name,unit); }
}
