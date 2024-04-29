package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.network.ClientController;
import it.polimi.ingsw.gc42.network.NetworkController;
import it.polimi.ingsw.gc42.network.PlayersNumberListener;
import it.polimi.ingsw.gc42.view.Interfaces.NewGameListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class NewGameViewController implements Observable, ViewController {
    @FXML
    private ScrollPane playersList;
    @FXML
    private TextField gameNameTextField;

    private int gameID;
    NetworkController controller;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    private boolean isNameSet = false;
    private int players = 0;
    private boolean startGame = false;

    @FXML
    private VBox startButton;
    private boolean isStartButtonEnabled = false;

    public void setServer(NetworkController controller, int index) throws RemoteException {
        this.controller = controller;
        this.gameID = index;
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
    }

    public void setPlayer(Player player) {
        try {
            controller.setViewController(new ClientController(this));
            controller.addPlayer(player);
            refresh();
            controller.pickGame(gameID);
        } catch (RemoteException e) {

        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getStartGame() {
        return startGame;
    }

    public void refresh() throws RemoteException {
        players = controller.getGame().getNumberOfPlayers();
        if (players >= 2) {
            // Doesn't start automatically, but allows to manually start the Game with 2 or 3 Players
            enableStartButton();
        }
        // Automatically starts once there are 4 Players
        if (players == 4) {
            notifyListeners("Game Started");
        }
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        for (int i = 1; i <= controller.getNumberOfPlayers(); i++) {
            content.getChildren().add(getNewListItem(controller.getPlayer(i)));
        }

        playersList.setContent(content);
    }

    private Pane getNewListItem(Player player) {
        HBox newListItem = new HBox();
        newListItem.setSpacing(20);
        newListItem.setAlignment(Pos.CENTER_LEFT);
        newListItem.setPadding(new Insets(10, 0, 0, 10));
        newListItem.setMaxHeight(50);

        Text name;
        name = new Text(player.getNickname());
        name.setFont(Font.font("Tahoma Regular", 11));
        name.setTextAlignment(TextAlignment.LEFT);

        newListItem.getChildren().add(name);
        return newListItem;
    }

    private void enableStartButton() {
        isStartButtonEnabled = true;
        startButton.setStyle("-fx-background-color: forestgreen; -fx-background-radius: 15;");
    }

    @FXML
    public void start() {
        if (isStartButtonEnabled) {
            startGame = true;
            notifyListeners("Game Started");
        }
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
    public Player getOwner() {
        // Don't need
        return null;
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
    public void notifyPlayersPointsChanged() {
        // Don't need
    }

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
    public void notifyPlayersObjectiveChanged(int playerID) {
        // Don't need
    }

    @Override
    public void notifyCommonObjectivesChanged() {
        // Don't need
    }

    @Override
    public void showWaitingForServerDialog() {
        // Don't need
    }

    @Override
    public void getReady() {
        // Don't need
    }
}
