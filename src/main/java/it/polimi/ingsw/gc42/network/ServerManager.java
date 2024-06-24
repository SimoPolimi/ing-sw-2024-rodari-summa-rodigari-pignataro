package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
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

/**
 * Implementation of the Server's logic component.
 * This class handles all the actions that can be performed on a List of Games and
 * on a single Game.
 * This class is a UnicastRemoteObject, so that it can be shared via RMI.
 * It implements RemoteServer and all the behaviors defined in that interface.
 */
public class ServerManager extends UnicastRemoteObject implements RemoteServer, Serializable {
    private GameCollection collection;
    int port;

    /**
     * Constructor Method
     * @param port the Port it's opened into
     * @throws RemoteException in case of a Network Connection Error
     */
    protected ServerManager(int port) throws RemoteException {
        this.port = port;
    }

    /**
     * Getter Method for the List of Available Games that a Player can join.
     * The data is returned in an HashMap that uses String as a key and as a value:
     * all data is written in the String format, including numbers, and all keys are String as well.
     * Each HashMap contains the data of a single Game, and they are all contained in an ArrayList of
     * HashMaps, so that one HashMap for every Game can be found.
     * The data can be retrieved using the following Keys:
     *
     * - Name: the Game's name
     *
     * - NumberOfPlayers: the number of Players already inside it
     *
     * - Status: a String representation of the Game's GameStatus
     *
     * - NumberOfDisconnectedPlayers: the number of Players in the Game but disconnected,
     * used to iterate through the next elements
     *
     * - DisconnectedPlayerX: the Disconnected Players' Nickname, so that the Client can determine if the User
     * can re-join this Game or not. NOTE: There are multiple of them, starting from DisconnectedPlayer0 all the way to
     * DisconnectedPlayerN, where N = NumberOfDisconnectedPlayers - 1.
     *
     * @return the List of Games
     * @throws RemoteException in case of a Network Communication Error
     */
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

            ArrayList<String> disconnectedPlayers = new ArrayList<>();
            for (int j = 1; j <= game.getGame().getNumberOfPlayers(); j++) {
                if (game.getPlayer(j).isDisconnected()) {
                    disconnectedPlayers.add(game.getPlayer(j).getNickname());
                }
            }

            int number = disconnectedPlayers.size();
            gameInfo.put("NumberOfDisconnectedPlayers", String.valueOf(number));

            for (int j = 0; j < disconnectedPlayers.size(); j++) {
                gameInfo.put("DisconnectedPlayer" + j, disconnectedPlayers.get(j));
            }

            availableGames.add(gameInfo);
        }
        return availableGames;
    }

    /**
     * Removes the Player from the Game entirely
     * @param gameID the Game's gameID
     * @param player the Player's playerID
     * @return a boolean value indicating if the removal was successful or not
     * @throws RemoteException in case of a Network Communication Error
     */
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
        collection.unlockNickname(collection.get(gameID).getPlayer(playerID).getNickname());
    }

    /**
     * Sets the Player as not Disconnected, then allows it to continue playing
     * @param gameID the Game in which the Player is located
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a communication error
     */
    @Override
    public void rejoinGame(int gameID, int playerID) throws RemoteException {
        collection.get(gameID).rejoinGame(playerID);
    }

    /**
     * Triggers a Turn Change
     * @param gameID the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Getter Method for the Player's playerID
     * @param gameID the Game's gameID
     * @param nickname the Player's Nickname
     * @return the Player's playerID
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public int getIndexOfPlayer(int gameID, String nickname) throws RemoteException {
        return collection.get(gameID).getGame().getIndexOfPlayer(nickname);
    }

    /**
     * Plays a Card from a certain Player in a specific set of Coordinates
     * The Card to play is defined as the index of the Card in the Player's Hand.
     * The Coordinates need to be in the Model's format
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [1, 2, 3]
     * @param x the x Coordinate where the Card will be placed
     * @param y the y Coordinate where the Card will be placed
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Flips one of the Player's Cards
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void flipCard(int gameID, int playerID, int cardID) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                collection.get(gameID).flipCard(playerID, cardID);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    /**
     * Draws a Card from one of the 2 Decks and places it inside the Player's Hand
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Deck to draw [RESOURCECARD, GOLDCARD]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void drawCard(int gameID, int playerID, CardType type) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {

                switch (type) {
                    case RESOURCECARD -> collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getResourcePlayingDeck());
                    case GOLDCARD -> collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getGoldPlayingDeck());
                    case OBJECTIVECARD -> collection.get(gameID).drawCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getObjectivePlayingDeck());
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    /**
     * Grabs a Card from one of the 4 Slots and places it inside the Player's Hand
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Slots to grab [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot in particular [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void grabCard(int gameID, int playerID, CardType type, int slot) throws RemoteException {
        Thread thread = new Thread(() -> {
            try {
                switch (type) {
                    case RESOURCECARD -> collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getResourcePlayingDeck(), slot);
                    case GOLDCARD -> collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getGoldPlayingDeck(), slot);
                    case OBJECTIVECARD -> collection.get(gameID).grabCard(collection.get(gameID).getPlayer(playerID), collection.get(gameID).getGame().getObjectivePlayingDeck(), slot);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    /**
     * Getter Method for Game
     * @param gameID the Game's gameID
     * @return the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public Game getGame(int gameID) throws RemoteException {
        return collection.get(gameID).getGame();
    }

    /**
     * Subscribes the Client inside a Game, so that it will automatically be notified when any event happens
     * @param gameID the Game's GameID
     * @param viewController the Client's RemoteViewController
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void addView(int gameID, RemoteViewController viewController) throws RemoteException {
        collection.get(gameID).addView(viewController);
    }

    /**
     * Setter Method for the Player's GameStatus
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param status the GameStatus to set
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Setter Method for the Player's Token
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param token the Token to set
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Adds a Player into a Game
     * @param gameID the Game's gameID
     * @param player the Player to add
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void addPlayer(int gameID, Player player) throws RemoteException {
        collection.get(gameID).addPlayer(player);
    }

    /**
     * Setter Method for the Game's GameStatus
     * @param gameID the Game's gameID
     * @param status the GameStatus to set
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Getter Method for the Game's name
     * @param gameID the Game's gameID
     * @return a String containing the Game's name
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public String getName(int gameID) throws RemoteException {
        return collection.get(gameID).getName();
    }

    /**
     * Setter Method for the Game's name
     * @param gameID the Game's gameID
     * @param name a String containing the Name to set in the Game
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Getter Method for the number of Players inside a Game
     * @param gameID the Game's gameID
     * @return the number of Players already in that Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public int getNumberOfPlayers(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getNumberOfPlayers();
    }

    /**
     * Getter Method for a Player
     * @param gameID the Game's gameID
     * @param index the Player's playerID
     * @return the Player
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public Player getPlayer(int gameID, int index) throws RemoteException {
        return collection.get(gameID).getPlayer(index);
    }

    /**
     * Setter Method for the Player's Starter Card.
     * The Starter Card is already inside the Model, in the temporaryStarterCard field.
     * This method tells the Server to move it from there to the Player's PlayArea in (0, 0).
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Flips the Player's Temporary Starter Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Sends a message to the Server telling it to create a new Game
     * @return the new Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public int newGame() throws RemoteException {
        GameController game = new GameController(null);
        game.setCurrentStatus(GameStatus.WAITING_FOR_PLAYERS);
        collection.add(game);
        return collection.size() - 1;
    }

    /**
     * Sends a message to the Server telling it to start a specific Game
     * @param gameID the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Setter Method for the Player's Secret Objective.
     * The Secret Objective is already stored inside the Model and IS NOT sent in here.
     * The Player has 2 Temporary Secret Objectives: this method contains either 1 or 2, indicating
     * which one to keep.
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param pickedCard an int representing the User's choice [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Sends a message to the Server, telling it how to contact the Client, so that
     * it can save that information to reuse it when it will need to contact it again
     * @param gameID the Game's gameID to which the Client is subscribed
     * @param ip the Client's IP Address
     * @param port the Client's Port
     * @param clientID the String used for the Binding in the RMI Registry in RMI
     * @throws RemoteException in case of a Network Communication Error
     * @throws NotBoundException in case the Client can't be found
     */
    @Override
    public void lookupClient(int gameID, String ip, int port, String clientID) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        addView(gameID, (RemoteViewController) registry.lookup(clientID));
    }

    /**
     * Setter Method for the GameCollection
     * @param collection the List of Games
     */
    public void setCollection(GameCollection collection) {
        this.collection = collection;
    }

    /**
     * Getter Method for the Decks.
     * @param gameID the Game's gameID
     * @param type a CardType indicating which Deck to return
     * @return an ArrayList of Card, containing all the Cards inside the Deck, in the same order
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ArrayList<Card> getDeck(int gameID, CardType type) throws RemoteException {
        ArrayList<Card> deck = null;
        switch (type) {
            case RESOURCECARD -> deck = collection.get(gameID).getGame().getResourcePlayingDeck().getDeck().getCopy();
            case GOLDCARD -> deck = collection.get(gameID).getGame().getGoldPlayingDeck().getDeck().getCopy();
            case OBJECTIVECARD -> deck = collection.get(gameID).getGame().getObjectivePlayingDeck().getDeck().getCopy();
        }
        // If the Deck is empty, it returns an empty ArrayList
        return deck;
    }

    /**
     * Getter Method for the Slots
     * @param gameID the Game's gameID
     * @param type a CardType indicating which Slots to consider [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot to return [1, 2]
     * @return a copy of the Card inside that Slot
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public Card getSlotCard(int gameID, CardType type, int slot) throws RemoteException {
        Card card = null;
        switch (type) {
            case RESOURCECARD -> card = collection.get(gameID).getGame().getResourcePlayingDeck().getSlot(slot);
            case GOLDCARD -> card = collection.get(gameID).getGame().getGoldPlayingDeck().getSlot(slot);
            case OBJECTIVECARD -> card = collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
        }
        return card;
    }

    /**
     * Getter Method for the Player's Secret Objective
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's ObjectiveCard
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ObjectiveCard getSecretObjective(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getSecretObjective();
    }

    /**
     * Getter Method for the Common Objectives
     * @param gameID the Game's gameID
     * @param slot an int indicating which Slot [1, 2]
     * @return the ObjectiveCard
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ObjectiveCard getCommonObjective(int gameID, int slot) throws RemoteException {
        return (ObjectiveCard) collection.get(gameID).getGame().getObjectivePlayingDeck().getSlot(slot);
    }

    /**
     * Getter Method for the Current Turn
     * @param gameID the Game's gameID
     * @return the playerID corresponding to the Player who has the Turn
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public int getPlayerTurn(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getPlayerTurn();
    }

    /**
     * Getter Methods for the Temporary Secret Objectives
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList containing the Players Temporary Secret Objectives
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getTemporaryObjectiveCards();
    }

    /**
     * Getter Method for the Player's Temporary Starter Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's Temporary Starter Card
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public StarterCard getTemporaryStarterCard(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getTemporaryStarterCard();
    }

    /**
     * Getter Method for the Player's GameStatus
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's GameStatus
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public GameStatus getPlayerStatus(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getStatus();
    }

    /**
     * Getter Method for the Player's info.
     * Each Player's info are contained inside an HashMap, and both Keys and Values are String.
     * The HashMap are contained inside an ArrayList, so that there is an HashMap for each Player.
     * The data can be retrieved with the following Keys:
     * - Nickname: the Player's Nickname
     * - Points: the Player's points
     * - Token: a String representation of the Player's Token [RED, BLUE, GREEN, YELLOW]
     * @param gameID the Game's gameID
     * @return the Collection of data
     * @throws RemoteException in case of a Network Connection Error
     */
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

    /**
     * Getter Method for the Player's Hand's size
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the number of Cards inside the Player's Hand
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public int getPlayersHandSize(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandSize();
    }

    /**
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the Player is the first
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public boolean isPlayerFirst(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).isFirst();
    }

    /**
     * Getter Method for the Available Placements.
     * Returns an ArrayList of Coordinates, each indicating a position where a Card can be played
     * inside the Player's PlayArea
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList of Coordinates
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getAvailablePlacements();
    }

    /**
     *
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return a boolean value indicating if the Card's cost requirements are satisfied and the Card can be played
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public boolean canCardBePlayed(int gameID, int playerID, int cardID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandCard(cardID).canBePlaced(collection
                .get(gameID).getPlayer(playerID).getPlayField().getPlayedCards());
    }

    /**
     * Getter Method for the Player's Token
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's Token
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public Token getPlayerToken(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getToken();
    }

    /**
     * Getter Method for the Player's last played Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Card last played by the Player
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public PlayableCard getPlayersLastPlayedCard(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getLastPlayedCard();
    }

    /**
     * Getter Method for the Player's Hand's Cards
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return the Card
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public PlayableCard getPlayersHandCard(int gameID, int playerID, int cardID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getHandCard(cardID);
    }

    /**
     * Getter Method for the Player's PlayField
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList containing all the Cards played by the Player
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ArrayList<PlayableCard> getPlayersPlayfield(int gameID, int playerID) throws RemoteException {
        return collection.get(gameID).getPlayer(playerID).getPlayField().getPlayedCards();
    }

    /**
     * Sends a message in the Game's Chat
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param message a String containing the Message content
     * @throws RemoteException in case of a Network Connection Error
     */
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

    /**
     * Getter Method for the Game's Chat
     * @param gameID the Game's gameID
     * @return an ArrayList of Message, containing the whole Chat
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ArrayList<ChatMessage> getChat(int gameID) throws RemoteException {
        return collection.get(gameID).getGame().getChat().getFullChat();
    }

    /**
     * Checks if a Nickname is available
     * @param nickname a String containing the Nickname
     * @return a boolean value indicating if the Nickname is available or not
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public boolean checkNickName(String nickname) throws RemoteException {
        return collection.isNickNameAvailable(nickname);
    }

    /**
     * Blocks a Nickname, so that nobody else can use it
     * @param nickname a String containing the Nickname
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public void blockNickName(String nickname) throws RemoteException {
        collection.blockNickname(nickname);
    }
}