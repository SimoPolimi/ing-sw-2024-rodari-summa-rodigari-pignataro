package it.polimi.ingsw.gc42.view.Dialog;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Abstract implementation of a Dialog.
 * This class contains all the common features and behaviors of a Dialog, with (almost) nothing related to the UI
 * and its behaviour.
 * Dialogs can be Dismissible (closable by clicking outside of it) or not.
 * This behavior is defined by the value of isDismissible.
 * It's up to the caller to hide the Dialog, as it's not handled by the Dialog itself.
 */
public abstract class Dialog {
    // Attributes
    private String title;
    private boolean isDismissible;
    protected final VBox container = new VBox();

    // Constructor Methods

    /**
     * Constructor Method
     * @param title a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     */
    public Dialog(String title, boolean isDismissible) {
        this.title = title;
        this.isDismissible = isDismissible;
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

    /**
     * Getter Method for title
     * @return the String shown as a Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter Method for title
     * @param title the String to show in the Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter Method for isDismissible
     * @return a boolean value that defines if the Dialog can be closed without picking a Card
     */
    public boolean isDismissible() {
        return isDismissible;
    }

    /**
     * Setter Method for isDismissible
     * @param dismissible  a boolean value that defines if the Dialog can be closed without picking a Card
     */
    public void setDismissible(boolean dismissible) {
        isDismissible = dismissible;
    }

    // Methods

    /**
     * Abstract Method to build the Dialog's UI.
     * This method is not implemented. All subclasses must implement it in their own way.
     * @return the Node (JavaFx) element that contains the whole UI.
     */
    public abstract Node build();

    /**
     * Abstract Method to handle Keyboard input events.
     * @param key a String containing the Key's name.
     */
    public abstract void onKeyPressed(String key);
}
