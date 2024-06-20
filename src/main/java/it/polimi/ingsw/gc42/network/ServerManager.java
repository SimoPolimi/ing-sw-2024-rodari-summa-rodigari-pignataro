package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.*;
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
    public ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException {
        ArrayList<HashMap<String, String>> availableGames = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            GameController game = collection.get(i);
            HashMap<String, String> gameInfo = new HashMap<>();
            gameInfo.put("Name", game.getName());
            gameInfo.put("NumberOfPlayers", String.valueOf(game.getGame().getNumberOfPlayers()));
            GameStatus status = game.getCurrentStatus();
            String string = "";
            switch (status) {
                case WAITING_FOR_PLAYERS -> string = "Waiting for players";
                case PLAYING -> string = "Playing";
                case READY -> string = "Ready";
                case READY_TO_CHOOSE_TOKEN -> string = "Ready to choose Token";
                case NOT_IN_GAME -> string = "Not in game";
                case READY_TO_CHOOSE_STARTER_CARD -> string = "Ready to choose Starter Card";
                case READY_TO_CHOOSE_SECRET_OBJECTIVE -> string = "Ready to choose Secret Objective";
                case WAITING_FOR_SERVER -> string = "Waiting for server";
                case READY_TO_DRAW_STARTING_HAND -> string = "Ready to Draw Starting Hand";
                case END_GAME -> string = "End Game";
                case CONNECTING -> string = "Connecting";
                case SEMI_LAST_TURN -> string = "Semi Last Turn";
                case MY_TURN -> string = "My Turn";
                case NOT_MY_TURN -> string = "Not My Turn";
            }
            gameInfo.put("Status", string);
            availableGames.add(gameInfo);
        }
        return availableGames;
    }

    @Override
    public boolean kickPlayer(int gameID, Player player) throws RemoteException {
        return collection.get(gameID).kickPlayer(player);
    }

    /**
     * Sets the Player's GameStatus to DISCONNECTED, so that it will be ignored by the Turn System
     * @param playerID the Player's playerID to disconnect
     */
    public void disconnectPlayer(int gameID, int playerID) throws RemoteException {
        collection.get(gameID).disconnectPlayer(playerID);
    }

    @Override
    public void nextTurn(int gameID) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).nextTurn();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public int getIndexOfPlayer(int gameID, String nickname) throws RemoteException {
        return collection.get(gameID).getGame().getIndexOfPlayer(nickname);
    }

    @Override
    public void playCard(int gameID, int playerID,  int cardID, int x, int y) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).playCard(playerID, cardID, x, y);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void flipCard(int gameID, int playerID, int cardiID) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).flipCard(playerID, cardiID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void drawCard(int gameID, int playerID, CardType type) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {

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
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void grabCard(int gameID, int playerID, CardType type, int slot) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
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
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public Game getGame(int gameID) throws RemoteException {
        return collection.get(gameID).getGame();
    }

    @Override
    public void addView(int gameID, RemoteViewController viewController) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).addView(viewController);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void setPlayerStatus(int gameID, int playerID, GameStatus status) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getPlayer(playerID).setStatus(status);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void setPlayerToken(int gameID, int playerID, Token token) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getPlayer(playerID).setToken(token);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void addPlayer(int gameID, Player player) throws RemoteException {
        collection.get(gameID).addPlayer(player);
    }

    @Override
    public void setCurrentStatus(int gameID, GameStatus status) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).setCurrentStatus(status);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public String getName(int gameID) throws RemoteException {
        return collection.get(gameID).getName();
    }

    @Override
    public void setName(int gameID, String name) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).setName(name);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
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
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getPlayer(playerID).setStarterCard();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void flipStarterCard(int gameID, int playerID) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getPlayer(playerID).getTemporaryStarterCard().flip();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
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
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).startGame();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public void setPlayerSecretObjective(int gameID, int playerID, int pickedCard) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getPlayer(playerID).setSecretObjective(collection.get(gameID)
                        .getPlayer(playerID).getTemporaryObjectiveCards().get(pickedCard));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
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
    public ArrayList<Card> getDeck(int gameID, CardType type) throws RemoteException {
        ArrayList<String> deckTextures = new ArrayList<>();
        ArrayList<Card> deck = null;
        switch (type) {
            case RESOURCECARD -> deck = collection.get(gameID).getGame().getResourcePlayingDeck().getDeck().getCopy();
            case GOLDCARD -> deck = collection.get(gameID).getGame().getGoldPlayingDeck().getDeck().getCopy();
            case OBJECTIVECARD -> deck = collection.get(gameID).getGame().getObjectivePlayingDeck().getDeck().getCopy();
        }
        // If the Deck is empty, it returns an empty ArrayList
        return deck;
    }

    @Override
    public Card getSlotCard(int gameID, CardType type, int slot) throws RemoteException {
        Card card = null;
        switch (type) {
            case RESOURCECARD -> {
                card = collection.get(gameID).getGame().getResourcePlayingDeck().getSlot(slot);
            }
            case GOLDCARD -> {
                card = collection.get(gameID).getGame().getGoldPlayingDeck().getSlot(slot);
            }
            case OBJECTIVECARD -> {
                card = collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
            }
        }
        return card;
    }

    @Override
    public ObjectiveCard getSecretObjective(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getSecretObjective();
    }

    @Override
    public ObjectiveCard getCommonObjective(int gameID, int slot) throws RemoteException {
        return (ObjectiveCard) collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
    }

    @Override
    public int getPlayerTurn(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getPlayerTurn();
    }

    @Override
    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getTemporaryObjectiveCards();
    }

    @Override
    public StarterCard getTemporaryStarterCard(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getTemporaryStarterCard();
    }

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

    @Override
    public ArrayList<PlayableCard> getPlayersPlayfield(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getPlayedCards();
    }

    @Override
    public void sendMessage(int gameID, int playerID, String message) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).getGame().getChat().sendMessage(new ChatMessage(message, collection.get(gameID).getPlayer(playerID).getNickname()));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    @Override
    public ArrayList<ChatMessage> getChat(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getChat().getFullChat();
    }

    @Override
    public boolean checkNickName(String nickname) throws RemoteException {
        return collection.isNickNameAvailable(nickname);
    }

    @Override
    public void blockNickName(String nickname) throws RemoteException {
        collection.blockNickname(nickname);
    }
}
