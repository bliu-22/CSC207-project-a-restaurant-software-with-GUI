package controllAndView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceiveInventoryController {

    @FXML public TextField ingredientName, ingredientQuantity;

    @FXML public Label feedBack;

    public Restaurant restaurant = Restaurant.getRestaurant();


    /**
     * The warning pop-up window.
     *
     * @param msg the warning massage that gets displayed by the pop-up window.
     */
    private void warning(String msg) {
        //Code adapted from http://code.makery.ch/blog/javafx-dialogs-official/ retrieved in Nov 2017
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void addToInventoryButtonPushed() {
        try {
            String ingName = this.ingredientName.getText().trim();
            String ingQty = this.ingredientQuantity.getText().trim();
            if (ingName.equals("")) {
                this.feedBack.setText("Ingredient name cannot be empty !!");
                warning("Ingredient name cannot be empty !!");
            } else {
                int units = Integer.parseInt(ingQty);
                if (restaurant.activeEmployee instanceof Server) {
                    Server server = (Server) restaurant.activeEmployee;
                    server.receiveIngredient(ingName, units);
                } else {
                    Chef chef = (Chef) restaurant.activeEmployee;
                    chef.receiveIngredient(ingName, units);
                }
                this.feedBack.setText(ingQty + " unit(s) of " + ingName +
                        " has been successfully added to the inventory.");
            }
        } catch (Exception e) {
            this.feedBack.setText("Quantity must be an integer !!");
            warning("Quantity must be an integer !!");
        }
    }

    @FXML
    public void closeButtonPushed(ActionEvent event) throws IOException {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}
