package model;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConfigWriter {

    // The Inventory of this Restaurant.
    private Inventory inventory = Inventory.getInventory();

    // This Restaurant.
    private Restaurant restaurant = Restaurant.getRestaurant();

    /**
     * A method that updates the configuration files through
     * replacing the old config info with current ones.
     */
    void writeToConfig() {
        this.writeToInventoryConfiguration();
        this.writeToMenuConfiguration();
        this.writeToRestaurantConfiguration();
    }

    /**
     * A method that updates the inventory configuration files
     * through replacing the old inventory config info with current ones.
     */
    private void writeToInventoryConfiguration() {
        String inventoryConfiguration = "src/inventory.txt";
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(inventoryConfiguration)))) {
            for (Map.Entry<String, double[]> entry : inventory.getContent().entrySet()) {
                String line = entry.getKey() + " | " + entry.getValue()[1] +
                        " | " + entry.getValue()[0] + " | " + entry.getValue()[2] +
                        System.lineSeparator();
                out.print(line);
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error writing to file '" + inventoryConfiguration + "'");
        }
    }

    /**
     * A method that updates the menu configuration files
     * through replacing the old menu config info with current ones.
     */
    private void writeToMenuConfiguration() {
        String menuConfiguration = "src/menu.txt";
        try (PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(menuConfiguration)))) {
            for (Map.Entry<String, Course> entry : this.restaurant.menu.entrySet()) {
                StringBuilder line = new StringBuilder();
                line.append(entry.getKey()).append(" | ").append(entry.getValue().getPrice()).append(" | ");
                for (Map.Entry<String, Integer> ingredient : entry.getValue().ingredients.entrySet()) {
                    line.append(ingredient.getKey()).append(":").append(ingredient.getValue()).append(" ; ");
                }
                String result = line.toString();
                result = result.substring(0, result.length() - 3);
                result = result + System.lineSeparator();
                out2.print(result);
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error writing to file '" + menuConfiguration + "'");
        }
    }

    /**
     * Return the Information of the employees in this restaurant in String.
     *
     * @return the Information of the employees in this restaurant in String.
     */
    private String reportEmployees() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, Server> entry1 : restaurant.servers.entrySet()) {
            stringBuilder.append("Server | ").append(entry1.getValue().getName()).append(" | ")
                    .append(Integer.toString(entry1.getKey())).append(System.lineSeparator());
        }
        for (Map.Entry<Integer, Chef> entry2 : restaurant.chefs.entrySet()) {
            stringBuilder.append("Chef | ").append(entry2.getValue().getName()).append(" | ")
                    .append(Integer.toString(entry2.getKey())).append(System.lineSeparator());
        }
        stringBuilder.append("Manager | ").append(restaurant.getManager().getName()).append(" | ")
                .append(Integer.toString(restaurant.getManager().getId())).append(System.lineSeparator());
        return stringBuilder.toString();
    }

    /**
     * A method that updates the restaurant configuration files
     * through replacing the old restaurant config info with current ones.
     */
    private void writeToRestaurantConfiguration() {
        String restaurantConfiguration = "src/restaurant.txt";
        try (PrintWriter out3 = new PrintWriter(new BufferedWriter(new FileWriter(restaurantConfiguration)))) {
            int TableFor2 = 0;
            int TableFor4 = 0;
            int TableFor8 = 0;
            int TableFor10 = 0;
            for (Map.Entry<Integer, Table> entry : restaurant.tables.entrySet()) {
                if (entry.getValue().getSize() == 2) {
                    TableFor2++;
                }
                if (entry.getValue().getSize() == 4) {
                    TableFor4++;
                }
                if (entry.getValue().getSize() == 8) {
                    TableFor8++;
                }
                if (entry.getValue().getSize() == 10) {
                    TableFor10++;
                }
            }
            String result = "TableFor2 | " + Integer.toString(TableFor2) +
                    System.lineSeparator() +
                    "TableFor4 | " + Integer.toString(TableFor4) +
                    System.lineSeparator() +
                    "TableFor8 | " + Integer.toString(TableFor8) +
                    System.lineSeparator() +
                    "TableFor10 | " + Integer.toString(TableFor10) +
                    System.lineSeparator() +
                    reportEmployees();
            out3.print(result);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error writing to file '" + restaurantConfiguration + "'");
        } catch (NullPointerException e){
            Logger.getGlobal().log(Level.SEVERE, "Table does not exist.");
        }
    }
}
