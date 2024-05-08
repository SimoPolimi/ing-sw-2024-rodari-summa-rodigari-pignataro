package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.Deck;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.network.interfaces.RemoteCollection;
import it.polimi.ingsw.gc42.network.interfaces.RemoteServer;
import it.polimi.ingsw.gc42.network.interfaces.RemoteViewController;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    public void flipCard(int gameID, int playerID, int cardiID) throws RemoteException {
        collection.get(gameID).flipCard(playerID, cardiID);
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
        collection.get(gameID).getPlayer(playerID).setStarterCard();
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
    public void lookupClient(int gameID, String ip, int port, String clientID) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        addView(gameID, (RemoteViewController) registry.lookup(clientID));
    }

    public void setCollection(GameCollection collection) {
        this.collection = collection;
    }

    @Override
    public ArrayList<String> getDeckTextures(int gameID, CardType type) throws RemoteException {
        ArrayList<String> deckTextures = new ArrayList<>();
        ArrayList<Card> deck = null;
        switch (type) {
            case RESOURCECARD -> deck = collection.get(gameID).getGame().getResourcePlayingDeck().getDeck().getCopy();
            case GOLDCARD -> deck = collection.get(gameID).getGame().getGoldPlayingDeck().getDeck().getCopy();
            case OBJECTIVECARD -> deck = collection.get(gameID).getGame().getObjectivePlayingDeck().getDeck().getCopy();
        }
        if (null != deck) {
            for (Card card: deck) {
                deckTextures.add(card.getBackImage());
            }
        }
        // If the Deck is empty, it returns an empty ArrayList
        return deckTextures;
    }

    @Override
    public String getSlotCardTexture(int gameID, CardType type, int slot) throws RemoteException {
        String string = "";
        switch (type) {
            case RESOURCECARD -> {
                string = collection.get(gameID).getGame().getResourcePlayingDeck().getSlot(slot).getFrontImage();
            }
            case GOLDCARD -> {
                string = collection.get(gameID).getGame().getGoldPlayingDeck().getSlot(slot).getFrontImage();
            }
            case OBJECTIVECARD -> {
                string = collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot).getFrontImage();
            }
        }
        return string;
    }

    @Override
    public String getSecretObjectiveName(int gameID, int playerID) throws RemoteException {
        ObjectiveCard card = collection.get(gameID).getPlayer(playerID).getSecretObjective();
        if (null != card) {
            return card.getObjective().getName();
        } else return "";
    }

    @Override
    public String getSecretObjectiveDescription(int gameID, int playerID) throws RemoteException {
        ObjectiveCard card = collection.get(gameID).getPlayer(playerID).getSecretObjective();
        if (null != card) {
            return card.getObjective().getDescription();
        } else return "";
    }

    @Override
    public String getCommonObjectiveName(int gameID, int slot) throws RemoteException {
        ObjectiveCard card = (ObjectiveCard) collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
        if (null != card) {
            return card.getObjective().getName();
        } else return "";
    }

    @Override
    public String getCommonObjectiveDescription(int gameID, int slot) throws RemoteException {
        ObjectiveCard card = (ObjectiveCard) collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
        if (null != card) {
            return card.getObjective().getDescription();
        } else return "";
    }

    @Override
    public int getPlayerTurn(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getPlayerTurn();
    }

    @Override
    public ArrayList<HashMap<String, String>> getTemporaryObjectiveTextures(int gameID, int playerID) throws RemoteException {
        ArrayList<HashMap<String, String>> temporaryObjectives = new ArrayList<>();
        ArrayList<ObjectiveCard> temporaryCards = collection.get(gameID).getPlayer(playerID).getTemporaryObjectiveCards();
        for (ObjectiveCard card: temporaryCards) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Front", card.getFrontImage());
            map.put("Back", card.getBackImage());
            map.put("Name", card.getObjective().getName());
            map.put("Description", card.getObjective().getDescription());
            temporaryObjectives.add(map);
        }
        return temporaryObjectives;
    }

    @Override
    public HashMap<String, String> getTemporaryStarterCardTextures(int gameID, int playerID) throws RemoteException {
        HashMap<String, String> map = new HashMap<>();
        StarterCard card = collection.get(gameID).getPlayer(playerID).getTemporaryStarterCard();
        map.put("Front", card.getFrontImage());
        map.put("Back", card.getBackImage());
        return map;
    }

    @Override
    public HashMap<String, String> getSecretObjectiveTextures(int gameID, int playerID) throws RemoteException {
        HashMap<String, String> map = new HashMap<>();
        ObjectiveCard card = collection.get(gameID).getPlayer(playerID).getSecretObjective();
        map.put("Front", card.getFrontImage());
        map.put("Back", card.getBackImage());
        return map;    }

    @Override
    public GameStatus getPlayerStatus(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getStatus();
    }

    @Override
    public ArrayList<HashMap<String, String>> getPlayersInfo(int gameID) throws RemoteException {
        ArrayList<HashMap<String, String>> playersInfo = new ArrayList<>();
        for (int i = 0; i < collection.get(gameID).getGame().getNumberOfPlayers(); i++) {
            Player player = collection.get(gameID).getPlayer(i+1);
            HashMap<String, String> map = new HashMap<>();
            map.put("Nickname", player.getNickname());
            map.put("Points", String.valueOf(player.getPoints()));
            if (null != player.getToken()) {
                switch (player.getToken()) {
                    case BLUE -> map.put("Token", "Blue");
                    case RED -> map.put("Token", "Red");
                    case GREEN -> map.put("Token", "Green");
                    case YELLOW -> map.put("Token", "Yellow");
                }
            } else {
                map.put("Token", "null");
            }
            playersInfo.add(map);
        }
        return playersInfo;
    }

    @Override
    public int getPlayersHandSize(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandSize();
    }

    @Override
    public boolean isPlayerFirst(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).isFirst();
    }

    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getAvailablePlacements();
    }

    @Override
    public boolean canCardBePlayed(int gameID, int playerID, int cardID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandCard(cardID).canBePlaced(collection
                .get(gameID).getPlayer(playerID).getPlayField().getPlayedCards());
    }

    @Override
    public Token getPlayerToken(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getToken();
    }

    @Override
    public PlayableCard getPlayersLastPlayedCard(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getLastPlayedCard();
    }

    @Override
    public PlayableCard getPlayersHandCard(int gameID, int playerID, int cardID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandCard(cardID);
    }
}
