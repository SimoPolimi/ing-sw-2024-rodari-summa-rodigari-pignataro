package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;

public class HandView {
    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;

    private ImageView KBNavHint;
    private Text textNav;
    private ImageView KBCollapseHint;
    private Text textCollapse;
    GUIController controller;
    private boolean isHidden;
    private boolean isPrivacyModeEnabled;
    private VBox container;

    public HandView(boolean isPrivacyModeEnabled, GUIController controller) {
        build();
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.controller = controller;
    }

    public Pane getPane() {
        return container;
    }

    private void build() {
        container = new VBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefHeight(578.0);
        container.setPrefWidth(100);
        container.setSpacing(30);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        //container.setPadding(new Insets(0, 0, 0, 40));

        HBox collapseHintContainer = new HBox();
        collapseHintContainer.setAlignment(Pos.CENTER_LEFT);
        collapseHintContainer.setPrefHeight(50);
        collapseHintContainer.setSpacing(15);
        collapseHintContainer.setPadding(new Insets(0, 0, 0, 15));

        KBCollapseHint = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/collapseHandHint.png"))));
        KBCollapseHint.setFitWidth(20);
        KBCollapseHint.setPreserveRatio(true);
        KBCollapseHint.setPickOnBounds(true);
        KBCollapseHint.setSmooth(true);
        if (!isPrivacyModeEnabled) {
            KBCollapseHint.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    controller.toggleHand();
                }
            });
            KBCollapseHint.setCursor(Cursor.HAND);
        }

        textCollapse = new Text("Collapse");
        textCollapse.setFill(Paint.valueOf("white"));
        textCollapse.setFont(Font.font("Contantia Italic", 15));
        if (!isPrivacyModeEnabled) {
            textCollapse.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    controller.toggleHand();
                }
            });
            textCollapse.setCursor(Cursor.HAND);
        }

        collapseHintContainer.getChildren().addAll(KBCollapseHint, textCollapse);

        handCardView1 = new HandCardView(isPrivacyModeEnabled);
        handCardView2 = new HandCardView(isPrivacyModeEnabled);
        handCardView3 = new HandCardView(isPrivacyModeEnabled);

        Pane card1Container = handCardView1.getPane();
        Pane card2Container = handCardView2.getPane();
        Pane card3Container = handCardView3.getPane();

        HBox bottomHintContainer = new HBox();
        bottomHintContainer.setAlignment(Pos.CENTER_LEFT);
        bottomHintContainer.setPrefHeight(50);
        bottomHintContainer.setSpacing(15);
        bottomHintContainer.setPadding(new Insets(0, 0, 0, 15));

        KBNavHint = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/navigateKeyboardHintVertical.png"))));
        KBNavHint.setFitWidth(20);
        KBNavHint.setPreserveRatio(true);
        KBNavHint.setPickOnBounds(true);
        KBNavHint.setSmooth(true);

        textNav = new Text("Navigate");
        textNav.setFill(Paint.valueOf("white"));
        textNav.setFont(Font.font("Contantia Italic", 15));

        bottomHintContainer.getChildren().addAll(KBNavHint, textNav);

        container.getChildren().addAll(collapseHintContainer, card1Container, card2Container, card3Container, bottomHintContainer);
    }

    public boolean isHidden() {
        return isHidden;
    }

    public HandCardView getHandCardView(int number) {
        switch (number) {
            case 1 -> {
                return handCardView1;
            }
            case 2 -> {
                return handCardView2;
            }
            case 3 -> {
                return handCardView3;
            }
            default -> {
                return null;
            }
        }
    }

    public void hide() {
        controller.blockInput();
        controller.deselectAllCards(false);
        isHidden = true;
        KBNavHint.setVisible(false);
        textNav.setVisible(false);
        textCollapse.setText("My Cards");

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(240);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(240);
        t2.play();

        handCardView1.hide(1, controller);
        handCardView2.hide(2, controller);
        handCardView3.hide(3, controller);
    }

    public void show() {
        controller.blockInput();
        isHidden = false;
        KBNavHint.setVisible(true);
        textNav.setVisible(true);
        textCollapse.setText("Collapse");

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(-240);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(-240);
        t2.play();

        handCardView1.show(1, controller);
        handCardView2.show(2, controller);
        handCardView3.show(3, controller);
    }

    public void refresh(Runnable runnable) {
        controller.blockInput();
        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), handCardView1.getImageView());
        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), handCardView2.getImageView());
        TranslateTransition t3 = new TranslateTransition(Duration.millis(350), handCardView3.getImageView());
        int distance;
        if (isHidden) {
            distance = -100;
        } else {
            distance = -300;
        }
        t1.setByX(distance);
        t2.setByX(distance);
        t3.setByX(distance);

        t1.setOnFinished((e) -> {
            runnable.run();
            showAfterRefresh();
        });
        t1.play();
        t2.play();
        t3.play();
    }

    private void showAfterRefresh() {
        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), handCardView1.getImageView());
        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), handCardView2.getImageView());
        TranslateTransition t3 = new TranslateTransition(Duration.millis(350), handCardView3.getImageView());
        int distance;
        if (isHidden) {
            distance = 100;
        } else {
            distance = 300;
        }
        t1.setByX(distance);
        t2.setByX(distance);
        t3.setByX(distance);
        t1.setOnFinished((e) -> controller.unlockInput());
        t1.play();
        t2.play();
        t3.play();
    }
}
