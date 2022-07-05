package controllAndView;

import javafx.fxml.FXML;
import model.Restaurant;
import model.Table;
import model.Server;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSceneController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML private Table table = server.tableOn;
    @FXML public ChoiceBox<String> selectBar;
    @FXML public Label message;
    @FXML public Button addCustomer;
    @FXML public Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i = 1;
        while (i <= table.getSize()){
            selectBar.getItems().add(Integer.toString(i));
            i++;
        }
    }

    @FXML
    public void addCustomerPushed() throws Exception{
        if(selectBar.getValue() == null){
            message.setText("You must choose a number.");
        } else {
            server.addCustomer(Integer.parseInt((selectBar.getValue())), table.getTableNumber());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrder.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((Stage)addCustomer.getScene().getWindow()).setScene(scene);
        }
    }

    @FXML
    public void backButtonPushed() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)back.getScene().getWindow()).setScene(scene);
    }
}
