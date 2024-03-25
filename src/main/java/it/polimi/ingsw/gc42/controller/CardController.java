package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.HandListener;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.PlayAreaListener;
import it.polimi.ingsw.gc42.model.interfaces.SecretObjectiveListener;
import it.polimi.ingsw.gc42.view.CardView;
import it.polimi.ingsw.gc42.view.HandCardView;
import it.polimi.ingsw.gc42.view.ObjectiveCardView;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CardController {
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
    private Text objectiveDescription;
    @FXML
    private StackPane objDescriptionBox;

    @FXML
    private ImageView overlay1;
    @FXML
    private ImageView overlay2;
    @FXML
    private ImageView overlay3;

    @FXML
    private StackPane playArea;

    // Attributes
    private GameController gameController;
    private int selectedCard = 0;
    private boolean canReadInput = true;
    private ObjectiveCardView objectiveCardView;
    private int lastSelected = 0;

    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;
    private boolean isHandVisible = true;


    public void initializeCards() {
        handCardView1 = new HandCardView(view1, text1, KBHint1, overlay1);
        handCardView2 = new HandCardView(view2, text2, KBHint2, overlay2);
        handCardView3 = new HandCardView(view3, text3, KBHint3, overlay3);

        /*objectiveCardView = new ObjectiveCardView(new CardView(secretObjective.getFrontImage(),
                secretObjective.getBackImage()), objectiveView, objectiveHint, secretObjective,
                KBObjectiveHint, false, objectiveDescription);*/
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setPlayer(Player player) {
        player.getPlayField().setListener(new PlayAreaListener() {
            @Override
            public void onEvent() {
                Card card = player.getPlayField().getLastPlayedCard();
                addToPlayArea(card, card.getX(), card.getY());
            }
        });
        player.setListener(new HandListener() {
            @Override
            public void onEvent() {
                Card card;
                if (handCardView1.getModelCard() == null) {
                    card = player.getHandCard(1);
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
                    card = player.getHandCard(2);
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
                    card = player.getHandCard(3);
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
        player.setListener(new SecretObjectiveListener() {
            @Override
            public void onEvent() {
                objectiveCardView = new ObjectiveCardView(new CardView(player.getSecretObjective().getFrontImage(),
                        player.getSecretObjective().getBackImage()), objectiveView, objectiveHint, player.getSecretObjective(),
                        KBObjectiveHint, false, objectiveDescription, objDescriptionBox);
            }
        });
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
        if (canReadInput()) {
            gameController.flipCard(handCardView1.getModelCard());
        }
    }

    @FXML
    public void onCard2Clicked() {
        if (canReadInput()) {
            gameController.flipCard(handCardView2.getModelCard());
        }
    }

    @FXML
    public void onCard3Clicked() {
        if (canReadInput()) {
            gameController.flipCard(handCardView3.getModelCard());
        }
    }

    private void flipCard(HandCardView handCardView) {
        handCardView.visualFlip(this);
    }

    public void moveDown() {
        lastSelected = selectedCard;
        if (selectedCard == 3) {
            selectedCard = 1;
        } else {
            selectedCard++;
        }
        selectCard(selectedCard);
    }

    public void moveUp() {
        lastSelected = selectedCard;
        if (selectedCard == 1) {
            selectedCard = 3;
        } else {
            selectedCard--;
        }
        selectCard(selectedCard);
    }

    protected void selectCard(int selectedCard) {
        if (isHandVisible) {
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
        if (isHandVisible) {
            hideHand();
            isHandVisible = false;
        } else {
            showHand();
            isHandVisible = true;
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
        if (canReadInput()) {
            blockInput();
            objectiveCardView.rotate(this);
        }
    }

    private void addToPlayArea(Card card, int x, int y) {
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
        // 0 degrees version
        /*
        imageView.setTranslateX(x * 125);
        imageView.setTranslateY(y * -60);
        */
        if (x > 3 || y > 3) {
            playArea.setScaleX(0.7);
            playArea.setScaleY(0.7);
        }
        return imageView;
    }
}


