package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient implements NetworkController{
    private String ipAddress;
    private int port;
    private Registry registry;
    private RemoteObject controller;
    private boolean isConnected = false;

    public RmiClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ipAddress, port);
        controller = (RemoteObject) registry.lookup("GameController");
        isConnected = true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public Game getGame() {
        try {
            return controller.getGame();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean kickPlayer(Player player) {
        try {
            return controller.kickPlayer(player);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextTurn() {
        try {
            controller.nextTurn();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playCard(PlayableCard card, int x, int y) {
        try {
            controller.playCard(card, x, y);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flipCard(Card card) {
        try {
            controller.flipCard(card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(Player player, PlayingDeck playingDeck) {
        try {
            controller.drawCard(player, playingDeck);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void grabCard(Player player, PlayingDeck playingDeck, int slot) {
        try {
            controller.grabCard(player, playingDeck, slot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawSecretObjectives() {
        try {
            controller.drawSecretObjectives();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addView(ViewController viewController) {
        try {
            controller.addView(viewController);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPlayer(Player player) {
        try {
            controller.addPlayer(player);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCurrentStatus(GameStatus status) {
        try {
            controller.setCurrentStatus(status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
