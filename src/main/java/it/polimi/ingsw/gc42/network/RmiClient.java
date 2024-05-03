package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.*;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class RmiClient implements NetworkController, Serializable {
    private String ipAddress;
    private int port;
    private Registry registry;
    //private RemoteCollection games;
    private RemoteServer server;
    private int gameID;
    private boolean isConnected = false;
    private Player owner;
    private int playerID;

    private Listener handCard1Listener;
    private Listener handCard2Listener;
    private Listener handCard3Listener;

    private ClientController clientController;
    private transient ArrayList<Listener> viewListeners = new ArrayList<>();

    public RmiClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ipAddress, port);
        //games = (RemoteCollection) registry.lookup("GameControllers");
        server = (RemoteServer) registry.lookup("ServerManager");
        isConnected = true;
    }

    @Override
    public void flipStarterCard(int playerID) {
        try {
            server.flipStarterCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPlayerStarterCard(int playerID) {
        try {
            server.setPlayerStarterCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPlayerSecretObjective(int playerID, int pickedCard) {
        try {
            server.setPlayerSecretObjective(gameID, playerID, pickedCard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException {
        if (isConnected) {
            Random random = new Random();
            int id = random.nextInt(100000000);
            registry.bind(String.valueOf(id), viewController);
            try {
                server.lookupClient(gameID, String.valueOf(id));
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
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
            return server.getGame(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player getPlayer(int index) {
        try {
            return server.getPlayer(gameID, index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        return server.getGame(gameID).getIndexOfPlayer(nickName);
    }

    @Override
    public void setPlayerToken(int playerID, Token token) {
        try {
            server.setPlayerToken(gameID, playerID, token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getNumberOfPlayers() {
        try {
            return server.getNumberOfPlayers(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startGame() {
        try {
            server.startGame(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPlayerStatus(int playerID, GameStatus status) {
        try {
            server.setPlayerStatus(gameID, playerID, status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean kickPlayer(Player player) {
        try {
            return server.kickPlayer(gameID, player);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextTurn() {
        try {
            server.nextTurn(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playCard(int handCard, int x, int y) {
        try {
            server.playCard(gameID, playerID, handCard, x, y);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flipCard(int playerID, int cardID) {
        try {
            server.flipCard(gameID, playerID, cardID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(int playerID, CardType type) {
        try {
            server.drawCard(gameID, playerID, type);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void grabCard(int playerID, CardType type, int slot) {
        try {
            server.grabCard(gameID, playerID, type, slot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawSecretObjectives() {
        try {
            server.drawSecretObjectives(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPlayer(Player player) {
        try {
            server.addPlayer(gameID, player);
            this.owner = player;
            playerID = server.getGame(gameID).getIndexOfPlayer(owner.getNickname());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCurrentStatus(GameStatus status) {
        try {
            server.setCurrentStatus(gameID, status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RemoteCollection getAvailableGames() throws RemoteException {
        return server.getGames();
    }

    public void pickGame(int index) throws RemoteException {
        gameID = index;
    }

    @Override
    public void getNewGameController() throws RemoteException {
        gameID = server.newGame();

    }

    @Override
    public void setName(String name) throws RemoteException {
        server.setName(gameID, name);
    }

    @Override
    public int getIndex() throws RemoteException {
        return gameID;
    }

    @Override
    public RemoteServer getServer() throws RemoteException {
        return server;
    }

    @Override
    public void removeListener(int playerID, int cardID, Listener listener) throws RemoteException {
        viewListeners.remove(listener);
        switch (cardID) {
            case 1 -> server.removeCardListener(gameID, playerID, cardID, handCard1Listener);
            case 2 -> server.removeCardListener(gameID, playerID, cardID, handCard2Listener);
            case 3 -> server.removeCardListener(gameID, playerID, cardID, handCard3Listener);
        }
    }
}
