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

public class SharedTokenPickerDialog extends Dialog implements Observable {

    private final ArrayList<Token> tokens = new ArrayList<>();
    private final ArrayList<ImageView> views = new ArrayList<>();
    private final ArrayList<Token> availableTokens = new ArrayList<>();
    private GUIController controller;
    private final ArrayList<Listener> listeners = new ArrayList<>();
    private Token pickedToken;
    private int lastSelected = -1;
    private int selectedToken = -1;

    // Constructor Method
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

    public Token getPickedToken() {
        return pickedToken;
    }


    // Methods

    // Makes the Card grey when another Player has picked it.
    public void greyToken(int number) {
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-1);
        views.get(number).setEffect(effect);
        availableTokens.remove(tokens.get(number));
    }

    // Makes the Card grey when another Player has picked it.
    public void greyToken(Token token) {
        switch (token) {
            case BLUE -> greyToken(0);
            case RED -> greyToken(1);
            case YELLOW -> greyToken(2);
            case GREEN -> greyToken(3);
        }
    }

    // Makes the Card colored again.
    public void ungreyToken(int number) {
        DropShadow effect = new DropShadow();
        effect.setWidth(50);
        effect.setHeight(50);
        effect.setBlurType(BlurType.GAUSSIAN);
        views.get(number).setEffect(effect);
        availableTokens.add(tokens.get(number));
    }

    public void ungreyToken(Token token) {
        switch (token) {
            case BLUE -> ungreyToken(0);
            case RED -> ungreyToken(1);
            case YELLOW -> ungreyToken(2);
            case GREEN -> ungreyToken(3);
        }
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

    @Override
    public Node build() {
        container.getChildren().add(initTokenContainer());
        container.getChildren().add(initHintContainer());
        return container;
    }

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

    private Text initText(String string) {
        Text text = new Text(string);
        text.setFont(Font.font("Constantia Italic", 15));
        text.setStrokeWidth(25);
        text.setFill(Paint.valueOf("white"));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

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

    private void deselectAllTokens() {
        if (selectedToken > 0 && selectedToken < tokens.size()) {
            deselectToken(tokens.get(selectedToken));
        }
        selectedToken = -1;
    }

    private void selectTokenNumber(int number) {
        if (number >= 0 && number < tokens.size()) {
            selectToken(tokens.get(number));
        }
    }

    private void deselectTokenNumber(int number) {
        if (number >= 0 && number < tokens.size()) {
            deselectToken(tokens.get(number));
        } else if (number == -1) {
            deselectAllTokens();
        }
    }

    private void pickToken(int number) {
        if (number != -1 && availableTokens.contains(tokens.get(number))) {
            pickedToken = tokens.get(number);
            notifyListeners("Token has been picked");
        }
    }

    private void selectToken(Token token) {
        if (availableTokens.contains(token)) {
            if (selectedToken >= 0 && selectedToken < tokens.size()) {
                deselectToken(tokens.get(selectedToken));
            }
            selectView(views.get(tokens.indexOf(token)));
            selectedToken = tokens.indexOf(token);
        }
    }

    private void deselectToken(Token token) {
        deselectView(views.get(tokens.indexOf(token)));
        lastSelected = tokens.indexOf(token);
    }

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
