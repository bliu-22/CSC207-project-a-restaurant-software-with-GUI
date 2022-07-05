package model;

import java.util.logging.Level;
import java.util.logging.Logger;

/*Class Person with a name, id and a boolean variable that
 indicates whether this Person is available.*/
public abstract class Employee {

    // This Employee's name.
    private String name;

    // This Employee's Id.
    private int id;

    // Check if this Employee is available at this moment.
    private boolean isAvailable = true;

    /**
     * Construct an instance of Person.
     *
     * @param name String name of this person.
     * @param id Integer id of this person.
     */
    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * @return String name of this person.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return int id of this person.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return boolean availability of this person.
     */
    public boolean getStatus() {
        return this.isAvailable;
    }

    /**
     * Change the availability of this person.
     */
    void changeStatus() {
        this.isAvailable = !this.isAvailable;
    }

    /**
     * Receive the ingredient send to the restaurant.
     * @param name String name of the ingredient send to the restaurant.
     * @param units int number of the ingredient send to the restaurant.
     */
    public void receiveIngredient(String name, int units) {
        Inventory inventory = Inventory.getInventory();
        inventory.addToInventory(name, units);
        if (inventory.getRequestedItem().contains(name)) {
            inventory.getRequestedItem().remove(name);
        }
        Logger.getGlobal().log(Level.INFO, this.name + " received " + units + " units of " + name);
    }

    @Override
    public String toString() {
        return "Name: " + name + " ID: " + String .valueOf(id);
    }
}