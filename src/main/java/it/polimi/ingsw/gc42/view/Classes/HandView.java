package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
    private int playerID;
    private NetworkController server;
    private boolean isFirstTime = true;

    private int selectedCard = 0;
    private int lastSelected = 0;

    public HandView(boolean isPrivacyModeEnabled, GUIController controller, NetworkController server) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.server = server;
        build();
        this.controller = controller;
        hide();
    }

    public int getSelectedCard() {
        return selectedCard;
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
        container.setPadding(new Insets(0, 0, 0, 40));
        if (isPrivacyModeEnabled) {
            container.setTranslateX(300);
            container.setTranslateY(100);
            container.setRotate(-60);
        }


        HBox collapseHintContainer = new HBox();
        collapseHintContainer.setAlignment(Pos.CENTER_LEFT);
        collapseHintContainer.setPrefHeight(50);
        collapseHintContainer.setSpacing(15);
        collapseHintContainer.setPadding(new Insets(0, 0, 0, 15));
        if (!isPrivacyModeEnabled) {
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
        }

        handCardView1 = new HandCardView(isPrivacyModeEnabled);
        handCardView2 = new HandCardView(isPrivacyModeEnabled);
        handCardView3 = new HandCardView(isPrivacyModeEnabled);
        if (!isPrivacyModeEnabled) {
            handCardView1.getImageView().setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!handCardView1.isBeingPlayed()) {
                        selectCard(1, true);
                    }
                }
            });
            handCardView1.getImageView().setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!handCardView1.isBeingPlayed()) {
                        deselectAllCards(true);
                    }
                }
            });
            handCardView2.getImageView().setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!handCardView2.isBeingPlayed()) {
                        selectCard(2, true);
                    }
                }
            });
            handCardView2.getImageView().setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectAllCards(true);
                }
            });
            handCardView3.getImageView().setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!handCardView3.isBeingPlayed()) {
                        selectCard(3, true);
                    }
                }
            });
            handCardView3.getImageView().setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deselectAllCards(true);
                }
            });
        }

        Pane card1Container = handCardView1.getPane();
        Pane card2Container = handCardView2.getPane();
        Pane card3Container = handCardView3.getPane();

        HBox bottomHintContainer = new HBox();
        bottomHintContainer.setAlignment(Pos.CENTER_LEFT);
        bottomHintContainer.setPrefHeight(50);
        bottomHintContainer.setSpacing(15);
        bottomHintContainer.setPadding(new Insets(0, 0, 0, 15));

        if (!isPrivacyModeEnabled) {
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
        }

        container.getChildren().addAll(collapseHintContainer, card1Container, card2Container, card3Container, bottomHintContainer);
    }

    public void setPlayer(int playerID) {
        this.playerID = playerID;

        handCardView1.getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (controller.canReadInput() && !controller.isCommonTableDown()) {
                            if (!isHidden()) {
                                controller.onEnterPressed();
                            } else controller.toggleHand();
                        }
                    }
                    case SECONDARY -> {
                        if (controller.canReadInput() && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
                            server.flipCard(playerID, 0);
                        }
                    }
                }
            }
        });
        handCardView2.getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (controller.canReadInput() && !controller.isCommonTableDown()) {
                            if (!isHidden()) {
                                controller.onEnterPressed();
                            } else controller.toggleHand();
                        }
                    }
                    case SECONDARY -> {
                        if (controller.canReadInput() && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
                            server.flipCard(playerID, 1);
                        }
                    }
                }
            }
        });
        handCardView3.getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (controller.canReadInput() && !controller.isCommonTableDown()) {
                            if (!isHidden()) {
                                controller.onEnterPressed();
                            } else controller.toggleHand();
                        }
                    }
                    case SECONDARY -> {
                        if (controller.canReadInput() && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
                            server.flipCard(playerID,2);
                        }
                    }
                }
            }
        });
        if (handCardView1.getModelCard() != null && handCardView2.getModelCard() != null
                && handCardView3.getModelCard() != null && isFirstTime) {
            refresh(null);
            isFirstTime = false;
        }
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setFirstTime(boolean firstTime) {
        this.isFirstTime = firstTime;
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
        deselectAllCards(false);
        if (!isPrivacyModeEnabled) {
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
        }

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
        Platform.runLater(() -> {
            controller.blockInput();
            TranslateTransition t1 = new TranslateTransition(Duration.millis(150), handCardView1.getImageView());
            TranslateTransition t2 = new TranslateTransition(Duration.millis(150), handCardView2.getImageView());
            TranslateTransition t3 = new TranslateTransition(Duration.millis(150), handCardView3.getImageView());
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
        });
    }

    public void showAfterRefresh() {
        Platform.runLater(() -> {
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
            t1.setOnFinished((e) -> {
                controller.unlockInput();
                if (null != server && server.getPlayer(playerID).getHandSize() == 3) {
                    controller.setPlayerCanDrawOrGrab(false);
                }
            });
            t1.play();
            t2.play();
            t3.play();
        });
    }

    private void selectCard(int selectedCard, boolean unlockInputAfter) {
        if (!isHidden && !controller.isShowingDialog() && !controller.isCommonTableDown()) {
            this.selectedCard = selectedCard;
            switch (selectedCard) {
                case 1:
                    handCardView1.select(controller);
                    if (2 == lastSelected) {
                        handCardView2.deselect(controller, unlockInputAfter);
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect(controller, unlockInputAfter);
                    }
                    break;
                case 2:
                    handCardView2.select(controller);
                    if (1 == lastSelected) {
                        handCardView1.deselect(controller, unlockInputAfter);
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect(controller, unlockInputAfter);
                    }
                    break;
                case 3:
                    handCardView3.select(controller);
                    if (1 == lastSelected) {
                        handCardView1.deselect(controller, unlockInputAfter);
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect(controller, unlockInputAfter);
                    }
                    break;
                default:
                    if (1 == lastSelected) {
                        handCardView1.deselect(controller, unlockInputAfter);
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect(controller, unlockInputAfter);
                    } else if (3 == lastSelected) {
                        handCardView3.deselect(controller, unlockInputAfter);
                    }
            }
            lastSelected = selectedCard;
        }
    }

    public void moveDown() {
        lastSelected = selectedCard;
        if (selectedCard == 3) {
            selectedCard = 1;
        } else {
            selectedCard++;
        }
        selectCard(selectedCard, true);
    }

    public void moveUp() {
        lastSelected = selectedCard;
        if (selectedCard == 1) {
            selectedCard = 3;
        } else {
            selectedCard--;
        }
        selectCard(selectedCard, true);
    }

    public void onFKeyPressed() {
        flipCard(selectedCard);
    }

    public void flipCard(int num) {
        switch (num) {
            case 1 -> server.flipCard(playerID, 0);
            case 2 -> server.flipCard(playerID, 1);
            case 3 -> server.flipCard(playerID, 2);
        }
    }

    public void deselectAllCards(boolean unlockInputAfter) {
        selectCard(0, unlockInputAfter);
    }

    public void setPlayingOverlay(int number, boolean value) {
        switch (number) {
            case 0 -> {
                if (handCardView1.isBeingPlayed()) {
                    handCardView1.removePlayingSelection(controller);
                }
                if (handCardView2.isBeingPlayed()) {
                    handCardView2.removePlayingSelection(controller);
                }
                if (handCardView3.isBeingPlayed()) {
                    handCardView3.removePlayingSelection(controller);
                }
            }
            case 1 -> {
                if (value && !handCardView1.isBeingPlayed()) {
                    handCardView1.setPlayingSelection(controller);
                }
                if (handCardView2.isBeingPlayed()) {
                    handCardView2.removePlayingSelection(controller);
                }
                if (handCardView3.isBeingPlayed()) {
                    handCardView3.removePlayingSelection(controller);
                }
            }
            case 2 -> {
                if (handCardView1.isBeingPlayed()) {
                    handCardView1.removePlayingSelection(controller);
                }
                if (value && !handCardView2.isBeingPlayed()) {
                    handCardView2.setPlayingSelection(controller);
                }
                if (handCardView3.isBeingPlayed()) {
                    handCardView3.removePlayingSelection(controller);
                }
            }
            case 3 -> {
                if (handCardView1.isBeingPlayed()) {
                    handCardView1.removePlayingSelection(controller);
                }
                if (handCardView2.isBeingPlayed()) {
                    handCardView2.removePlayingSelection(controller);
                }
                if (value && !handCardView3.isBeingPlayed()) {
                    handCardView3.setPlayingSelection(controller);
                }
            }
        }
    }

 }
