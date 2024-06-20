package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Implementation of the Objective Card in GUI.
 * It contains a CardView for the textures and a few Texts and ImageViews to show Hints for the supported actions.
 * It supports the following actions:
 * - Rotate (Show)
 * - Rotate (Hide)
 * - Show details
 * - Hide details
 * It supports a Privacy Mode, where the CardView is not interactable and it only shows the Back Side texture
 */
public class ObjectiveCardView {
    // Attributes
    private CardView cardView;
    private ImageView imageView;
    private Text hint;
    private ImageView hintImage;
    private boolean isShowingDetails;
    private Text title;
    private Text description;
    private StackPane objDescriptionBox;
    private VBox container;
    private boolean isPrivacyModeEnabled;
    private GUIController controller;

    // Constructor Method

    /**
     * Constructor Method
     * @param isPrivacyModeEnabled: a boolean indicating if Privacy Mode is enabled or not
     * @param controller: the GUIController that created this ObjectiveCardView
     */
    public ObjectiveCardView(boolean isPrivacyModeEnabled, GUIController controller) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.controller = controller;
        build();
    }

    // Getters and Setters

    /**
     * Getter Method for cardView
     * @return the CardView containing the textures of this ObjectiveCardView
     */
    public CardView getCardView() {
        return cardView;
    }

    /**
     * Setter Method for cardView
     * @param cardView: the CardView containing the textures of this ObjectiveCardView
     */
    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    /**
     * Getter Method for imageView
     * @return the ImageView (JavaFx element) that shows this ObjectiveCardView's texture
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Setter Method for imageView
     * @param imageView: the ImageView (JavaFx element) that shows this ObjectiveCardView's texture
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * Getter Method for hint
     * @return the Text (JavaFx element) that's showing this ObjectiveCardView's "Show/Hide" hint
     */
    public Text getHint() {
        return hint;
    }

    /**
     * Setter Method for hint
     * @param hint: the Text (JavaFx element) that's showing this ObjectiveCardView's "Show/Hide" hint
     */
    public void setHint(Text hint) {
        this.hint = hint;
    }

    /**
     * Getter Method for hintImage
     * @return the ImageView (JavaFx element) that's showing this ObjectiveCardView's "Show/Hide" hint icon
     */
    public ImageView getHintImage() {
        return hintImage;
    }

    /**
     * Setter Method for hintImage
     * @param hintImage: the ImageView (JavaFx element) that's showing this ObjectiveCardView's "Show/Hide" hint icon
     */
    public void setHintImage(ImageView hintImage) {
        this.hintImage = hintImage;
    }

    /**
     * Getter Method for isShowingDetails
     * @return a boolean value indicating if the ObjectiveCardView is currently showing it details (name and description) or not
     */
    public boolean isShowingDetails() {
        return isShowingDetails;
    }

    /**
     * Setter Method for isShowingDetails
     * @param showingDetails: a boolean value indicating if the ObjectiveCardView is currently showing it details (name and description) or not
     */
    public void setShowingDetails(boolean showingDetails) {
        isShowingDetails = showingDetails;
    }

    /**
     * Getter Method for title
     * @return the Text (JavaFx element) that's showing this ObjectiveCardView's Name
     */
    public Text getTitle() {
        return title;
    }

    /**
     * Setter Method for title
     * @param title: the Text (JavaFx element) that's showing this ObjectiveCardView's Name
     */
    public void setTitle(Text title) {
        this.title = title;
    }

    /**
     * Getter Method for description
     * @return the Text (JavaFx element) that's showing this ObjectiveCardView's Description
     */
    public Text getDescription() {
        return this.description;
    }

    /**
     * Setter Method for description
     * @param description: the Text (JavaFx element) that's showing this ObjectiveCardView's Description
     */
    public void setDescription(Text description) {
        this.description = description;
    }

    /**
     * Setter Method for the ObjectiveCard.
     * Sets the Objective's name, description and textures in the UI
     */
    public void setModelCard(ObjectiveCard card) {
        if (!isPrivacyModeEnabled) {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
        } else {
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getBackImage()))));
        }
        imageView.setVisible(true);
        show();
        if (!isPrivacyModeEnabled) {
            imageView.setOnMouseEntered((e) -> {
                if (!isShowingDetails && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
                    select();
                }
            });
            imageView.setOnMouseExited((e) -> {
                if (!isShowingDetails && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
                    deselect();
                }
            });
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!controller.isShowingDialog() && !controller.isCommonTableDown()) {
                        rotate();
                    }
                }
            });
        }
        title.setText(card.getObjective().getName());
        description.setText(card.getObjective().getDescription());
    }

    /**
     * Visually animates the "Rotate" feature, both to Show and to Hide.
     * This animation moves the ImageView between its Hidden state (close to the Screen's edge, slightly rotated) to its
     * Shown state (horizontal, slightly bigger, all shown on-screen, with Name and Description visible)
     */
    public void rotate() {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), imageView);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), imageView);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(controller.getCurrentAnimationSpeed()), imageView);
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
            select();

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
            deselect();
        }
        rotateTransition.setOnFinished(e -> controller.unlockInput());
        rotateTransition.play();
        translateTransition.play();
        scaleTransition.play();
        isShowingDetails = !isShowingDetails;
    }

    /**
     * Visually selects the ObjectiveCardView by applying a yellow glowing effect to the CardView
     */
    public void select() {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(glowEffect);
    }

    /**
     * Visually deselects the ObjectiveCardView by removing the yellow glowing effect from the CardView
     */
    public void deselect() {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(shadow);
    }

    /**
     * Visually animates the "Show" feature of "Rotate"
     */
    public void show() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), imageView);
        transition.setByX(-200);
        transition.play();
    }

    /**
     * Builds the UI
     */
    private void build() {
        container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(30);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        if (isPrivacyModeEnabled) {
            container.setTranslateX(-200);
            container.setTranslateY(300);
        }

        HBox hintContainer = new HBox();
        hintContainer.setAlignment(Pos.CENTER);
        hintContainer.setSpacing(10);
        hintContainer.setPadding(new Insets(0, 0, 20, 0));

        if (!isPrivacyModeEnabled) {
            hintImage = new ImageView(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/KBObjectiveHint.png"))));
            hintImage.setFitWidth(20);
            hintImage.setPreserveRatio(true);
            hintImage.setSmooth(true);
            hintImage.setPickOnBounds(true);

            hint = new Text("Secret Objective");
            hint.setFill(Paint.valueOf("white"));
            hint.setFont(Font.font("Constantia Italic", 15));
            HBox.setMargin(hint, new Insets(3, 0, 0, 0));

            hintContainer.getChildren().addAll(hintImage, hint);
        }

        imageView = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/cards/card99Front.png"))));
        imageView.setFitWidth(160);
        imageView.setPreserveRatio(true);
        imageView.setRotate(60);
        imageView.setTranslateX(70);
        DropShadow effect = new DropShadow();
        effect.setBlurType(BlurType.GAUSSIAN);
        effect.setHeight(50);
        effect.setWidth(50);
        effect.setRadius(24.5);
        imageView.setEffect(effect);
        imageView.setCursor(Cursor.HAND);
        imageView.setPickOnBounds(false);

        objDescriptionBox = new StackPane();
        objDescriptionBox.setOpacity(0.7);
        objDescriptionBox.setStyle("-fx-background-color: #ececec; -fx-background-radius: 10;");
        objDescriptionBox.setVisible(false);
        VBox.setMargin(objDescriptionBox, new Insets(20, 0, 0, 30));
        objDescriptionBox.setPadding(new Insets(10));

        VBox textContainer = new VBox();
        textContainer.setSpacing(10);

        title = new Text("Title");
        title.setStrokeType(StrokeType.OUTSIDE);
        title.setStrokeWidth(25);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setUnderline(true);
        title.setVisible(false);
        title.setWrappingWidth(150);
        title.setFont(Font.font("Constantia Italic", 15));

        description = new Text("Description");
        description.setStrokeType(StrokeType.OUTSIDE);
        description.setStrokeWidth(20);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setVisible(false);
        description.setWrappingWidth(150);
        description.setFont(Font.font("Constantia Italic", 15));

        textContainer.getChildren().addAll(title, description);

        objDescriptionBox.getChildren().addAll(textContainer);

        container.getChildren().addAll(hintContainer, imageView, objDescriptionBox);
    }

    /**
     * Getter Method for container
     * @return the Pane containing this ObjectiveCardView's UI
     */
    public Pane getPane() {
        return container;
    }

    /**
     * Visually animates the "Hide" feature of "Rotate"
     */
    public void hide() {
        imageView.setTranslateX(imageView.getTranslateX() + 210);
    }
}
