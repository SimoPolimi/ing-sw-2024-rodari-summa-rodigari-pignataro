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
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.Objects;

public class Server extends Application {
    private ServerNetworkController rmiController;
    private ServerNetworkController socketController;
    private boolean isRunning = false;

    @FXML
    private Text rmiIpText;
    @FXML
    private Text rmiPortText;
    @FXML
    private Text socketIpText;
    @FXML
    private Text socketPortText;
    @FXML
    private VBox startButton;
    @FXML
    private Text startTxt;
    @FXML
    private ImageView rmiIpCopyIcon;
    @FXML
    private ImageView rmiPortCopyIcon;
    @FXML
    private ImageView socketIpCopyIcon;
    @FXML
    private ImageView socketPortCopyIcon;


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

    @FXML
    public void toggleServer() throws AlreadyBoundException, IOException, NotBoundException {
        if (!isRunning) {
            // Creates the GameCollection
            GameCollection collection = new GameCollection();

            startButton.setStyle("-fx-background-color: red; -fx-background-radius: 15");
            startTxt.setText("Stop");
            // Creates the RMI Network Controller
            rmiController = new RmiControllerServer();
            rmiController.setWhenReady(new Runnable() {
                @Override
                public void run() {
                    rmiIpText.setText(rmiController.getIpAddress());
                    rmiIpText.setVisible(true);
                    rmiIpCopyIcon.setVisible(true);
                    rmiPortText.setText(rmiController.getPort());
                    rmiPortText.setVisible(true);
                    rmiPortCopyIcon.setVisible(true);
                }
            });
            // Creates the Socket Network Controller
            socketController = new SocketControllerServer();
            socketController.setWhenReady(new Runnable() {
                @Override
                public void run() {
                    socketIpText.setText(socketController.getIpAddress());
                    socketIpText.setVisible(true);
                    socketIpCopyIcon.setVisible(true);
                    socketPortText.setText(socketController.getPort());
                    socketPortText.setVisible(true);
                    socketPortCopyIcon.setVisible(true);
                }
            });
            // Passes THE SAME GameCollection to both: any edit made by one will be visible
            // to the other
            rmiController.setCollection(collection);
            socketController.setCollection(collection);

            // Starts both connections
            rmiController.start();
            socketController.start();
            isRunning = true;
        } else {
            // Closes the RMI Connection
            startButton.setStyle("-fx-background-color: green; -fx-background-radius: 15");
            startTxt.setText("Start");
            rmiIpText.setVisible(false);
            rmiIpCopyIcon.setVisible(false);
            rmiPortText.setVisible(false);
            rmiPortCopyIcon.setVisible(false);
            rmiController.stop();
            // Closes the Socket Connection
            socketIpText.setVisible(false);
            socketIpCopyIcon.setVisible(false);
            socketPortText.setVisible(false);
            socketPortCopyIcon.setVisible(false);
            socketController.stop();
            isRunning = false;
        }
    }

    @FXML
    public  void copyIPAddressRMI() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), rmiIpCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection ip = new StringSelection(rmiController.getIpAddress());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ip,null);
    }

    @FXML
    public  void copyPortRMI() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), rmiPortCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection port = new StringSelection(rmiController.getPort());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(port,null);
    }

    @FXML
    public  void copyIPAddressSocket() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), socketIpCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection ip = new StringSelection(socketController.getIpAddress());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ip,null);
    }

    @FXML
    public  void copyPortSocket() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), socketPortCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection port = new StringSelection(socketController.getPort());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(port,null);
    }
}
