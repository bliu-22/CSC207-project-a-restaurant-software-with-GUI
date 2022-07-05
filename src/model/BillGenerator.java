package model;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Map;

/* Class BillGenerator that generates the bills for each Table of customers. */
class BillGenerator {
    private int lineLength = -20;

    /**
     * Set the length of the lines that this BillGenerator generates.
     *
     * @param length the length of each line.
     */
    void setLineLength(int length) {
        lineLength = -length;
    }

    /**
     * generate a single bill for the given table.
     *
     * @param table is the table to generate bill for.
     * @return the string representation of the bill.
     */
    String generateBill(Table table) {
        StringBuilder result = new StringBuilder();
        // Generate the header for the bill
        result.append(generateLine("Bill for table :", Integer.toString(table.getTableNumber())));
        result.append(generateLine("Table of :", Integer.toString(table.orderTotal.size())));
        result.append(generateLine("Server : ", table.getServer().getName()));
        // Fill in the content of the bill
        // Each course and requirements added will be added to the bill
        for (Map.Entry<Integer, ArrayList<Course>> entry : table.orderPlaced.entrySet()) {
            for (Course course : entry.getValue()) {
                if (course.getStatus().equals("Delivered and accepted")) {
                    result.append(course.toString());
                }
            }
        }
        // Add the summary of the bill.
        result.append(generateBillSummary(table, 0));
        return result.toString();
    }

    /**
     * Generate bill for a customer on the table.
     *
     * @param table    the table the customer is on.
     * @param customer the customer to generate bill for.
     * @return the bill.
     */
    String generateBill(Table table, int customer) {
        // The format of this bill is similar to the bill generated above.
        if (customer == 0){
            return generateBill(table);
        }
        StringBuilder result = new StringBuilder();
        result.append(generateLine("Bill for table :", Integer.toString(table.getTableNumber())));
        result.append(generateLine("Customer : ", Integer.toString(customer)));
        result.append(generateLine("Table of :", Integer.toString(table.orderTotal.size())));
        result.append(generateLine("Server : ", table.getServer().getName()));
        for (Course course : table.orderPlaced.get(customer)) {
            if (course.getStatus().equals("Delivered and accepted")) {
                result.append(course.toString());
            }
        }
        result.append(generateBillSummary(table, customer));
        return result.toString();
    }

    /**
     * A helper to make formatting String easier.
     *
     * @param start the word at the start of the line.
     * @param end   the word at the end of the line.
     * @return a line with a specific length.
     */
    private String generateLine(String start, String end) {
        StringBuilder str = new StringBuilder();
        Formatter fmt = new Formatter(str);
        fmt.format("%" + lineLength + "s%5s%n", start, end);
        return str.toString();
    }

    /**
     * Generate tax, service fee, and total of the bill.
     *
     * @param table    table this bill is for
     * @param customer the customer the bill is for
     * @return the string representing the final part of a bill.
     */
    private String generateBillSummary(Table table, int customer) {
        double total = table.getTotal(customer);
        double serviceFee = 0;
        double tax;
        StringBuilder result = new StringBuilder();
        if (table.orderTotal.size() >= 8) {
            serviceFee = (double) Math.round(table.getTotal(0) * 0.15 * 100) / 100;
            result.append(generateLine("Service fee(15%): ", Double.toString(serviceFee)));
        }
        tax = (double) Math.round((table.getTotal(0) + serviceFee) * 0.13 * 100) / 100;
        total += serviceFee + tax;
        total = (double) Math.round(total * 100) / 100;
        result.append(generateLine("Tax: ", Double.toString(tax)));
        result.append(generateLine("Total : ", Double.toString(total)));
        return result.toString();
    }
}
