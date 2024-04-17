package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.scene.control.TextField;
import java.util.ArrayList;

public class LoginViewController implements Observable {
    @FXML
    private VBox netModeSelector;
    @FXML
    private HBox rmiMode;
    @FXML
    private HBox socketMode;
    @FXML
    private VBox playButton;
    @FXML
    private TextField nickNameTextArea;

    private NetworkMode selectedNetworkMode = NetworkMode.RMI;
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private String nickName = null;
    private boolean isPlayButtonEnabled = false;

    public void init() {
        nickNameTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                nickName = nickNameTextArea.getText();
                if (null != nickName && !nickName.isEmpty()) {
                    enablePlayButton();
                } else {
                    disablePlayButton();
                }
            }
        });
        rmiMode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setSelectedNetworkMode(NetworkMode.RMI);
            }
        });
        socketMode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setSelectedNetworkMode(NetworkMode.SOCKET);
            }
        });
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onEnterPressed();
            }
        });
    }

    public String getNickName() {
        return nickName;
    }

    public void onEnterPressed() {
        if (isPlayButtonEnabled) {
            notifyListeners("Play Button Clicked");
        }
    }

    public void setSelectedNetworkMode(NetworkMode selectedNetworkMode) {
        if (this.selectedNetworkMode != selectedNetworkMode) {
            if (selectedNetworkMode == NetworkMode.RMI) {
                TranslateTransition leftTransition = new TranslateTransition(Duration.millis(150), netModeSelector);
                leftTransition.setByX(-46);
                leftTransition.setInterpolator(Interpolator.EASE_BOTH);
                leftTransition.play();
            } else {
                TranslateTransition rightTransition = new TranslateTransition(Duration.millis(150), netModeSelector);
                rightTransition.setByX(46);
                rightTransition.setInterpolator(Interpolator.EASE_BOTH);
                rightTransition.play();
            }
            this.selectedNetworkMode = selectedNetworkMode;
        }
    }

    private void enablePlayButton() {
        isPlayButtonEnabled = true;
        playButton.setStyle("-fx-background-color: forestgreen; -fx-background-radius: 15;");
    }

    private void disablePlayButton() {
        isPlayButtonEnabled = false;
        playButton.setStyle("-fx-background-color: grey; -fx-background-radius: 15;");
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener listener : listeners) {
            listener.onEvent();
        }
    }
}
