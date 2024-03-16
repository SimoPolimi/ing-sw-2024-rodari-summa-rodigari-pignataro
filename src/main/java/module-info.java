module it.polimi.ingsw.gc42 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens it.polimi.ingsw.gc42 to javafx.fxml;
    exports it.polimi.ingsw.gc42;
    exports it.polimi.ingsw.gc42.model.classes;
    opens it.polimi.ingsw.gc42.model.classes to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.classes.cards;
    opens it.polimi.ingsw.gc42.model.classes.cards to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.classes.game;
    opens it.polimi.ingsw.gc42.model.classes.game to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.exceptions;
    opens it.polimi.ingsw.gc42.model.exceptions to javafx.fxml;
    exports it.polimi.ingsw.gc42.model.interfaces;
    opens it.polimi.ingsw.gc42.model.interfaces to javafx.fxml;
    exports it.polimi.ingsw.gc42.model;
    opens it.polimi.ingsw.gc42.model to javafx.fxml;
    exports it.polimi.ingsw.gc42.view;
    opens it.polimi.ingsw.gc42.view to javafx.fxml;
    exports it.polimi.ingsw.gc42.controller;
    opens it.polimi.ingsw.gc42.controller to javafx.fxml;
}