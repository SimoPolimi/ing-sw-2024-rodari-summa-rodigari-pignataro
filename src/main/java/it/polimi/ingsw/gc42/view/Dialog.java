package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.CardController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public abstract class Dialog {
    // Attributes
    private String title;
    private boolean isDismissable;
    protected final VBox container = new VBox();

    // Constructor Methods
    public Dialog(String title, boolean isDismissable) {
        this.title = title;
        this.isDismissable = isDismissable;
        container.setAlignment(Pos.CENTER);
        container.setSpacing(20);
        Text text = new Text(title);
        text.setFont(Font.font("Constantia Italic", 20));
        text.setUnderline(true);
        text.setStrokeWidth(25);
        text.setFill(Paint.valueOf("white"));
        text.setTextAlignment(TextAlignment.CENTER);
        container.getChildren().add(text);
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDismissable() {
        return isDismissable;
    }

    public void setDismissable(boolean dismissable) {
        isDismissable = dismissable;
    }

    // Methods
    public abstract Node build();
    public void addNode(Node node) {
        container.getChildren().add(node);
    }

    public abstract void onKeyPressed(String key);
}
