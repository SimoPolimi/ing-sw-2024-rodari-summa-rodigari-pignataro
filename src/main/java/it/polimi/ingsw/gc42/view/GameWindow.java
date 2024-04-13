package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class GameWindow extends Application {

    private boolean isFullScreen = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GUIController controller = fxmlLoader.getController();
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
                    case L -> {

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


        Player player = new Player(Token.RED);
        player.setFirst(true);
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
