module it.polimi.ingsw.gc42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.gc42 to javafx.fxml;
    exports it.polimi.ingsw.gc42;
    exports it.polimi.ingsw.gc42.model.classes;
    opens it.polimi.ingsw.gc42.model.classes to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.classes.cards;
    opens it.polimi.ingsw.gc42.model.classes.cards to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.classes.game;
    opens it.polimi.ingsw.gc42.model.classes.game to javafx.fxml;
    opens it.polimi.ingsw.gc42.model to javafx.fxml;
}