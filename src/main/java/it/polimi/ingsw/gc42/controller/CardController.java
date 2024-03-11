package it.polimi.ingsw.gc42.controller;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.CardView;
import it.polimi.ingsw.gc42.view.HandCardView;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

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

    private int selectedCard = 0;
    public boolean canReadKeyboard = true;


    private RotateTransition flipCardHalf1;
    private RotateTransition flipCardHalf2;
    private boolean oddRotation = true;

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
                flipCard(handCardView1, 1);
            }
        });
        handCardView2.getModelCard().setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(handCardView2, 2);
            }
        });
        handCardView3.getModelCard().setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(handCardView3, 3);
            }
        });
    }

    public void setCards(Card card1, Card card2, Card card3) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        /*setCardViews(card1, card2, card3);

        card1.setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(cardView1, 1);
            }
        });

        card2.setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(cardView2, 2);
            }
        });

        card3.setListener(new Listener() {
            @Override
            public void onEvent() {
                flipCard(cardView3, 3);
            }
        });*/
    }

    public void setCardViews(Card card1, Card card2, Card card3) {
        this.cardView1 = new CardView("/card1Front.png", "/card1Back.png");
        this.cardView2 = new CardView("/card2Front.png", "/card2Back.png");
        this.cardView3 = new CardView("/card3Front.png", "/card3Back.png");
    }

    @FXML
    public void onFlipCard1ButtonClicked() {
        handCardView1.flip();
    }

    @FXML
    public void onFlipCard2ButtonClicked() {
        handCardView2.flip();
    }

    @FXML
    public void onFlipCard3ButtonClicked() {
        handCardView3.flip();
    }

    private void flipCard(HandCardView cardView, int num) {
        initRotateTransition(cardView, num);
        flipCardHalf1.play();
    }

    private void doFLip(CardView card, ImageView view) {
        if (!card.isFrontFacing()) {
            view.setImage(card.getBack());
        } else {
            view.setImage(card.getFront());
        }
        flipCardHalf2.play();
    }

    public void initRotateTransition(HandCardView card, int num) {
        ImageView imageView = card.getImageView();
        ScaleTransition jumpHalf1 = new ScaleTransition(Duration.millis(200), imageView);
        jumpHalf1.setFromX(1.2);
        jumpHalf1.setFromY(1.2);
        jumpHalf1.setToX(1.5);
        jumpHalf1.setToY(1.5);
        jumpHalf1.setAutoReverse(true);
        jumpHalf1.setCycleCount(2);
        canReadKeyboard = false;
        jumpHalf1.setOnFinished(e -> canReadKeyboard = true);
        jumpHalf1.play();

        flipCardHalf1 = new RotateTransition(Duration.millis(200), imageView);
        flipCardHalf1.setAxis(Rotate.Y_AXIS);
        flipCardHalf1.setOnFinished(event -> doFLip(card.getCard(), imageView));
        flipCardHalf2 = new RotateTransition(Duration.millis(200), imageView);
        flipCardHalf2.setAxis(Rotate.Y_AXIS);
        if (card.getCard().isOddRotation()) {
            flipCardHalf1.setFromAngle(0);
            flipCardHalf1.setToAngle(90);
            flipCardHalf2.setFromAngle(90);
            flipCardHalf2.setToAngle(180);
        } else {
            flipCardHalf1.setFromAngle(-180);
            flipCardHalf1.setToAngle(-90);
            flipCardHalf2.setFromAngle(-90);
            flipCardHalf2.setToAngle(0);
        }
        card.getCard().setOddRotation(!card.getCard().isOddRotation());
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
    public void deselectCard() {
        selectCard(0);
    }
}


