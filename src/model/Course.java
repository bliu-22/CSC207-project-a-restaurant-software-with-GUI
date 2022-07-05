package model;


import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/* Class Course that represents a course offered by the Restaurant. */
public class Course {

    // The name of this Course.
    private String name;

    // The status of this Course shows "Order Placed" at the beginning.
    private String status = "Created";

    // The price of this Course with no extra materials on this course.
    private double price;

    // The Table that ordered this Course.
    private Table table;

    // an unique customer number that tells which customer does this Course belongs to.
    private int customerNumber;

    /* An HashMap representing the initial amount of ingredients required to make this Course
       with the ingredient name as key and the number of this ingredient needed as value. */
    public HashMap<String, Integer> ingredients;

    /* An HashMap of requirements that add to this Course
       with the ingredient name as key and the number of this ingredient need to add as value*/
    private HashMap<String, Integer> requirements = new HashMap<>();

    /**
     * Constructs an instance of class Course.
     *
     * @param name the Course's name.
     * @param price the price of this Course.
     * @param ingredients an HashMap with ingredient's name as key and ingredient's amount as value.
     */
    public Course(String name, double price, HashMap<String, Integer> ingredients) {
        this.name = name;
        if (!ingredients.isEmpty()) {
            this.ingredients = ingredients;
        }
        this.price = price;
    }

    /**
     * @return the name of this Course in String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the price of this Course in double.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @return the customer number that tells which customer does this Course belongs to.
     */
    public int getCustomerNumber() {
        return this.customerNumber;
    }

    /**
     * Set a customer number that tells which customer does this Course belongs to.
     */
    void setCustomerNumber(int number) {
        this.customerNumber = number;
    }

    /**
     * @return the initial amount of ingredients required to make this Course.
     */
    public HashMap<String, Integer> getIngredients() {
        return this.ingredients;
    }

    /**
     * @return the Table that ordered this Course.
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Set a Table that ordered this Course.
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Add or subtract a specific ingredient from this Course by 1 unit.
     *
     * @param ingredient the ingredient to be added or subtracted form this Course.
     * @param addOrSubtract determines to add or remove ingredient
     */
    void addOrRemoveIngredients(String ingredient, boolean addOrSubtract) {
        //determine to add or to subtract.
        int change = addOrSubtract ? 1 : -1;

        // Changes the ingredient that this course need.
        if (this.ingredients.containsKey(ingredient)) {
            int originalValue = this.ingredients.get(ingredient);
            if (change == 1 || this.ingredients.get(ingredient) > 1) {
                ingredients.replace(ingredient, originalValue + change);
            } else {
                ingredients.remove(ingredient);
            }
        } else {
            ingredients.put(ingredient, change);
        }

        // Record all ingredient changes in a hash map.
        if (requirements.containsKey(ingredient)) {
            requirements.replace(ingredient, requirements.get(ingredient) + change);
        } else {
            requirements.put(ingredient, change);
        }
    }

    /**
     * If this course's status is "Delivered and accepted",
     * then return the total price in double of this Course and the materials add to this Course.
     * Otherwise return 0.0
     *
     * @return the total price in double of this Course and the materials add to this Course
     *         if this course's status is "Delivered and accepted", otherwise return 0.0
     */
    double getFinalPrice() {
        double result = this.price;
        if (getStatus().equals("Delivered and accepted")) {
            for (Map.Entry<String, Integer> entry : this.requirements.entrySet()) {
                if (entry.getValue() > 0) {
                    double price = Inventory.getInventory().getPrice(entry.getKey());
                    result += price * entry.getValue();
                }
            }
            return result;
        } else {
            return 0.0;
        }
    }

    /**
     * Change the status of this Course.
     *
     * @param status a String representing the status of this Course
     */
    void changeStatus(String status) {
        this.status = status;
    }

    /**
     * Create a duplicate copy of this Course
     *
     * @return A copy of this Course will be returned
     */
    public Course createDuplicate() {
        HashMap<String, Integer> ing = new HashMap<>();
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            ing.put(entry.getKey(), entry.getValue());
        }
        return new Course(this.name, this.price, ing);
    }

    /**
     * @return String representing the status of this Course.
     */
    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Formatter fmt = new Formatter(result);
        fmt.format("%-25s%5s%n", this.name, this.price);
        for (Map.Entry<String, Integer> entry : this.requirements.entrySet()) {
            if (entry.getValue() > 0) {
                double price = Inventory.getInventory().getPrice(entry.getKey());
                fmt.format("%-25s%5s", " " + entry.getValue() + " " + entry.getKey(),price * entry.getValue());
            } else if (entry.getValue() < 0) {
                fmt.format("%-25s", " " + entry.getValue() + " " + entry.getKey());
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
}
