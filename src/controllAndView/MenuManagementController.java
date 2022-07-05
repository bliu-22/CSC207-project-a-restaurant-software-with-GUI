package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.*;


import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuManagementController implements Initializable {
    @FXML
    public TextField line;
    @FXML
    public ListView<String> menuList;
    @FXML
    public Label feedback;
    @FXML
    public Restaurant restaurant = Restaurant.getRestaurant();
    @FXML
    public Manager manager = (Manager) restaurant.activeEmployee;

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

        for (Map.Entry<String, Course> entry : restaurant.menu.entrySet()) {
            String info = entry.getKey() + ": $" + String.valueOf(entry.getValue().getPrice());
            menuList.getItems().add(info);
        }
    }

    @FXML
    public void addButtonPushed() {
        try {
            manager.addToMenu(line.getText());
            refresh();

        } catch (Exception e) {
            feedback.setText("Invalid format, please refer to the sample format");
        }
    }

    @FXML
    public void removeButtonPushed() {
        String itemSelected = (menuList.getSelectionModel().getSelectedItem());
        String courseToRemove = itemSelected.substring(0, itemSelected.indexOf(":"));
        manager.removeCourse(courseToRemove);
        refresh();
        feedback.setText(courseToRemove + " has been removed");
    }

    /**
     * A helper to refresh the ListView
     */
    private void refresh() {
        menuList.getItems().clear();
        for (Map.Entry<String, Course> entry : restaurant.menu.entrySet()) {
            String info = entry.getKey() + ": $" + String.valueOf(entry.getValue().getPrice());
            menuList.getItems().add(info);
        }
    }

}
