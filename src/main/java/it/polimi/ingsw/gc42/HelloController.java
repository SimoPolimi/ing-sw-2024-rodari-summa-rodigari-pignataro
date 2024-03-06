package it.polimi.ingsw.gc42;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController implements onClickListener {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private void onTestClick() {
        welcomeText.setText("Test");
    }

    @Override
    public void onClick() {
        onTestClick();
    }
}