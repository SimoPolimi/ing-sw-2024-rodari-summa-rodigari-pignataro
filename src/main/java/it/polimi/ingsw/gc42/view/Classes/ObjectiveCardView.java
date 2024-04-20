package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
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
    private VBox container;
    private boolean isPrivacyModeEnabled;
    private GUIController controller;

    public ObjectiveCardView(boolean isPrivacyModeEnabled, GUIController controller) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.controller = controller;
        build();
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
        imageView.setImage(modelCard.getFrontImage());
        imageView.setVisible(true);
        show();
        imageView.setOnMouseEntered((e) -> {
            if (!isShowingDetails && !controller.isShowingDialog()) {
                select();
            }
        });
        imageView.setOnMouseExited((e) -> {
            if (!isShowingDetails && !controller.isShowingDialog()) {
                deselect();
            }
        });
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rotate();
            }
        });
        title.setText(modelCard.getObjective().getName());
        description.setText(modelCard.getObjective().getDescription());
    }

    public void rotate() {

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

    public void select() {
        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(glowEffect);
    }

    public void deselect() {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(shadow);
    }

    public void show() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), imageView);
        transition.setByX(-200);
        transition.play();
    }

    private void build() {
        container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(30);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);

        HBox hintContainer = new HBox();
        hintContainer.setAlignment(Pos.CENTER);
        hintContainer.setSpacing(10);
        hintContainer.setPadding(new Insets(0, 0, 20, 0));

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

    public Pane getPane() {
        return container;
    }

    public void hide() {
        imageView.setTranslateX(imageView.getTranslateX() + 210);
    }
}
