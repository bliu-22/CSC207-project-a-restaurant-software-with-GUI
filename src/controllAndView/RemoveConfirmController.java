package controllAndView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class RemoveConfirmController implements Initializable {

    @FXML
    public Label message1;
    static boolean answer = false;
    @FXML
    public Button yesButton, noButton;

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
        answer = false;
    }

    /**
     * Initialize the text message by passing in a String
     *
     * @param str the String to display
     */
    public void initData(String str) {
        message1.setText(str);
    }

    @FXML
    public void yesButtonPushed() {
        answer = true;
        Stage window = (Stage) yesButton.getScene().getWindow();
        window.close();
    }

    @FXML
    public void noButtonPushed() {
        answer = false;
        Stage window = (Stage) noButton.getScene().getWindow();
        window.close();
    }


}
