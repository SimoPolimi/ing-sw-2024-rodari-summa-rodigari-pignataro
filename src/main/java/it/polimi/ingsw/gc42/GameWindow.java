package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        controller.initializeCards();
        stage.setTitle("Prova Titolo Finestra!");
        stage.setMinHeight(670);
        stage.setMinWidth(800);
        stage.setScene(scene);

        stage.setTitle("Codex Naturalis");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));

        scene.setOnKeyPressed(e -> {
            if (controller.canReadKeyboard) {
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

        controller.setCards(card1, card2, card3);
    }



    public static void main(String[] args) {
        launch();
    }
}
