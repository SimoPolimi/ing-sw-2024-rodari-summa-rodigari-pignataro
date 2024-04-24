package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.view.Classes.NetworkMode;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.util.Objects;

public class Server extends Application {
    private NetworkMode selectedNetworkMode = NetworkMode.RMI;
    private NetworkController networkController;

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
    public void startServer() throws AlreadyBoundException, IOException {
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
                portText.setText(networkController.getPort());
                portText.setVisible(true);
            }
        });
        networkController.start();
    }

    @FXML
    public void setRmiMode() {
        setSelectedNetworkMode(NetworkMode.RMI);
    }

    @FXML
    public void setSocketMode() {
        setSelectedNetworkMode(NetworkMode.SOCKET);
    }
}
