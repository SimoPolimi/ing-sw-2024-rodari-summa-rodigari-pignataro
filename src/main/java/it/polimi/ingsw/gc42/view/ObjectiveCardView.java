package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.Objective;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ObjectiveCardView {
    private CardView cardView;
    private ImageView imageView;
    private Text hint;
    private ImageView hintImage;
    private boolean isShowingDetails;
    private ObjectiveCard modelCard;
    private Listener listener;
    private Text title;
    private Text description;
    private StackPane objDescriptionBox;

    public ObjectiveCardView(CardView cardView, ImageView imageView, Text hint, ObjectiveCard modelCard, ImageView hintImage,
                             boolean isShowingDetails, Text title, Text description, StackPane objDescriptionBox) {
        this.cardView = cardView;
        this.imageView = imageView;
        imageView.setImage(cardView.getFront());
        this.hint = hint;
        this.modelCard = modelCard;
        this.hintImage = hintImage;
        this.isShowingDetails = isShowingDetails;
        this.title = title;
        title.setText(modelCard.getObjective().getName());
        this.description = description;
        description.setText(modelCard.getObjective().getDescription());
        this.objDescriptionBox = objDescriptionBox;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
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

    public ImageView getHintImage() {
        return hintImage;
    }

    public void setHintImage(ImageView hintImage) {
        this.hintImage = hintImage;
    }

    public boolean isShowingDetails() {
        return isShowingDetails;
    }

    public void setShowingDetails(boolean showingDetails) {
        isShowingDetails = showingDetails;
    }

    public Text getTitle() {
        return title;
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public Text getDescription() {
        return this.description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public void setModelCard(ObjectiveCard modelCard) {
        if (null != modelCard && null != listener) {
            modelCard.removeListener(listener);
        }
        this.modelCard = modelCard;
    }

    public void rotate(CardController controller) {

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(350), imageView);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(350), imageView);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(350), imageView);
        if (!isShowingDetails) {
            rotateTransition.setByAngle(-60);
            translateTransition.setByX(-120);
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(1.3);
            scaleTransition.setToY(1.3);
            title.setVisible(true);
            description.setVisible(true);
            objDescriptionBox.setVisible(true);
        } else {
            rotateTransition.setByAngle(60);
            translateTransition.setByX(120);
            scaleTransition.setFromX(1.3);
            scaleTransition.setToX(1);
            scaleTransition.setFromY(1.3);
            scaleTransition.setToY(1);
            title.setVisible(false);
            description.setVisible(false);
            objDescriptionBox.setVisible(false);
        }
        rotateTransition.setOnFinished(e -> controller.unlockInput());
        rotateTransition.play();
        translateTransition.play();
        scaleTransition.play();
        isShowingDetails = !isShowingDetails;
    }
}
