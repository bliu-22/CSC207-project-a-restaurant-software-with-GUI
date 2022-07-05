package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class Server, inherited from class Person. */
public class Server extends Employee {
    // An ArrayList<Course> which takes all the Courses that have been cooked by the Chefs.
    public static ArrayList<Course> orderCooked = new ArrayList<>();

    // The tips from Tables with number of people greater than or equal to 8.
    private double tipReceived;

    // A BillGenerator to get the bills for each Tables of customers.
    private BillGenerator billEditor = new BillGenerator();

    // A PaymentReceiver to receive the payments from each Table of customers.
    private PaymentReceiver paymentReceiver = new PaymentReceiver();

    // The Table that this Server is currently serving.
    public Table tableOn;

    // The Course that this Server is currently delivering or working on with.
    public Course courseEditing;


    /**
     * Constructs an instance of the class Server.
     *
     * @param name the Server's name.
     * @param id the Server's name.
     */
    public Server(String name, int id) {
        super(name, id);
    }

    /**
     * Add customer(s) to a table int.
     *
     * @param numOfCustomer the number of customer.
     * @param table the designated table to add customer(s) to.
     */
    public void addCustomer(int numOfCustomer, int table) {
        Restaurant restaurant = Restaurant.getRestaurant();
        Table table1 = restaurant.tables.get(table);
        if (table1.getSize() >= numOfCustomer && !table1.checkStatus()) {
            table1.changeAvailability();
            table1.addCustomer(numOfCustomer);
            table1.addServer(this);
        } else if (table1.checkStatus()) {
            Logger.getGlobal().log(Level.SEVERE,"This table is not available right now.");
        }
    }

    /**
     * Add a course to the order list of given Table.
     *
     * @param table the Table that the Server is taking order on.
     * @param customer the customer number of this Table.
     * @param course the Course that this customer orders.
     */
    public boolean addToOrder(Table table, int customer, Course course) {
        if (Chef.checkAvailability(course)) {
            table.addToOrder(course, customer);
            Chef.recordOrCancelUseOfIngredient(course, true);
            return true;
        } else {
            Logger.getGlobal().log(Level.INFO, course.getName() +" is not available");
            return false;
        }
    }

    public void editOrder(Course course, String ingredient, boolean addOrSubtract){
        // determine if the inventory has enough ingredient to add
        if ((!Inventory.getInventory().hasEnough(ingredient, 1)) && !addOrSubtract) {
            return;
        }
        //Changes the ingredient in the inventory
        if (addOrSubtract) {
            Inventory.getInventory().useIngredient(ingredient, 1);
        } else {
            Inventory.getInventory().addToInventory(ingredient, 1);
        }
        //make the change
        course.addOrRemoveIngredients(ingredient, addOrSubtract);
    }

    /**
     * Let a Table finish Ordering and send the orders of
     * the given Table to Chefs to cook.
     *
     * @param table the Table that the Server is taking order on.
     */
    public void finishOrdering(Table table){
        for (Map.Entry<Integer, ArrayList<Course>> entry : table.getOrderPlaced().entrySet())
        for (Course course : entry.getValue()){
            if (course.getStatus().equals("Created")){
                course.changeStatus("Waiting");
                Chef.addToOrderList(course);
                String info = "Customer " + entry.getKey() + " on table " + table.getTableNumber();
                info = info + " ordered " + course.getName();
                Logger.getGlobal().log(Level.INFO, info);
            }
        }
    }

    /**
     * Cancel a course from the order list of given Table.
     * The waitList from the Chefs will also cancel this Course.
     *
     * @param table the Table that the Server is canceling order on.
     * @param course the Course that this customer wants to cancel.
     */
    public void cancelOrder(Table table, Course course) {
        String info = "Course " + course.getName() + " for customer ";
        info = info + course.getCustomerNumber() + " on table " + table.getTableNumber() + " is cancelled.";
        switch (course.getStatus()) {
            case "Created":
                course.changeStatus("Cancelled");
                Chef.recordOrCancelUseOfIngredient(course, false);
                Logger.getGlobal().log(Level.INFO, info);
                break;
            case "Waiting":
                course.changeStatus("Cancelled");
                Chef.waitList.remove(course);
                Chef.recordOrCancelUseOfIngredient(course, false);
                Logger.getGlobal().log(Level.INFO, info);
                break;
            default:
                Logger.getGlobal().log(Level.INFO, "Cannot cancel " +
                        course.getName() + " for table " + table.getTableNumber());
                break;
        }
    }

    /**
     * Get the first Course in waitList to be delivered.
     */
    public void getDelivering(){
        if (!orderCooked.isEmpty()) {
            this.courseEditing = orderCooked.get(0);
        }
    }

    /**
     * Deliver a course to the Table.
     *
     * @param accepted whether or not the course is accepted.
     */
    public void deliverCourse(boolean accepted) {
        deliverCourse(accepted, "");
    }

    /**
     * Deliver a course to the Table.
     *
     * @param accepted whether or not the course is accepted.
     * @param message  the additional message when rejecting a course.
     */
    public void deliverCourse(boolean accepted, String message) {
        if (accepted) {
            courseEditing.getTable().acceptCourse(courseEditing);
            orderCooked.remove(courseEditing);
        } else {
            courseEditing.getTable().rejectCourse(courseEditing, message);
            orderCooked.remove(courseEditing);
        }
    }

    /**
     * Print out one bill for the given Table.
     *
     * @param table the Table to get bill for.
     */
    public String getBill(Table table) {
        billEditor.setLineLength(25);
        return billEditor.generateBill(table);
    }

    /**Print the bill for the given customer on the given Table.
     *
     * @param table the Table the customer is on.
     * @param customer the customer to get the bill for.
     * @return the bill needed.
     */
    public String getBill(Table table, int customer) {
        billEditor.setLineLength(24);
        return billEditor.generateBill(table, customer);
    }

    /**
     * Receive payment for the Table.
     *
     * @param table the Table that is paying.
     * @param payment the amount paid.
     * @return true if the payment is enough, false if the payment is not enough.
     */
    public boolean receivePayment(Table table, double payment) {
        return paymentReceiver.receivePayment(table, payment);
    }

    /**
     * Receive payment for separate bill.
     *
     * @param table the Table the customer is on.
     * @param payment the amount paid.
     * @param customer the person paying.
     * @return true/false depending on whether the payment is enough.
     */
    public boolean receivePayment(Table table, double payment, int customer){
        return paymentReceiver.receivePayment(table, payment, customer);
    }

    /**
     * The amount of fees that gave by the customers.
     *
     * @param amount the amount of tips pass to the Server.
     */
    void receiveTip(double amount){
        this.tipReceived += amount;
    }

    /**
     * Clean up a Table.
     *
     * @param table the Table to clean up.
     */
    public void cleanTable(Table table) {
        table.reset();
        Logger.getGlobal().log(Level.INFO,"Table" + table.getTableNumber() + " is cleaned up.");
    }

    @Override
    public String toString() {
        return super.toString() + " Tip: " + tipReceived;
    }
}