package it.polimi.ingsw.gc42.view.Classes.gui;

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

/**
 * Implementation of Hand for the GUI.
 * The HandView contains 3 HandCardViews, one for each Card inside the Hand.
 * It also contains all the necessary Texts and ImageViews to represent the Hints for the supported actions.
 * It supports the following actions:
 * - Show
 * - Hide
 * - Refresh
 * - Select Card
 * - Deselect Card
 * It also contains the NetworkController, to communicate the User's intentions to the Server.
 * The HandView supports a Privacy Mode: in this Mode it's not interactable, it's always shown in its Hidden form
 * and its HandCardViews are all set to PrivacyMode, aka showing their Back Side texture the whole time.
 */
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

    // Constructor Method

    /**
     * Constructor Method
     * @param isPrivacyModeEnabled: a boolean indicating if Privacy Mode is enabled or not.
     * @param controller: the GUIController that created this HandView.
     * @param server: The NetworkController used to communicate.
     */
    public HandView(boolean isPrivacyModeEnabled, GUIController controller, NetworkController server) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.server = server;
        build();
        this.controller = controller;
        hide();
    }

    // Getters and Setters
    /**
     * Getter Method for selectedCard
     * @return an int indicating which HandCardView is currently selected (0 if none; 1, 2, 3 otherwise)
     */
    public int getSelectedCard() {
        return selectedCard;
    }

    /**
     * Getter Method for container
     * @return the Pane containing the UI of this HandView
     */
    public Pane getPane() {
        return container;
    }

    /**
     * Builds the UI of this HandView, that is then stored inside container.
     */
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

    /**
     * Getter Method for isHidden
     * @return a boolean indicating if the HandView is currently hidden or not.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Setter Method for isFirstTime.
     * This is used to regulate the HandView behavior: the first time the "Show" feature is called, it runs an animation.
     * @param firstTime: a boolean indicating if the next "Show" action will be the first one or not.
     */
    public void setFirstTime(boolean firstTime) {
        this.isFirstTime = firstTime;
    }

    /**
     * Getter Method for the HandCardViews
     * @param number: an int indicating which HandCardView has to be returned (1, 2, 3)
     * @return the selected HandCardView
     */
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

    /**
     * Visually hides the HandView
     */
    public void hide() {
        controller.blockInput();
        deselectAllCards(false);
        if (!isPrivacyModeEnabled) {
            isHidden = true;
            KBNavHint.setVisible(false);
            textNav.setVisible(false);
            textCollapse.setText("My Cards");


            TranslateTransition t1 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), textCollapse);
            t1.setByY(240);
            t1.play();

            TranslateTransition t2 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), KBCollapseHint);
            t2.setByY(240);
            t2.play();
        }

        handCardView1.hide(1, controller);
        handCardView2.hide(2, controller);
        handCardView3.hide(3, controller);
    }

    /**
     * Visually shows the HandView
     */
    public void show() {
        controller.blockInput();
        isHidden = false;
        KBNavHint.setVisible(true);
        textNav.setVisible(true);
        textCollapse.setText("Collapse");

        TranslateTransition t1 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), textCollapse);
        t1.setByY(-240);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), KBCollapseHint);
        t2.setByY(-240);
        t2.play();

        handCardView1.show(1, controller);
        handCardView2.show(2, controller);
        handCardView3.show(3, controller);
    }

    /**
     * Visually refreshes the HandView, updating the HandCardView's textures.
     * During this process the HandView is animated and brought outside the screen, where it can be updated seamlessly.
     * The HandView is then automatically brought back in-screen at the end.
     * @param runnable: some Code to run after the Hand has been refreshed
     */
    public void refresh(Runnable runnable) {
        Platform.runLater(() -> {
            controller.blockInput();
            TranslateTransition t1 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView1.getImageView());
            TranslateTransition t2 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView2.getImageView());
            TranslateTransition t3 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView3.getImageView());
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

    /**
     * Visually animates the last part of the "Refresh" feature, bringing the Hand back on-screen
     */
    public void showAfterRefresh() {
        Platform.runLater(() -> {
            TranslateTransition t1 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView1.getImageView());
            TranslateTransition t2 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView2.getImageView());
            TranslateTransition t3 = new TranslateTransition(Duration.millis(controller.getCurrentAnimationSpeed()), handCardView3.getImageView());
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
                if (null != server && server.getPlayersHandSize(playerID) == 3) {
                    controller.setPlayerCanDrawOrGrab(false);
                }
            });
            t1.play();
            t2.play();
            t3.play();
        });
    }

    /**
     * Visually selects one of the HandCardViews inside this HandView, by showing a yellow glowing effect around it.
     * @param selectedCard: an int indicating which HandCardView has to be selected.
     * @param unlockInputAfter: a boolean indicating if the selection must be PERMANENT or not, aka if the HandCardView
     *                        must be automatically deselected when the Mouse exits from it or not.
     */
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

    /**
     * Handles the Keyboard navigation when the DOWN ARROW key is pressed
     */
    public void moveDown() {
        lastSelected = selectedCard;
        if (selectedCard == 3) {
            selectedCard = 1;
        } else {
            selectedCard++;
        }
        selectCard(selectedCard, true);
    }

    /**
     * Handles the Keyboard navigation when the UP ARROW key is pressed
     */
    public void moveUp() {
        lastSelected = selectedCard;
        if (selectedCard == 1) {
            selectedCard = 3;
        } else {
            selectedCard--;
        }
        selectCard(selectedCard, true);
    }

    /**
     * Handles the flipping of a HandCardView when the F key is pressed or when a RIGHT CLICK is made
     */
    public void onFKeyPressed() {
        flipCard(selectedCard);
    }

    /**
     * Sends the Server a message to perform a FLIP action to one of the Cards in the Hand.
     * @param num: an int indicating which Card to flip (1, 2, 3)
     */
    public void flipCard(int num) {
        switch (num) {
            case 1 -> server.flipCard(playerID, 0);
            case 2 -> server.flipCard(playerID, 1);
            case 3 -> server.flipCard(playerID, 2);
        }
    }

    /**
     * Visually deselects all the HandCardViews currently selected
     * @param unlockInputAfter: a boolean indicating if the action has to be PERMANENT or not.
     */
    public void deselectAllCards(boolean unlockInputAfter) {
        selectCard(0, unlockInputAfter);
    }

    /**
     * Visually highlights one of the HandCardViews with a red glowing effect, to show it's being played
     * @param number: an int indicating which HandCardView must be selected
     * @param value: a boolean value
     */
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
