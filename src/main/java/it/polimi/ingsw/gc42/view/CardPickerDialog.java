package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.CardController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Objects;

public class CardPickerDialog extends Dialog implements Observable {
    // Attributes
    private CardController controller;
    private int numberOfCards;
    private boolean cardsCanBeFlipped;
    private final ArrayList<HandCardView> cards = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private Card pickedCard;
    private int lastSelected = -1;
    private int selectedCard = -1;

    // Constructor Methods
    public CardPickerDialog(String title, boolean isDismissable, boolean cardsCanBeFlipped, CardController controller) {
        super(title, isDismissable);
        this.cardsCanBeFlipped = cardsCanBeFlipped;
        this.controller = controller;
        numberOfCards = 0;
    }

    // Getters and Setters

    public Card getPickedCard() {
        return pickedCard;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public boolean isCardsCanBeFlipped() {
        return cardsCanBeFlipped;
    }

    public void setCardsCanBeFlipped(boolean cardsCanBeFlipped) {
        this.cardsCanBeFlipped = cardsCanBeFlipped;
    }

    public void addCard(Card card) {
        HandCardView handCardView = new HandCardView(new ImageView(), new Text(), new ImageView(), new ImageView(), card);
        handCardView.getImageView().setPreserveRatio(true);
        handCardView.getImageView().setFitWidth(140);
        cards.add(handCardView);
    }

    // Methods
    public Node build() {
        container.getChildren().add(initCardContainer());
        container.getChildren().add(initHintContainer());
        return container;
    }

    private Node initCardContainer() {
        HBox cardContainer = new HBox();
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setSpacing(40);
        cardContainer.setPadding(new Insets(20));
        for (HandCardView handCardView: cards) {
            ImageView view = handCardView.getImageView();
            DropShadow effect = new DropShadow();
            effect.setWidth(50);
            effect.setHeight(50);
            effect.setBlurType(BlurType.GAUSSIAN);
            view.setEffect(effect);
            view.setOnMouseEntered((e) -> {
                selectCard(handCardView);
            });
            view.setOnMouseExited((e) -> {
                deselectCard(handCardView);
            });
            view.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if( cardsCanBeFlipped && event.getButton() == MouseButton.SECONDARY){
                        handCardView.flip();
                        handCardView.visualFlip(controller);
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        pickCard(cards.indexOf(handCardView));
                    }
                }
            });
            cardContainer.getChildren().add(view);
        }
        return cardContainer;
    }

    private Node initHintContainer() {
        HBox hintContainer = new HBox();
        hintContainer.setSpacing(20);
        hintContainer.setAlignment(Pos.CENTER);

        VBox navigateHint = new VBox();
        navigateHint.setSpacing(10);
        navigateHint.setAlignment(Pos.TOP_CENTER);
        ImageView kbHint = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/navigateKeyboardHintHorizontal.png"))));
        kbHint.setPreserveRatio(true);
        kbHint.setFitHeight(20);
        Text navigateHintText = initText("Navigate");
        navigateHint.getChildren().addAll(kbHint, navigateHintText);
        hintContainer.getChildren().add(navigateHint);

        if (cardsCanBeFlipped) {
            HBox flipHints = new HBox();
            flipHints.setSpacing(10);
            ImageView mouseFlip = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/RightMouseButton.png"))));
            ImageView kbFlip = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flipKeyboardHint.png"))));
            mouseFlip.setPreserveRatio(true);
            mouseFlip.setFitWidth(20);
            kbFlip.setPreserveRatio(true);
            kbFlip.setFitWidth(20);
            flipHints.getChildren().addAll(mouseFlip, kbFlip);
            VBox flipHintWithText = new VBox();
            flipHintWithText.setSpacing(10);
            flipHintWithText.setAlignment(Pos.TOP_CENTER);
            Text flipText = initText("Flip");
            flipHintWithText.getChildren().addAll(flipHints, flipText);
            hintContainer.getChildren().add(flipHintWithText);
        }

        HBox selectHints = new HBox();
        selectHints.setSpacing(10);
        ImageView mouseSelect = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/LeftMouseButton.png"))));
        ImageView kbSelect = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/EnterHint.png"))));
        mouseSelect.setPreserveRatio(true);
        mouseSelect.setFitWidth(20);
        kbSelect.setPreserveRatio(true);
        kbSelect.setFitWidth(20);
        selectHints.getChildren().addAll(mouseSelect, kbSelect);
        VBox selectHintWithText = new VBox();
        selectHintWithText.setSpacing(10);
        selectHintWithText.setAlignment(Pos.TOP_CENTER);
        Text selectText = initText("Select Card");
        selectHintWithText.getChildren().addAll(selectHints, selectText);
        hintContainer.getChildren().add(selectHintWithText);

        return hintContainer;
    }

    private Text initText(String string) {
        Text text = new Text(string);
        text.setFont(Font.font("Constantia Italic", 15));
        text.setStrokeWidth(25);
        text.setFill(Paint.valueOf("white"));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    public void onKeyPressed(String key) {
        switch (key) {
            case "F":
                cards.get(selectedCard).flip();
                cards.get(selectedCard).visualFlip(controller);
                break;
            case "RIGHT":
                moveRight();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "ENTER":
                pickCard(selectedCard);
                break;
            default:
                break;
        }
    }

    private void pickCard(int number) {
        pickedCard = cards.get(number).getModelCard();
        notifyListeners("Card has been picked");
    }

    private void selectCard(HandCardView handCardView) {
        if (selectedCard >= 0 && selectedCard < cards.size()) {
            deselectCard(cards.get(selectedCard));
        }
        handCardView.select(controller);
        selectedCard = cards.indexOf(handCardView);
    }

    private void deselectCard(HandCardView handCardView) {
        handCardView.deselect(controller);
        lastSelected = cards.indexOf(handCardView);
    }

    private void deselectAllCards() {
        if (selectedCard > 0 && selectedCard < cards.size()) {
            deselectCard(cards.get(selectedCard));
        }
        selectedCard = -1;
    }

    private void selectCardNumber(int number) {
        if (number >= 0 && number < cards.size()) {
            selectCard(cards.get(number));
        }
    }

    private void deselectCardNumber(int number) {
        if (number >= 0 && number < cards.size()) {
            deselectCard(cards.get(number));
        } else if (number == -1) {
            deselectAllCards();
        }
    }

    private void moveLeft() {
        deselectCardNumber(lastSelected);
        lastSelected = selectedCard;
        if (selectedCard == -1) {
            selectedCard = 0;
        } else if (selectedCard == 0) {
            selectedCard = cards.size() - 1;
        } else {
            selectedCard--;
        }
        selectCardNumber(selectedCard);
    }

    private void moveRight() {
        deselectCardNumber(lastSelected);
        lastSelected = selectedCard;
        if (selectedCard == -1) {
            selectedCard = 0;
        } else if(selectedCard == cards.size() - 1) {
            selectedCard = 0;
        } else {
            selectedCard++;
        }
        selectCardNumber(selectedCard);
    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}
