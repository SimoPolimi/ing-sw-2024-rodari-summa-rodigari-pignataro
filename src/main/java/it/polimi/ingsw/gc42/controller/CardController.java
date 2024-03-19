package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.CardView;
import it.polimi.ingsw.gc42.view.HandCardView;
import it.polimi.ingsw.gc42.view.ObjectiveCardView;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CardController {

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
    private ImageView overlay1;
    @FXML
    private ImageView overlay2;
    @FXML
    private ImageView overlay3;

    private int selectedCard = 0;
    private boolean canReadInput = true;
    private Card card1;
    private Card card2;
    private Card card3;

    private Card objectiveCard;
    private CardView cardViewObj;
    private ObjectiveCardView objectiveCardView;
    private int lastSelected = 0;

    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;
    private boolean isHandVisible = true;


    public void initializeCards(Game game) {
        setCards(game.getResourcePlayingDeck().getDeck().draw(), game.getResourcePlayingDeck().getDeck().draw(), game.getGoldPlayingDeck().getDeck().draw());
        handCardView1 = new HandCardView(view1, text1, KBHint1, card1, overlay1);
        handCardView2 = new HandCardView(view2, text2, KBHint2, card2, overlay2);
        handCardView3 = new HandCardView(view3, text3, KBHint3, card3, overlay3);
        handCardView1.getModelCard().setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(handCardView1);
            }
        });
        handCardView2.getModelCard().setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(handCardView2);
            }
        });
        handCardView3.getModelCard().setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(handCardView3);
            }
        });

        objectiveCard = game.getObjectivePlayingDeck().getDeck().draw();
        cardViewObj = new CardView(objectiveCard.getFrontImage(), objectiveCard.getBackImage());
        objectiveCardView = new ObjectiveCardView(cardViewObj, objectiveView, objectiveHint, (ObjectiveCard) objectiveCard, KBObjectiveHint, false, objectiveDescription);
    }

    public void setCards(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
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
            handCardView1.flip();
        }
    }

    @FXML
    public void onCard2Clicked() {
        if (canReadInput()) {
            handCardView2.flip();
        }
    }

    @FXML
    public void onCard3Clicked() {
        if (canReadInput()) {
            handCardView3.flip();
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
                handCardView1.flip();
                break;
            case 2:
                handCardView2.flip();
                break;
            case 3:
                handCardView3.flip();
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
}


