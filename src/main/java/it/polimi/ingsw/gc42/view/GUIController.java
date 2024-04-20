package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.view.Classes.HandView;
import it.polimi.ingsw.gc42.view.Dialog.SharedTokenPickerDialog;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.Classes.CardView;
import it.polimi.ingsw.gc42.view.Classes.HandCardView;
import it.polimi.ingsw.gc42.view.Classes.ObjectiveCardView;
import it.polimi.ingsw.gc42.view.Dialog.CardPickerDialog;
import it.polimi.ingsw.gc42.view.Dialog.Dialog;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class GUIController implements ViewController {
    // Imports from the GUI
    @FXML
    private ImageView objectiveView;
    @FXML
    private ImageView KBObjectiveHint;
    @FXML
    private Text objectiveHint;
    @FXML
    private Text objectiveTitle;
    @FXML
    private Text objectiveDescription;
    @FXML
    private StackPane objDescriptionBox;

    @FXML
    private StackPane root;
    @FXML
    private StackPane playArea;
    @FXML
    private StackPane phantomPlayArea;
    @FXML
    private AnchorPane mainArea;
    //private StackPane mainArea;
    @FXML
    private VBox dialog;
    @FXML
    private ImageView playerToken;
    @FXML
    private ImageView blackToken;
    @FXML
    private VBox miniScoreboardContainer;
    @FXML
    private StackPane commonTableContainer;
    @FXML
    private AnchorPane playerTableContainer;
    @FXML
    private AnchorPane backgroundContainer;
    @FXML
    private Text commonTableTxt;

    // Attributes
    private Player player;
    private Dialog showingDialog;
    private GameController gameController;
    private int selectedCard = 0;
    private int cardBeingPlayed = 0;
    private boolean isShowingPlacements = false;
    private boolean canReadInput = true;
    private ObjectiveCardView objectiveCardView;
    private int lastSelected = 0;
    private double playAreaScale = 1;
    private HandView hand;
    private boolean isShowingDialog = false;
    private final ArrayList<Dialog> dialogQueue = new ArrayList<>();
    private boolean isCommonTableDown = false;


    public void build() {
        hand = new HandView(true, this);
        objectiveView.setVisible(false);

        hand.getHandCardView(1).getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (canReadInput && !isCommonTableDown) {
                            if (!hand.isHidden()) {
                                onEnterPressed();
                            } else toggleHand();
                        }
                    }
                    case SECONDARY -> onCard1Clicked();
                }
            }
        });
        hand.getHandCardView(2).getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (canReadInput && !isCommonTableDown) {
                            if (!hand.isHidden()) {
                                onEnterPressed();
                            } else toggleHand();
                        }
                    }
                    case SECONDARY -> onCard2Clicked();
                }
            }
        });
        hand.getHandCardView(3).getImageView().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (event.getButton()) {
                    case PRIMARY -> {
                        if (canReadInput && !isCommonTableDown) {
                            if (!hand.isHidden()) {
                                onEnterPressed();
                            } else toggleHand();
                        }
                    }
                    case SECONDARY -> onCard3Clicked();
                }
            }
        });

        hand = new HandView(true, this);
        hand.hide();

        playerTableContainer.getChildren().addAll(hand.getPane());
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.getGame().setListener(new Listener() {
            @Override
            public void onEvent() {
                refreshScoreBoard();
            }
        });
    }

    public GameController getGameController() {
        return gameController;
    }

    public StackPane getRoot() {
        return root;
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
                refreshScoreBoard();
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
                hand.refresh(new Runnable() {
                    @Override
                    public void run() {
                        Card card;
                        card = player.getHandCard(0);
                        if (null != hand.getHandCardView(1).getModelCard()) {
                            hand.getHandCardView(1).getModelCard().removeListener(listener1);
                        }
                        setPlayingOverlay(1, false);
                        hand.getHandCardView(1).setModelCard(card);
                        if (null != card) {
                            hand.getHandCardView(1).getModelCard().setListener(listener1);
                        }

                        card = player.getHandCard(1);
                        if (null != hand.getHandCardView(2).getModelCard()) {
                            hand.getHandCardView(2).getModelCard().removeListener(listener2);
                        }
                        setPlayingOverlay(2, false);
                        hand.getHandCardView(2).setModelCard(card);
                        if (null != card) {
                            hand.getHandCardView(2).getModelCard().setListener(listener2);
                        }

                        card = player.getHandCard(2);
                        if (null != hand.getHandCardView(3).getModelCard()) {
                            hand.getHandCardView(3).getModelCard().removeListener(listener3);
                        }
                        setPlayingOverlay(3, false);
                        hand.getHandCardView(3).setModelCard(card);
                        if (null != card) {
                            hand.getHandCardView(3).getModelCard().setListener(listener3);
                        }
                    }
                });
            }
        });
        player.setListener(new ReadyToChooseSecretObjectiveListener() {
            @Override
            public void onEvent() {
                CardPickerDialog dialog = new CardPickerDialog("Choose a Secret Objective!", false, false, GUIController.this);
                ArrayList<ObjectiveCard> cards = player.getTemporaryObjectiveCards();
                for (ObjectiveCard card : cards) {
                    dialog.addCard(card);
                }
                dialog.setListener(new CardPickerListener() {
                    @Override
                    public void onEvent() {
                        player.setSecretObjective((ObjectiveCard) dialog.getPickedCard());
                        objectiveCardView = new ObjectiveCardView(new CardView(player.getSecretObjective().getFrontImage(),
                                player.getSecretObjective().getBackImage()), objectiveView, objectiveHint, player.getSecretObjective(),
                                KBObjectiveHint, false, objectiveTitle, objectiveDescription, objDescriptionBox);
                        objectiveCardView.getImageView().setTranslateX(objectiveCardView.getImageView().getTranslateX() + 200);
                        objectiveCardView.getImageView().setVisible(true);
                        objectiveCardView.show();
                        objectiveView.setOnMouseEntered((e) -> {
                            if (!objectiveCardView.isShowingDetails() && !isShowingDialog) {
                                objectiveCardView.select();
                            }
                        });
                        objectiveView.setOnMouseExited((e) -> {
                            if (!objectiveCardView.isShowingDetails() && !isShowingDialog) {
                                objectiveCardView.deselect();
                            }
                        });
                        hideDialog();
                        player.setStatus(GameStatus.READY_TO_CHOOSE_STARTER_CARD);
                    }
                });
                showDialog(dialog);
            }
        });
    }

    public boolean isShowingDialog() {
        return isShowingDialog;
    }

    public void setShowingDialog(boolean status) {
        isShowingDialog = status;
        if (!status && !dialogQueue.isEmpty()) {
            showDialog(dialogQueue.removeFirst());
        }
    }

    public boolean canReadInput() {
        return canReadInput;
    }

    public void blockInput() {
        canReadInput = false;
    }

    public void unlockInput() {
        canReadInput = true;
    }

    @FXML
    public void onCard1Clicked() {
        if (canReadInput() && !isShowingDialog && !isCommonTableDown) {
            gameController.flipCard(hand.getHandCardView(1).getModelCard());
        }
    }

    @FXML
    public void onCard2Clicked() {
        if (canReadInput() && !isShowingDialog && !isCommonTableDown) {
            gameController.flipCard(hand.getHandCardView(2).getModelCard());
        }
    }

    @FXML
    public void onCard3Clicked() {
        if (canReadInput() && !isShowingDialog && !isCommonTableDown) {
            gameController.flipCard(hand.getHandCardView(3).getModelCard());
        }
    }

    private void flipCard(HandCardView handCardView) {
        if (!handCardView.isBeingPlayed()) {
            handCardView.visualFlip(this);
        }
    }

    public void moveDown() {
        if (!isShowingDialog && !isCommonTableDown) {
            lastSelected = selectedCard;
            if (selectedCard == 3) {
                selectedCard = 1;
            } else {
                selectedCard++;
            }
            selectCard(selectedCard, true);
        }
    }

    public void moveUp() {
        if (!isShowingDialog && !isCommonTableDown) {
            lastSelected = selectedCard;
            if (selectedCard == 1) {
                selectedCard = 3;
            } else {
                selectedCard--;
            }
            selectCard(selectedCard, true);
        }
    }

    protected void selectCard(int selectedCard, boolean unlockInputAfter) {
        if (!hand.isHidden() && !isShowingDialog && !isCommonTableDown) {
            this.selectedCard = selectedCard;
            switch (selectedCard) {
                case 1:
                    hand.getHandCardView(1).select(this);
                    if (2 == lastSelected) {
                        hand.getHandCardView(2).deselect(this, unlockInputAfter);
                    }
                    if (3 == lastSelected) {
                        hand.getHandCardView(3).deselect(this, unlockInputAfter);
                    }
                    break;
                case 2:
                    hand.getHandCardView(2).select(this);
                    if (1 == lastSelected) {
                        hand.getHandCardView(1).deselect(this, unlockInputAfter);
                    }
                    if (3 == lastSelected) {
                        hand.getHandCardView(3).deselect(this, unlockInputAfter);
                    }
                    break;
                case 3:
                    hand.getHandCardView(3).select(this);
                    if (1 == lastSelected) {
                        hand.getHandCardView(1).deselect(this, unlockInputAfter);
                    }
                    if (2 == lastSelected) {
                        hand.getHandCardView(2).deselect(this, unlockInputAfter);
                    }
                    break;
                default:
                    if (1 == lastSelected) {
                        hand.getHandCardView(1).deselect(this, unlockInputAfter);
                    }
                    if (2 == lastSelected) {
                        hand.getHandCardView(2).deselect(this, unlockInputAfter);
                    } else if (3 == lastSelected) {
                        hand.getHandCardView(3).deselect(this, unlockInputAfter);
                    }
            }
            lastSelected = selectedCard;
        }
    }

    public void onFKeyPressed() {
        switch (selectedCard) {
            case 1:
                onCard1Clicked();
                break;
            case 2:
                onCard2Clicked();
                break;
            case 3:
                onCard3Clicked();
            default:
                break;
        }
    }


    @FXML
    public void selectCard1() {
        lastSelected = selectedCard;
        selectCard(1, true);
    }

    @FXML
    public void selectCard2() {
        lastSelected = selectedCard;
        selectCard(2, true);
    }

    @FXML
    public void selectCard3() {
        lastSelected = selectedCard;
        selectCard(3, true);
    }

    @FXML
    public void deselectAllCardsFX() {
        selectCard(0, true);
    }

    public void deselectAllCards(boolean unlockInputAfter) {
        selectCard(0, unlockInputAfter);
    }

    @FXML
    public void toggleHand() {
        if (!isShowingDialog && canReadInput && !isCommonTableDown) {
            if (!hand.isHidden()) {
                hand.hide();
            } else {
                hand.show();
            }
        }
    }

    @FXML
    public void flipObjective() {
        if (canReadInput() && !isShowingDialog && !isCommonTableDown) {
            blockInput();
            objectiveCardView.rotate(this);
        }
    }

    private void addToPlayArea(PlayableCard card, int x, int y) {
        playArea.getChildren().add(initImageView(card.getShowingImage(), x, y));
    }

    public void showAvailablePlacements() {
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
        int oldBeingPLayed = cardBeingPlayed;
        if (cardBeingPlayed == selectedCard) {
            cardBeingPlayed = 0;
        } else {
            cardBeingPlayed = selectedCard;
        }
        if (cardBeingPlayed > 0) {
            boolean canBePlayed = true;
            if (hand.getHandCardView(cardBeingPlayed).getModelCard() instanceof GoldCard) {
                canBePlayed = ((GoldCard) hand.getHandCardView(cardBeingPlayed).getModelCard()).canBePlaced(player.getPlayField().getPlayedCards());
            }
            if (canBePlayed) {
                setPlayingOverlay(cardBeingPlayed, true);
                showAvailablePlacements();
            } else {
                hand.getHandCardView(cardBeingPlayed).showError();
                cardBeingPlayed = oldBeingPLayed;
            }
        } else {
            setPlayingOverlay(cardBeingPlayed, true);
            hideAvailablePlacements();
        }
    }

    public void placeCard(Coordinates coordinates) {
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
            gameController.playCard(player.getHandCard(cardBeingPlayed - 1), coordinates.getX(), coordinates.getY());
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
        if (playAreaScale > 0.3 && (Math.abs(x) >= 8 || Math.abs(y) >= 8)) {
            scalePlayArea(0.3);
        } else if (playAreaScale > 0.5 && (Math.abs(x) >= 5 || Math.abs(y) >= 5)) {
            scalePlayArea(0.5);
        } else if (playAreaScale > 0.7 && (Math.abs(x) >= 3 || Math.abs(y) >= 3)) {
            scalePlayArea(0.7);
        }
        return imageView;
    }

    public void showDialog(Dialog content) {
        if (!isShowingDialog) {
            showingDialog = content;
            dialog.getChildren().clear();
            dialog.getChildren().add(content.build());

            blockInput();
            ScaleTransition transition = new ScaleTransition(Duration.millis(150), dialog);
            transition.setFromX(0);
            transition.setToX(1.1);
            transition.setFromY(0);
            transition.setToY(1.1);
            transition.setInterpolator(Interpolator.TANGENT(Duration.millis(150), 1));
            ScaleTransition bounceBack = new ScaleTransition(Duration.millis(80), dialog);
            bounceBack.setFromX(1.1);
            bounceBack.setToX(1);
            bounceBack.setFromY(1.1);
            bounceBack.setToY(1);

            transition.setOnFinished(e -> bounceBack.play());
            bounceBack.setOnFinished(e -> unlockInput());

            deselectAllCards(true);
            setShowingDialog(true);
            backgroundContainer.setEffect(new GaussianBlur(10));
            if (content.isDismissable()) {
                backgroundContainer.setOnMouseClicked((e) -> {
                    hideDialog();
                });
            }
            dialog.setVisible(true);
            transition.play();
        } else {
            dialogQueue.add(content);
        }
    }

    public void hideDialog() {
        blockInput();
        ScaleTransition bounce = new ScaleTransition(Duration.millis(80), dialog);
        bounce.setFromX(1);
        bounce.setToX(1.1);
        bounce.setFromY(1);
        bounce.setToY(1.1);
        ScaleTransition transition = new ScaleTransition(Duration.millis(150), dialog);
        transition.setFromX(1.1);
        transition.setToX(0);
        transition.setFromY(1.1);
        transition.setToY(0);

        bounce.setOnFinished(e -> transition.play());
        transition.setOnFinished(e -> {
            dialog.setVisible(false);
            dialog.getChildren().clear();
            unlockInput();
            setShowingDialog(false);
        });
        backgroundContainer.setEffect(null);
        backgroundContainer.setOnMouseClicked(null);
        bounce.play();
    }

    public void onDialogKeyboardPressed(String key) {
        showingDialog.onKeyPressed(key);
    }

    @Override
    public void showSecretObjectivesSelectionDialog() {
        player.drawSecretObjectives(gameController.getGame().getObjectivePlayingDeck());
    }

    @Override
    public void showStarterCardSelectionDialog() {
        Card card = gameController.getGame().getStarterDeck().draw();
        CardPickerDialog dialog = new CardPickerDialog("This is your Starter Card, choose a Side!", false
                , true, this);
        dialog.addCard(card);
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                player.setStarterCard((StarterCard) dialog.getPickedCard());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
            }
        });
        showDialog(dialog);
    }

    @Override
    public void showTokenSelectionDialog() {
        SharedTokenPickerDialog dialog = new SharedTokenPickerDialog("Pick your Token!", false, this);
        dialog.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                player.setToken(dialog.getPickedToken());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
            }
        });
        showDialog(dialog);
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

    public void setPlayingOverlay(int number, boolean value) {
        switch (number) {
            case 0 -> {
                if (hand.getHandCardView(1).isBeingPlayed()) {
                    hand.getHandCardView(1).removePlayingSelection(this);
                }
                if (hand.getHandCardView(2).isBeingPlayed()) {
                    hand.getHandCardView(2).removePlayingSelection(this);
                }
                if (hand.getHandCardView(3).isBeingPlayed()) {
                    hand.getHandCardView(3).removePlayingSelection(this);
                }
            }
            case 1 -> {
                if (value && !hand.getHandCardView(1).isBeingPlayed()) {
                    hand.getHandCardView(1).setPlayingSelection(this);
                }
                if (hand.getHandCardView(2).isBeingPlayed()) {
                    hand.getHandCardView(2).removePlayingSelection(this);
                }
                if (hand.getHandCardView(3).isBeingPlayed()) {
                    hand.getHandCardView(3).removePlayingSelection(this);
                }
            }
            case 2 -> {
                if (hand.getHandCardView(1).isBeingPlayed()) {
                    hand.getHandCardView(1).removePlayingSelection(this);
                }
                if (value && !hand.getHandCardView(2).isBeingPlayed()) {
                    hand.getHandCardView(2).setPlayingSelection(this);
                }
                if (hand.getHandCardView(3).isBeingPlayed()) {
                    hand.getHandCardView(3).removePlayingSelection(this);
                }
            }
            case 3 -> {
                if (hand.getHandCardView(1).isBeingPlayed()) {
                    hand.getHandCardView(1).removePlayingSelection(this);
                }
                if (hand.getHandCardView(2).isBeingPlayed()) {
                    hand.getHandCardView(2).removePlayingSelection(this);
                }
                if (value && !hand.getHandCardView(3).isBeingPlayed()) {
                    hand.getHandCardView(3).setPlayingSelection(this);
                }
            }
        }
    }

    private void scalePlayArea(double scale) {
        ScaleTransition t1 = new ScaleTransition(Duration.millis(250), playArea);
        ScaleTransition t2 = new ScaleTransition(Duration.millis(250), phantomPlayArea);
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

    public void onEnterPressed() {
        playCard();
    }

    private void initMiniScoreBoard() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Integer> alreadySeen = new ArrayList<>();
        // Players are ordered by their points
        while (alreadySeen.size() != gameController.getGame().getNumberOfPlayers()) {
            int currentMax = 1;
            for (int i = 1; i <= gameController.getGame().getNumberOfPlayers(); i++) {
                if (gameController.getGame().getPlayer(i).getPoints() >= gameController.getGame().getPlayer(currentMax).getPoints() && !alreadySeen.contains(i)) {
                    currentMax = i;
                }
            }
            players.add(gameController.getGame().getPlayer(currentMax));
            alreadySeen.add(currentMax);
        }

        for (Player player : players) {
            if (null != player.getToken()) {
                ImageView tokenView = null;
                switch (player.getToken()) {
                    case BLUE ->
                            tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blueToken.png"))));
                    case RED ->
                            tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/redToken.png"))));
                    case YELLOW ->
                            tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/yellowToken.png"))));
                    case GREEN ->
                            tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/greenToken.png"))));
                }
                tokenView.setPreserveRatio(true);
                tokenView.setFitWidth(15);

                Label name = new Label(player.getNickname());
                name.setFont(Font.font("Tahoma Regular", 15));
                name.setTextOverrun(OverrunStyle.ELLIPSIS);
                name.setTextAlignment(TextAlignment.LEFT);
                name.setMaxWidth(100);
                name.setMinWidth(50);

                if (player.isFirst()) {
                    name.setTextFill(Paint.valueOf("gold"));
                } else {
                    name.setTextFill(Paint.valueOf("white"));
                }
                name.setTextAlignment(TextAlignment.LEFT);

                Text points = new Text(String.valueOf(player.getPoints()));
                points.setFont(Font.font("Tahoma Bold", 12));
                points.setStrokeWidth(25);
                if (player.isFirst()) {
                    points.setFill(Paint.valueOf("gold"));
                } else {
                    points.setFill(Paint.valueOf("white"));
                }

                HBox container = new HBox(tokenView, name, points);
                container.setAlignment(Pos.CENTER_LEFT);
                container.setSpacing(5);

                miniScoreboardContainer.getChildren().add(container);
            }
        }
    }

    private void refreshScoreBoard() {
        miniScoreboardContainer.getChildren().clear();
        initMiniScoreBoard();
    }

    @FXML
    public void toggleCommonTable() {
        if (canReadInput()) {
            if (!isCommonTableDown) {
                bringCommonTableDown();
            } else {
                bringCommonTableUp();
            }
        }
    }

    public void bringCommonTableDown() {
        blockInput();
        isCommonTableDown = true;

        TranslateTransition transition = new TranslateTransition(Duration.millis(400), mainArea);
        transition.setByY((mainArea.getHeight()+400)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("Go back to your Table");
        });
        transition.play();

    }

    public void bringCommonTableUp() {
        blockInput();
        isCommonTableDown = false;

        TranslateTransition transition = new TranslateTransition(Duration.millis(400), mainArea);
        transition.setByY(-(mainArea.getHeight()+400)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("See the Common Table");
        });
        transition.play();

    }
}