package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.network.ClientController;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.Classes.ChatView;
import it.polimi.ingsw.gc42.view.Interfaces.NewGameListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the complex behavior of the "New Game" Screen.
 * Here are handles the Chat, the list of Players who joined the Game, and is registered the User's input to start the Game.
 * When the Player clicks on the "Start Game" button, all Listeners are notified.
 */
public class NewGameViewController implements Observable, ViewController {
    @FXML
    private ScrollPane playersList;
    @FXML
    private TextField gameNameTextField;
    @FXML
    private StackPane chatContainer;
    @FXML
    private StackPane chatBoxContainer;
    @FXML
    private TextField chatTextField;
    @FXML
    private Button sendButton;

    private int gameID;
    NetworkController controller;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isNameSet = false;
    private int players = 0;
    private boolean startGame = false;

    private ChatView chatView;

    @FXML
    private VBox startButton;
    private boolean isStartButtonEnabled = false;

    /**
     * Setter Method for the NetworkController
     * @param controller the NetworkController used for communications
     * @param gameID the gameID associated to the newly created Game
     */
    public void setServer(NetworkController controller, int gameID) {
        this.controller = controller;
        this.gameID = gameID;
        gameNameTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!gameNameTextField.getText().isEmpty()) {
                    try {
                        controller.setName(gameNameTextField.getText());
                        isNameSet = true;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    isNameSet = false;
                }
            }
        });
        chatView = new ChatView(false, chatContainer, chatBoxContainer, chatTextField, sendButton, null, this);
    }

    /**
     * Setter Method for the Player.
     * The Player is sent to the Server.
     * @param player the Player created after the User picked a NickName
     */
    public void setPlayer(Player player) {
        try {
            controller.pickGame(gameID);
            controller.setViewController( new ClientController(this));
            controller.addPlayer(player);
            refresh();
        } catch (RemoteException ignored) {

        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for startGame
     * @return the boolean value indicating if this User created the Game
     */
    public boolean getStartGame() {
        return startGame;
    }

    /**
     * Refreshes the List of Players who joined the Game
     * @throws RemoteException if there is a connection error
     */
    public void refresh() throws RemoteException {
        players = controller.getNumberOfPlayers();
        if (players >= 2) {
            // Doesn't start automatically, but allows to manually start the Game
            enableStartButton();
        }
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        ArrayList<HashMap<String, String>> players = controller.getPlayersInfo();
        for (HashMap<String, String> player : players) {
            content.getChildren().add(getNewListItem(player.get("Nickname")));
        }

        playersList.setContent(content);
    }

    /**
     * Creates the UI for the single Player entry to the List
     * @param playerName the Player's NickName to add to the List
     * @return the Pane (JavaFX element) of this UI element
     */
    private Pane getNewListItem(String playerName) {
        HBox newListItem = new HBox();
        newListItem.setSpacing(20);
        newListItem.setAlignment(Pos.CENTER_LEFT);
        newListItem.setPadding(new Insets(10, 0, 0, 10));
        newListItem.setMaxHeight(50);

        Text name;
        name = new Text(playerName);
        name.setFont(Font.font("Tahoma Regular", 11));
        name.setTextAlignment(TextAlignment.LEFT);

        newListItem.getChildren().add(name);
        return newListItem;
    }

    /**
     * Applies a green color effect to the "Start Game" Button and enables its clicking behavior
     */
    private void enableStartButton() {
        isStartButtonEnabled = true;
        startButton.setStyle("-fx-background-color: forestgreen; -fx-background-radius: 15;");
    }

    /**
     * Notifies all Listeners that the User clicked the "Start Game" Button
     */
    @FXML
    public void start() {
        if (isStartButtonEnabled) {
            startGame = true;
            notifyListeners("Game Started");
        }
    }

    /**
     * Adds a Listener to the List to be notified of when the Player clicks the Button
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
     * Notifies all Listeners
     * @param context a String explaining which event was triggered
     */
    @Override
    public void notifyListeners(String context) {
        switch (context) {
            case "Game Started" -> {
                for (Listener l: listeners) {
                    if (l instanceof NewGameListener) {
                        l.onEvent();
                    }
                }
            }
        }
    }

    @Override
    public void showSecretObjectivesSelectionDialog() {
        // Don't need
    }

    @Override
    public void showStarterCardSelectionDialog() {
        // Don't need
    }

    @Override
    public void showTokenSelectionDialog() {
        // Don't need
    }

    @Override
    public int getOwner() {
        return 1;
    }

    @Override
    public void askToDrawOrGrab() {
        // Don't need
    }

    @Override
    public void notifyGameIsStarting() {
        // TODO: Implement
    }

    @Override
    public void notifyDeckChanged(CardType type) {
        // Don't need
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) {
        // Don't need
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) {
        // Don't need
    }

    /**
     * Updates the number of Players, then refreshes the List
     */
    @Override
    public void notifyNumberOfPlayersChanged() {
        Platform.runLater(() -> {
            try {
                refresh();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void notifyPlayersTokenChanged(int playerID) {
        // Don't need
    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) {
        // Don't need
    }

    @Override
    public void notifyPlayersHandChanged(int playerID) {
        // Don't need
    }

    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) {
        // Don't need
    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) {
        // Don't need
    }

    @Override
    public void notifyCommonObjectivesChanged() {
        // Don't need
    }

    @Override
    public void notifyTurnChanged() {
        // Don't need
    }

    @Override
    public void showWaitingForServerDialog() {
        // Don't need
    }

    @Override
    public void getReady(int numberOfPlayers) {
        // Don't need
    }

    @Override
    public void notifyLastTurn() throws RemoteException {
        // Don't need
    }

    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        // Don't need
    }

    /**
     * Adds the newly received Message to the Chat
     * @param message the new Message received
     */
    @Override
    public void notifyNewMessage(ChatMessage message) {
        chatView.addMessage(message);
    }

    /**
     * @return the User's NickName
     */
    @Override
    public String getPlayerNickname() {
        return controller.getPlayersInfo().getFirst().get("Nickname");
    }

    /**
     * @return the NetworkController used for communication
     */
    @Override
    public NetworkController getNetworkController() {
        return controller;
    }

    @Override
    public void blockInput() {
        // Don't need
    }

    @Override
    public void unlockInput() {
        // Don't need
    }
}
