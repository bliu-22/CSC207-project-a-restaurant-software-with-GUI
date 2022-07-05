package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Class Chef, inherited from class Person. */
public class Chef extends Employee {

    // The Courses that the Chefs are going to cooking.
    static ArrayList<Course> waitList = new ArrayList<>();

    // The Courses that the Chefs are going to recooking.
    static ArrayList<Course> returnedCourses = new ArrayList<>();

    // The Course this Chef is cooking.
    private Course courseCooking = null;

    /**
     * Construct a Chef with a given name and id.
     *
     * @param name this Chef's name.
     * @param id   this Chef's ID.
     */
    public Chef(String name, int id) {
        super(name, id);
    }

    /**
     * @return the Names in the WaitList as an ArrayList<String>.
     */
    public static ArrayList<String> getWaitListNames() {
        ArrayList<String> courseName = new ArrayList<>();
        for (Course course : Chef.waitList) {
            courseName.add(course.getName());
        }
        return courseName;
    }


    /**
     * @return the Names in the ReturnCourses as an ArrayList<String>.
     */
    public static ArrayList<String> getReturnCoursesNames() {
        ArrayList<String> courseName = new ArrayList<>();
        for (Course course : Chef.returnedCourses) {
            courseName.add(course.getName());
        }
        return courseName;
    }

    /**
     * @return the Course that this chef is cooking right now.
     */
    public Course getCourseCooking() {
        return this.courseCooking;
    }

    /**
     * Add a Course to orderPlaced for Chefs to cook.
     *
     * @param course the Course that need to be add
     *               to orderPlaced for Chefs to cook.
     */
    static void addToOrderList(Course course) {
        waitList.add(course);
    }

    /**
     * Let this chef Cook the first course in the static ArrayList called waitList.
     */
    public void startCooking() {
        if ((!waitList.isEmpty()) && this.getStatus()) {
            Course cooking = waitList.get(0);
            cooking.changeStatus("Being Cooked");
            this.courseCooking = cooking;
            waitList.remove(0);
            this.changeStatus();
            String info = "Chef " + this.getName() + " started cooking " + courseCooking.getName();
            info = info + " for table " + courseCooking.getTable().getTableNumber();
            Logger.getGlobal().log(Level.INFO, info);
        } else if (waitList.isEmpty()) {
            Logger.getGlobal().log(Level.WARNING, "No course is waiting to be cooked.");
        } else {
            Logger.getGlobal().log(Level.WARNING,"This chef is not available.");
        }
    }

    /**
     * Let this chef recook the first course in the static ArrayList called returnedCourses.
     */
    public void dealWithReturnedCourse() {
        if ((!returnedCourses.isEmpty()) && this.getStatus()) {
            this.courseCooking = returnedCourses.get(0);
            returnedCourses.remove(0);
            this.changeStatus();
        } else if (returnedCourses.isEmpty()) {
            Logger.getGlobal().log(Level.WARNING,"No returned course to be cooked.");
        } else {
            Logger.getGlobal().log(Level.WARNING,"This chef is not available.");
        }
    }

    /**
     * This chef finishes the course he is working on.
     * the course he is working on is moved to the static ArrayList
     * for Server to be delivered.
     */
    public void finishCooking() {
        if (!this.getStatus()) {
            String info = courseCooking.getName() + " for table";
            info = info + courseCooking.getTable().getTableNumber() + " is cooked by Chef ";
            info = info + this.getName() + " and waiting for delivery.";
            Logger.getGlobal().log(Level.INFO, info);
            this.courseCooking.changeStatus("Ready for delivering");
            Server.orderCooked.add(this.courseCooking);
            this.courseCooking = null;
            this.changeStatus();
        }
    }

    /**
     * Check if there is enough ingredient for the course.
     *
     * @param course the course he is checking.
     * @return true if there is enough ingredient and false if
     * there is not enough ingredient.
     */
    static boolean checkAvailability(Course course) {
        Inventory inventory = Inventory.getInventory();
        for (Map.Entry<String, Integer> entry : course.ingredients.entrySet()) {
            if (!inventory.hasEnough(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Use or return the use of the ingredient of this course.
     * Remove the ingredient when use the
     *
     * @param course the course to be cooked ot returned
     * @param use    input true to use ingredient and return otherwise.
     */
    static void recordOrCancelUseOfIngredient(Course course, boolean use) {
        Inventory inventory = Inventory.getInventory();
        if (use) {
            for (Map.Entry<String, Integer> entry : course.ingredients.entrySet()) {
                inventory.useIngredient(entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, Integer> entry : course.ingredients.entrySet()) {
                inventory.addToInventory(entry.getKey(), ((double) entry.getValue()));
            }
        }
    }
}
