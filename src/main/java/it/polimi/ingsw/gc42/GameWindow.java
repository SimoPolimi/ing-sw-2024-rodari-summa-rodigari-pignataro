package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
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


        Player player = new Player(Token.BLUE);
        Game game = gameController.getGame();
        game.addPlayer(player);
        controller.setPlayer(player);
        gameController.drawStartingHand();
        gameController.playCard(game.getStarterDeck().draw(), 0, 0);
        gameController.playCard(game.getResourcePlayingDeck().getDeck().draw(), 1, 0);
        gameController.playCard(game.getGoldPlayingDeck().getDeck().draw(), 0, 1);
        gameController.playCard(game.getResourcePlayingDeck().getDeck().draw(), -1, 0);
        Card card = game.getGoldPlayingDeck().getDeck().draw();
        card.flip();
        gameController.playCard(card, 0, -1);
        gameController.playCard(game.getResourcePlayingDeck().getDeck().draw(), 1, 1);
        gameController.playCard(game.getGoldPlayingDeck().getDeck().draw(), 1, -1);
    }



    public static void main(String[] args) {
        launch();
    }
}
