package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.HelloController;
import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.chart.Axis;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.NoSuchElementException;
import java.util.Objects;

public class HandCardView {
    private CardView card;
    private ImageView imageView;
    private Text hint;
    private ImageView hintIcon;
    private ImageView overlay;
    private boolean isSelected;
    private Card modelCard;
    private Listener listener;

    public HandCardView(ImageView imageView, Text hint, ImageView hintIcon, Card modelCard, ImageView overlay) {
        this.imageView = imageView;
        imageView.setImage(modelCard.getFrontImage());
        this.hint = hint;
        this.hintIcon = hintIcon;
        setModelCard(modelCard);
        this.isSelected = false;
        this.overlay = overlay;
        this.card = new CardView(modelCard.getFrontImage(), modelCard.getBackImage());
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
        this.card = new CardView(modelCard.getFrontImage(), modelCard.getBackImage());
    }

    private void flipCard(int cardId) throws NoSuchCardException {
        if (this.modelCard.getId() == cardId) {
            card.flip();
        } else throw new NoSuchCardException();
    }

    public void flip() {
        modelCard.flip();
    }

    public void select(CardController controller) {
        controller.blockInput();
        this.isSelected = true;
        hint.setVisible(true);
        hintIcon.setVisible(true);
        overlay.setVisible(true);
        ScaleTransition select1 = new ScaleTransition(Duration.millis(100), this.imageView);
        select1.setFromX(1);
        select1.setFromY(1);
        select1.setToX(1.2);
        select1.setToY(1.2);
        select1.setOnFinished(e -> controller.unlockInput());
        select1.play();
    }

    public void deselect(CardController controller){
        controller.blockInput();
        this.isSelected = false;
        hint.setVisible(false);
        hintIcon.setVisible(false);
        overlay.setVisible(false);
        ScaleTransition deselect2 = new ScaleTransition(Duration.millis(100), this.imageView);
        deselect2.setFromX(1.2);
        deselect2.setFromY(1.2);
        deselect2.setToX(1);
        deselect2.setToY(1);
        deselect2.setOnFinished(e -> controller.unlockInput());
        deselect2.play();
    }

    public void hide(int position, CardController controller) {
        TranslateTransition t = new TranslateTransition(Duration.millis(350), imageView);
        t.setByX(-180);
        t.setOnFinished(e -> controller.unlockInput());

        switch (position) {
            case 1:
                t.setByY(80);
                t.play();
                RotateTransition r1 = new RotateTransition(Duration.millis(350), imageView);
                r1.setAxis(Rotate.Z_AXIS);
                imageView.setRotate(0);
                r1.setByAngle(-45);
                r1.play();
                break;
            case 3:
                t.setByY(-80);
                t.play();

                RotateTransition r3 = new RotateTransition(Duration.millis(350), imageView);
                r3.setAxis(Rotate.Z_AXIS);
                imageView.setRotate(0);
                r3.setByAngle(45);
                r3.play();
                break;
            default: break;
        }
        t.play();
    }

    public void show(int position, CardController controller) {
        TranslateTransition t = new TranslateTransition(Duration.millis(350), imageView);
        t.setByX(180);
        t.setOnFinished(e -> controller.unlockInput());

        switch (position) {
            case 1:
                t.setByY(-80);
                t.play();
                RotateTransition r1 = new RotateTransition(Duration.millis(350), imageView);
                r1.setAxis(Rotate.Z_AXIS);
                r1.setByAngle(45);
                r1.play();
                break;
            case 3:
                t.setByY(80);
                t.play();

                RotateTransition r3 = new RotateTransition(Duration.millis(350), imageView);
                r3.setAxis(Rotate.Z_AXIS);
                r3.setByAngle(-45);
                r3.play();
                break;
            default: break;
        }
        t.play();
    }

    public void visualFlip(CardController controller) {
        overlay.setVisible(false);
        ScaleTransition jumpHalf1 = new ScaleTransition(Duration.millis(200), imageView);
        jumpHalf1.setFromX(1.2);
        jumpHalf1.setFromY(1.2);
        jumpHalf1.setToX(1.5);
        jumpHalf1.setToY(1.5);
        jumpHalf1.setAutoReverse(true);
        jumpHalf1.setCycleCount(2);
        controller.blockInput();
        jumpHalf1.setOnFinished(e -> controller.unlockInput());
        jumpHalf1.play();

        RotateTransition flipCardHalf2 = new RotateTransition(Duration.millis(200), imageView);

        RotateTransition flipCardHalf1 = new RotateTransition(Duration.millis(200), imageView);
        flipCardHalf1.setAxis(Rotate.Y_AXIS);
        flipCardHalf1.setOnFinished(event -> {
            if (!card.isFrontFacing()) {
                imageView.setImage(card.getBack());
            } else {
                imageView.setImage(card.getFront());
            }
            flipCardHalf2.play();
        });
        flipCardHalf2.setAxis(Rotate.Y_AXIS);
        flipCardHalf2.setOnFinished(e -> {
            if (!isSelected) {
                deselect(controller);
            } else {
                overlay.setVisible(true);
            }
        });

        if (card.isOddRotation()) {
            flipCardHalf1.setFromAngle(0);
            flipCardHalf1.setToAngle(90);
            flipCardHalf2.setFromAngle(90);
            flipCardHalf2.setToAngle(180);
        } else {
            flipCardHalf1.setFromAngle(-180);
            flipCardHalf1.setToAngle(-90);
            flipCardHalf2.setFromAngle(-90);
            flipCardHalf2.setToAngle(0);
        }
        card.setOddRotation(!card.isOddRotation());
        flipCardHalf1.play();
    }
}