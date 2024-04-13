package it.polimi.ingsw.gc42.view;

import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LauncherController {
    @FXML
    private ImageView guiIcon;
    @FXML
    private ImageView tuiIcon;
    private int selectedMode = 0;


    @FXML
    private void launchGUIWindow() throws IOException {
        GameWindow guiWindow = new GameWindow();
        guiIcon.getScene().getWindow().hide();
        guiWindow.start(new Stage());
    }

    @FXML
    private void launchTUIWindow() throws Exception {
        GameTerminal tui = new GameTerminal();
        guiIcon.getScene().getWindow().hide();
        tui.start(new Stage());
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

    public void selectMode() throws Exception {
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
    }

    private void deselectView(ImageView view) {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        view.setEffect(shadow);
    }
}
