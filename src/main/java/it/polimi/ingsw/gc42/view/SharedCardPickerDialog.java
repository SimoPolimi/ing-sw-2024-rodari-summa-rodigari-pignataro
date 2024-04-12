package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;

public class SharedCardPickerDialog extends CardPickerDialog implements Observable {

    private final ArrayList<HandCardView> availableCards = new ArrayList<>();

    // Constructor Method
    public SharedCardPickerDialog(String title, boolean isDismissible, boolean cardsCanBeFlipped, CardController controller) {
        super(title, isDismissible, cardsCanBeFlipped, controller);
        availableCards.addAll(cards);
    }

    // Methods

    // Makes the Card grey when another Player has picked it.
    public void greyCard(int number) {
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-1);
        cards.get(number).getImageView().setEffect(effect);
        availableCards.remove(cards.get(number));
    }

    // Makes the Card colored again.
    public void ungreyCard(int number) {
        DropShadow effect = new DropShadow();
        effect.setWidth(50);
        effect.setHeight(50);
        effect.setBlurType(BlurType.GAUSSIAN);
        cards.get(number).getImageView().setEffect(effect);
        availableCards.add(cards.get(number));
    }
}
