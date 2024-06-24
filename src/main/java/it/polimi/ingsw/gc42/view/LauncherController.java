package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.Server;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * This class handles the complex behavior of the Launcher.
 * It handles the keyboard inputs, the animations and the whole logic behind them.
 */
public class LauncherController {
    @FXML
    private ImageView guiIcon;
    @FXML
    private ImageView tuiIcon;
    private int selectedMode = 0;
    @FXML
    private HBox serverButton;


    /**
     * Animates the GUI Icon, then hides the current Launcher window and launches the GameWindow
     */
    @FXML
    private void launchGUIWindow() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(60), guiIcon);
        transition.setFromX(guiIcon.getScaleX());
        transition.setToX(1);
        transition.setFromY(guiIcon.getScaleY());
        transition.setToY(1);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.setOnFinished((e) -> {
            GameWindow guiWindow = new GameWindow();
            guiIcon.getScene().getWindow().hide();
            try {
                guiWindow.start(new Stage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        transition.play();
    }

    /**
     * Animates the TUI Icon, then hides the current Launcher window and launches the GameTerminal
     */
    @FXML
    private void launchTUIWindow() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(60), tuiIcon);
        transition.setFromX(tuiIcon.getScaleX());
        transition.setToX(1);
        transition.setFromY(tuiIcon.getScaleY());
        transition.setToY(1);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.setOnFinished((e) -> {
            GameTerminal tui = new GameTerminal();
            Platform.setImplicitExit(false);

            guiIcon.getScene().getWindow().hide();
            try {
                tui.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        transition.play();
    }

    /**
     * Closes the window
     */
    @FXML
    public void exit() {
        guiIcon.getScene().getWindow().hide();
    }

    /**
     * Animates the GUI Icon when the Mouse hovers on it
     */
    @FXML
    public void hoverOnGuiMode() {
        selectedMode = 1;
        selectView(guiIcon);
    }

    /**
     * Animates the TUI Icon when the Mouse hovers on it
     */
    @FXML
    public void hoverOnTuiMode() {
        selectedMode = 2;
        selectView(tuiIcon);
    }

    /**
     * Removes the yellow glow effect from both Icons
     */
    @FXML
    public void deselectAllModes() {
        selectedMode = 0;
        hoverOutOfTuiMode();
        hoverOutOfGuiMode();
    }

    /**
     * Animates the GUI Icon to remove the focus effect
     */
    public void hoverOutOfGuiMode() {
        deselectView(guiIcon);
    }

    /**
     * Animates the TUI Icon to remove the focus effect
     */
    public void hoverOutOfTuiMode() {
        deselectView(tuiIcon);
    }

    /**
     * Handles the Keyboard's LEFT Key input event.
     * It moves the focus from the currently selected Mode to the previous one.
     * If no mode is selected, or if the first one (GUI) is, it selects the last one (TUI).
     */
    public void moveLeft() {
        if (selectedMode == 1) {
            selectedMode = 2;
        } else if (selectedMode == 0){
            selectedMode = 1;
        } else {
            selectedMode--;
        }
        updateView();
    }

    /**
     * Handles the Keyboard's RIGHT Key input event.
     * It moves the focus from the currently selected Mode to the next one.
     * If no mode is selected, or if the last one (TUI) is, it selects the first one (GUI).
     */
    public void moveRight() {
        if (selectedMode == 2) {
            selectedMode = 1;
        } else {
            selectedMode++;
        }
        updateView();
    }

    /**
     * Handles the Keyboard's ENTER Key input event, as well as the Mouse click event.
     * It launches the appropriate Mode based on which Icon was selected when the event happened.
     */
    public void selectMode() {
        switch (selectedMode) {
            case 1:
                launchGUIWindow();
                break;
            case 2:
                launchTUIWindow();
                break;
            default:
                break;
        }
    }

    /**
     * Updates the View after an event has been registered
     */
    private void updateView() {
        switch (selectedMode) {
            case 0 -> deselectAllModes();
            case 1 -> {
                hoverOnGuiMode();
                hoverOutOfTuiMode();
            }
            case 2 -> {
                hoverOutOfGuiMode();
                hoverOnTuiMode();
            }
        }
    }

    /**
     * Animates the selection of an Icon by applying a yellow glowing effect to it
     * @param view the ImageView to select
     */
    private void selectView(ImageView view) {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);
        view.setEffect(glowEffect);
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), view);
        transition.setFromX(1);
        transition.setToX(1.1);
        transition.setFromY(1);
        transition.setToY(1.1);
        transition.play();
    }

    /**
     * Animates the deselection of an Icon by removing the yellow glowing effect from it
     * @param view the ImageView to deselect
     */
    private void deselectView(ImageView view) {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(20);
        shadow.setHeight(20);
        shadow.setBlurType(BlurType.GAUSSIAN);
        view.setEffect(shadow);
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), view);
        transition.setFromX(view.getScaleX());
        transition.setToX(1);
        transition.setFromY(view.getScaleY());
        transition.setToY(1);
        transition.play();
    }

    /**
     * Launches the Server
     * @throws IOException in case the view file can't be found
     */
    @FXML
    public void openServer() throws IOException {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), serverButton);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();

        Server server = new Server();
        Stage stage = new Stage();
        stage.setX(100);
        stage.setY(100);
        server.start(stage);
    }
}
