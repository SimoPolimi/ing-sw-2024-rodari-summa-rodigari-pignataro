package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Objects;

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
    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private ImageView connectionIcon;

    private NetworkMode selectedNetworkMode = NetworkMode.RMI;
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private String nickName = null;
    private boolean isPlayButtonEnabled = false;
    private boolean isConnected = false;

    public void init() {
        nickNameTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                nickName = nickNameTextArea.getText();
                if (null != nickName && !nickName.isEmpty()) {
                    // TODO: add '&& isConnected' to the if
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
        ipTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                connect();
            }
        });
        portTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                connect();
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

    public String getIPAddress() {
        return ipTextField.getText();
    }

    public int getPort() {
        String port = portTextField.getText();
        if (!port.isEmpty() && !port.matches("^[a-zA-Z]*$") && !(port.length() > 5)) {
            return Integer.parseInt(portTextField.getText());
        } else return -1;
    }

    public void setSelectedNetworkMode(NetworkMode selectedNetworkMode) {
        if (this.selectedNetworkMode != selectedNetworkMode) {
            if (selectedNetworkMode == NetworkMode.RMI) {
                TranslateTransition leftTransition = new TranslateTransition(Duration.millis(150), netModeSelector);
                leftTransition.setByX(-56);
                leftTransition.setInterpolator(Interpolator.EASE_BOTH);
                leftTransition.play();
            } else {
                TranslateTransition rightTransition = new TranslateTransition(Duration.millis(150), netModeSelector);
                rightTransition.setByX(56);
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

    private void connect() {
        connectionIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/connectingIcon.png"))));
        String ip = getIPAddress();
        int port = getPort();
        if (ip != null && !ip.isEmpty() && port != -1) {
            //TODO: Connect to Server
            if (isConnected) {
                //TODO: Change isConnected to a real connection check, like 'if(null != stub)'
                showConnectionSuccess();
            } else {
                // Couldn't connect
                showConnectionError();
            }
        } else {
            // Wrong IP and/or Port: impossible to connect
            showConnectionError();
        }
    }

    private void showConnectionError() {
        TranslateTransition delay = new TranslateTransition(Duration.millis(100), connectionIcon);
        delay.setOnFinished((e) -> {
            connectionIcon.setImage(new Image(Objects.requireNonNull(getClass()
                            .getResourceAsStream("/connectionErrorIcon.png"))));
            isConnected = false;
        });
        delay.play();
    }

    private void showConnectionSuccess() {
        TranslateTransition delay = new TranslateTransition(Duration.millis(100), connectionIcon);
        delay.setOnFinished((e) -> {
            connectionIcon.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/connectionSuccessfulIcon.png"))));
            isConnected = true;
        });
        delay.play();
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
