package controllAndView;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class ContentDisplayController {
    @FXML
    public Label content;

    /**
     * Initialize the content being displayed by passing in a String
     *
     * @param str the String to display
     */
    public void initData(String str) {
        content.setText(str);
    }
}
