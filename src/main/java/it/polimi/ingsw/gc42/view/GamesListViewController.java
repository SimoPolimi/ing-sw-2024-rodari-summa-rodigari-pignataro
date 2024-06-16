package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.Interfaces.ExistingGameListener;
import it.polimi.ingsw.gc42.view.Interfaces.NewGameListener;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the UI and logic for the "Pick Game" Window in GUI.
 * This Windows shows the User a list of all the available Games and asks him to pick the one he wants to join.
 * Once a Game is picked, all the instances of ExistingGameListener are notified so that the GUI can act accordingly.
 * The picked Game's gameID is saved in an inner attribute, and can be queried in response to the Listener call.
 * When a Game is picked, the Server is immediately notified from this class, so no need to do that externally.
 * </p>
 * This Window also allows the User to create a New Game.
 * This is handled by alerting all instances of NewGameListener.
 */
public class GamesListViewController {
    @FXML
    private ScrollPane gamesList;
    @FXML
    private VBox refreshButton;

    private NetworkController server;

    private Player player;

    private final ArrayList<Listener> listeners = new ArrayList<>();
    private int pickedGame;

    /**
     * Setter Method for the NetworkController
     * @param server the NetworkController to use for communications
     */
    public void setServer(NetworkController server) {
        this.server = server;
    }

    /**
     * Refreshes the list of Available Games
     * @throws RemoteException in the case of a network communication error.
     */
    @FXML
    public void refresh() throws RemoteException {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), refreshButton);
        transition.setFromX(1);
        transition.setFromY(1);
        transition.setToX(0.8);
        transition.setToY(0.8);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();


        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        ArrayList<HashMap<String, String>> availableGames = server.getAvailableGames();
        for (int i = 0; i < availableGames.size(); i++) {
            if (availableGames.get(i).get("Status").equals("Waiting for players")) {
                Pane newListItem = getNewListItem(availableGames.get(i).get("Name"), availableGames
                        .get(i).get("NumberOfPlayers"), availableGames.get(i).get("Status"),i);
                int finalI = i;
                newListItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        pickedGame = finalI;
                        notifyListeners("Existing Game");
                    }
                });
                content.getChildren().add(newListItem);
            }
        }

        if (availableGames.isEmpty()) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            Text text = new Text("No available games. Create a new one.");
            text.setFont(Font.font("Tahoma Regular", 15));

            hbox.getChildren().add(text);
            content.getChildren().add(hbox);
            gamesList.setFitToHeight(true);
            gamesList.setFitToWidth(true);
        } else {
            gamesList.setFitToHeight(false);
            gamesList.setFitToWidth(true);
        }

        gamesList.setContent(content);
    }

    /**
     * Creates the UI element for one of the entries of the list
     * @param gameName a String containing the Game's name, chosen by the Player who created it
     * @param gameNumber an int value specifying how many Players are already inside this Game
     * @param gameStatus the GameStatus of that Game
     * @param gameID the Game's gameID, used to trigger the picking feature appropriately
     * @return the Pane (JavaFx element) containing the UI for this element.
     */
    private Pane getNewListItem(String gameName, String gameNumber, String gameStatus, int gameID) {
        HBox newListItem = new HBox();
        newListItem.setSpacing(20);
        newListItem.setAlignment(Pos.CENTER_LEFT);
        newListItem.setPadding(new Insets(10, 0, 0, 10));
        newListItem.setMaxHeight(50);

        Text name;
        Text number;
        Text status;
        name = new Text(gameName);
        name.setFont(Font.font("Tahoma Regular", 11));
        name.setTextAlignment(TextAlignment.LEFT);

        number = new Text(gameNumber);
        number.setFont(Font.font("Tahoma Bold", 11));

        status = new Text(gameStatus);
        status.setFont(Font.font("Tahoma Bold", 11));

        HBox joinButton = new HBox();
        joinButton.setSpacing(15);
        joinButton.setAlignment(Pos.CENTER);
        joinButton.setPrefWidth(50);
        joinButton.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 5");
        joinButton.setCursor(Cursor.HAND);
        joinButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                joinGame(gameID);
            }
        });

        Text connectTxt = new Text("JOIN");
        connectTxt.setFont(Font.font("Tahoma Bold", 11));
        connectTxt.setTextAlignment(TextAlignment.CENTER);

        joinButton.getChildren().add(connectTxt);

        newListItem.getChildren().addAll(name, number, status, joinButton);
        newListItem.setCursor(Cursor.HAND);
        return newListItem;
    }

    /**
     * Handles the behavior of picking a Game, alerting the Server of the joining of this new Player
     * @param gameID the Game's gameID in which the User wants to join.
     */
    private void joinGame(int gameID) {
        try {
            server.pickGame(gameID);
            server.addPlayer(player);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setter Method for the Player.
     * @param player the empty Player created when the Nickname was chosen, ready to be sent to the Server.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Notifies all NewGameListeners that the User chose to create a New Game.
     */
    @FXML
    public void newGame() {
        notifyListeners("New Game");
    }

    /**
     * Adds a Listener to the List of Listeners that will be notified when the User has performed an action.
     * @param listener the Listener to add in the List
     */
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an existing Listener to the List.
     * If the Listener is not in the List, no action is performed.
     * @param listener the Listener to remove from the List
     */
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the appropriate Listener inside the List that the User has performed an action.
     * Which Listeners are called changes based on the action.
     * Currently supports:
     * - NewGameListener: notified if the User chose to create a New Game
     * - ExistingGameListener: notified if the User chose to join an existing Game.
     * @param context a string specifying which event has been triggered
     */
    public void notifyListeners(String context) {
        switch (context) {
            case "New Game" -> {
                for (Listener l: listeners) {
                    if (l instanceof NewGameListener) {
                        l.onEvent();
                    }
                }
            }
            case "Existing Game" -> {
                for (Listener l: listeners) {
                    if (l instanceof ExistingGameListener) {
                        l.onEvent();
                    }
                }
            }
        }
    }
}
