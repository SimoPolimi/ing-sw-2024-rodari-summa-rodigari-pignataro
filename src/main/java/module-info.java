module it.polimi.ingsw.gc42 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.desktop;
    requires java.naming;
    requires java.rmi;

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
    exports it.polimi.ingsw.gc42.controller;
    opens it.polimi.ingsw.gc42.controller to javafx.fxml;
    exports it.polimi.ingsw.gc42.view.Dialog;
    opens it.polimi.ingsw.gc42.view.Dialog to javafx.fxml;
    exports it.polimi.ingsw.gc42.view.Exceptions;
    opens it.polimi.ingsw.gc42.view.Exceptions to javafx.fxml;
    exports it.polimi.ingsw.gc42.view.Classes;
    opens it.polimi.ingsw.gc42.view.Classes to javafx.fxml;
    exports it.polimi.ingsw.gc42.view.Interfaces;
    opens it.polimi.ingsw.gc42.view.Interfaces to javafx.fxml;
    exports it.polimi.ingsw.gc42.network;
    opens it.polimi.ingsw.gc42.network to javafx.fxml;
    exports it.polimi.ingsw.gc42.network.interfaces;
    opens it.polimi.ingsw.gc42.network.interfaces to javafx.fxml;
    opens it.polimi.ingsw.gc42.network.messages to java.base;
    opens it.polimi.ingsw.gc42.view;
    opens it.polimi.ingsw.gc42.network.messages.GameMessages to java.base;
    opens it.polimi.ingsw.gc42.network.messages.PlayerMessages to java.base;
    exports it.polimi.ingsw.gc42.view.Classes.gui;
    opens it.polimi.ingsw.gc42.view.Classes.gui to javafx.fxml;
    exports it.polimi.ingsw.gc42.view.Classes.tui;
    opens it.polimi.ingsw.gc42.view.Classes.tui;
}