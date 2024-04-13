package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.view.GameTerminal;
import it.polimi.ingsw.gc42.view.GameWindow;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {
    @FXML
    public Button guiModeButton;
    @FXML
    public Button tuiModeButton;
    @FXML
    public Button exitButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/launcher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Launcher");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void launchGUIWindow() throws IOException {
        GameWindow guiWindow = new GameWindow();
        guiModeButton.getScene().getWindow().hide();
        guiWindow.start(new Stage());
    }

    @FXML
    private void launchTUIWindow() throws Exception {
        GameTerminal tui = new GameTerminal();
        tuiModeButton.getScene().getWindow().hide();
        tui.start(new Stage());
    }

    @FXML
    public void exit() {
        exitButton.getScene().getWindow().hide();
    }
}