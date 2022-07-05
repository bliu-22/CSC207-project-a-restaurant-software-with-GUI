package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import model.*;

public class ManagerMainController implements Initializable {
    @FXML
    public RadioButton switchToServers, switchToChefs;
    @FXML
    public TextField staffName, staffId;
    @FXML
    public Button tableManagement, back;
    @FXML
    public ListView<Employee> staffList;
    @FXML
    public TreeView<String> paymentRecords;
    @FXML
    private TreeItem<String> root = new TreeItem<>("Tables");
    @FXML
    public Label feedback, totalRev;
    @FXML
    public Restaurant restaurant = Restaurant.getRestaurant();
    @FXML
    private Manager manager = (Manager) restaurant.activeEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        totalRev.setText("Total Revenue: " + restaurant.getRevenue());
        switchToServers.setSelected(true);
        for (Map.Entry<Integer, Server> entry : restaurant.servers.entrySet()) {
            staffList.getItems().addAll(entry.getValue());
        }
        root.setExpanded(true);
        for (Map.Entry<Integer, ArrayList<String>> entry : restaurant.orderHistory.entrySet()) {
            int size = entry.getValue().size();
            TreeItem<String> k = new TreeItem<>("Table " + entry.getKey());
            for (int i = 0; i < size; i++) {
                k.getChildren().add(new TreeItem<>
                        ("Table" + entry.getKey() + ":Bill " + String.valueOf(i + 1)));
            }
            root.getChildren().add(k);
        }
        paymentRecords.setRoot(root);


    }


    @FXML
    public void switchToChefsPushed(){
        staffList.getItems().clear();
        for (Map.Entry<Integer, Chef> entry : restaurant.chefs.entrySet()) {
            staffList.getItems().add(entry.getValue());
        }
    }

    @FXML
    public void switchToServersPushed(){

        staffList.getItems().clear();
        for (Map.Entry<Integer, Server> entry : restaurant.servers.entrySet()) {
            staffList.getItems().add(entry.getValue());
        }
    }

    @FXML
    public void addButtonPushed() {
        try {

            String name = staffName.getText();
            String id = staffId.getText();

            if (name.trim().equals("")) {
                feedback.setText("Input cannot be empty");
            } else {
                int idInt = Integer.parseInt(id);
                if (switchToChefs.isSelected()) {
                    feedback.setText(manager.addChef(name, idInt));
                    staffList.getItems().clear();
                    for (Map.Entry<Integer, Chef> entry : restaurant.chefs.entrySet()) {
                        staffList.getItems().addAll(entry.getValue());
                    }
                } else {
                    feedback.setText(manager.addServer(name, idInt));
                    staffList.getItems().clear();
                    for (Map.Entry<Integer, Server> entry : restaurant.servers.entrySet()) {
                        staffList.getItems().addAll(entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            feedback.setText("ID must be an integer");
        }
    }

    @FXML
    public void removeButtonPushed() throws IOException {
        Employee itemToRemove = staffList.getSelectionModel().getSelectedItem();

        if (itemToRemove != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoveConfirm.fxml"));
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Confirmation");
            window.setScene(new Scene(loader.load()));
            RemoveConfirmController controller = loader.getController();
            controller.initData("Sure to remove " + itemToRemove + "?");
            window.showAndWait();
        }
        if (RemoveConfirmController.answer) {

            if (itemToRemove instanceof Server) {
                staffList.getItems().remove(itemToRemove);
                manager.removeServer(itemToRemove.getId());
            } else if (itemToRemove instanceof Chef) {
                staffList.getItems().remove(itemToRemove);
                manager.removeChef(itemToRemove.getId());
            }

        }

    }

    @FXML
    public void backButtonPushed() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage) back.getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void invManagementButtonPushed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("InvManagement.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Inventory Management");
        window.setScene(new Scene(root));
        window.showAndWait();
    }

    @FXML
    public void menuManagementButtonPushed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MenuManagement.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Menu Management");
        window.setScene(new Scene(root));
        window.showAndWait();
    }

    @FXML
    public void trackOrderButtonPushed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContentDisplay.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Order(s) undelivered");
        window.setScene(new Scene(loader.load()));
        ContentDisplayController controller = loader.getController();
        controller.initData(manager.trackOrder());
        window.showAndWait();

    }

    @FXML
    public void tableManagementButtonPushed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableManagement.fxml"));
        Stage window = (Stage) tableManagement.getScene().getWindow();
        window.setTitle("Table Management");
        window.setScene(new Scene(loader.load()));
    }

    @FXML
    public void checkDetailButtonPushed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContentDisplay.fxml"));
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Bill detail");
        window.setScene(new Scene(loader.load()));
        ContentDisplayController controller = loader.getController();


        if (paymentRecords.getSelectionModel().getSelectedItem() != null) {
            String selectedBill = (String) ((TreeItem) paymentRecords.getSelectionModel().getSelectedItem()).getValue();
            if (selectedBill.contains(":")) {
                int key = Integer.parseInt(selectedBill.substring(5, selectedBill.indexOf(":")));

                int index = Integer.parseInt(selectedBill.substring(selectedBill.indexOf(" ") + 1)) - 1;

                controller.initData(restaurant.orderHistory.get(key).get(index));
                window.showAndWait();
            }
        }

    }

}
