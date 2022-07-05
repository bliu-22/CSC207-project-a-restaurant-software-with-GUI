package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import model.*;

public class TableManagementController implements Initializable {
    @FXML
    public Button back;
    @FXML
    public TreeView<String> tableTree;
    @FXML
    private TreeItem<String> root = new TreeItem<>("Tables");
    @FXML
    private TreeItem<String> innerRoot1 = new TreeItem<>("Table size 2");
    @FXML
    private TreeItem<String> innerRoot2 = new TreeItem<>("Table size 4");
    @FXML
    private TreeItem<String> innerRoot3 = new TreeItem<>("Table size 8");
    @FXML
    private TreeItem<String> innerRoot4 = new TreeItem<>("Table size 10");
    @FXML
    public RadioButton size2, size4, size8, size10;
    @FXML
    public Label feedback;
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
        root.setExpanded(true);
        root.getChildren().addAll(innerRoot1, innerRoot2, innerRoot3, innerRoot4);
        for (Map.Entry<Integer, Table> entry : restaurant.tables.entrySet()) {
            String text = entry.getKey() + " not occupied";
            if (entry.getValue().checkStatus()) {
                text = entry.getKey() + " is occupied";
            }
            switch (entry.getValue().getSize()) {
                case 2:
                    innerRoot1.getChildren().add(new TreeItem<>(text));
                    break;
                case 4:
                    innerRoot2.getChildren().add(new TreeItem<>(text));
                    break;
                case 8:
                    innerRoot3.getChildren().add(new TreeItem<>(text));
                    break;
                case 10:
                    innerRoot4.getChildren().add(new TreeItem<>(text));
                    break;
            }
        }
        tableTree.setRoot(root);
    }

    @FXML
    public void backButtonPushed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerMain.fxml"));
        Stage window = (Stage) back.getScene().getWindow();
        window.setScene(new Scene(loader.load()));
    }

    @FXML
    public void addTableButtonPushed() {
        int size = 2;
        if (size2.isSelected()) {
            manager.addTable(2);
            size = 2;
        }
        if (size4.isSelected()) {
            manager.addTable(4);
            size = 4;
        }
        if (size8.isSelected()) {
            manager.addTable(8);
            size = 8;
        }
        if (size10.isSelected()) {
            manager.addTable(10);
            size = 10;
        }
        feedback.setText("A new table of size " + String.valueOf(size) + " has been added");
        refresh();
    }

    @FXML
    public void removeTableButtonPushed() {
        try {
            String tableToRemove = (String) ((TreeItem) tableTree.getSelectionModel().getSelectedItems().get(0)).getValue();

            int num = Integer.parseInt(tableToRemove.substring(0, tableToRemove.indexOf(" ")));
            if (!restaurant.tables.get(num).checkStatus()) {
                feedback.setText(manager.removeTable(num));
                refresh();
            } else {
                feedback.setText("Cannot remove occupied table");
            }
        } catch (Exception e) {
            feedback.setText("");
        }
    }

    /**
     * A helper to refresh the TreeView
     */
    private void refresh() {
        TreeItem<String> root = new TreeItem<>("Tables");
        TreeItem<String> innerRoot1 = new TreeItem<>("Table size 2");
        TreeItem<String> innerRoot2 = new TreeItem<>("Table size 4");
        TreeItem<String> innerRoot3 = new TreeItem<>("Table size 8");
        TreeItem<String> innerRoot4 = new TreeItem<>("Table size 10");
        root.setExpanded(true);
        root.getChildren().addAll(innerRoot1, innerRoot2, innerRoot3, innerRoot4);
        for (Map.Entry<Integer, Table> entry : restaurant.tables.entrySet()) {
            String text = entry.getKey() + " not occupied";
            if (entry.getValue().checkStatus()) {
                text = entry.getKey() + " is occupied";
            }
            switch (entry.getValue().getSize()) {
                case 2:
                    innerRoot1.getChildren().add(new TreeItem<>(text));
                    break;
                case 4:
                    innerRoot2.getChildren().add(new TreeItem<>(text));
                    break;
                case 8:
                    innerRoot3.getChildren().add(new TreeItem<>(text));
                    break;
                case 10:
                    innerRoot4.getChildren().add(new TreeItem<>(text));
                    break;
            }
        }
        tableTree.setRoot(root);
    }
}
