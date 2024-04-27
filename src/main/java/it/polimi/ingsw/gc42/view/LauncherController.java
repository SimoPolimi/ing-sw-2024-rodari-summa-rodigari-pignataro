package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.NetworkController;
import it.polimi.ingsw.gc42.network.Server;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LauncherController {
    @FXML
    private ImageView guiIcon;
    @FXML
    private ImageView tuiIcon;
    private int selectedMode = 0;


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
            guiIcon.getScene().getWindow().hide();
            try {
                tui.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        transition.play();
    }

    @FXML
    public void exit() {
        guiIcon.getScene().getWindow().hide();
    }

    @FXML
    public void hoverOnGuiMode() {
        selectedMode = 1;
        selectView(guiIcon);
    }

    @FXML
    public void hoverOnTuiMode() {
        selectedMode = 2;
        selectView(tuiIcon);
    }

    @FXML
    public void deselectAllModes() {
        selectedMode = 0;
        hoverOutOfTuiMode();
        hoverOutOfGuiMode();
    }

    public void hoverOutOfGuiMode() {
        deselectView(guiIcon);
    }

    public void hoverOutOfTuiMode() {
        deselectView(tuiIcon);
    }

    public void moveLeft() throws Exception {
        if (selectedMode == 1) {
            selectedMode = 2;
        } else if (selectedMode == 0){
            selectedMode = 1;
        } else {
            selectedMode--;
        }
        updateView();
    }

    public void moveRight() throws Exception {
        if (selectedMode == 2) {
            selectedMode = 1;
        } else {
            selectedMode++;
        }
        updateView();
    }

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

    private void updateView() {
        switch (selectedMode) {
            case 0 -> {
                deselectAllModes();
            }
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

    @FXML
    public void openServer() throws IOException {
        Server server = new Server();
        Stage stage = new Stage();
        stage.setX(100);
        stage.setY(100);
        server.start(stage);
    }
}
