package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.Course;
import model.Restaurant;
import model.Server;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import model.Table;

public class ServerMainController implements Initializable {
    @FXML Restaurant restaurant = Restaurant.getRestaurant();
    @FXML Server server = ((Server)restaurant.activeEmployee);
    @FXML public TreeView<String> tableLists;
    @FXML public Button proceed;
    @FXML private TreeItem<String> root = new TreeItem<>("Tables");
    @FXML private TreeItem<String> innerRoot1 = new TreeItem<>("Table size 2");
    @FXML private TreeItem<String> innerRoot2 = new TreeItem<>("Table size 4");
    @FXML private TreeItem<String> innerRoot3 = new TreeItem<>("Table size 8");
    @FXML private TreeItem<String> innerRoot4 = new TreeItem<>("Table size 10");
    @FXML public Label status;
    @FXML public Button addCustomer;
    @FXML public Button getBill;
    @FXML public Button receive;
    @FXML public Button logOut;
    @FXML public Button deliver;
    @FXML public Button orderStatus;
    @FXML
    public void deliverButtonPushed() throws Exception{
        if (server.orderCooked.size() == 0){
            status.setText("No dish in the list.");
        } else {
            server.getDelivering();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeliverCourse.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((Stage) deliver.getScene().getWindow()).setScene(scene);
        }
    }

    @FXML
    public void receiveButtonPushed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ReceiveInventory.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Receive Goods");
        window.setScene(new Scene(root));
        window.showAndWait();
    }

    @FXML
    public void proceedButtonPushed() throws Exception{
        Table table = getClickedTable();
        if (table == null){
            status.setText("You must choose a table.");
        } else {
            if (table.checkStatus()) {
                server.tableOn = table;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrder.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ((Stage) proceed.getScene().getWindow()).setScene(scene);
            } else {
                status.setText("Table has no customer.");
            }
        }
    }

    @FXML
    public void addCustomerButtonPushed() throws Exception{
        if(tableLists.getSelectionModel().getSelectedItems().size() == 0 ||
                tableLists.getSelectionModel().getSelectedItems().get(0).getValue().substring(0,5).equals("Table")){
            status.setText("You must choose a table.");
        } else {
            int separator = tableLists.getSelectionModel().getSelectedItems().get(0).getValue().indexOf(" ");
            String idNum = tableLists.getSelectionModel().getSelectedItems().get(0).getValue().substring(0, separator);
            server.tableOn = restaurant.tables.get(Integer.parseInt(idNum));
            if (server.tableOn.checkStatus()){
                status.setText("Can't add to this table.");
            } else {
                tableLists.getSelectionModel().getSelectedItems().get(0).setValue(idNum + " is occupied.");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addScene.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                ((Stage) addCustomer.getScene().getWindow()).setScene(scene);
            }
        }
    }

    @FXML
    public void logOutButtonPushed() throws Exception{
        restaurant.activeEmployee = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)logOut.getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void getBillButtonPushed() throws Exception{
        boolean canGetBill = true;
        Table table = getClickedTable();
        if (table == null){
            status.setText("You must choose a table.");
        } else {
            if (table.checkStatus()) {
                for (Map.Entry<Integer, ArrayList<Course>> entry : table.getOrderPlaced().entrySet()) {
                    for (Course course : entry.getValue()){
                        if (!course.getStatus().equals("Delivered and accepted") &&
                                !course.getStatus().equals("Cancelled")){
                            canGetBill = false;
                        }
                    }
                }
                if (canGetBill) {
                    server.tableOn = table;
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BillScene.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ((Stage) getBill.getScene().getWindow()).setScene(scene);
                } else {
                    status.setText("Unfinished course remaining.");
                }
            } else {
                status.setText("Table has no customer.");
            }
        }
    }

    @FXML public void orderStatusButtonPushed() throws Exception{
        Table table = getClickedTable();
        if (table == null){
            status.setText("You must choose a table.");
        } else {
            String status = table.getOrderStatus();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContentDisplay.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            ContentDisplayController controller = fxmlLoader.getController();
            controller.initData(status);
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    private Table getClickedTable(){
        if(tableLists.getSelectionModel().getSelectedItems().size() == 0 ||
                tableLists.getSelectionModel().getSelectedItems().get(0).getValue().substring(0,5).equals("Table")){
            return null;
        } else {
            int separator = tableLists.getSelectionModel().getSelectedItems().get(0).getValue().indexOf(" ");
            String idNum = tableLists.getSelectionModel().getSelectedItems().get(0).getValue().substring(0, separator);
            return restaurant.tables.get(Integer.parseInt(idNum));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setExpanded(true);
        root.getChildren().addAll(innerRoot1, innerRoot2, innerRoot3, innerRoot4);
        for (Map.Entry<Integer, Table> entry : restaurant.tables.entrySet()) {
            String text = entry.getKey() + " not occupied.";
            if(entry.getValue().checkStatus()){
                text = entry.getKey() + " is occupied.";
            }
            switch (entry.getValue().getSize()){
                case 2: innerRoot1.getChildren().add(new TreeItem<>(text));
                break;
                case 4: innerRoot2.getChildren().add(new TreeItem<>(text));
                break;
                case 8: innerRoot3.getChildren().add(new TreeItem<>(text));
                break;
                case 10: innerRoot4.getChildren().add(new TreeItem<>(text));
                break;
            }
        }
        tableLists.setRoot(root);
        tableLists.setShowRoot(true);
        if (server.orderCooked.size() != 0){
            addCustomer.setDisable(true);
            receive.setDisable(true);
            addCustomer.setDisable(true);
            proceed.setDisable(true);
            getBill.setDisable(true);
            orderStatus.setDisable(true);
            status.setText("You must deliver the course first.");
        }
    }
}
