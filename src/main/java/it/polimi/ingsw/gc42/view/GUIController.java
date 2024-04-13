package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.Classes.CardView;
import it.polimi.ingsw.gc42.view.Classes.HandCardView;
import it.polimi.ingsw.gc42.view.Classes.ObjectiveCardView;
import it.polimi.ingsw.gc42.view.Dialog.CardPickerDialog;
import it.polimi.ingsw.gc42.view.Dialog.Dialog;
import it.polimi.ingsw.gc42.view.Dialog.SharedCardPickerDialog;
import it.polimi.ingsw.gc42.view.Exceptions.AlreadyShowingADialogException;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class GUIController implements ViewController {
    // Imports from the GUI
    @FXML
    private ImageView view1;
    @FXML
    private ImageView view2;
    @FXML
    private ImageView view3;

    @FXML
    private ImageView KBHint1;
    @FXML
    private ImageView KBHint2;
    @FXML
    private ImageView KBHint3;
    @FXML
    private ImageView MouseHint1;
    @FXML
    private ImageView MouseHint2;
    @FXML
    private ImageView MouseHint3;

    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;

    @FXML
    private ImageView KBNavHint;
    @FXML
    private Text textNav;
    @FXML
    private ImageView KBCollapseHint;
    @FXML
    private Text textCollapse;

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
    private StackPane playArea;
    @FXML
    private AnchorPane mainArea;
    @FXML
    private VBox dialog;

    // Attributes
    private Player player;
    private Dialog showingDialog;
    private GameController gameController;
    private int selectedCard = 0;
    private boolean canReadInput = true;
    private ObjectiveCardView objectiveCardView;
    private int lastSelected = 0;

    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;
    private boolean isHandVisible = true;
    private boolean isShowingDialog = false;
    private final ArrayList<Dialog> dialogQueue = new ArrayList<>();


    public void initializeCards() {
        handCardView1 = new HandCardView(view1, text1, KBHint1, MouseHint1);
        handCardView2 = new HandCardView(view2, text2, KBHint2, MouseHint2);
        handCardView3 = new HandCardView(view3, text3, KBHint3, MouseHint3);
        handCardView1.getImageView().setVisible(false);
        handCardView2.getImageView().setVisible(false);
        handCardView3.getImageView().setVisible(false);
        objectiveView.setVisible(false);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                PlayableCard card = player.getPlayField().getLastPlayedCard();
                addToPlayArea(card, card.getX(), card.getY());
            }
        });
        player.setListener(new HandListener() {
            @Override
            public void onEvent() {
                Card card;
                if (handCardView1.getModelCard() == null) {
                    card = player.getHandCard(0);
                    handCardView1.setModelCard(card);
                    if (null != card) {
                        handCardView1.getModelCard().setListener(new Listener() {
                            @Override
                            public void onEvent() {
                                flipCard(handCardView1);
                            }
                        });
                    }
                }
                if (handCardView2.getModelCard() == null) {
                    card = player.getHandCard(1);
                    handCardView2.setModelCard(card);
                    if (null != card) {
                        handCardView2.getModelCard().setListener(new Listener() {
                            @Override
                            public void onEvent() {
                                flipCard(handCardView2);
                            }
                        });
                    }
                }
                if (handCardView3.getModelCard() == null) {
                    card = player.getHandCard(2);
                    handCardView3.setModelCard(card);
                    if (null != card) {
                        handCardView3.getModelCard().setListener(new Listener() {
                            @Override
                            public void onEvent() {
                                flipCard(handCardView3);
                            }
                        });
                    }
                }
            }
        });
        player.setListener(new ReadyToChooseSecretObjectiveListener() {
            @Override
            public void onEvent() {
                CardPickerDialog dialog = new CardPickerDialog("Choose a Secret Objective!", false, false, GUIController.this);
                ArrayList<ObjectiveCard> cards = player.getTemporaryObjectiveCards();
                for (ObjectiveCard card: cards) {
                    dialog.addCard(card);
                }
                dialog.setListener(new CardPickerListener() {
                    @Override
                    public void onEvent() {
                        player.setSecretObjective((ObjectiveCard) dialog.getPickedCard());
                        objectiveCardView = new ObjectiveCardView(new CardView(player.getSecretObjective().getFrontImage(),
                                player.getSecretObjective().getBackImage()), objectiveView, objectiveHint, player.getSecretObjective(),
                                KBObjectiveHint, false, objectiveTitle, objectiveDescription, objDescriptionBox);
                        objectiveCardView.getImageView().setVisible(true);
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
                try {
                    showDialog(dialog);
                } catch (AlreadyShowingADialogException e) {
                    dialogQueue.add(dialog);
                }
            }
        });
    }

    public boolean isShowingDialog() {
        return isShowingDialog;
    }
    public void setShowingDialog(boolean status) {
        isShowingDialog = status;
        if (!status && !dialogQueue.isEmpty()) {
            try {
                showDialog(dialogQueue.removeFirst());
            } catch (AlreadyShowingADialogException e) {
                e.printStackTrace();
            }
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
        if (canReadInput() && !isShowingDialog) {
            gameController.flipCard(handCardView1.getModelCard());
        }
    }

    @FXML
    public void onCard2Clicked() {
        if (canReadInput() && !isShowingDialog) {
            gameController.flipCard(handCardView2.getModelCard());
        }
    }

    @FXML
    public void onCard3Clicked() {
        if (canReadInput() && !isShowingDialog) {
            gameController.flipCard(handCardView3.getModelCard());
        }
    }

    private void flipCard(HandCardView handCardView) {
        handCardView.visualFlip(this);
    }

    public void moveDown() {
        if (!isShowingDialog) {
            lastSelected = selectedCard;
            if (selectedCard == 3) {
                selectedCard = 1;
            } else {
                selectedCard++;
            }
            selectCard(selectedCard);
        }
    }

    public void moveUp() {
        if (!isShowingDialog) {
            lastSelected = selectedCard;
            if (selectedCard == 1) {
                selectedCard = 3;
            } else {
                selectedCard--;
            }
            selectCard(selectedCard);
        }
    }

    protected void selectCard(int selectedCard) {
        if (isHandVisible && !isShowingDialog) {
            this.selectedCard = selectedCard;
            switch (selectedCard) {
                case 1:
                    handCardView1.select(this);
                    if (2 == lastSelected) {
                        handCardView2.deselect(this);
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect(this);
                    }
                    break;
                case 2:
                    handCardView2.select(this);
                    if (1 == lastSelected) {
                        handCardView1.deselect(this);
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect(this);
                    }
                    break;
                case 3:
                    handCardView3.select(this);
                    if (1 == lastSelected) {
                        handCardView1.deselect(this);
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect(this);
                    }
                    break;
                default:
                    if (1 == lastSelected) {
                        handCardView1.deselect(this);
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect(this);
                    } else if (3 == lastSelected) {
                        handCardView3.deselect(this);
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
        selectCard(1);
    }

    @FXML
    public void selectCard2() {
        lastSelected = selectedCard;
        selectCard(2);
    }

    @FXML
    public void selectCard3() {
        lastSelected = selectedCard;
        selectCard(3);
    }

    @FXML
    public void deselectAllCards() {
        selectCard(0);
    }

    public void toggleHand() {
        if (!isShowingDialog) {
            if (isHandVisible) {
                hideHand();
                isHandVisible = false;
            } else {
                showHand();
                isHandVisible = true;
            }
        }
    }

    private void showHand() {
        KBNavHint.setVisible(true);
        textNav.setVisible(true);
        textCollapse.setText("Collapse");
        blockInput();

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(-285);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(-285);
        t2.play();

        handCardView1.show(1, this);
        handCardView2.show(2, this);
        handCardView3.show(3, this);
    }

    private void hideHand() {
        deselectAllCards();
        KBNavHint.setVisible(false);
        textNav.setVisible(false);
        textCollapse.setText("My Cards");
        blockInput();

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(285);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(285);
        t2.play();

        handCardView1.hide(1, this);
        handCardView2.hide(2, this);
        handCardView3.hide(3, this);
    }

    @FXML
    public void flipObjective() {
        if (canReadInput() && !isShowingDialog) {
            blockInput();
            objectiveCardView.rotate(this);
        }
    }

    private void addToPlayArea(PlayableCard card, int x, int y) {
        playArea.getChildren().add(initImageView(card.getShowingImage(), x, y));
    }

    private ImageView initImageView(Image image, int x, int y) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);
        // 45 degrees version
        imageView.setTranslateX((x * 125) + (y * -125));
        imageView.setTranslateY((x * -60) + (y * -60));
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        imageView.setEffect(shadow);
        if (x >= 3 || y >= 3) {
            playArea.setScaleX(0.7);
            playArea.setScaleY(0.7);
        }
        return imageView;
    }

    public void showDialog(Dialog content) throws AlreadyShowingADialogException {
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

            deselectAllCards();
            setShowingDialog(true);
            mainArea.setEffect(new GaussianBlur(10));
            if (content.isDismissable()) {
                mainArea.setOnMouseClicked((e) -> {
                    hideDialog();
                });
            }
            dialog.setVisible(true);
            transition.play();
        } else throw new AlreadyShowingADialogException();
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
        mainArea.setEffect(null);
        mainArea.setOnMouseClicked(null);
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
        ArrayList<Card> starterCards = gameController.getGame().getStarterDeck().getCopy();
        SharedCardPickerDialog dialog = new SharedCardPickerDialog("Choose a Starter Card!", false, true, this);
        for (Card card: starterCards) {
            dialog.addCard(card);
        }
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                player.setStarterCard((StarterCard) dialog.getPickedCard());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
            }
        });
        try {
            showDialog(dialog);
        } catch (AlreadyShowingADialogException e) {
            dialogQueue.add(dialog);
        }
    }
}