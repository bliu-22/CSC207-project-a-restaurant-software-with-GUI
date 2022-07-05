package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Restaurant;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import model.Server;
import model.Course;
import javafx.scene.control.ListView;

public class ServerRequirementController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML private Course course = server.courseEditing;
    @FXML public ListView<String> existingList;
    @FXML public ListView<String> availableList;
    @FXML public Button back;
    @FXML public Label message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshExistingList();
        refreshAvailableList();
    }

    @FXML
    public void removeButtonPushed(){
        if(existingList.getSelectionModel().getSelectedItems().size() == 0) {
            message.setText("Select an ingredient plz.");
        } else {
            String click = existingList.getSelectionModel().getSelectedItems().get(0);
            int index = click.indexOf(":");
            String name = click.substring(0, index - 1);
            server.editOrder(course, name, false);
            refreshAvailableList();
            refreshExistingList();
        }
    }

    @FXML
    public void addButtonPushed(){
        if(availableList.getSelectionModel().getSelectedItems().size() == 0) {
            message.setText("Select an ingredient plz.");
        } else {
            String click = availableList.getSelectionModel().getSelectedItems().get(0);
            int index = click.indexOf(":");
            String name = click.substring(0, index - 1);
            server.editOrder(course, name, true);
            refreshAvailableList();
            refreshExistingList();
        }
    }

    @FXML
    public void backButtonPushed() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrder.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)back.getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void refreshAvailableList(){
        availableList.getItems().clear();
        for (Map.Entry<String, double[]> entry : restaurant.getInventory().getContent().entrySet()) {
            availableList.getItems().add(entry.getKey() + " : " + entry.getValue()[1]);
        }
    }

    @FXML
    public void refreshExistingList(){
        existingList.getItems().clear();
        for (Map.Entry<String, Integer> entry : course.getIngredients().entrySet()) {
            existingList.getItems().add(entry.getKey() + " : " + entry.getValue());
        }
    }
}
