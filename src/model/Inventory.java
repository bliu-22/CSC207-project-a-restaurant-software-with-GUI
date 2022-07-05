package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Class Inventory that represents the ingredients and their quantities in a HashMap. */
public class Inventory {

    // ingredientInfo{"Name"=[lowLevel, unitsLeft, price]}
    private HashMap<String, double[]> ingredientInfo = new HashMap<>();

    /* All ingredients have less materials and the Manager need
       to send a message to buy more of those materials. */
    private static ArrayList<String> ItemRequested = new ArrayList<>();

    // Create a new single static Inventory
    private static Inventory inventory = new Inventory();

    /**
     * Use of Singleton Design Pattern to get this Inventory
     *
     * @return this Inventory
     */
    public static Inventory getInventory() {
        return inventory;
    }


    public HashMap<String, double[]> getContent() {
        return this.ingredientInfo;
    }

    /**
     * Returns the price of a ingredient with given name
     *
     * @param name a String representing the name of the ingredient to get price of.
     * @return the price of a ingredient as a double
     */
    double getPrice(String name) {
        if (this.ingredientInfo.containsKey(name)) {
            return this.ingredientInfo.get(name)[2];
        } else {
            return 0.0;
        }
    }

    /**
     * Return a ArrayList of ingredients that are requested
     *
     * @return ArrayList that contains the ingredients requested.
     */
    ArrayList<String> getRequestedItem() {
        return ItemRequested;
    }

    /**
     * Add an ingredient to ItemRequested.
     *
     * @param newRequestedItem the name of the ingredient to be added.
     */
    void addRequestedItem(String newRequestedItem) {
        ItemRequested.add(newRequestedItem);
    }

    /**
     * Add an ingredient with given name and number of units to Inventory
     *
     * @param name the name of the ingredient
     * @param units the number of units of the ingredient
     */
    void addToInventory(String name, double units) {
        if (this.ingredientInfo.containsKey(name.trim())) {
            this.ingredientInfo.get(name.trim())[1] += units;
        } else {
            double[] d = new double[3];
            d[1] = units;
            this.ingredientInfo.put(name.trim(), d);
        }
    }

    /**
     * Set low limit for an ingredient
     *
     * @param name the name of the ingredient to set low limit
     * @param low the low limit to be set
     */
    void setLowLimit(String name, double low) {
        if (this.ingredientInfo.containsKey(name)) {
            this.ingredientInfo.get(name)[0] = low;
        } else {
            double[] d = new double[3];
            d[0] = low;
            this.ingredientInfo.put(name, d);
        }
    }

    /**
     * Set the price of a given ingredient
     *
     * @param name the name of the ingredient
     * @param price the price to be set
     */
    void setPrice(String name, double price) {
        if (this.ingredientInfo.containsKey(name)) {
            this.ingredientInfo.get(name)[2] = price;
        } else {
            double[] d = new double[3];
            d[2] = price;
            this.ingredientInfo.put(name, d);
        }
    }

    /**
     * Consume certain units of a given ingredient
     *
     * @param name the ingredient's name to be used
     * @param numUse the amount of ingredient used
     */
    void useIngredient(String name, int numUse) {
        if (hasEnough(name, numUse)) {
            this.ingredientInfo.get(name)[1] -= numUse;
        }
    }

    /**
     * Check if there is enough number of a given ingredient
     *
     * @param name the name of the ingredient to check
     * @param needed the number given as a standard
     * @return a boolean is returned to tell whether or not the ingredient is enough
     */
    boolean hasEnough(String name, double needed) {
        return this.ingredientInfo.containsKey(name) && needed <= this.ingredientInfo.get(name)[1];
    }

    /**
     * Return an ArrayList of ingredients that are below their low limit.
     *
     * @return the ArrayList that contains ingredients below low limit.
     */
    ArrayList<String> getLow() {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, double[]> entry : this.ingredientInfo.entrySet()) {
            if (entry.getValue()[1] < entry.getValue()[0]) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Remaining ingredients: ");
        result.append(System.lineSeparator());
        boolean b = false;
        for (Map.Entry<String, double[]> entry : this.ingredientInfo.entrySet()) {
            result.append(entry.getKey()).append(" ~~ ");
            result.append(" Units Left: ").append(entry.getValue()[1]).append(", ");
            result.append(" Low Limit: ").append(entry.getValue()[0]).append(", ");
            result.append(" Price: ").append(entry.getValue()[2]);
            result.append(System.lineSeparator());
            b = true;
        }
        if (!b) {
            return "Does not have any ingredients yet !! Please addToInventory first !!";
        }
        return result.toString();
    }

}
