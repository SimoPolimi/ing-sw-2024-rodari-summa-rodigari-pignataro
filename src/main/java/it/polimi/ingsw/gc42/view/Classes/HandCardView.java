package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.GoldCard;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.Axis;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Objects;

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
    private boolean isPrivacyModeEnabled;
    private HBox cardContainer;

    public HandCardView(Card modelCard, boolean isPrivacyModeEnabled) {
        build();
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        setModelCard(modelCard);
        this.isSelected = false;
        this.isBeingPlayed = false;
        this.card = new CardView(modelCard.getFrontImage(), modelCard.getBackImage());
    }

    public HandCardView(boolean isPrivacyModeEnabled) {
        build();
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
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
            if (!isPrivacyModeEnabled) {
                this.imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(modelCard.getShowingImage()))));
            } else {
                this.imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(modelCard.getBackImage()))));
            }
            imageView.setVisible(true);
            if (card.isFrontFacing()) {
                card.setOddRotation(true);
                if (imageView.getRotate() == 180) {
                    imageView.setRotate(0);
                }
            }
        } else {
            imageView.setVisible(false);
        }
    }

    public Pane getPane() {
        return cardContainer;
    }


    public void flip() {
        if (!isBeingPlayed && null != modelCard) {
            modelCard.flip();
        }
    }

    private void build() {
        cardContainer = new HBox();
        cardContainer.setAlignment(Pos.CENTER_LEFT);
        cardContainer.setPrefWidth(340);
        cardContainer.setSpacing(40);

         imageView = new ImageView(new Image(Objects.requireNonNull(getClass()
                 .getResourceAsStream("/cards/card1Front.png"))));
         imageView.setFitWidth(140);
         imageView.setPreserveRatio(true);
         DropShadow effect = new DropShadow();
         effect.setBlurType(BlurType.GAUSSIAN);
         effect.setHeight(50);
         effect.setWidth(50);
         effect.setRadius(24.5);
         imageView.setEffect(effect);
         imageView.setVisible(false);
         imageView.setCursor(Cursor.HAND);
         imageView.setPickOnBounds(false);

        VBox hintContainer = new VBox();
         hintContainer.setAlignment(Pos.CENTER);
         hintContainer.setSpacing(40);

         HBox flipHintContainer = new HBox();
         flipHintContainer.setAlignment(Pos.CENTER_LEFT);
         flipHintContainer.setSpacing(10);

         hintIconFlipKB = new ImageView(new Image(Objects.requireNonNull(getClass()
                 .getResourceAsStream("/flipKeyboardHint.png"))));
         hintIconFlipKB.setFitWidth(20);
         hintIconFlipKB.setPreserveRatio(true);
         hintIconFlipKB.setPickOnBounds(true);
         hintIconFlipKB.setVisible(false);
         hintIconFlipKB.setSmooth(true);

         hintIconFlipMouse = new ImageView(new Image(Objects.requireNonNull(getClass()
                 .getResourceAsStream("/RightMouseButton.png"))));
        hintIconFlipMouse.setFitWidth(20);
        hintIconFlipMouse.setPreserveRatio(true);
        hintIconFlipMouse.setPickOnBounds(true);
        hintIconFlipMouse.setVisible(false);
        hintIconFlipMouse.setSmooth(true);

        hintFlip = new Text("Flip");
        hintFlip.setFill(Paint.valueOf("white"));
        hintFlip.setFont(Font.font("Contantia Italic", 15));
        hintFlip.setVisible(false);

        flipHintContainer.getChildren().addAll(hintIconFlipKB, hintIconFlipMouse, hintFlip);

        HBox enterHintContainer = new HBox();
        enterHintContainer.setAlignment(Pos.CENTER_LEFT);
        enterHintContainer.setSpacing(10);

        hintIconEnterKB = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/EnterHint.png"))));
        hintIconEnterKB.setFitWidth(20);
        hintIconEnterKB.setPreserveRatio(true);
        hintIconEnterKB.setPickOnBounds(true);
        hintIconEnterKB.setVisible(false);
        hintIconEnterKB.setSmooth(true);

        hintIconEnterMouse = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/LeftMouseButton.png"))));
        hintIconEnterMouse.setFitWidth(20);
        hintIconEnterMouse.setPreserveRatio(true);
        hintIconEnterMouse.setPickOnBounds(true);
        hintIconEnterMouse.setVisible(false);
        hintIconEnterMouse.setSmooth(true);

        hintEnter = new Text("Place");
        hintEnter.setFill(Paint.valueOf("white"));
        hintEnter.setFont(Font.font("Contantia Italic", 15));
        hintEnter.setVisible(false);

        enterHintContainer.getChildren().addAll(hintIconEnterKB, hintIconEnterMouse, hintEnter);

         hintContainer.getChildren().addAll(flipHintContainer, enterHintContainer);

        cardContainer.getChildren().addAll(imageView, hintContainer);
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

    public void deselect(GUIController controller, boolean unlockInputAfter){
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
            if (unlockInputAfter) {
                deselect2.setOnFinished(e -> {
                    controller.unlockInput();
                });
            }
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
           imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(modelCard.getShowingImage()))));
            flipCardHalf2.play();
        });
        flipCardHalf2.setAxis(Rotate.Y_AXIS);
        flipCardHalf2.setOnFinished(e -> {
            if (!isSelected) {
                deselect(controller, true);
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
        deselect(controller, true);
    }

    public void showError() {
        RotateTransition transition = new RotateTransition(Duration.millis(50), imageView);
        transition.setFromAngle(0);
        transition.setToAngle(30);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.setAxis(Rotate.Z_AXIS);

        RotateTransition transition1 = new RotateTransition(Duration.millis(50), imageView);
        transition1.setFromAngle(0);
        transition1.setToAngle(-30);
        transition1.setAutoReverse(true);
        transition1.setCycleCount(2);
        transition1.setAxis(Rotate.Z_AXIS);

        transition.setOnFinished((e) -> transition1.play());
        transition.play();
    }
}