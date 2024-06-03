package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.TranslateTransition;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class ChatView {
    // Attributes
    private final StackPane container;
    private final StackPane contentContainer;
    private final TextField chatTextField;
    private final GUIController controller;

    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    private boolean isShowing = false;
    private boolean unreadMessagesAvailable = false;

    private static final int ANIMATION_DURATION = 350;
    public static final int ANIMATION_LENGTH = 490;

    // Constructor Method
    public ChatView(StackPane container, StackPane contentContainer, TextField chatTextField, GUIController controller) {
        this.container = container;
        this.contentContainer = contentContainer;
        this.chatTextField = chatTextField;
        this.controller = controller;
        build();
    }

    // Getters and Setters
    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public Pane getPane() {
        return container;
    }

    // Methods
    private void build() {
        contentContainer.getChildren().clear();
        if (messages.isEmpty()) {
            Text text = new Text("No chat messages available");
            text.setFill(Paint.valueOf("white"));
            text.setFont(Font.font("Contantia Italic", 15));
            contentContainer.getChildren().add(text);
        } else {
            // TODO:Implement
        }
    }

    public void toggle() {
        if (isShowing) {
            hide();
        } else {
            show();
        }
    }

    public void hide() {
        controller.blockInput();
        isShowing = false;
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
        container.requestFocus();
    }

    public void show() {
        controller.blockInput();
        isShowing = true;
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(-ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        build();
    }

    public void notifyNewMessage() {
        //TODO: Implement
    }
}
