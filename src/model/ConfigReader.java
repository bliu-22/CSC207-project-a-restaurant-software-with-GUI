package model;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class ConfigReader that reads the restaurant's
 * configuration from restaurant.txt, menu.txt and inventory.txt */
class ConfigReader {

    // A path to the restaurant, inventory and configuration file.
    private final String restaurantConfiguration = "src/restaurant.txt";
    private final String inventoryConfiguration = "src/inventory.txt";
    private final String menuConfiguration = "src/menu.txt";

    // Create a file by the given path for restaurant, inventory and menu.
    private File resConfig = new File(restaurantConfiguration);
    private File invConfig = new File(inventoryConfiguration);
    private File menuConfig = new File(menuConfiguration);

    // The Inventory of this Restaurant.
    private Inventory inventory = Inventory.getInventory();

    // This Restaurant.
    private Restaurant restaurant = Restaurant.getRestaurant();


    /**
     * A method to set up the Restaurant.
     */
    void generateRestaurant() {
        //Add a default manager before config is read, if it reads a manager later on, the info will then be changed.
        restaurant.setManager("admin", 1);
        if (this.resConfig.exists()) {
            this.generateStaffAndTable();
        }
        if (this.invConfig.exists()) {
            this.generateInventory();
        }
        if (this.menuConfig.exists()) {
            this.generateMenu();
        }
    }

    /**
     * A method that adds employees and tables to the Restaurant
     * accordingly by reading lines from restaurant.txt.
     */
    private void generateStaffAndTable() {
        String line;
        try {
            FileReader fileReader = new FileReader(new File(this.restaurantConfiguration));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] s = line.split("\\|");
                for (int i = 0; i < s.length; i++) {
                    s[i] = s[i].trim();
                }

                switch (s[0]) {
                    case "TableFor2":
                        for (int j = 0; j < Integer.valueOf(s[1]); j++) {
                            this.restaurant.getManager().addTable(2);
                        }
                        break;
                    case "TableFor4":
                        for (int j = 0; j < Integer.valueOf(s[1]); j++) {
                            this.restaurant.getManager().addTable(4);
                        }
                        break;
                    case "TableFor8":
                        for (int j = 0; j < Integer.valueOf(s[1]); j++) {
                            this.restaurant.getManager().addTable(8);
                        }
                        break;
                    case "TableFor10":
                        for (int j = 0; j < Integer.valueOf(s[1]); j++) {
                            this.restaurant.getManager().addTable(10);
                        }
                        break;
                    case "Server":
                        this.restaurant.getManager().addServer(s[1], Integer.valueOf(s[2]));
                        break;
                    case "Manager":
                        this.restaurant.setManager(s[1], Integer.valueOf(s[2]));
                        break;
                    case "Chef":
                        this.restaurant.getManager().addChef(s[1], Integer.valueOf(s[2]));
                        break;
                    default:
                        Logger.getGlobal().log(Level.WARNING,"Invalid input in" + this.restaurantConfiguration);
                        break;
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to open file '" + this.restaurantConfiguration + "'");
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error reading file '" + this.restaurantConfiguration + "'");
        }
    }

    /**
     * A method that adds ingredients to Restaurant's Inventory
     * accordingly by reading lines from inventory.txt
     */
    private void generateInventory() {
        String line;
        try {
            FileReader fileReader = new FileReader(new File(this.inventoryConfiguration));

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] ingredient = line.split("\\|");
                for (int i = 0; i < ingredient.length; i++) {
                    ingredient[i] = ingredient[i].trim();
                }
                this.inventory.addToInventory(ingredient[0], Double.valueOf(ingredient[1]));
                this.inventory.setLowLimit(ingredient[0], Double.valueOf(ingredient[2]));
                this.inventory.setPrice(ingredient[0], Double.valueOf(ingredient[3]));
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to open file '" + this.inventoryConfiguration + "'");
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error reading file '" + this.inventoryConfiguration + "'");
        }
    }

    /**
     * A method that adds items to menu accordingly by reading lines from menu.txt
     */
    private void generateMenu() {
        String line;
        try {
            FileReader fileReader = new FileReader(new File(this.menuConfiguration));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                this.restaurant.getManager().addToMenu(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to open file '" + this.inventoryConfiguration + "'");
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error reading file '" + this.inventoryConfiguration + "'");
        }
    }
}