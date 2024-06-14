package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation of Deck for the GUI.
 * A DeckView is the UI element that shows a Deck on screen.
 * It's composed of a StackPane, containing all the ImageViews of the Card inside the Deck,
 * all showing their Back Side texture.
 */
public class DeckView {
    private final StackPane container;

    // Constructor Method
    /**
     * Constructor Method
     * @param container: the StackPane that will contain the Cards' ImageViews.
     */
    public DeckView(StackPane container) {
        this.container = container;
    }

    /**
     * Getter Method for container
     * @return the StackPane containing the Cards.
     */
    public Pane getContainer() {
        return container;
    }

    /**
     * Updates the DeckView model by deleting the Images inside and recreating them from scratch.
     * @param cards: an ArrayList of Card that represents the content of the Deck to create.
     */
    public void refresh(ArrayList<Card> cards) {
        container.getChildren().clear();
        if (!cards.isEmpty()) {
            for (int i = cards.size()-1; i >= 0; i--) {
                ImageView view = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(cards.get(i).getBackImage()))));
                view.setPreserveRatio(true);
                view.setFitWidth(160);
                DropShadow effect = new DropShadow();
                effect.setHeight(5);
                effect.setWidth(5);
                effect.setRadius(2);
                effect.setBlurType(BlurType.GAUSSIAN);
                view.setEffect(effect);
                view.setTranslateY((double) i /2);
                container.getChildren().add(view);
            }
            container.setCursor(Cursor.HAND);
        } else {
            container.setVisible(false);
            container.setCursor(Cursor.DEFAULT);
        }
    }
}
