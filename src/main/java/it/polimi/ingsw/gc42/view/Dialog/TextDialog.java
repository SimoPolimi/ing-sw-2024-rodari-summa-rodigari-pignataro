package it.polimi.ingsw.gc42.view.Dialog;

import javafx.scene.Node;

/**
 * Dialog that only shows a Text message.
 */
public class TextDialog extends Dialog {

    // Constructor Method

    /**
     * Constructor Method
     * @param title a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     */
    public TextDialog(String title, boolean isDismissible) {
        super(title, isDismissible);
    }

    // Methods

    /**
     * Created this Dialog's UI.
     * @return the Node (JavaFx element) containing the UI.
     */
    @Override
    public Node build() {
        return container;
    }

    /**
     * Handles the Keyboard's input events.
     * @param key a String containing the Key's name.
     */
    @Override
    public void onKeyPressed(String key) {

    }
}
