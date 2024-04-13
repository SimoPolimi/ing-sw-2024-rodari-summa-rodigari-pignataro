package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.view.GameTerminal;
import it.polimi.ingsw.gc42.view.GameWindow;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/launcher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LauncherController controller = fxmlLoader.getController();
        stage.setScene(scene);
        stage.setTitle("Launcher");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        stage.setResizable(false);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> {
                    try {
                        controller.moveLeft();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case RIGHT -> {
                    try {
                        controller.moveRight();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case ENTER -> {
                    try {
                        controller.selectMode();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case ESCAPE -> {
                    controller.exit();
                }
            }
        });

        stage.show();
    }

}