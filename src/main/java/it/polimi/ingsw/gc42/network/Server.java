package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
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
import javafx.scene.paint.Color;
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

/**
 * This Class handles the GUI version of Server
 */
public class Server extends Application {

    // Attributes
    private ServerNetworkController rmiController;
    private ServerNetworkController socketController;
    private boolean isRunning = false;
    private GameCollection collection;
    private boolean canReadInput = true;

    // JavaFx imports
    @FXML
    private Text ipText;
    @FXML
    private VBox startButton;
    @FXML
    private Text startTxt;
    @FXML
    private ImageView ipCopyIcon;
    @FXML
    private ScrollPane gamesList;
    @FXML
    private Text errorTxt;
    @FXML
    private Text socketStatus;
    @FXML
    private Text rmiStatus;


    /**
     * Launches the Server
     * @param args needed to run
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if the file can't be found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/server-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));

        stage.show();
    }

    /**
     * Toggles the Server between the ON and OFF statuses
     * @throws IOException if the file can't be found
     * @throws NotBoundException if it tries to stop a Server that's not running
     */
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
                        ipText.setText(rmiController.getIpAddress());
                        ipText.setVisible(true);
                        ipCopyIcon.setVisible(true);
                        rmiStatus.setText("Running");
                        rmiStatus.setFill(Color.GREEN);
                    }
                });
                // Creates the Socket Network Controller
                socketController = new SocketControllerServer();
                socketController.setWhenReady(() -> {
                    socketStatus.setText("Running");
                    socketStatus.setFill(Color.GREEN);
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
                ipText.setVisible(false);
                ipCopyIcon.setVisible(false);
                rmiStatus.setText("Not running");
                rmiStatus.setFill(Color.RED);
                rmiController.stop();
                // Closes the Socket Connection
                socketController.stop();
                collection.empty();
                isRunning = false;
                socketStatus.setText("Not running");
                socketStatus.setFill(Color.RED);
            }
            refresh();
        }
    }

    /**
     * Copies into the User's clipboard the IP Address
     */
    @FXML
    public  void copyIPAddress() {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), ipCopyIcon);
        transition.setByX(-0.2);
        transition.setByY(-0.2);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
        StringSelection ip = new StringSelection(rmiController.getIpAddress());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ip,null);
    }

    /**
     * Refreshes the List of ongoing Games
     * @throws RemoteException in case of a Network Communication Error
     */
    @FXML
    public void refresh() throws RemoteException {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        if (null == collection || collection.size() == 0) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            String string;
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

    /**
     * Builds the UI for the List component
     * @param obj the GameController associated to that Game
     * @return the UI component
     */
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

    /**
     * Translates a GameStatus into a String
     * @param status the GameStatus to translate
     * @return the String version of the same GameStatus
     */
    private String statusToString(GameStatus status) {
        String string = "Unknown";
        switch (status) {
            case WAITING_FOR_PLAYERS -> string = "Waiting for players";
            case PLAYING -> string = "Playing";
        }
        return string;
    }
}
