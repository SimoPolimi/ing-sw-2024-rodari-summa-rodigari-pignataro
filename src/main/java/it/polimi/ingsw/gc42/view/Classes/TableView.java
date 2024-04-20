package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.game.Player;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Node;

public class TableView {
    // Attributes
    private Player player;
    private boolean isPrivacyModeEnabled;
    private int angle;

    // Constructor Method

    public TableView(boolean isPrivacyModeEnabled, int angle) {
        this.isPrivacyModeEnabled = isPrivacyModeEnabled;
        this.angle = angle;
    }

    // Getters and Setters
    public void setPlayer(Player player) {
        this.player = player;
        // TODO: set Player's Listeners
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPrivacyModeEnabled() {
        return isPrivacyModeEnabled;
    }

    public void setPrivacyModeEnabled(boolean privacyModeEnabled) {
        isPrivacyModeEnabled = privacyModeEnabled;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    // Methods:
    public Pane build() {
        AnchorPane playerTableContainer = new AnchorPane();
        AnchorPane.setRightAnchor(playerTableContainer, 0.0);
        AnchorPane.setLeftAnchor(playerTableContainer, 0.0);
        AnchorPane.setTopAnchor(playerTableContainer, 0.0);
        AnchorPane.setBottomAnchor(playerTableContainer, 0.0);




        return playerTableContainer;
    }
}
