package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class PaymentReceiver that makes the Servers
 * receive payments from each Table of customers. */
class PaymentReceiver {
    private final BillGenerator billGenerator = new BillGenerator();

    /**
     * Receive payment for the table
     *
     * @param table   the table that is paying
     * @param payment the amount paid
     * @return true if the payment is enough, false if the payment is not enough
     */
    boolean receivePayment(Table table, double payment) {
        // Get the total amount table should pay after (potential)service fee and tax.
        return receivePayment(table, payment, 0);

    }


    /**
     * Receive payment for separate bill.
     *
     * @param table    the table the customer is on.
     * @param payment  the amount paid.
     * @param customer the person paying.
     * @return true/false depending on whether the payment is enough.
     */
    boolean receivePayment(Table table, double payment, int customer) {
        // determine the total amount this person should pay.
        double total = table.getTotal(customer);
        if (table.orderTotal.size() >= 8) {
            total = (double) Math.round(total * 1.15 * 100) / 100;
        }
        total = (double) Math.round(total * 1.13 * 100) / 100;
        //check if the payment is enough.
        if (payment < total) {
            return false;
        } else {
            Restaurant.getRestaurant().addToRevenue(payment);
            Restaurant.getRestaurant().orderHistory.get(table.getTableNumber())
                    .add(billGenerator.generateBill(table, customer));
            table.getServer().receiveTip((double) Math.round((payment - total)* 100) / 100);
            this.completeOrder(table, customer);
            String message;
            if (customer == 0){
                message = "Table " + table.getTableNumber() + " paid its bill together.";
            }else {
                message = "Customer " + customer + " on table " + table.getTableNumber();
                message = message + " paid his/her bill.";
            }
            Logger.getGlobal().log(Level.INFO, message);
            return true;
        }
    }

    /**
     * Complete all course this person paid for so that it will no longer appear in a bill.
     *
     * @param table    the table the person is on
     * @param customer the person paying
     */
    private void completeOrder(Table table, int customer) {
        if (customer == 0) {
            for (Map.Entry<Integer, ArrayList<Course>> entry : table.orderPlaced.entrySet()) {
                this.completeOrder(table, entry.getKey());
            }
        } else {
            for (Course course : table.orderPlaced.get(customer)) {
                if (course.getStatus().equals("Delivered and accepted")) {
                    course.changeStatus("Completed");
                }
            }
        }
    }
}
