package it.polimi.ingsw.gc42.view.Dialog;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.GUIController;
import it.polimi.ingsw.gc42.view.Classes.gui.HandCardView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Dialog that shows some Cards and lets the User pick one of them.
 * This Dialog supports up to 6 Cards. More than that work too, but the UI will not adapt,
 * so they'll very likely end up outside the screen in smaller screens or zoom levels.
 * The Cards inside this Dialog can be Flipped. This can be enabled by setting "true" in cardsCanBeFlipped.
 * Like other Dialogs, this can be Dismissible (closable by clicking outside it) or not.
 * This behavior is defined by the value of isDismissible.
 * This Dialog asks the User to pick a Card. When a Card is picked, the Dialog will block its inputs and notify all
 * Listeners of the event.
 * It's up to the caller to hide the Dialog, as it's not handled by the Dialog itself.
 * However, the Dialog will ignore inputs after the first pick, to avoid unpredictable behaviors.
 */
public class CardPickerDialog extends Dialog implements Observable {
    // Attributes
    private GUIController controller;
    private boolean cardsCanBeFlipped;
    protected final ArrayList<HandCardView> cards = new ArrayList<>();
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private Card pickedCard;
    int pickedCardNumber;
    private int lastSelected = -1;
    private int selectedCard = -1;
    private boolean canClick = true;

    // Constructor Methods

    /**
     * Constructor Method
     * @param title a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     * @param cardsCanBeFlipped a boolean value that specifies if the Cards inside can be flipped or not
     * @param controller the GUIController that created this Dialog
     */
    public CardPickerDialog(String title, boolean isDismissible, boolean cardsCanBeFlipped, GUIController controller) {
        super(title, isDismissible);
        this.cardsCanBeFlipped = cardsCanBeFlipped;
        this.controller = controller;
    }

    // Getters and Setters

    /**
     * Getter Method for pickedCard
     * @return the Card that was picked by the User
     */
    public Card getPickedCard() {
        return pickedCard;
    }

    /**
     * Getter Method for pickedCard's index
     * @return an int value that represents the index of the picked Card inside the List
     */
    public int getPickedCardNumber() {
        return pickedCardNumber;
    }

    /**
     * Getter Method for the Number of Cards
     * @return the number of Cards shown inside this Dialog
     */
    public int getNumberOfCards() {
        return cards.size();
    }

    /**
     * Getter Method for cardsCanBeFlipped
     * @return the boolean value that specifies if the Cards can be Flipped
     */
    public boolean isCardsCanBeFlipped() {
        return cardsCanBeFlipped;
    }

    /**
     * Setter Method for cardsCanBeFlipped
     * @param cardsCanBeFlipped a boolean value that specifies if the Cards can be Flipped
     */
    public void setCardsCanBeFlipped(boolean cardsCanBeFlipped) {
        this.cardsCanBeFlipped = cardsCanBeFlipped;
    }

    /**
     * Adds a Card to the List, ready to be shown in this Dialog.
     * This Method needs to be called BEFORE building the UI.
     * @param card the Card to add to this Dialog
     */
    public void addCard(Card card) {
        HandCardView handCardView = new HandCardView(card,false);
        handCardView.getImageView().setPreserveRatio(true);
        handCardView.getImageView().setFitWidth(140);
        handCardView.getImageView().setCursor(Cursor.HAND);
        cards.add(handCardView);
    }

    // Methods

    /**
     * Builds the UI for this Dialog
     * @return the Node (JavaFx element) containing the whole UI.
     */
    public Node build() {
        container.getChildren().add(initCardContainer());
        container.getChildren().add(initHintContainer());
        if (getNumberOfCards() == 1) {
            selectCardNumber(0);
        }
        return container;
    }

    /**
     * Creates the UI for a single Card
     * @return the Node (JavaFx element) containing the whole UI.
     */
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
            view.setPickOnBounds(false);
            view.setOnMouseEntered((e) -> {
                selectCard(handCardView, true);
            });
            view.setOnMouseExited((e) -> {
                deselectCard(handCardView, true);
            });
            view.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if( cardsCanBeFlipped && event.getButton() == MouseButton.SECONDARY){
                        handCardView.flip();
                        handCardView.visualFlip(controller);
                    } else if (event.getButton() == MouseButton.PRIMARY && canClick) {
                        pickCard(cards.indexOf(handCardView));
                    }
                }
            });
            cardContainer.getChildren().add(view);
        }
        return cardContainer;
    }

    /**
     * Creates the UI for the Hint Container.
     * The Hint configuration varies based on the boolean values previously set.
     * - "Navigate" hints are always shown
     * - "Flip" hints are only shown if cardsCanBeFlipped = true
     * - "Select" hints are always shown
     * @return the Node (JavaFx element) containing the whole UI.
     */
    private Node initHintContainer() {
        HBox hintContainer = new HBox();
        hintContainer.setSpacing(20);
        hintContainer.setAlignment(Pos.CENTER);

        if (getNumberOfCards() > 1) {
            VBox navigateHint = new VBox();
            navigateHint.setSpacing(10);
            navigateHint.setAlignment(Pos.TOP_CENTER);
            ImageView kbHint = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/navigateKeyboardHintHorizontal.png"))));
            kbHint.setPreserveRatio(true);
            kbHint.setFitHeight(20);
            Text navigateHintText = initText("Navigate");
            navigateHint.getChildren().addAll(kbHint, navigateHintText);
            hintContainer.getChildren().add(navigateHint);
        }

        HBox selectHints = new HBox();
        selectHints.setSpacing(10);
        ImageView mouseSelect = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/LeftMouseButton.png"))));
        ImageView kbSelect = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/EnterHint.png"))));
        mouseSelect.setPreserveRatio(true);
        mouseSelect.setFitWidth(20);
        kbSelect.setPreserveRatio(true);
        kbSelect.setFitWidth(20);
        selectHints.setAlignment(Pos.CENTER);
        selectHints.getChildren().addAll(mouseSelect, kbSelect);
        VBox selectHintWithText = new VBox();
        selectHintWithText.setSpacing(10);
        selectHintWithText.setAlignment(Pos.TOP_CENTER);
        Text selectText = initText("Select Card");
        selectHintWithText.getChildren().addAll(selectHints, selectText);
        hintContainer.getChildren().add(selectHintWithText);

        if (cardsCanBeFlipped) {
            HBox flipHints = new HBox();
            flipHints.setSpacing(10);
            ImageView mouseFlip = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/RightMouseButton.png"))));
            ImageView kbFlip = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flipKeyboardHint.png"))));
            mouseFlip.setPreserveRatio(true);
            mouseFlip.setFitWidth(20);
            kbFlip.setPreserveRatio(true);
            kbFlip.setFitWidth(20);
            flipHints.setAlignment(Pos.CENTER);
            flipHints.getChildren().addAll(mouseFlip, kbFlip);
            VBox flipHintWithText = new VBox();
            flipHintWithText.setSpacing(10);
            flipHintWithText.setAlignment(Pos.TOP_CENTER);
            Text flipText = initText("Flip");
            flipHintWithText.getChildren().addAll(flipHints, flipText);
            hintContainer.getChildren().add(flipHintWithText);
        }

        return hintContainer;
    }

    /**
     * Creates the Text (JavaFx element) element with Font and Color applied.
     * @param string the content to show in this Text.
     * @return the Text (JavaFx element)
     */
    private Text initText(String string) {
        Text text = new Text(string);
        text.setFont(Font.font("Constantia Italic", 15));
        text.setStrokeWidth(25);
        text.setFill(Paint.valueOf("white"));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    /**
     * Handles all the different Keyboard input events.
     * Supported events are:
     * - F
     * - RIGHT
     * - LEFT
     * - ENTER
     * Unsupported events or Strings will be ignored.
     * @param key a String containing the Key's name.
     */
    public void onKeyPressed(String key) {
        switch (key) {
            case "F":
                if (cardsCanBeFlipped) {
                    cards.get(selectedCard).flip();
                    cards.get(selectedCard).visualFlip(controller);
                }
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

    /**
     * Picks a Card and blocks all future inputs in this Dialog.
     * Notifies all Listeners that a Card has been picked.
     * @param number the index of the picked Card.
     */
    private void pickCard(int number) {
        if (number != -1) {
            pickedCard = cards.get(number).getModelCard();
            pickedCardNumber = number;
            notifyListeners("Card has been picked");
            canClick = false;
        }
    }

    /**
     * Visually selects a Card with a yellow glowing effect.
     * Deselect the previously selected Card automatically.
     * @param handCardView the HandCardView of the Card that will be selected
     * @param unlockInputAfter a boolean value that defines if the Card needs to be automatically deselected when
     *                         another Card is selected.
     */
    private void selectCard(HandCardView handCardView, boolean unlockInputAfter) {
        if (selectedCard >= 0 && selectedCard < cards.size()) {
            deselectCard(cards.get(selectedCard), unlockInputAfter);
        }
        handCardView.select(controller);
        selectedCard = cards.indexOf(handCardView);
    }

    /**
     * Visually deselects a Card by removing the yellow glowing effect, if present.
     * @param handCardView the HandCardView of the Card that will be deselected
     * @param unlockInputAfter a boolean value that defines if the Card needs to be automatically selected when
     *                         another Card is deselected.
     */
    private void deselectCard(HandCardView handCardView, boolean unlockInputAfter) {
        handCardView.deselect(controller, unlockInputAfter);
        lastSelected = cards.indexOf(handCardView);
    }

    /**
     * Visually deselects ALL Cards, removing the yellow glowing effect from them.
     * @param unlockInputAfter a boolean value that defines if the Card needs to be automatically selected when
     *                         another Card is deselected.
     */
    private void deselectAllCards(boolean unlockInputAfter) {
        if (selectedCard > 0 && selectedCard < cards.size()) {
            deselectCard(cards.get(selectedCard), unlockInputAfter);
        }
        selectedCard = -1;
    }

    /**
     * Visually selects a Card by applying a yellow glowing effect to its ImageView.
     * @param number the index of the Card that will be selected.
     */
    private void selectCardNumber(int number) {
        if (number >= 0 && number < cards.size()) {
            selectCard(cards.get(number), true);
        }
    }

    /**
     * Visually deselects a Card by removing the yellow glowing effect, if present.
     * @param number the index of the Card that will be deselected.
     */
    private void deselectCardNumber(int number) {
        if (number >= 0 && number < cards.size()) {
            deselectCard(cards.get(number), true);
        } else if (number == -1) {
            deselectAllCards(true);
        }
    }

    /**
     * Visually moves the focus from the currently selected Card to the previous, the one on the right.
     * If no Card is currently selected, the focus will go to the last one.
     * If the first Card is currently selected, the focus will jump to the last one.
     */
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


    /**
     * Visually moves the focus from the currently selected Card to the next, the one on the left.
     * If no Card is currently selected, the focus will go to the first one.
     * If the last Card is currently selected, the focus will jump to the first one.
     */
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

    /**
     * Adds a Listener to the List of Listeners that will be notified when the User has picked a Card.
     * @param listener the Listener to add in the List
     */
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes an existing Listener to the List.
     * If the Listener is not in the List, no action is performed.
     * @param listener the Listener to remove from the List
     */
    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the Listener inside the List that the User has picked a Card.
     * @param context a string specifying which event has been triggered
     */
    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}
