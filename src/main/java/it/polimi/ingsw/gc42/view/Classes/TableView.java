package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class TableView {
    // Attributes
    private Player player;
    private boolean isPrivacyModeEnabled;
    private AnchorPane playerTableContainer;
    private HandView hand;
    private ObjectiveCardView secretObjective;
    private GUIController controller;
    private ImageView playerToken;
    private ImageView blackToken;
    private StackPane playArea;
    private StackPane phantomPlayArea;

    private boolean isShowingPlacements = false;
    private double playAreaScale = 1;
    private int cardBeingPlayed = 0;
    private boolean canPlayCards = true;

    // Constructor Method

    public TableView(boolean isPrivacyModeEnabled, GUIController controller) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.controller = controller;
        build();
        hand.hide();
        secretObjective.hide();
    }

    // Getters and Setters


    public boolean isCanPlayCards() {
        return canPlayCards;
    }

    public void setCanPlayCards(boolean canPlayCards) {
        this.canPlayCards = canPlayCards;
    }

    public HandView getHand() {
        return hand;
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (player.isFirst()) {
            blackToken.setVisible(true);
        }
        player.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                setPlayerToken(player.getToken());
                controller.refreshScoreBoard();
            }
        });
        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                PlayableCard card = player.getPlayField().getLastPlayedCard();
                addToPlayArea(card, card.getX(), card.getY());
            }
        });
        Listener listener1 = new Listener() {
            @Override
            public void onEvent() {
                flipCard(hand.getHandCardView(1));
            }
        };
        Listener listener2 = new Listener() {
            @Override
            public void onEvent() {
                flipCard(hand.getHandCardView(2));
            }
        };
        Listener listener3 = new Listener() {
            @Override
            public void onEvent() {
                flipCard(hand.getHandCardView(3));
            }
        };
        player.setListener(new HandListener() {
            @Override
            public void onEvent() {
                Card card;
                card = player.getHandCard(0);
                if (null != hand.getHandCardView(1).getModelCard()) {
                    hand.getHandCardView(1).getModelCard().removeListener(listener1);
                }
                hand.setPlayingOverlay(1, false);
                hand.getHandCardView(1).setModelCard(card);
                if (null != card) {
                    hand.getHandCardView(1).getModelCard().setListener(listener1);
                }

                card = player.getHandCard(1);
                if (null != hand.getHandCardView(2).getModelCard()) {
                    hand.getHandCardView(2).getModelCard().removeListener(listener2);
                }
                hand.setPlayingOverlay(2, false);
                hand.getHandCardView(2).setModelCard(card);
                if (null != card) {
                    hand.getHandCardView(2).getModelCard().setListener(listener2);
                }

                card = player.getHandCard(2);
                if (null != hand.getHandCardView(3).getModelCard()) {
                    hand.getHandCardView(3).getModelCard().removeListener(listener3);
                }
                hand.setPlayingOverlay(3, false);
                hand.getHandCardView(3).setModelCard(card);
                if (null != card) {
                    hand.getHandCardView(3).getModelCard().setListener(listener3);
                }
                if (player.getHandSize() == 3) {
                    controller.setPlayerCanDrawOrGrab(false);
                }
            }
        });
        player.setListener(new SecretObjectiveListener() {
            @Override
            public void onEvent() {
                setSecretObjective(player.getSecretObjective());
            }
        });
        hand.setPlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPrivacyModeEnabled() {
        return isPrivacyModeEnabled;
    }

    public void setPrivacyModeEnabled(boolean privacyModeEnabled) {
        isPrivacyModeEnabled = privacyModeEnabled;
    }

    public void setSecretObjective(ObjectiveCard card) {
       secretObjective.setModelCard(card);
    }

    // Methods:
    private void build() {
        playerTableContainer = new AnchorPane();
        AnchorPane.setRightAnchor(playerTableContainer, 0.0);
        AnchorPane.setLeftAnchor(playerTableContainer, 0.0);
        AnchorPane.setTopAnchor(playerTableContainer, 0.0);
        AnchorPane.setBottomAnchor(playerTableContainer, 0.0);
        playerTableContainer.setPadding(new Insets(0, 40, 0, 0));

        hand = new HandView(isPrivacyModeEnabled, controller);

        secretObjective = new ObjectiveCardView(isPrivacyModeEnabled, controller);

        playerToken = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/blueToken.png"))));
        playerToken.setFitWidth(50);
        playerToken.setPreserveRatio(true);
        playerToken.setVisible(false);
        AnchorPane.setLeftAnchor(playerToken, 400.0);
        AnchorPane.setTopAnchor(playerToken, 100.0);
        DropShadow effect = new DropShadow();
        effect.setBlurType(BlurType.GAUSSIAN);
        effect.setWidth(50);
        effect.setHeight(50);
        effect.setRadius(24.5);
        playerToken.setEffect(effect);
        if (isPrivacyModeEnabled) {
            playerToken.setTranslateX(320);
            playerToken.setTranslateY(-100);
        }

        blackToken = new ImageView(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/blackToken.png"))));
        blackToken.setFitWidth(50);
        blackToken.setPreserveRatio(true);
        blackToken.setVisible(false);
        AnchorPane.setLeftAnchor(blackToken, 360.0);
        AnchorPane.setTopAnchor(blackToken, 60.0);
        DropShadow effect2 = new DropShadow();
        effect.setBlurType(BlurType.GAUSSIAN);
        effect.setWidth(50);
        effect.setHeight(50);
        effect.setRadius(24.5);
        blackToken.setEffect(effect2);
        if (isPrivacyModeEnabled) {
            blackToken.setTranslateX(320);
            blackToken.setTranslateY(-100);
        }

        playArea = new StackPane();
        playArea.setAlignment(Pos.CENTER);
        playArea.setDisable(true);
        AnchorPane.setBottomAnchor(playArea, 0.0);
        AnchorPane.setLeftAnchor(playArea, 250.0);
        AnchorPane.setTopAnchor(playArea, 150.0);
        AnchorPane.setRightAnchor(playArea, 270.0);

        phantomPlayArea = new StackPane();
        phantomPlayArea.setAlignment(Pos.CENTER);
        AnchorPane.setBottomAnchor(phantomPlayArea, 0.0);
        AnchorPane.setLeftAnchor(phantomPlayArea, 250.0);
        AnchorPane.setTopAnchor(phantomPlayArea, 150.0);
        AnchorPane.setRightAnchor(phantomPlayArea, 270.0);
        phantomPlayArea.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent zoomEvent) {
                if (zoomEvent.getZoomFactor() > 0) {
                    scalePlayArea(playAreaScale+0.1, true);
                } else if (zoomEvent.getZoomFactor() < 0) {
                    scalePlayArea(playAreaScale-0.1, true);
                }
                zoomEvent.consume();
            }
        });
        phantomPlayArea.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() > 0) {
                    scalePlayArea(playAreaScale+0.1, true);
                } else if (scrollEvent.getDeltaY() < 0) {
                    scalePlayArea(playAreaScale-0.1, true);
                }
                scrollEvent.consume();
            }
        });

        playerTableContainer.getChildren().addAll(hand.getPane(), secretObjective.getPane(),
                playerToken, blackToken, playArea, phantomPlayArea);
    }

    public Pane getPane() {
        return playerTableContainer;
    }

    public void toggleHand() {
        if (!hand.isHidden()) {
            hand.hide();
        } else {
            hand.show();
        }
    }

    public void rotateObjective() {
        secretObjective.rotate();
    }

    private void addToPlayArea(PlayableCard card, int x, int y) {
        playArea.getChildren().add(initImageView(card.getShowingImage(), x, y));
    }

    private void showAvailablePlacements() {
        if (!isShowingPlacements) {
            ArrayList<Coordinates> placements = player.getPlayField().getAvailablePlacements();
            for (Coordinates placement : placements) {
                ImageView spotView = initImageView(new Image(
                                Objects.requireNonNull(getClass().getResourceAsStream("/availableSpot.png"))),
                        placement.getX(), placement.getY());
                spotView.setOnMouseClicked((e) -> {
                    placeCard(placement);
                    hideAvailablePlacements();
                    isShowingPlacements = false;
                });
                phantomPlayArea.getChildren().add(spotView);
                isShowingPlacements = true;
            }
        }
    }

    private void hideAvailablePlacements() {
        isShowingPlacements = false;
        phantomPlayArea.getChildren().clear();
    }

    public void playCard() {
        if (canPlayCards) {
            int oldBeingPlayed = cardBeingPlayed;
            if (cardBeingPlayed == hand.getSelectedCard()) {
                cardBeingPlayed = 0;
            } else {
                cardBeingPlayed = hand.getSelectedCard();
            }
            if (cardBeingPlayed > 0) {
                boolean canBePlayed = true;
                if (hand.getHandCardView(cardBeingPlayed).getModelCard() instanceof GoldCard) {
                    canBePlayed = ((GoldCard) hand.getHandCardView(cardBeingPlayed).getModelCard()).canBePlaced(player.getPlayField().getPlayedCards());
                }
                if (canBePlayed) {
                    hand.setPlayingOverlay(cardBeingPlayed, true);
                    showAvailablePlacements();
                } else {
                    hand.getHandCardView(cardBeingPlayed).showError();
                    cardBeingPlayed = oldBeingPlayed;
                }
            } else {
                hand.setPlayingOverlay(cardBeingPlayed, true);
                hideAvailablePlacements();
            }
        }
    }

    public void placeCard(Coordinates coordinates) {
        setCanPlayCards(false);
        ScaleTransition transition = new ScaleTransition(Duration.millis(150), hand.getHandCardView(cardBeingPlayed).getImageView());
        transition.setFromX(1);
        transition.setFromY(1);
        transition.setToX(0);
        transition.setToY(0);
        transition.setOnFinished((e) -> {
            hand.getHandCardView(cardBeingPlayed).getImageView().setScaleX(1);
            hand.getHandCardView(cardBeingPlayed).getImageView().setScaleY(1);
        });
        transition.setOnFinished((e) -> {
            controller.getGameController().playCard(player
                    .getHandCard(cardBeingPlayed - 1), coordinates.getX(), coordinates.getY());
        });
        transition.play();
    }

    private ImageView initImageView(Image image, int x, int y) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX((x * 125) + (y * -125));
        imageView.setTranslateY((x * -60) + (y * -60));
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(shadow);
        imageView.setCursor(Cursor.HAND);
        if (playAreaScale > 0.3 && (Math.abs(x) >= 12 || Math.abs(y) >= 12)) {
            scalePlayArea(0.4, false);
        } else if (playAreaScale > 0.5 && (Math.abs(x) >= 9 || Math.abs(y) >= 9)) {
            scalePlayArea(0.5, false);
        } else if (playAreaScale > 0.7 && (Math.abs(x) >= 6 || Math.abs(y) >= 6)) {
            scalePlayArea(0.7, false);
        }
        return imageView;
    }

    private void setPlayerToken(Token playerToken) {
        switch (playerToken) {
            case BLUE -> {
                this.playerToken.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blueToken.png"))));
                this.playerToken.setVisible(true);
            }
            case RED -> {
                this.playerToken.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/redToken.png"))));
                this.playerToken.setVisible(true);
            }
            case YELLOW -> {
                this.playerToken.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/yellowToken.png"))));
                this.playerToken.setVisible(true);
            }
            case GREEN -> {
                this.playerToken.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/greenToken.png"))));
                this.playerToken.setVisible(true);
            }
            default -> {
                this.playerToken.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blueToken.png"))));
                this.playerToken.setVisible(false);
            }
        }
    }

    private void scalePlayArea(double scale, boolean fastAnimation) {
        if (scale > 0.3 && scale < 1) {
            ScaleTransition t1;
            ScaleTransition t2;
            if (!fastAnimation) {
                t1 = new ScaleTransition(Duration.millis(250), playArea);
                t2 = new ScaleTransition(Duration.millis(250), phantomPlayArea);
            } else {
                t1 = new ScaleTransition(Duration.millis(100), playArea);
                t2 = new ScaleTransition(Duration.millis(100), phantomPlayArea);
            }
            t1.setFromX(playAreaScale);
            t2.setFromX(playAreaScale);
            t1.setToX(scale);
            t2.setFromX(scale);
            t1.setFromY(playAreaScale);
            t2.setFromY(playAreaScale);
            t1.setToY(scale);
            t2.setToY(scale);
            t1.play();
            t2.play();
            playAreaScale = scale;
        }
    }

    private void flipCard(HandCardView handCardView) {
        if (!handCardView.isBeingPlayed()) {
            handCardView.visualFlip(controller);
        }
    }
}
