package it.polimi.ingsw.gc42.view.Dialog;

import javafx.scene.Node;

public class TextDialog extends Dialog {

    public TextDialog(String title, boolean isDismissable) {
        super(title, isDismissable);
    }

    @Override
    public Node build() {
        return container;
    }

    @Override
    public void onKeyPressed(String key) {

    }
}
