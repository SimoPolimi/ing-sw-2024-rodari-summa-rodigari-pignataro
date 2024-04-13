package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.view.AlreadyShowingADialogException;
import it.polimi.ingsw.gc42.view.CardPickerDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameWindow extends Application {

    public Card card1;
    public Card card2;
    public Card card3;

    private boolean isFullScreen = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameWindow.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CardController controller = fxmlLoader.getController();
        GameController gameController = new GameController();
        controller.initializeCards();
        stage.setMinHeight(670);
        stage.setMinWidth(800);
        stage.setScene(scene);

        controller.setGameController(gameController);

        stage.setTitle("Codex Naturalis");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));

        scene.setOnKeyPressed(e -> {
            if (controller.canReadInput()) {
                switch (e.getCode()) {
                    case F -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("F");
                        }
                        controller.onFKeyPressed();
                    }
                    case DOWN -> {
                        controller.moveDown();
                    }
                    case UP -> {
                        controller.moveUp();
                    }
                    case E -> {
                        controller.toggleHand();
                    }
                    case O -> {
                        controller.flipObjective();
                    }
                    case LEFT -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("LEFT");
                        }
                    }
                    case RIGHT -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("RIGHT");
                        }
                    }
                    case ENTER -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("ENTER");
                        }
                    }
                }
            }
            scene.setOnKeyReleased(e1 -> {
                switch (e.getCode()) {
                    case F11 -> {
                        isFullScreen = !isFullScreen;
                        stage.setFullScreen(isFullScreen);
                    }
                }
            });
        });

        stage.show();

        gameController.addView(controller);


        Player player = new Player(Token.BLUE);
        Game game = gameController.getGame();
        controller.setPlayer(player);
        gameController.addPlayer(player);
        gameController.setCurrentStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
        player.setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);

        /*gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), 1, 0);
        gameController.playCard((PlayableCard) game.getGoldPlayingDeck().getDeck().draw(), 0, 1);
        gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), -1, 0);
        PlayableCard card = (PlayableCard) game.getGoldPlayingDeck().getDeck().draw();
        card.flip();
        gameController.playCard(card, 0, -1);
        gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), 1, 1);
        gameController.playCard((PlayableCard) game.getGoldPlayingDeck().getDeck().draw(), 1, -1);
        gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), -1, -1);
        gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), 2, 0);
        gameController.playCard((PlayableCard) game.getResourcePlayingDeck().getDeck().draw(), 3, 0);*/
    }



    public static void main(String[] args) {
        launch();
    }
}
