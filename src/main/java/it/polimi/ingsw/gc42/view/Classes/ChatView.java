package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.view.GUIController;
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

public class ChatView {
    // Attributes
    private final StackPane container;
    private final StackPane contentContainer;
    private final TextField chatTextField;
    private final Button chatButton;
    private final Text chatHint;
    private final GUIController controller;

    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    private boolean isShowing = false;
    private boolean unreadMessagesAvailable = false;

    private static final int ANIMATION_DURATION = 350;
    public static final int ANIMATION_LENGTH = 490;

    // Constructor Method
    public ChatView(StackPane container, StackPane contentContainer, TextField chatTextField, Button sendButton, Text chatHint, GUIController controller) {
        this.container = container;
        this.contentContainer = contentContainer;
        this.chatTextField = chatTextField;
        this.chatButton = sendButton;
        this.chatHint = chatHint;
        this.controller = controller;
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
        chatHint.setText("Show Chat");
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
        container.requestFocus();
    }

    public void show() {
        controller.blockInput();
        isShowing = true;
        chatHint.setText("Hide Chat");
        TranslateTransition transition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), container);
        transition.setByX(-ANIMATION_LENGTH);
        transition.setOnFinished((e) -> controller.unlockInput());
        transition.play();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        Platform.runLater(() -> {
            build();
            notifyNewMessage(message);
        });
    }

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
