package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.exceptions.NoSuchCardException;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import javafx.animation.RotateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ObjectiveCardView {
    private CardView cardView;
    private ImageView imageView;
    private Text hint;
    private ImageView hintImage;
    private boolean isShowingDetails;
    private Card modelCard;
    private Listener listener;
    private Text description;

    public ObjectiveCardView(CardView cardView, ImageView imageView, Text hint, ImageView hintImage, boolean isShowingDetails) {
        this.cardView = cardView;
        this.imageView = imageView;
        this.hint = hint;
        this.hintImage = hintImage;
        this.isShowingDetails = isShowingDetails;
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

    public Text getDescription() {
        return this.description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public void setModelCard(Card modelCard) {
        if (null != modelCard && null != listener) {
            modelCard.removeListener(listener);
        }
        this.modelCard = modelCard;
    }

    public void rotate(CardController controller) {

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(350), imageView);
        if (!isShowingDetails) {
            rotateTransition.setByAngle(-60);
            description.setVisible(true);
        } else {
            rotateTransition.setByAngle(60);
            description.setVisible(false);
        }
        rotateTransition.setOnFinished(e -> controller.canReadKeyboard = true);
        rotateTransition.play();
        isShowingDetails = !isShowingDetails;
    }
}
