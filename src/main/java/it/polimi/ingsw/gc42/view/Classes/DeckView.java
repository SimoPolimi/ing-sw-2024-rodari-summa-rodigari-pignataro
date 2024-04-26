package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class DeckView {
    private final StackPane container;

    public DeckView(StackPane container) {
        this.container = container;
    }

    public Pane getContainer() {
        return container;
    }

    public void refresh(ArrayList<Card> cards) {
        container.getChildren().clear();
        if (!cards.isEmpty()) {
            for (int i = cards.size()-1; i >= 0; i--) {
                ImageView view = new ImageView(cards.get(i).getBackImage());
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
