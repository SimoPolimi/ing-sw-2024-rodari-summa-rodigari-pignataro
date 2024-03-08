package it.polimi.ingsw.gc42;

import it.polimi.ingsw.gc42.classes.Deck;
import it.polimi.ingsw.gc42.classes.cards.*;
import it.polimi.ingsw.gc42.classes.game.Game;
import it.polimi.ingsw.gc42.exceptions.NoSuchDeckTypeException;
import it.polimi.ingsw.gc42.interfaces.DeckListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
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


}
