package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.Interfaces.*;

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
            Player player =  server.getPlayer(gameID, index);
            return player;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        return server.getGame(gameID).getIndexOfPlayer(nickName);
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
    public void playCard(PlayableCard card, int x, int y) {
        try {
            server.playCard(gameID, card, x, y);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flipCard(Card card) {
        try {
            server.flipCard(gameID, card);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(Player player, CardType type) {
        try {
            server.drawCard(gameID, player, type);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void grabCard(Player player, CardType type, int slot) {
        try {
            server.grabCard(gameID, player, type, slot);
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
        initListeners();
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
    public StarterCard drawStarterCard() throws RemoteException {
        return server.drawStarterCard(gameID);
    }

    @Override
    public void setGameListener(Listener listener) {
        viewListeners.add(listener);
    }

    @Override
    public void setPlayerListener(int playerID, Listener listener) {
        viewListeners.add(listener);
        this.playerID = playerID;
    }

    @Override
    public void setHandCardListener(int playerID, int cardID, Listener listener) throws RemoteException {
        viewListeners.add(listener);
        server.getPlayer(gameID, playerID).getHandCard(cardID).setListener(new Listener() {
            @Override
            public void onEvent() {
                notifyListener("Hand Card " + cardID);
            }
        });
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

    private void initListeners() throws RemoteException {
        server.setGameListener(gameID, new GameListener() {
            @Override
            public void onEvent() {
                notifyListener("Game Listener");
            }
        });
        server.setGameListener(gameID, new ResourceDeckViewListener() {
            @Override
            public void onEvent() {
                notifyListener("Resource Deck");
            }
        });
        server.setGameListener(gameID, new GoldDeckViewListener() {
            @Override
            public void onEvent() {
                notifyListener("Gold Deck");
            }
        });
        server.setGameListener(gameID, new ResourceSlot1Listener() {
            @Override
            public void onEvent() {
                notifyListener("Resource Down 1");
            }
        });
        server.setGameListener(gameID, new ResourceSlot2Listener() {
            @Override
            public void onEvent() {
                notifyListener("Resource Down 2");
            }
        });
        server.setGameListener(gameID, new GoldSlot1Listener() {
            @Override
            public void onEvent() {
                notifyListener("Gold Down 1");
            }
        });
        server.setGameListener(gameID, new GoldSlot2Listener() {
            @Override
            public void onEvent() {
                notifyListener("Gold Down 2");
            }
        });
    }

    private void notifyListener(String context) {
        switch (context) {
            case "Game Listener" -> {
                for (Listener l: viewListeners) {
                    if (l instanceof GameListener) {
                        l.onEvent();
                    }
                }
            }
            case "Resource Deck" -> {
                for (Listener l: viewListeners) {
                    if (l instanceof ResourceDeckViewListener) {
                        l.onEvent();
                    }
                }
            }
            case "Gold Deck" -> {
                for (Listener l: viewListeners) {
                    if (l instanceof GoldDeckViewListener) {
                        l.onEvent();
                    }
                }
            }
            case "Resource Down 1" -> {
                for (Listener l: viewListeners) {
                    if (l instanceof ResourceSlot1Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Resource Down 2" -> {
                for (Listener l: viewListeners) {
                    if (l instanceof ResourceSlot2Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Gold Down 1" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof GoldSlot1Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Gold Down 2" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof GoldSlot2Listener) {
                        l.onEvent();
                    }
                }
            }
            case "Secret Objectives Selection Dialog" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof ReadyToChooseSecretObjectiveListener) {
                        l.onEvent();
                    }
                }
            }
            case "Starter Card Selection Dialog" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof ReadyToChooseStarterCardListener) {
                        l.onEvent();
                    }
                }
            }
            case "Token Selection Dialog" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof ReadyToChooseTokenListener) {
                        l.onEvent();
                    }
                }
            }
            case "Draw or Grab" -> {
                for (Listener l : viewListeners) {
                    if (l instanceof DrawOrGrabListener) {
                        l.onEvent();
                    }
                }
            }
        }
    }
}
