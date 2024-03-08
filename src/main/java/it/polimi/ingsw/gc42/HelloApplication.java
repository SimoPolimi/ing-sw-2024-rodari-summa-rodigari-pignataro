package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.classes.Deck;
import it.polimi.ingsw.gc42.classes.cards.*;
import it.polimi.ingsw.gc42.classes.game.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        Image a = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/logo.png")));
        stage.getIcons().add(a);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }




    // Test Method
    public static boolean testCard() {
        Game g = new Game();
        while (g.getPlayingDeck().getResourceCardDeck().getCounter() >= 0) {
            Card c = g.getPlayingDeck().getResourceCardDeck().draw();
        }
        return g.isResourceDeckEmpty();
    }
}
