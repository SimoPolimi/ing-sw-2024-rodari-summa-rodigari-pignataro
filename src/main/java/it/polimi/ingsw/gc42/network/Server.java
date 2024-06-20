package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.BindException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

public class Server extends Application {
    private static String[] args;

    private ServerNetworkController rmiController;
    private ServerNetworkController socketController;
    private boolean isRunning = false;
    private GameCollection collection;
    private boolean canReadInput = true;

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
    @FXML
    private ScrollPane gamesList;
    @FXML
    private Text errorTxt;


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
    public void toggleServer() throws IOException, NotBoundException {
        errorTxt.setVisible(false);
        // Animates the button
        if (canReadInput) {
            canReadInput = false;
            ScaleTransition transition = new ScaleTransition(Duration.millis(200), startButton);
            transition.setByX(-0.2);
            transition.setByY(-0.2);
            transition.setAutoReverse(true);
            transition.setCycleCount(2);
            transition.setOnFinished((e) -> canReadInput = true);
            transition.play();

            if (!isRunning) {
                // Creates the GameCollection
                collection = new GameCollection();

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
                try {
                    rmiController.start();
                    socketController.start();
                } catch (AlreadyBoundException | BindException e) {
                    errorTxt.setVisible(true);
                }
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
                collection.empty();
                isRunning = false;
            }
            refresh();
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

    @FXML
    public void refresh() throws RemoteException {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        if (null == collection || collection.size() == 0) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            String string = "";
            if (isRunning) {
                string = "No available games: nobody is playing on this server";
            } else {
                string = "Server is not running";
            }
            Text text = new Text(string);
            text.setFont(javafx.scene.text.Font.font("Tahoma Regular", 15));

            hbox.getChildren().add(text);
            content.getChildren().add(hbox);
            gamesList.setFitToHeight(true);
            gamesList.setFitToWidth(true);
        } else {
            for (int i = 0; i < collection.size(); i++) {
                Pane newListItem = getNewListItem(collection.get(i));
                content.getChildren().add(newListItem);
            }
            gamesList.setFitToHeight(false);
            gamesList.setFitToWidth(true);
        }
        gamesList.setContent(content);
    }

    private Pane getNewListItem(GameController obj) {
        HBox newListItem = new HBox();
        newListItem.setSpacing(20);
        newListItem.setAlignment(Pos.CENTER_LEFT);
        newListItem.setPadding(new Insets(10, 0, 0, 10));
        newListItem.setMaxHeight(50);

        Text name;
        Text number;
        Text status;
        try {
            name = new Text(obj.getName());
            name.setFont(javafx.scene.text.Font.font("Tahoma Regular", 11));
            name.setTextAlignment(TextAlignment.LEFT);

            number = new Text(String.valueOf(obj.getGame().getNumberOfPlayers()));
            number.setFont(javafx.scene.text.Font.font("Tahoma Bold", 11));

            status = new Text(statusToString(obj.getCurrentStatus()));
            status.setFont(javafx.scene.text.Font.font("Tahoma Bold", 11));

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        newListItem.getChildren().addAll(name, number, status);
        newListItem.setCursor(Cursor.HAND);
        return newListItem;
    }

    private String statusToString(GameStatus status) {
        String string = "Unknown";
        switch (status) {
            case WAITING_FOR_PLAYERS -> {
                string = "Waiting for players";
            }
            case PLAYING -> {
                string = "Playing";
            }
        }
        return string;
    }
}
