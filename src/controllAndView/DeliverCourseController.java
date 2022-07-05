package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Restaurant;
import model.Server;
import model.Table;
import java.net.URL;
import java.util.ResourceBundle;

public class DeliverCourseController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML public TextArea message;
    @FXML public Button yes;
    @FXML public Button no;
    @FXML public Label courseInfo;

    @FXML
    public void yesButtonPushed() throws Exception{
        server.deliverCourse(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)yes.getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void noButtonPushed() throws Exception{
        String text = message.getText();
        server.deliverCourse(false, text);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)no.getScene().getWindow()).setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String name = server.courseEditing.getName();
        int cusNum = server.courseEditing.getCustomerNumber();
        Table table = server.courseEditing.getTable();
        String info = "Course Name : " + name + System.lineSeparator() +
                "Table Number : " + table.getTableNumber() + System.lineSeparator() +
                "Customer Number : " + cusNum + System.lineSeparator();
        courseInfo.setText(info);
    }
}
