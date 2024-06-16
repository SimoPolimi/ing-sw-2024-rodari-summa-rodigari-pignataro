package it.polimi.ingsw.gc42.view.Dialog;

import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Dialog that shows the four Tokens and lets the User pick one of them.
 * The Tokens shown in this Dialog are not customizable.
 * All four of them are always shown.
 * Like other Dialogs, this can be Dismissible (closable by clicking outside it) or not.
 * This behavior is defined by the value of isDismissible.
 * This Dialog asks the User to pick a Token.
 * When a Token is picked, the Dialog will block its inputs and notify all
 * Listeners of the event.
 * It's up to the caller to hide the Dialog, as it's not handled by the Dialog itself.
 * However, the Dialog will ignore inputs after the first pick, to avoid unpredictable behaviors.
 * If another Player picks a Token, this Dialog is updated and that Token becomes unavailable.
 * This is shown by applying a greyscale filter to the Token's ImageView.
 * Unavailable Tokens are not selectable, both by Keyboard and Mouse inputs.
 */
public class SharedTokenPickerDialog extends Dialog implements Observable {
    // Attributes
    private final ArrayList<Token> tokens = new ArrayList<>();
    private final ArrayList<ImageView> views = new ArrayList<>();
    private final ArrayList<Token> availableTokens = new ArrayList<>();
    private GUIController controller;
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private Token pickedToken;
    private int lastSelected = -1;
    private int selectedToken = -1;

    // Constructor Method

    /**
     * Constructor Method
     * @param title a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     * @param controller the GUIController that created this Dialog
     */
    public SharedTokenPickerDialog(String title, boolean isDismissible, GUIController controller) {
        super(title, isDismissible);
        this.controller = controller;
        tokens.add(Token.BLUE);
        tokens.add(Token.RED);
        tokens.add(Token.YELLOW);
        tokens.add(Token.GREEN);
        availableTokens.addAll(tokens);
        initViews();
    }

    // Getters and Setters

    /** Getter Method for the Picked Token
     * @return the Token picked by the User.
     */
    public Token getPickedToken() {
        return pickedToken;
    }


    // Methods

    /**
     * Makes the Token gray when another Player has picked it.
     * @param number the index of the Token to mark as unavailable.
     */
    public void grayToken(int number) {
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-1);
        views.get(number).setEffect(effect);
        availableTokens.remove(tokens.get(number));
    }

    /**
     * Makes the Token gray when another Player has picked it.
     * @param token the Token to mark as unavailable.
     */    public void grayToken(Token token) {
        switch (token) {
            case BLUE -> grayToken(0);
            case RED -> grayToken(1);
            case YELLOW -> grayToken(2);
            case GREEN -> grayToken(3);
        }
    }

    /**
     * Removes the B/W filter from the Token and makes it selectable
     * @param number the index of the Token to mark as available.
     */
    public void ungreyToken(int number) {
        DropShadow effect = new DropShadow();
        effect.setWidth(50);
        effect.setHeight(50);
        effect.setBlurType(BlurType.GAUSSIAN);
        views.get(number).setEffect(effect);
        availableTokens.add(tokens.get(number));
    }

    /**
     * Removes the B/W filter from the Token and makes it selectable
     * @param token the Token to mark as available.
     */
    public void ungreyToken(Token token) {
        switch (token) {
            case BLUE -> ungreyToken(0);
            case RED -> ungreyToken(1);
            case YELLOW -> ungreyToken(2);
            case GREEN -> ungreyToken(3);
        }
    }

    /**
     * Adds a Listener to the List of Listeners that will be notified when the User has picked a Token.
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
     * Notifies all the Listener inside the List that the User has picked a Token.
     * @param context a string specifying which event has been triggered
     */
    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }

    /**
     * Builds this Dialog's UI.
     * @return the Node (JavaFx element) that contains the whole UI
     */
    @Override
    public Node build() {
        container.getChildren().add(initTokenContainer());
        container.getChildren().add(initHintContainer());
        return container;
    }

    /**
     * Creates the container for the Token's ImageViews
     * @return the Node (JavaFx element) containing this component's UI
     */
    private Node initTokenContainer() {
        HBox tokenContainer = new HBox();
        tokenContainer.setAlignment(Pos.CENTER);
        tokenContainer.setSpacing(40);
        tokenContainer.setPadding(new Insets(20));
        for (Token token: tokens) {
            tokenContainer.getChildren().add(views.get(tokens.indexOf(token)));
        }
        return tokenContainer;
    }

    /**
     * Creates the UI for the Hint Container.
     * Supported hints are:
     * - "Navigate"
     * - "Select"
     * @return the Node (JavaFx element) containing the whole UI.
     */
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
        Text selectText = initText("Select Token");
        selectHintWithText.getChildren().addAll(selectHints, selectText);
        hintContainer.getChildren().add(selectHintWithText);

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
     * Handles the Keyboard's input events.
     * Supported inputs are:
     * - RIGHT
     * - LEFT
     * - ENTER
     * Unsupported inputs will be ignored.
     * @param key a String containing the Key's name.
     */
    @Override
    public void onKeyPressed(String key) {
        switch (key) {
            case "RIGHT":
                moveRight();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "ENTER":
                pickToken(selectedToken);
                break;
            default:
                break;
        }
    }

    /**
     * Visually moves the focus from the currently selected Token to the previous, the one on the right.
     * If no Token is currently selected, the focus will go to the last one.
     * If the first Token is currently selected, the focus will jump to the last one.
     */
    private void moveLeft() {
        if (selectedToken == -1) {
            selectedToken = 0;
        } else if (selectedToken == 0) {
            selectedToken = tokens.size() - 1;
        } else {
            selectedToken--;
        }
        if (!availableTokens.contains(tokens.get(selectedToken))) {
            moveLeft();
        } else {
            if (lastSelected != -1 && availableTokens.contains(tokens.get(lastSelected))) {
                deselectTokenNumber(lastSelected);
            }
            lastSelected = selectedToken;
            selectTokenNumber(selectedToken);
        }
    }

    /**
     * Visually moves the focus from the currently selected Token to the next, the one on the left.
     * If no Token is currently selected, the focus will go to the first one.
     * If the last Token is currently selected, the focus will jump to the first one.
     */
    private void moveRight() {
        if (selectedToken == -1) {
            selectedToken = 0;
        } else if(selectedToken == tokens.size() - 1) {
            selectedToken = 0;
        } else {
            selectedToken++;
        }
        if (!availableTokens.contains(tokens.get(selectedToken))) {
            moveRight();
        } else {
            if (lastSelected != -1 && availableTokens.contains(tokens.get(lastSelected))) {
                deselectTokenNumber(lastSelected);
            }
            lastSelected = selectedToken;
            selectTokenNumber(selectedToken);
        }    }

    /**
     * Visually deselects ALL Tokens, removing the yellow glowing effect from them.
     */
    private void deselectAllTokens() {
        if (selectedToken > 0 && selectedToken < tokens.size()) {
            deselectToken(tokens.get(selectedToken));
        }
        selectedToken = -1;
    }

    /**
     * Visually selects a Token by applying a yellow glowing effect to its ImageView.
     * @param number the index of the Token that will be selected.
     */
    private void selectTokenNumber(int number) {
        if (number >= 0 && number < tokens.size()) {
            selectToken(tokens.get(number));
        }
    }

    /**
     * Visually deselects a Token by removing the yellow glowing effect, if present.
     * @param number the index of the Token that will be deselected.
     */
    private void deselectTokenNumber(int number) {
        if (number >= 0 && number < tokens.size()) {
            deselectToken(tokens.get(number));
        } else if (number == -1) {
            deselectAllTokens();
        }
    }

    /**
     * Picks a Token and blocks all future inputs in this Dialog.
     * Notifies all Listeners that a Token has been picked.
     * @param number the index of the picked Token.
     */
    private void pickToken(int number) {
        if (number != -1 && availableTokens.contains(tokens.get(number))) {
            pickedToken = tokens.get(number);
            notifyListeners("Token has been picked");
        }
    }

    /**
     * Visually selects a Token with a yellow glowing effect.
     * Deselect the previously selected Token automatically.
     * @param token the Token that will be selected
     */
    private void selectToken(Token token) {
        if (availableTokens.contains(token)) {
            if (selectedToken >= 0 && selectedToken < tokens.size()) {
                deselectToken(tokens.get(selectedToken));
            }
            selectView(views.get(tokens.indexOf(token)));
            selectedToken = tokens.indexOf(token);
        }
    }

    /**
     * Visually deselects a Token by removing the yellow glowing effect, if present.
     * @param token the Token that will be deselected
     */
    private void deselectToken(Token token) {
        deselectView(views.get(tokens.indexOf(token)));
        lastSelected = tokens.indexOf(token);
    }

    /**
     * Animates the selection of the ImageView
     * @param view the ImageView that will be animated
     */
    private void selectView(ImageView view) {
        controller.blockInput();
        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);
        view.setEffect(glowEffect);
        ScaleTransition select1 = new ScaleTransition(Duration.millis(100), view);
        select1.setFromX(1);
        select1.setFromY(1);
        select1.setToX(1.3);
        select1.setToY(1.3);
        select1.setOnFinished(e -> controller.unlockInput());
        select1.play();
    }

    /**
     * Animated the deselection of the ImageView
     * @param view the ImageView to deselect
     */
    private void deselectView(ImageView view) {
        controller.blockInput();
        DropShadow shadow = new DropShadow();
        shadow.setWidth(50);
        shadow.setHeight(50);
        shadow.setBlurType(BlurType.GAUSSIAN);
        view.setEffect(shadow);
        ScaleTransition deselect2 = new ScaleTransition(Duration.millis(100), view);
        deselect2.setFromX(1.3);
        deselect2.setFromY(1.3);
        deselect2.setToX(1);
        deselect2.setToY(1);
        deselect2.setOnFinished(e -> controller.unlockInput());
        deselect2.play();
    }

    /**
     * Created the ImageViews, assigning them a texture
     */
    private void initViews() {
        for (Token token : tokens) {
            ImageView view = null;
            switch (token) {
                case BLUE -> {
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blueToken.png"))));
                }
                case RED -> {
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/redToken.png"))));
                }
                case YELLOW -> {
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/yellowToken.png"))));
                }
                case GREEN -> {
                    view = new ImageView(
                            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/greenToken.png"))));
                }
            }
            if (null != view) {
                view.setPreserveRatio(true);
                view.setFitWidth(70);
                DropShadow effect = new DropShadow();
                effect.setWidth(50);
                effect.setHeight(50);
                effect.setBlurType(BlurType.GAUSSIAN);
                view.setEffect(effect);
                view.setOnMouseEntered((e) -> {
                    if (availableTokens.contains(token)) {
                        selectToken(token);
                    }
                });
                view.setOnMouseExited((e) -> {
                    if (availableTokens.contains(token)) {
                        deselectToken(token);
                    }
                });
                view.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            pickToken(tokens.indexOf(token));
                        }
                    }
                });
                views.add(view);
            }
        }
    }
}
