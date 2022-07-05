package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Restaurant;
import model.Server;
import model.Table;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class BillSceneController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML private Table table = server.tableOn;
    @FXML public ChoiceBox<String> selectBar;
    @FXML public Label bill;
    @FXML private String billString;
    @FXML public TextField cash;
    @FXML public Label message;
    @FXML private boolean finishBill = true;
    @FXML public Button receive;
    @FXML public Button finish;
    @FXML private int billsPayed;
    @FXML public Button getBill;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Map.Entry<Integer, Double> entry : table.orderTotal.entrySet()) {
            if (entry.getValue() != 0.0){
                selectBar.getItems().add(Integer.toString(entry.getKey()));
                finishBill = false;
            }
        }
        if (finishBill) {
            getBill.setDisable(true);
            message.setText("Bills are successfully payed.");
        } else {
            selectBar.getItems().add("All");
        }
        receive.setDisable(true);
    }

    @FXML
    public void getBillPushed(){
        if (selectBar.getValue() == null){
            message.setText("Select whose bill you wanna get.");
        } else {
            if (selectBar.getValue().equals("All")) {
                billString = server.getBill(table);
            } else {
                billString = server.getBill(table, Integer.parseInt(( selectBar.getValue())));
            }
            bill.setText(billString);
            receive.setDisable(false);
        }
    }

    @FXML
    public void receiveButtonPushed(){
        if(cash.getText() == null){
            message.setText("you must enter num of cash!");
        } else {
            try {
                if(selectBar.getValue().equals("All")){
                    double payment = Double.valueOf(cash.getText());
                    finishBill = server.receivePayment(server.tableOn, payment);
                    if (finishBill) {
                        for (Map.Entry<Integer, Double> entry : table.orderTotal.entrySet()) {
                            table.orderTotal.replace(entry.getKey(), 0.0);
                        }
                    }
                } else {
                    String nowPaying = selectBar.getValue();
                    int numPay = Integer.parseInt(nowPaying);
                    int numOfPerson = server.tableOn.orderPlaced.size();
                    double payment = Double.valueOf(cash.getText());
                    finishBill = server.receivePayment(server.tableOn, payment, numPay);
                    if(finishBill){
                        billsPayed++;
                        table.orderTotal.replace(numPay, 0.0);
                        finishBill = false;
                    }
                    if (billsPayed == numOfPerson){
                        finishBill = true;
                    }
                }
            } catch (Exception e){
                message.setText("You must enter a num.");
            }
            selectBar.getItems().clear();
            initialize(null,null);
        }
    }

    @FXML
    public void finishButtonPushed() throws Exception{
        if(finishBill){
            server.cleanTable(server.tableOn);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((Stage)finish.getScene().getWindow()).setScene(scene);
        } else {
            message.setText("You must finish the bill.");
        }
    }
}
