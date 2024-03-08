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

import static it.polimi.ingsw.gc42.classes.cards.Kingdom.ANIMAL;

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
    public boolean testCard() {
        ResourceCard c = new ResourceCard(new CardSide(null, null, null, null),
                new CardSide(null, null, null, null), true, 1,
                0, 0, Resource.FEATHER, 5);
        List<Card> a = new ArrayList<Card>();
        a.add(c);
        Game g = new Game();
        Deck d = new Deck(a, 1, g, CardType.RESOURCECARD);
        d.draw();
        return g.isResourceDeckEmpty();
    }
}
