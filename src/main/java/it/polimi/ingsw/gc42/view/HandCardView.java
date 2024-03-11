package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.NoSuchElementException;

public class HandCardView {
    private CardView card;
    private ImageView imageView;
    private Text hint;
    private ImageView hintIcon;
    private boolean isSelected;
    private Card modelCard;
    private Listener listener;

    public HandCardView(CardView card, ImageView imageView, Text hint, ImageView hintIcon, Card modelCard) {
        this.card = card;
        this.imageView = imageView;
        this.hint = hint;
        this.hintIcon = hintIcon;
        setModelCard(modelCard);
        this.isSelected = false;
    }

    public CardView getCard() {
        return card;
    }

    public void setCard(CardView card) {
        this.card = card;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Text getHint() {
        return hint;
    }

    public void setHint(Text hint) {
        this.hint = hint;
    }

    public ImageView getHintIcon() {
        return hintIcon;
    }

    public void setHintIcon(ImageView hintIcon) {
        this.hintIcon = hintIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Card getModelCard() {
        return modelCard;
    }

    public void setModelCard(Card modelCard) {
        if (null != modelCard && null != listener) {
            modelCard.removeListener(listener);
        }
        this.modelCard = modelCard;
        listener = new Listener() {
            @Override
            public void onEvent() {
                try {
                    flipCard(modelCard.getId());
                } catch (NoSuchCardException e) {
                    e.printStackTrace();
                }
            }
        };
        modelCard.setListener(listener);
    }

    private void flipCard(int cardId) throws NoSuchCardException {
        if (this.modelCard.getId() == cardId) {
            card.flip();
        } else throw new NoSuchCardException();
    }

    public void flip() {
        modelCard.flip();
    }

    public void select() {
        this.isSelected = true;
        hint.setVisible(true);
        hintIcon.setVisible(true);
        ScaleTransition select1 = new ScaleTransition(Duration.millis(100), this.imageView);
        select1.setFromX(1);
        select1.setFromY(1);
        select1.setToX(1.2);
        select1.setToY(1.2);
        select1.play();
    }

    public void deselect(){
        this.isSelected = false;
        hint.setVisible(false);
        hintIcon.setVisible(false);
        ScaleTransition deselect2 = new ScaleTransition(Duration.millis(100), this.imageView);
        deselect2.setFromX(1.2);
        deselect2.setFromY(1.2);
        deselect2.setToX(1);
        deselect2.setToY(1);
        deselect2.play();
    }
}
