package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.CardView;
import it.polimi.ingsw.gc42.view.HandCardView;
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
    private Text textCollapse ;

    private int selectedCard = 0;
    public boolean canReadKeyboard = true;
    private Card card1;
    private Card card2;
    private Card card3;

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;

    private int lastSelected = 0;

    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;
    private boolean isHandVisible = true;


    public void initializeCards() {
        card1 = new Card(null, null, true, 1, 0, 0);
        card2 = new Card(null, null, true, 2, 0, 0);
        card3 = new Card(null, null, true, 3, 0, 0);
        cardView1 = new CardView("/card1Front.png", "/card1Back.png");
        cardView2 = new CardView("/card2Front.png", "/card2Back.png");
        cardView3 = new CardView("/card3Front.png", "/card3Back.png");
        handCardView1 = new HandCardView(cardView1, view1, text1, KBHint1, card1);
        handCardView2 = new HandCardView(cardView2, view2, text2, KBHint2, card2);
        handCardView3 = new HandCardView(cardView3, view3, text3, KBHint3, card3);
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
    }

    public void setCards(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
    }

    public void setCardViews(Card card1, Card card2, Card card3) {
        this.cardView1 = new CardView("/card1Front.png", "/card1Back.png");
        this.cardView2 = new CardView("/card2Front.png", "/card2Back.png");
        this.cardView3 = new CardView("/card3Front.png", "/card3Back.png");
    }

    @FXML
    public void onCard1Clicked() {
        handCardView1.flip();
    }

    @FXML
    public void onCard2Clicked() {
        handCardView2.flip();
    }

    @FXML
    public void onCard3Clicked() {
        handCardView3.flip();
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
                    handCardView1.select();
                    if (2 == lastSelected) {
                        handCardView2.deselect();
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect();
                    }
                    break;
                case 2:
                    handCardView2.select();
                    if (1 == lastSelected) {
                        handCardView1.deselect();
                    }
                    if (3 == lastSelected) {
                        handCardView3.deselect();
                    }
                    break;
                case 3:
                    handCardView3.select();
                    if (1 == lastSelected) {
                        handCardView1.deselect();
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect();
                    }
                    break;
                default:
                    if (1 == lastSelected) {
                        handCardView1.deselect();
                    }
                    if (2 == lastSelected) {
                        handCardView2.deselect();
                    } else if (3 == lastSelected) {
                        handCardView3.deselect();
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
        canReadKeyboard = false;

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
        canReadKeyboard = false;

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
}


