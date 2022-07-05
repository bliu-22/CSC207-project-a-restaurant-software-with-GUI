package controllAndView;

import model.LogConfigurator;
import model.Restaurant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
    Restaurant restaurant = Restaurant.getRestaurant();
    private Stage window = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        restaurant.start();
        LogConfigurator.setupLogger();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        window.setScene(scene);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
