package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class InvManagementController implements Initializable {
    @FXML
    public Label invInfo;
    @FXML
    public Label feedback;
    @FXML
    public TextField ingredientName, ingredientQty, ingredientPrice;
    @FXML
    public Restaurant restaurant = Restaurant.getRestaurant();
    @FXML
    private Manager manager = (Manager) restaurant.activeEmployee;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invInfo.setText(manager.checkInventory());

    }

    @FXML
    public void addToInvButtonPushed() {

        try {

            String ingName = ingredientName.getText().trim();
            String ingQty = ingredientQty.getText().trim();

            if (ingName.equals("")) {
                feedback.setText("Ingredient name cannot be empty");
            } else {
                int unit = Integer.parseInt(ingQty);
                manager.receiveIngredient(ingName, unit);
                feedback.setText(ingQty + " unit(s) of " + ingName +
                        " has been successfully added to the inventory");
                invInfo.setText(manager.checkInventory());
            }
        } catch (Exception e) {
            feedback.setText("Quantity must be an integer");
        }
    }

    @FXML
    public void setLowButtonPushed() {
        try {

            String ingName = ingredientName.getText().trim();
            String ingQty = ingredientQty.getText().trim();

            if (ingName.equals("")) {
                feedback.setText("Ingredient name cannot be empty");
            } else {
                double unit = Double.valueOf(ingQty);
                manager.setIngLimit(ingName, unit);
                feedback.setText("The low limit of " + ingName +
                        " has been set to " + ingQty);
                invInfo.setText(manager.checkInventory());
            }
        } catch (Exception e) {
            feedback.setText("Quantity must be a number");
        }
    }

    @FXML
    public void setPriceButtonPushed() {
        try {

            String ingName = ingredientName.getText().trim();
            String ingPrice = ingredientPrice.getText().trim();

            if (ingName.trim().equals("")) {
                feedback.setText("Ingredient name cannot be empty");
            } else {
                double price = Double.valueOf(ingPrice);
                manager.setIngPrice(ingName, price);

                feedback.setText("The price of " + ingName +
                        " has been set to " + ingPrice);
                invInfo.setText(manager.checkInventory());
            }
        } catch (Exception e) {
            feedback.setText("Price must be a number");
        }
    }

    @FXML
    public void requestButtonPushed() {
        manager.generateRequest();
        feedback.setText("Requests have been generated. Please refer to requests.txt");
    }
}
