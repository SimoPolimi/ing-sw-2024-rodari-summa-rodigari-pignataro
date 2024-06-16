package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.SocketClient;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.network.RmiClient;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class creates and handles the behavior of the Login Screen.
 * This screen allows the Player to input the Server's IP Address, to select a Network Mode (RMI or Socket)
 * and to pick a NickName.
 * It checks if a NickName is valid before allowing the User to go to the next Screen.
 * This class is Observable: the Listeners are notified when the User is ready to go to the next Screen.
 */
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
    private ImageView connectionIcon;
    @FXML
    private Text errorTxt;

    private NetworkMode selectedNetworkMode = NetworkMode.RMI;
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private String nickName = null;
    private boolean isPlayButtonEnabled = false;
    private boolean isConnected = false;
    private NetworkController networkController;

    /**
     * Initializes the UI elements
     */
    public void init() {
        nickNameTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                nickName = nickNameTextArea.getText();
                try {
                    checkIfCanEnableButton(nickName);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
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

    /**
     * @return the Network Controller created at the moment of establishing the connection
     */
    public NetworkController getNetworkController() {
        return networkController;
    }

    /**
     * @return a String containing the User's NickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Handles the Keyboard's ENTER Key input event
     */
    public void onEnterPressed() {
        if (isPlayButtonEnabled) {
            try {
                networkController.blockNickName(nickName);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            notifyListeners("Play Button Clicked");
        }
    }

    /**
     * @return a String containing the IP Address
     */
    public String getIPAddress() {
        return ipTextField.getText();
    }

    /**
     * @param selectedNetworkMode the selected Network Mode
     */
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
            connect();
        }
    }

    /**
     * Applies a green color to the Play Button, and enables its clicking behavior
     */
    private void enablePlayButton() {
        errorTxt.setVisible(false);
        isPlayButtonEnabled = true;
        playButton.setStyle("-fx-background-color: forestgreen; -fx-background-radius: 15;");
    }

    /**
     * Applies a gray color to the Play Button, and disables its clicking behavior
     */
    private void disablePlayButton() {
        isPlayButtonEnabled = false;
        playButton.setStyle("-fx-background-color: grey; -fx-background-radius: 15;");
    }

    /**
     * Attempts establishing a connection.
     * Animates the Connection Icon to let the User know what's going on.
     * If the connection is successful, the Icon is updated to a "Successful" one.
     * If the connection fails, the Icon is updated to a "Failed" one.
     */
    @FXML
    private void connect() {
        connectionIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/connectingIcon.png"))));
        String ip = getIPAddress();
        if (ip != null && !ip.isEmpty()) {
            if (selectedNetworkMode == NetworkMode.RMI) {
                networkController = new RmiClient(getIPAddress());
                try {
                    networkController.connect();
                    isConnected = true;
                } catch (IOException | NotBoundException e) {
                    // Connection not successful
                    isConnected = false;
                }
            } else {
                networkController = new SocketClient(getIPAddress());
                try {
                    networkController.connect();
                    isConnected = true;
                } catch (NotBoundException | IOException e) {
                    isConnected = false;
                }
            }
            if (isConnected) {
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

    /**
     * Updates the Icon's ImageView in case of a Failed Connection
     */
    private void showConnectionError() {
        TranslateTransition delay = new TranslateTransition(Duration.millis(100), connectionIcon);
        delay.setOnFinished((e) -> {
            connectionIcon.setImage(new Image(Objects.requireNonNull(getClass()
                            .getResourceAsStream("/connectionErrorIcon.png"))));
            isConnected = false;
        });
        delay.play();
        disablePlayButton();
    }

    /**
     * Updates the Icon's ImageView in case of a Successful Connection
     */
    private void showConnectionSuccess() {
        TranslateTransition delay = new TranslateTransition(Duration.millis(100), connectionIcon);
        delay.setOnFinished((e) -> {
            connectionIcon.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/connectionSuccessfulIcon.png"))));
            isConnected = true;
        });
        delay.play();
        try {
            checkIfCanEnableButton(nickNameTextArea.getText());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the User inputted a valid NickName, and if the Connection is Successful, in which case it unlocks the
     * option to go to the next Screen.
     * @param nickName the User's NickName
     * @throws RemoteException in case of a connection error.
     */
    private void checkIfCanEnableButton(String nickName) throws RemoteException {
        if (null != nickName && !nickName.isEmpty() && isConnected) {
            if (networkController.checkNickName(nickName)) {
                enablePlayButton();
            } else {
                disablePlayButton();
                showInvalidNickName();
            }
        } else {
            disablePlayButton();
        }
    }

    /**
     * Shows an error message in case of an invalid NickName
     */
    private void showInvalidNickName() {
        errorTxt.setVisible(true);
    }

    /**
     * Adds a Listener that will be notified when the User is ready to go to the next Screen
     * @param listener the Listener to add
     */
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an existing Listener from the List, if present
     * @param listener the Listener to remove
     */
    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all Listeners that the User is ready to go to the next Screen
     * @param context a String message explaining what event was triggered
     */
    @Override
    public void notifyListeners(String context) {
        for (Listener listener : listeners) {
            listener.onEvent();
        }
    }
}
