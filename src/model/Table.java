package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class Table that represents a table in the restaurant. */
public class Table {

    // The Table's number.
    private int tableNumber;

    // Increment the old Table number by 1 to generate a new Table number.
    private static int numOfTable;

    // A HashMap of customers as keys and Courses that ordered by each customer as value.
    public HashMap<Integer, ArrayList<Course>> orderPlaced;

    // A HashMap of customers as keys and total amount of payments for each customer as value.
    public HashMap<Integer, Double> orderTotal;

    // The current Server who serves this Table.
    private Server server;

    // The size of this Table.
    private int size;

    // Check if this Table has assigned already.
    private boolean isOccupied = false;


    /**
     * Construct an instance of Table.
     *
     * @param tableSize int the size of the table.
     */
    public Table(int tableSize) {
        this.tableNumber = numOfTable + 1;
        this.orderPlaced = new HashMap<>();
        numOfTable++;
        this.size = tableSize;
        orderTotal = new HashMap<>();
        Restaurant.getRestaurant().orderHistory.put(tableNumber, new ArrayList<>());
    }

    /**
     * @return the Table number of this Table
     */
    public int getTableNumber() {
        return this.tableNumber;
    }

    /**
     * @return return the order of this Table in the form of a HashMap.
     */
    public HashMap<Integer, ArrayList<Course>> getOrderPlaced() {
        return orderPlaced;
    }

    /**
     * @return the status of this Table.
     */
    public boolean checkStatus() {
        return this.isOccupied;
    }


    /**
     * @param customer the customer number, 0 for default.
     * @return the total amount of payment for the customer of given customer number.
     * If the customer number is 0, then return the total amount of payment for the whole Table.
     */
    double getTotal(int customer) {
        double total = 0.0;
        if (customer == 0) {
            for (Map.Entry<Integer, ArrayList<Course>> entry : orderPlaced.entrySet()) {
                for (Course course : entry.getValue()) {
                    total += course.getFinalPrice();
                }
            }
        } else {
            for (Course course : orderPlaced.get(customer)) {
                total += course.getFinalPrice();
            }
        }
        return total;
    }

    /**
     * Get the size of this Table.
     *
     * @return int size of this Table.
     */
    public int getSize() {
        return this.size;
    }


    /**
     * Add a course to the order list of this Table.
     *
     * @param course   the Course that the customer orders.
     * @param customer the customer number of this Table.
     */
    void addToOrder(Course course, int customer) {
        if (!(this.orderPlaced.size() == 0)) {
            course.setTable(this);
            course.setCustomerNumber(customer);
            this.orderPlaced.get(customer).add(course);
        } else {
            Logger.getGlobal().log(Level.WARNING,"Waiting for server.");
        }
    }

    /**
     * Add some number of customer to the Table.
     *
     * @param number is the number of customer to be added.
     *               Precondition: number <= this.size
     */
    void addCustomer(int number) {
        if (number <= size && number > 0) {
            for (int i = 1; i <= number; i++) {
                orderPlaced.put(i, new ArrayList<>());
                orderTotal.put(i, 0.00);
            }
            isOccupied = true;
        }
    }

    /**
     * Accept a course that is delivered to this Table.
     *
     * @param course Course that is delivered to this Table.
     */
    void acceptCourse(Course course) {
        if (orderPlaced.get(course.getCustomerNumber()).contains(course)) {
            course.changeStatus("Delivered and accepted");
            orderTotal.replace(course.getCustomerNumber(), orderTotal.get(course.getCustomerNumber())
                    + course.getFinalPrice());
            String info = course.getName() + " for customer " + course.getCustomerNumber();
            info = info + " on table " + course.getTable().getTableNumber() + " is accepted.";
            Logger.getGlobal().log(Level.INFO, info);
        }
    }

    /**
     * Add the server serves this Table.
     *
     * @param server Server serves this Table.
     */
    void addServer(Server server) {
        this.server = server;
    }

    /**
     * Remove server serving for this Table.
     */
    private void removeServer() {
        this.server = null;
    }

    /**
     * @return Server serving for this Table
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Reject the course delivered to this Table.
     *
     * @param course  Course delivered to this Table.
     * @param message String the reason why you reject the course.
     */
    void rejectCourse(Course course, String message) {
        String reason = "";
        if (!message.equals("")) {
            reason = " since " + message;
        }
        course.changeStatus("Rejected" + reason);
        Chef.returnedCourses.add(course);
        String info = course.getName() + " for table " + this.getTableNumber() + ", customer ";
        info = info + course.getCustomerNumber() + " is returned since:" + message;
        Logger.getGlobal().log(Level.INFO, info);
    }

    /**
     * Change the availability of the Table.
     */
    void changeAvailability() {
        this.isOccupied = !this.isOccupied;
    }

    /**
     * Reset this Table.
     */
    void reset() {
        for (Map.Entry<Integer, ArrayList<Course>> entry : orderPlaced.entrySet()) {
            for (Course course : entry.getValue()) {
                if (!course.getStatus().equals("Completed") && !course.getStatus().equals("Cancelled")) {
                    Logger.getGlobal().log(Level.WARNING, "Table " + tableNumber + "  is not finished.");
                    return;
                }
            }
        }
        this.isOccupied = false;
        this.removeServer();
        this.orderPlaced = new HashMap<>();
        this.orderTotal = new HashMap<>();
    }

    /**
     * @return a String representing the status of the order
     */
    public String getOrderStatus() {
        StringBuilder result = new StringBuilder();
        result.append("The Order Status of Table ").append(this.tableNumber);
        result.append(System.lineSeparator());
        if (!this.orderPlaced.isEmpty()) {
            for (Map.Entry<Integer, ArrayList<Course>> entry : this.orderPlaced.entrySet()) {
                for (Course course1 : entry.getValue()) {
                    result.append(course1.getName()).append(" : ").append(course1.getStatus());
                    result.append(System.lineSeparator());
                }
            }
            return result.toString();
        } else {
            return "Does not have any orders or dishes yet !!";
        }
    }

    @Override
    public String toString() {
        String status = isOccupied ? "Occupied" : "Vacant";
        return tableNumber + " " + status;
    }
}
