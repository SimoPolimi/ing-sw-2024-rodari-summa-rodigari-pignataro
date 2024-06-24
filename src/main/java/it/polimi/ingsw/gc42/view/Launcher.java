package it.polimi.ingsw.gc42.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class creates and handles the behavior of the Launcher Window.
 * The Launcher is the Window where the User can pick between GUI and TUI Mode.
 */
public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates and shows the Scene
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if the view file can't be found
     */
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
                case ESCAPE -> controller.exit();
            }
        });

        stage.show();
    }

}