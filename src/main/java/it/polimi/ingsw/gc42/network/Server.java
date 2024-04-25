package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.Objects;

public class Server extends Application {
    private NetworkMode selectedNetworkMode = NetworkMode.RMI;
    private NetworkController networkController;
    private boolean isRunning = false;

    @FXML
    private VBox netModeSelector;
    @FXML
    private Text ipText;
    @FXML
    private Text portText;
    @FXML
    private HBox rmiMode;
    @FXML
    private HBox socketMode;
    @FXML
    private VBox startButton;
    @FXML
    private Text startTxt;
    @FXML
    private ImageView ipCopyIcon;
    @FXML
    private ImageView portCopyIcon;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/server-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));

        stage.show();
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

    @FXML
    public void toggleServer() throws AlreadyBoundException, IOException, NotBoundException {
        if (!isRunning) {
            startButton.setStyle("-fx-background-color: red; -fx-background-radius: 15");
            startTxt.setText("Stop");
            if (selectedNetworkMode == NetworkMode.RMI) {
                networkController = new RmiController();
            } else {
                networkController = new SocketController();
            }
            networkController.setWhenReady(new Runnable() {
                @Override
                public void run() {
                    ipText.setText(networkController.getIpAddress());
                    ipText.setVisible(true);
                    ipCopyIcon.setVisible(true);
                    portText.setText(networkController.getPort());
                    portText.setVisible(true);
                    portCopyIcon.setVisible(true);
                }
            });
            networkController.start();
            isRunning = true;
        } else {
            startButton.setStyle("-fx-background-color: green; -fx-background-radius: 15");
            startTxt.setText("Start");
            ipText.setVisible(false);
            ipCopyIcon.setVisible(false);
            portText.setVisible(false);
            portCopyIcon.setVisible(false);
            networkController.stop();
            isRunning = false;
        }
    }

    @FXML
    public void setRmiMode() {
        if (!isRunning) {
            setSelectedNetworkMode(NetworkMode.RMI);
        }
    }

    @FXML
    public void setSocketMode() {
        if (!isRunning) {
            setSelectedNetworkMode(NetworkMode.SOCKET);
        }
    }

    @FXML
    public  void copyIPAddress() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), ipCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection ip = new StringSelection(networkController.getIpAddress());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ip,null);
    }

    @FXML
    public  void copyPort() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), portCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection port = new StringSelection(networkController.getPort());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(port,null);
    }
}
