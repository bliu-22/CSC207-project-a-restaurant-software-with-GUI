package controllAndView;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import model.*;

public class ChefMainController implements Initializable {

    @FXML public Text chefName, chefID;

    @FXML public ListView<String> waitList, returnedList;

    @FXML public Text tableNumber, customerNumber, dishName;

    @FXML public TextFlow returnedCourseComments;

    @FXML public TableView<IngredientInfo> ingredientUnitsTableView;

    @FXML public TableColumn<IngredientInfo, String> ingredient;

    @FXML public TableColumn<IngredientInfo, Integer> units;

    @FXML public Button receiveIngredient;

    @FXML public Button back;

    // This Restaurant.
    public Restaurant restaurant = Restaurant.getRestaurant();

    // The current Chef user.
    private Chef chef = (Chef) restaurant.activeEmployee;

    /**
     * An inner class called IngredientInfo.
     */
    public class IngredientInfo {

        // This ingredient name.
        private final SimpleStringProperty ingredientName;

        // Number of this ingredient needed for cooking this Course.
        private final SimpleIntegerProperty ingredientUnits;

        /**
         * Create a new IngredientInfo by taking an ingredientName and a ingredientUnits.
         *
         * @param ingredientName a SimpleStringProperty represent this ingredient name.
         * @param ingredientUnits a SimpleIntegerProperty represent Number of
         *                        this ingredient needed for cooking this Course.
         */
        private IngredientInfo(String ingredientName, Integer ingredientUnits) {
            this.ingredientName = new SimpleStringProperty(ingredientName);
            this.ingredientUnits = new SimpleIntegerProperty(ingredientUnits);
        }

        /**
         * @return this ingredient's name in String.
         */
        public String getIngredientName() {
            return this.ingredientName.get();
        }

        /**
         * @return the number of this ingredient needed for cooking this Course in Integer.
         */
        public Integer getIngredientUnits() {
            return this.ingredientUnits.get();
        }

    }

    /**
     * @return an ObservableList<IngredientInfo> to the TableView.
     */
    private ObservableList<IngredientInfo> getAllIngredientsInfo() {
        ObservableList<IngredientInfo> allIngredientsInfo = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : this.chef.getCourseCooking().ingredients.entrySet()) {
            allIngredientsInfo.add(new IngredientInfo(entry.getKey(), entry.getValue()));
        }
        return allIngredientsInfo;
    }

    /**
     * Set the display details for this Chef's Information and the Courses information.
     */
    private void DisplayDishDetailInfo() {
        this.tableNumber.setText("" + this.chef.getCourseCooking().getTable().getTableNumber());
        this.customerNumber.setText("" + this.chef.getCourseCooking().getCustomerNumber());
        this.dishName.setText(this.chef.getCourseCooking().getName());
        this.ingredient.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        this.units.setCellValueFactory(new PropertyValueFactory<>("ingredientUnits"));
        this.ingredientUnitsTableView.setItems(getAllIngredientsInfo());
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.chefName.setText(this.chef.getName());
        this.chefID.setText("" + this.chef.getId());
        this.waitList.getItems().addAll(Chef.getWaitListNames());
        this.returnedList.getItems().addAll(Chef.getReturnCoursesNames());
        if (this.chef.getCourseCooking() != null) {
            DisplayDishDetailInfo();
            if ((!this.chef.getCourseCooking().getStatus().equals("Being Cooked"))
                    && (this.chef.getCourseCooking().getStatus().length() < 100)) {
                Text text = new Text(this.chef.getCourseCooking().getStatus());
                this.returnedCourseComments.getChildren().add(text);
            }
        }
    }

    @FXML
    public void startDishWaitListButtonPushed() {
        if (!Chef.getWaitListNames().isEmpty()) {
            if (this.chef.getCourseCooking() == null) {
                this.chef.startCooking();
                this.waitList.getItems().setAll(Chef.getWaitListNames());
                DisplayDishDetailInfo();
            } else {
                warning("Please Finish Cooking the current dish first !!");
            }
        } else {
            warning("There are no courses in the wait list yet!!");
        }
    }

    @FXML
    public void startDishReturnedListButtonPushed() {
        if (!Chef.getReturnCoursesNames().isEmpty()) {
            if (this.chef.getCourseCooking() == null) {
                this.chef.dealWithReturnedCourse();
                this.returnedList.getItems().setAll(Chef.getReturnCoursesNames());
                if (this.chef.getCourseCooking().getStatus().length() < 100) {
                    Text text = new Text(this.chef.getCourseCooking().getStatus());
                    this.returnedCourseComments.getChildren().add(text);
                }
                DisplayDishDetailInfo();
            } else {
                warning("Please Finish Cooking the current dish first !!");
            }
        } else {
            warning("There are no courses in the returned list yet !!");
        }
    }

    @FXML
    public void completeDishButtonPushed() {
        if (this.chef.getCourseCooking() != null) {
            this.chef.finishCooking();
            this.tableNumber.setText("");
            this.customerNumber.setText("");
            this.dishName.setText("");
            this.returnedCourseComments.getChildren().clear();
            this.ingredient.setCellValueFactory(new PropertyValueFactory<>(""));
            this.units.setCellValueFactory(new PropertyValueFactory<>(""));
            ObservableList<IngredientInfo> nullIngredientsInfo = FXCollections.observableArrayList();
            this.ingredientUnitsTableView.setItems(nullIngredientsInfo);
        } else {
            warning("No course being started and cooked yet !!");
        }
    }

    @FXML
    public void receiveIngredientButtonPushed() throws IOException {
        if (this.chef.getCourseCooking() == null) {
            Parent root = FXMLLoader.load(getClass().getResource("ReceiveInventory.fxml"));
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Receive Goods");
            window.setScene(new Scene(root));
            window.showAndWait();
        } else {
            warning("Please finish the cooking first !!");
        }
    }

    @FXML
    public void backButtonPushed() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Stage)back.getScene().getWindow()).setScene(scene);
    }
}
