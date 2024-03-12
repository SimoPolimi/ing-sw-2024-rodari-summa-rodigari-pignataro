package it.polimi.ingsw.gc42.view;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ObjectiveCardView {
    private CardView cardView;
    private ImageView imageView;
    private Text hint;
    private ImageView hintImage;
    private boolean isShowingDetails;

    public ObjectiveCardView(CardView cardView, ImageView imageView, Text hint, ImageView hintImage, boolean isShowingDetails) {
        this.cardView = cardView;
        this.imageView = imageView;
        this.hint = hint;
        this.hintImage = hintImage;
        this.isShowingDetails = isShowingDetails;
    }
}
