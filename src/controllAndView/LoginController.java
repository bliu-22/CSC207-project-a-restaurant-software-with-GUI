package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Restaurant;
import javafx.stage.Stage;
import model.Manager;

public class LoginController {
    @FXML public RadioButton manager;
    @FXML public RadioButton server;
    @FXML public RadioButton chef;
    @FXML public ToggleGroup group;
    @FXML public TextField nameText;
    @FXML public TextField idText;
    @FXML public Label message;
    @FXML public Button login;
    @FXML public Button close;
    @FXML private Restaurant restaurant = Restaurant.getRestaurant();
    @FXML private Manager realManager = restaurant.getManager();

    @FXML
    public void loginButtonPushed(){
        String name = nameText.getText().trim();
        String id = idText.getText();
        String occupation = ((RadioButton)group.getSelectedToggle()).getText();
        try {
            int idNum = Integer.parseInt(id);
            switch (occupation){
                case "Manager": if(realManager.getName().equals(name) && realManager.getId() == idNum){
                    restaurant.activeEmployee = realManager;
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManagerMain.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ((Stage)login.getScene().getWindow()).setScene(scene);
                } else {
                    message.setText("Your information doesn't match to any manager.");
                }
                break;
                case "Server": if(restaurant.servers.containsKey(idNum) &&
                        restaurant.servers.get(idNum).getName().equals(name)){
                    restaurant.activeEmployee = restaurant.servers.get(idNum);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerMain.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ((Stage)login.getScene().getWindow()).setScene(scene);
                } else {
                    message.setText("Your information doesn't match to any server.");
                }
                break;
                case "Chef": if(restaurant.chefs.containsKey(idNum) &&
                        restaurant.chefs.get(idNum).getName().equals(name)){
                    restaurant.activeEmployee = restaurant.chefs.get(idNum);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChefMain.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    ((Stage)login.getScene().getWindow()).setScene(scene);
                } else {
                    message.setText("Your information doesn't match to any chef.");
                }
                break;
            }
        } catch (Exception e){
            message.setText("You must write something in the text bar.");
        }
    }

    @FXML
    public void closeButtonPushed(){
        boolean canEnd = restaurant.end();
        if (canEnd){
            ((Stage)login.getScene().getWindow()).close();
        } else {
            message.setText("Customer still in the restaurant.");
        }
    }
}
