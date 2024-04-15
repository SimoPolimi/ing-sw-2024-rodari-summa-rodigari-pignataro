package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class HandCardView {
    private CardView card;
    private ImageView imageView;
    private Text hintFlip;
    private ImageView hintIconFlipKB;
    private ImageView hintIconFlipMouse;
    private Text hintEnter;
    private ImageView hintIconEnterKB;
    private ImageView hintIconEnterMouse;
    private boolean isSelected;
    private boolean isBeingPlayed;
    private Card modelCard;
    private Listener listener;

    public HandCardView(ImageView imageView, Text hintFlip, ImageView hintIconFlipKB, ImageView hintIconFlipMouse,
                        Text hintEnter, ImageView hintIconEnterKB, ImageView hintIconEnterMouse, Card modelCard) {
        this.imageView = imageView;
        imageView.setImage(modelCard.getFrontImage());
        this.hintFlip = hintFlip;
        this.hintIconFlipKB = hintIconFlipKB;
        this.hintIconFlipMouse = hintIconFlipMouse;
        this.hintEnter = hintEnter;
        this.hintIconEnterKB = hintIconEnterKB;
        this.hintIconEnterMouse = hintIconEnterMouse;
        setModelCard(modelCard);
        this.isSelected = false;
        this.isBeingPlayed = false;
        this.card = new CardView(modelCard.getFrontImage(), modelCard.getBackImage());
    }

    public HandCardView(ImageView imageView, Text hintFlip, ImageView hintIconFlipKB,
                        ImageView hintIconFlipMouse, Text hintEnter, ImageView hintIconEnterKB, ImageView hintIconEnterMouse) {
        this.imageView = imageView;
        this.hintFlip = hintFlip;
        this.hintIconFlipKB = hintIconFlipKB;
        this.hintIconFlipMouse = hintIconFlipMouse;
        this.hintEnter = hintEnter;
        this.hintIconEnterKB = hintIconEnterKB;
        this.hintIconEnterMouse = hintIconEnterMouse;
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

    public Text getHintFlip() {
        return hintFlip;
    }

    public void setHintFlip(Text hintFlip) {
        this.hintFlip = hintFlip;
    }

    public ImageView getHintIconFlipKB() {
        return hintIconFlipKB;
    }

    public void setHintIconFlipKB(ImageView hintIconFlipKB) {
        this.hintIconFlipKB = hintIconFlipKB;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isBeingPlayed() {
        return isBeingPlayed;
    }

    public void setBeingPlayed(boolean beingPlayed) {
        this.isBeingPlayed = beingPlayed;
    }

    public Card getModelCard() {
        return modelCard;
    }

    public void setModelCard(Card modelCard) {
        this.modelCard = modelCard;
        if (null != modelCard) {
            this.card = new CardView(modelCard.getFrontImage(), modelCard.getBackImage());
            this.imageView.setImage(modelCard.getShowingImage());
            imageView.setVisible(true);
            if (card.isFrontFacing()) {
                card.setOddRotation(true);
            }
        }
    }

    private void flipCard(int cardId) throws NoSuchCardException {
        if (this.modelCard.getId() == cardId) {
            card.flip();
        } else throw new NoSuchCardException();
    }

    public void flip() {
        if (!isBeingPlayed)
        modelCard.flip();
    }

    public void select(GUIController controller) {
        if (!isBeingPlayed) {
            controller.blockInput();
            this.isSelected = true;
            hintFlip.setVisible(true);
            hintIconFlipKB.setVisible(true);
            hintIconFlipMouse.setVisible(true);
            hintEnter.setVisible(true);
            hintIconEnterKB.setVisible(true);
            hintIconEnterMouse.setVisible(true);
            DropShadow glowEffect = new DropShadow();
            glowEffect.setWidth(100);
            glowEffect.setHeight(100);
            glowEffect.setColor(Color.YELLOW);
            glowEffect.setBlurType(BlurType.GAUSSIAN);
            imageView.setEffect(glowEffect);
            ScaleTransition select1 = new ScaleTransition(Duration.millis(100), this.imageView);
            select1.setFromX(1);
            select1.setFromY(1);
            select1.setToX(1.3);
            select1.setToY(1.3);
            select1.setOnFinished(e -> controller.unlockInput());
            select1.play();
        }
    }

    public void deselect(GUIController controller){
        if (!isBeingPlayed) {
            controller.blockInput();
            this.isSelected = false;
            hintFlip.setVisible(false);
            hintIconFlipKB.setVisible(false);
            hintIconFlipMouse.setVisible(false);
            hintEnter.setVisible(false);
            hintIconEnterKB.setVisible(false);
            hintIconEnterMouse.setVisible(false);
            DropShadow shadow = new DropShadow();
            shadow.setWidth(50);
            shadow.setHeight(50);
            shadow.setBlurType(BlurType.GAUSSIAN);
            imageView.setEffect(shadow);
            ScaleTransition deselect2 = new ScaleTransition(Duration.millis(100), this.imageView);
            deselect2.setFromX(1.3);
            deselect2.setFromY(1.3);
            deselect2.setToX(1);
            deselect2.setToY(1);
            deselect2.setOnFinished(e -> controller.unlockInput());
            deselect2.play();
        }
    }

    public void hide(int position, GUIController controller) {
        TranslateTransition t = new TranslateTransition(Duration.millis(350), imageView);
        t.setByX(-155);
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
        deselect(controller);
        t.play();
    }

    public void show(int position, GUIController controller) {
        TranslateTransition t = new TranslateTransition(Duration.millis(350), imageView);
        t.setByX(155);
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

    public void visualFlip(GUIController controller) {
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
           imageView.setImage(modelCard.getShowingImage());
            flipCardHalf2.play();
        });
        flipCardHalf2.setAxis(Rotate.Y_AXIS);
        flipCardHalf2.setOnFinished(e -> {
            if (!isSelected) {
                deselect(controller);
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

    public void setPlayingSelection(GUIController controller) {
        isBeingPlayed = true;
        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.ORANGERED);
        glowEffect.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(glowEffect);
        ScaleTransition select1 = new ScaleTransition(Duration.millis(100), this.imageView);
        select1.setFromX(1);
        select1.setFromY(1);
        select1.setToX(1.3);
        select1.setToY(1.3);
        select1.setOnFinished(e -> controller.unlockInput());
        select1.play();
    }

    public void removePlayingSelection(GUIController controller) {
        isBeingPlayed = false;
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(shadow);
        ScaleTransition deselect2 = new ScaleTransition(Duration.millis(100), this.imageView);
        deselect2.setFromX(1.3);
        deselect2.setFromY(1.3);
        deselect2.setToX(1);
        deselect2.setToY(1);
        deselect2.setOnFinished(e -> controller.unlockInput());
        deselect2.play();
        deselect(controller);
    }
}