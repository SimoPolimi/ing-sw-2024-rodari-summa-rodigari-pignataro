package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerManager extends UnicastRemoteObject implements RemoteServer, Serializable {
    private GameCollection collection;
    int port;

    protected ServerManager(int port) throws RemoteException {
        this.port = port;
    }

    @Override
    public int addGame(GameController game) throws RemoteException {
        collection.add(game);
        return collection.size() - 1;
    }

    @Override
    public RemoteCollection getGames() throws RemoteException {
        return collection;
    }

    @Override
    public boolean kickPlayer(int gameID, Player player) throws RemoteException {
        return collection.get(gameID).kickPlayer(player);
    }

    @Override
    public void nextTurn(int gameID) throws RemoteException {
        collection.get(gameID).nextTurn();
    }

    @Override
    public void playCard(int gameID, int playerID,  int cardID, int x, int y) throws RemoteException {
        collection.get(gameID).playCard(playerID, cardID, x, y);
    }

    @Override
    public void flipCard(int gameID, Card card) throws RemoteException {
        collection.get(gameID).flipCard(card);
    }

    @Override
    public void drawCard(int gameID, int playerID, CardType type) throws RemoteException {
        switch (type) {
            case RESOURCECARD -> {
                collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getResourcePlayingDeck());
            }
            case GOLDCARD -> {
                collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getGoldPlayingDeck());
            }
            case OBJECTIVECARD -> {
                collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getObjectivePlayingDeck());
            }
        }
    }

    @Override
    public void grabCard(int gameID, int playerID, CardType type, int slot) throws RemoteException {
        switch (type) {
            case RESOURCECARD -> {
                collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getResourcePlayingDeck(), slot);
            }
            case GOLDCARD -> {
                collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getGoldPlayingDeck(), slot);
            }
            case OBJECTIVECARD -> {
                collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getObjectivePlayingDeck(), slot);
            }
        }
    }

    @Override
    public void drawSecretObjectives(int gameID) throws RemoteException {
        collection.get(gameID).drawSecretObjectives();
    }

    @Override
    public Game getGame(int gameID) throws RemoteException {
        return collection.get(gameID).getGame();
    }

    @Override
    public void addView(int gameID, RemoteViewController viewController) throws RemoteException {
        collection.get(gameID).addView(viewController);
    }

    @Override
    public void setPlayerStatus(int gameID, int playerID, GameStatus status) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).setStatus(status);
    }

    @Override
    public void setPlayerToken(int gameID, int playerID, Token token) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).setToken(token);
    }

    @Override
    public void addPlayer(int gameID, Player player) throws RemoteException {
        collection.get(gameID).addPlayer(player);
    }

    @Override
    public void setCurrentStatus(int gameID, GameStatus status) throws RemoteException {
        collection.get(gameID).setCurrentStatus(status);
    }

    @Override
    public String getName(int gameID) throws RemoteException {
        return collection.get(gameID).getName();
    }

    @Override
    public void setName(int gameID, String name) throws RemoteException {
        collection.get(gameID).setName(name);
    }

    @Override
    public int getNumberOfPlayers(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getNumberOfPlayers();
    }

    @Override
    public Player getPlayer(int gameID, int index) throws RemoteException {
        return collection.get(gameID).getPlayer(index);
    }

    @Override
    public void removeCardListener(int gameID, int playerID, int cardID, Listener listener) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).getHandCard(cardID).removeListener(listener);
    }

    @Override
    public void setPlayerStarterCard(int gameID, int playerID) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).setStarterCard(collection.get(gameID)
                .getPlayer(playerID).getTemporaryStarterCard());
    }

    @Override
    public void flipStarterCard(int gameID, int playerID) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).getTemporaryStarterCard().flip();
    }

    @Override
    public int newGame() throws RemoteException {
        GameController game = new GameController(null);
        game.setCurrentStatus(GameStatus.WAITING_FOR_PLAYERS);
        collection.add(game);
        return collection.size() - 1;
    }

    @Override
    public void startGame(int gameID) throws RemoteException {
        collection.get(gameID).startGame();
    }

    @Override
    public void setPlayerSecretObjective(int gameID, int playerID, int pickedCard) throws RemoteException {
        collection.get(gameID).getPlayer(playerID).setSecretObjective(collection.get(gameID)
                .getPlayer(playerID).getTemporaryObjectiveCards().get(pickedCard));
    }

    @Override
    public void lookupClient(int gameID, String clientID) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(port);
        addView(gameID, (RemoteViewController) registry.lookup(clientID));
    }

    public void setCollection(GameCollection collection) {
        this.collection = collection;
    }
}
