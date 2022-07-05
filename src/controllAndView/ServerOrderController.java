package controllAndView;

import javafx.fxml.FXML;
import model.Restaurant;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.Course;
import model.Server;
import model.Table;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class ServerOrderController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML Table table = server.tableOn;
    @FXML public ChoiceBox<String> selectBar;
    @FXML public ListView<Course> menu;
    @FXML public ListView<Course> courseOrdered;
    @FXML public Label message;
    @FXML public Button addRequire;
    @FXML public Button back;
    @FXML public Button finishOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i = 1;
        while (i <= table.getOrderPlaced().size()){
            selectBar.getItems().add(Integer.toString(i));
            i++;
        }
        refreshMenu();
        refreshCourseOrdered();
    }

    @FXML
    public void addCourseClicked(){
        if (selectBar.getValue() == null){
            message.setText("Select a num plz.");
        } else if (menu.getSelectionModel().getSelectedItems().size() == 0) {
            message.setText("Select a course plz");
        } else {
            int id = Integer.parseInt((selectBar.getValue()));
            Course course = (menu.getSelectionModel().getSelectedItems().get(0)).createDuplicate();
            boolean succeed = server.addToOrder(table, id, course);
            if (succeed) {
                courseOrdered.getItems().add(course);
            }
        }
    }

    @FXML
    public void addRequireClicked() throws Exception{
        if (courseOrdered.getSelectionModel().getSelectedItems().size() == 0) {
            message.setText("Select a course plz");
        } else {
            server.courseEditing = courseOrdered.getSelectionModel().getSelectedItems().get(0);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerRequirement.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) addRequire.getScene().getWindow()).setScene(scene);
        }
    }

    @FXML
    public void backButtonPushed() throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)back.getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void refreshMenu(){
        menu.getItems().clear();
        for (Map.Entry<String, Course> entry : restaurant.getMenu().entrySet()) {
            menu.getItems().add(entry.getValue());
        }
    }

    @FXML
    public void refreshCourseOrdered(){
        courseOrdered.getItems().clear();
        for (Map.Entry<Integer, ArrayList<Course>> entry : table.getOrderPlaced().entrySet()) {
            for (int k = 0; k < entry.getValue().size(); k++){
                if (!entry.getValue().get(k).getStatus().equals("Cancelled")) {
                    courseOrdered.getItems().add(entry.getValue().get(k));
                }
            }
        }
    }

    @FXML
    public void cancelPushed(){
        if (courseOrdered.getSelectionModel().getSelectedItems().size() == 0) {
            message.setText("Select a course plz");
        } else {
            Course course =courseOrdered.getSelectionModel().getSelectedItems().get(0);
            server.cancelOrder(server.tableOn, course);
            refreshCourseOrdered();
        }
    }

    @FXML
    public void finishOrderPushed() throws Exception{
        server.finishOrdering(server.tableOn);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)finishOrder.getScene().getWindow()).setScene(scene);
    }
}
