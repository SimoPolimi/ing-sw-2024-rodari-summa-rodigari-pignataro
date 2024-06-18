package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *  Implementation of the Chat for the GUI.
 *  This Class contains everything needed to build the Chat, plus everything needed to make it work.
 */
public class ChatView {
    // Attributes
    private boolean hasBackground;
    private final StackPane container;
    private final StackPane contentContainer;
    private final TextField chatTextField;
    private final Button chatButton;
    private final Text chatHint;
    private final ViewController controller;

    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    private boolean isShowing = false;
    private boolean unreadMessagesAvailable = false;

    private static final int ANIMATION_DURATION = 350;
    public static final int ANIMATION_LENGTH = 490;

    // Constructor Method

    /**
     * Constructor Method.
     * @param hasBackground: a boolean value that specifies if the Chat has a background or not, used to adjust some colors.
     * @param container: the StackPane that will contain the Chat, used to animate the show/hide behavior.
     * @param contentContainer: the StackPane that will contain the Messages, used to give updates in real time.
     * @param chatTextField: The TextField where it will read the User's Messages.
     * @param sendButton: The Button that registers the User's "Send" click.
     * @param chatHint: The Text that contains the "Show/Hide" hint, that will updated during the Show/Hide behavior.
     * @param controller: The main GUI Controller that needs to be accessed for input-blocking etc.
     */
    public ChatView(boolean hasBackground, StackPane container, StackPane contentContainer, TextField chatTextField, Button sendButton, Text chatHint, ViewController controller) {
        this.hasBackground = hasBackground;
        if (!hasBackground) {
            isShowing = true;
        }
        this.container = container;
        this.contentContainer = contentContainer;
        this.chatTextField = chatTextField;
        this.chatButton = sendButton;
        this.chatHint = chatHint;
        this.controller = controller;

        // Retrieves the full chat before building it
        try {
            this.messages.addAll(controller.getNetworkController().getFullChat());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        build();

        // Send messages when ENTER is pressed while typing
        chatTextField.setOnKeyPressed((e) -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                sendMessage();
            }
        });
        // Send messages when clicking the SEND Button
        sendButton.setOnMouseClicked((e) -> sendMessage());
    }

    // Getters and Setters

    /**
     * Getter Method for isShowing.
     * @return a boolean value indicating if the Chat is currently shown or hidden.
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * Setter Method for isShowing.
     * @param showing: a boolean value indicating if the Chat is currently shown or hidden.
     */
    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    /**
     * Getter Method for container.
     * @return the StackPane that contains the whole Chat.
     */
    public Pane getPane() {
        return container;
    }

    // Methods

    /**
     * Re-builds the entire Chat as needed.
     */
    public void refresh() {
        build();
    }

    /**
     * Builds the Chat, including all the GUI elements, and puts them inside container.
     */
    private void build() {
        contentContainer.getChildren().clear();
        if (messages.isEmpty()) {
            Text text = new Text("No chat messages available");
            if (hasBackground) {
                text.setFill(Paint.valueOf("white"));
            } else {
                text.setFill(Color.BLACK);
            }
            text.setFont(Font.font("Contantia Italic", 15));
            contentContainer.getChildren().add(text);
        } else {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPrefWidth(contentContainer.getWidth());
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

            VBox vBox = new VBox();
            vBox.setSpacing(20);
            vBox.setPadding(new Insets(10, 10, 10, 10));
            vBox.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

            for (ChatMessage message : messages) {
                AnchorPane pane = new AnchorPane();
                pane.getChildren().add(createMessageBox(message));
                vBox.getChildren().add(pane);
            }

            scrollPane.setContent(vBox);
            scrollPane.setVvalue(1);

            contentContainer.getChildren().add(scrollPane);
        }
    }

    /**
     * Creates the Message GUI element to add in the Chat.
     * @param message: the new Message that needs to be added.
     * @return the Pane that contains the whole Message, ready to be added in the ScrollPane.
     */
    private Pane createMessageBox(ChatMessage message) {
        VBox box = new VBox();
        box.setMaxWidth(200);
        box.setPrefHeight(50);
        box.setPrefWidth(200);
        box.setSpacing(6);
        box.setPadding(new Insets(10, 10, 10, 10));
        if (message.getSender().equals(controller.getPlayerNickname())) {
            // Message Sent
            box.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 15;");
            AnchorPane.setRightAnchor(box, 0.0);
        } else if(message.getSender().equals("Server")) {
            // Message from Server
            box.setStyle("-fx-background-color: #ccb619; -fx-background-radius: 15;");

        } else {
            // Message Received from others
            box.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 15;");
            AnchorPane.setLeftAnchor(box, 0.0);
        }

        Label senderName = new Label(message.getSender());
        senderName.setTextOverrun(OverrunStyle.ELLIPSIS);
        senderName.setTextAlignment(TextAlignment.LEFT);
        senderName.setMaxWidth(180);
        senderName.setFont(Font.font("Tahoma Bold", 15));
        senderName.setTextFill(Color.BLACK);
        senderName.setStyle("-fx-text-fill: black");

        Text body = new Text(message.getText());
        body.setWrappingWidth(180);
        body.setFont(Font.font("Tahoma Regular", 12));

        Text timeStamp = new Text();
        timeStamp.setText(message.getDateTime().getHour() + ":" + message.getDateTime().getMinute());
        timeStamp.setFill(Paint.valueOf("dimgrey"));
        timeStamp.setStrokeWidth(0.6);
        timeStamp.setFont(Font.font("Tahoma Bold", 8));
        VBox.setMargin(timeStamp, new Insets(5, 0, 0, 0));

        box.getChildren().addAll(senderName, body, timeStamp);
        return box;
    }

    /**
     * Creates a new Message containing the Player's playerID and the TextField's content and sends it to the Server.
     */
    private void sendMessage() {
        if (!chatTextField.getText().isEmpty()) {
            try {
                controller.getNetworkController().sendMessage(controller.getOwner(), chatTextField.getText());
                chatTextField.clear();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inverts the current mode: Shown -> Hidden, Hidden -> Shown.
     */
    public void toggle() {
        if (isShowing) {
            hide();
        } else {
            show();
        }
    }

    /**
     * Animates the Hide feature
     */
    public void hide() {
        chatTextField.setDisable(true);
        controller.blockInput();
        isShowing = false;
        if (null != chatHint) {
            chatHint.setText("Show Chat");
        }
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
    }

    /**
     * Animates the Show feature
     */
    public void show() {
        chatTextField.setDisable(false);
        controller.blockInput();
        isShowing = true;
        if (null != chatHint) {
            chatHint.setText("Hide Chat");
        }
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(-ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
    }

    /**
     * Adds a new Message to the Chat, and visually updates it.
     * @param message: the new Message to add.
     */
    public void addMessage(ChatMessage message) {
        messages.add(message);
        Platform.runLater(() -> {
            build();
            notifyNewMessage(message);
        });
    }

    /**
     * Visually notifies the User of a new Message if the Chat is hidden (not if it's shown).
     * @param message: the new Message that needs to be notified to the User.
     */
    private void notifyNewMessage(ChatMessage message) {
        // Notify only for messages from other players and only when the Chat is hidden
        if (!isShowing && !message.getSender().equals("Server") && !message.getSender().equals(controller.getPlayerNickname())) {
            VBox messageBox = new VBox(createMessageBox(message));
            messageBox.setTranslateX(-300);
            messageBox.setTranslateY(400);
            messageBox.setScaleX(0);
            messageBox.setScaleY(0);
            container.getChildren().add(messageBox);
            ScaleTransition scaling = new ScaleTransition(Duration.millis(200), messageBox);
            scaling.setToX(1);
            scaling.setToY(1);
            scaling.setOnFinished((e) -> {
                TranslateTransition translate = new TranslateTransition(Duration.millis(4000), messageBox);
                translate.setByY(-1000);
                translate.setOnFinished((e2) -> {
                    container.getChildren().remove(messageBox);
                });

                RotateTransition rotate = new RotateTransition(Duration.millis(700), messageBox);
                rotate.setByAngle(5);
                rotate.setAutoReverse(true);
                rotate.setCycleCount(4);

                rotate.play();
                translate.play();
            });
            scaling.play();
        }
    }
}
