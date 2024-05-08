package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
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

public class GamesListViewController {
    @FXML
    private ScrollPane gamesList;
    @FXML
    private VBox refreshButton;

    private NetworkController server;

    private Player player;

    private final ArrayList<Listener> listeners = new ArrayList<>();
    private int pickedGame;

    public void setServer(NetworkController server) {
        this.server = server;
    }

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

    private void joinGame(int gameID) {
        try {
            server.pickGame(gameID);
            server.addPlayer(player);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @FXML
    public void newGame() {
        notifyListeners("New Game");
    }

    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

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
