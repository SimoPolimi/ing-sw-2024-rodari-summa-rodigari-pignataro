package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.network.interfaces.RemoteServer;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This Class handles the RMI Client
 */
public class RmiClient implements NetworkController, Serializable {
    // Attributes
    private final String ipAddress;
    private final int port = 23689;
    private Registry registry;
    private Registry localRegistry;
    private RemoteServer server;
    private int gameID;
    private boolean isConnected = false;
    private Player owner;
    private int playerID;
    private int id;
    private ClientController viewController;

    /**
     * Constructor Method
     * @param ipAddress the Client's IP Address
     */
    public RmiClient(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Connects to the Server, doing a lookup inside the Server's RmiRegistry
     * @throws RemoteException in case of a Network Communication Error
     * @throws NotBoundException in case of a Registry Error
     */
    @Override
    public void connect() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ipAddress, port);
        server = (RemoteServer) registry.lookup("ServerManager");
        isConnected = true;
    }

    /**
     * Flips the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     */
    @Override
    public void flipStarterCard(int playerID) {
        try {
            server.flipStarterCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Decks.
     * @param type a CardType indicating which Deck to return
     * @return an ArrayList of Card, containing all the Cards inside the Deck, in the same order
     */
    @Override
    public ArrayList<Card> getDeck(CardType type) {
        try {
            return server.getDeck(gameID, type);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Slots
     * @param type a CardType indicating which Slots to consider [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot to return [1, 2]
     * @return a copy of the Card inside that Slot
     */
    @Override
    public Card getSlotCard(CardType type, int slot) {
        try {
            return server.getSlotCard(gameID, type, slot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's Secret Objective
     * @param playerID the Player's playerID
     * @return the Player's ObjectiveCard
     */
    @Override
    public ObjectiveCard getSecretObjective(int playerID) {
        try {
            return server.getSecretObjective(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Current Turn
     * @return the playerID corresponding to the Player who has the Turn
     */
    @Override
    public int getPlayerTurn() {
        try {
            return server.getPlayerTurn(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setter Method for the Player's Starter Card.
     * The Starter Card is already inside the Model, in the temporaryStarterCard field.
     * This method tells the Server to move it from there to the Player's PlayArea in (0, 0).
     * @param playerID the Player's playerID
     */
    @Override
    public void setPlayerStarterCard(int playerID) {
        try {
            server.setPlayerStarterCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setter Method for the Player's Secret Objective.
     * The Secret Objective is already stored inside the Model and IS NOT sent in here.
     * The Player has 2 Temporary Secret Objectives: this method contains either 1 or 2, indicating
     * which one to keep.
     * @param playerID the Player's playerID
     * @param pickedCard an int representing the User's choice [1, 2]
     */
    @Override
    public void setPlayerSecretObjective(int playerID, int pickedCard) {
        try {
            server.setPlayerSecretObjective(gameID, playerID, pickedCard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the View class on which to execute actions, and sends a message to subscribe
     * to a specific Game to receive messages from
     * @param viewController the View class
     * @throws AlreadyBoundException if the Class is already connected
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException {
        if (isConnected) {
            this.viewController = viewController;
            Random random = new Random();
            id = random.nextInt(100000000);
            int port;
            // Temporarily creates a Socket with port 0 because it gets assigned a random available port
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Use that port in the RMI connection
            port = serverSocket.getLocalPort();
            // Closes the socket because it's not needed anymore
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            localRegistry = LocateRegistry.createRegistry(port);
            localRegistry.bind(String.valueOf(id), viewController);
            try {
                server.lookupClient(gameID, InetAddress.getLocalHost().getHostAddress(), port, String.valueOf(id));
            } catch (NotBoundException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Closes the RMI Connection and sends one last message to the Server
     */
    @Override
    public void disconnect() {
        try {
            server.disconnectPlayer(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for isConnected
     * @return a boolean value indicating if it's connected or not
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter Method for the Player's playerID
     * @param nickName the Player's Nickname
     * @return the Player's playerID
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        return server.getIndexOfPlayer(gameID, nickName);
    }

    /**
     * Setter Method for the Player's Token
     * @param playerID the Player's playerID
     * @param token the Token to set
     */
    @Override
    public void setPlayerToken(int playerID, Token token) {
        try {
            server.setPlayerToken(gameID, playerID, token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the number of Players inside a Game
     * @return the number of Players already in that Game
     */
    @Override
    public int getNumberOfPlayers() {
        try {
            return server.getNumberOfPlayers(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @param status the GameStatus to set
     */
    @Override
    public void setPlayerStatus(int playerID, GameStatus status) {
        try {
            server.setPlayerStatus(gameID, playerID, status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes the Player from the Game entirely
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the removal was successful or not
     */
    @Override
    public boolean kickPlayer(int playerID) {
        try {
            return server.kickPlayer(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Triggers a Turn Change
     */
    @Override
    public void nextTurn() {
        try {
            server.nextTurn(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays a Card from a certain Player in a specific set of Coordinates
     * The Card to play is defined as the index of the Card in the Player's Hand.
     * The Coordinates need to be in the Model's format
     * @param handCard the index of the Card inside the Player's Hand [1, 2, 3]
     * @param x the x Coordinate where the Card will be placed
     * @param y the y Coordinate where the Card will be placed
     */
    @Override
    public void playCard(int handCard, int x, int y) {
        try {
            server.playCard(gameID, playerID, handCard, x, y);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Flips one of the Player's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     */
    @Override
    public void flipCard(int playerID, int cardID) {
        try {
            server.flipCard(gameID, playerID, cardID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Draws a Card from one of the 2 Decks and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Deck to draw [RESOURCECARD, GOLDCARD]
     */
    @Override
    public void drawCard(int playerID, CardType type) {
        try {
            server.drawCard(gameID, playerID, type);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Grabs a Card from one of the 4 Slots and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Slots to grab [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot in particular [1, 2]
     */
    @Override
    public void grabCard(int playerID, CardType type, int slot) {
        try {
            server.grabCard(gameID, playerID, type, slot);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Methods for the Temporary Secret Objectives
     * @param playerID the Player's playerID
     * @return an ArrayList containing the Players Temporary Secret Objectives
     */
    @Override
    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int playerID) {
        try {
            return server.getTemporaryObjectiveCards(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     * @return the Player's Temporary Starter Card
     */
    @Override
    public StarterCard getTemporaryStarterCard(int playerID) {
        try {
            return server.getTemporaryStarterCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @return the Player's GameStatus
     */
    @Override
    public GameStatus getPlayerStatus(int playerID) {
        try {
            return server.getPlayerStatus(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's info.
     * Each Player's info are contained inside an HashMap, and both Keys and Values are String.
     * The HashMap are contained inside an ArrayList, so that there is an HashMap for each Player.
     * The data can be retrieved with the following Keys:
     * - Nickname: the Player's Nickname
     * - Points: the Player's points
     * - Token: a String representation of the Player's Token [RED, BLUE, GREEN, YELLOW]
     * @return the Collection of data
     */
    @Override
    public ArrayList<HashMap<String, String>> getPlayersInfo() {
        try {
            return server.getPlayersInfo(gameID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's Hand's size
     * @param playerID the Player's playerID
     * @return the number of Cards inside the Player's Hand
     */
    @Override
    public int getPlayersHandSize(int playerID) {
        try {
            return server.getPlayersHandSize(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the Player is the first
     */
    @Override
    public boolean isPlayerFirst(int playerID) {
        try {
            return server.isPlayerFirst(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Available Placements.
     * Returns an ArrayList of Coordinates, each indicating a position where a Card can be played
     * inside the Player's PlayArea
     * @param playerID the Player's playerID
     * @return an ArrayList of Coordinates
     */
    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int playerID) {
        try {
            return server.getAvailablePlacements(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return a boolean value indicating if the Card's cost requirements are satisfied and the Card can be played
     */
    @Override
    public boolean canCardBePlayed(int playerID, int cardID) {
        try {
            return server.canCardBePlayed(gameID, playerID, cardID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's Token
     * @param playerID the Player's playerID
     * @return the Player's Token
     */
    @Override
    public Token getPlayerToken(int playerID) {
        try {
            return server.getPlayerToken(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's PlayField
     * @param playerID the Player's playerID
     * @return an ArrayList containing all the Cards played by the Player
     */
    @Override
    public ArrayList<PlayableCard> getPlayersPlayfield(int playerID) {
        try {
            return server.getPlayersPlayfield(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's last played Card
     * @param playerID the Player's playerID
     * @return the Card last played by the Player
     */
    @Override
    public PlayableCard getPlayersLastPlayedCard(int playerID) {
        try {
            return server.getPlayersLastPlayedCard(gameID, playerID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the Player's Hand's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return the Card
     */
    @Override
    public PlayableCard getPlayersHandCard(int playerID, int cardID) {
        try {
            return server.getPlayersHandCard(gameID, playerID, cardID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a Player into a Game
     * @param player the Player to add
     */
    @Override
    public void addPlayer(Player player) {
        try {
            server.addPlayer(gameID, player);
            this.owner = player;
            playerID = server.getIndexOfPlayer(gameID, owner.getNickname());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Setter Method for the Game's GameStatus
     * @param status the GameStatus to set
     */
    @Override
    public void setCurrentStatus(GameStatus status) {
        try {
            server.setCurrentStatus(gameID, status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter Method for the List of Available Games that a Player can join.
     * The data is returned in an HashMap that uses String as a key and as a value:
     * all data is written in the String format, including numbers, and all keys are String as well.
     * Each HashMap contains the data of a single Game, and they are all contained in an ArrayList of
     * HashMaps, so that one HashMap for every Game can be found.
     * The data can be retrieved using the following Keys:
     * - Name: the Game's name
     * - NumberOfPlayers: the number of Players already inside it
     * - Status: a String representation of the Game's GameStatus
     * - NumberOfDisconnectedPlayers: the number of Players in the Game but disconnected,
     * used to iterate through the next elements
     * - DisconnectedPlayerX: the Disconnected Players' Nickname, so that the Client can determine if the User
     * can re-join this Game or not. NOTE: There are multiple of them, starting from DisconnectedPlayer0 all the way to
     * DisconnectedPlayerN, where N = NumberOfDisconnectedPlayers - 1.
     * @return the List of Games
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException {
        return server.getAvailableGames();
    }

    /**
     * Picks a Game and saves its gameID
     * @param index the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void pickGame(int index) throws RemoteException {
        gameID = index;
    }

    /**
     * Sends a message to the Server telling it to create a new Game.
     * Saves the newly obtained gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void createNewGame() throws RemoteException {
        gameID = server.newGame();

    }

    /**
     * Setter Method for the Game's name
     * @param name a String containing the Name to set in the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void setName(String name) throws RemoteException {
        server.setName(gameID, name);
    }

    /**
     * Sends a message in the Game's Chat
     * @param playerID the Player's playerID
     * @param message a String containing the Message content
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public void sendChatMessage(int playerID, String message) throws RemoteException {
        server.sendMessage(gameID, playerID, message);
    }

    /**
     * Getter Method for the Game's Chat
     * @return an ArrayList of Message, containing the whole Chat
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ArrayList<ChatMessage> getFullChat() throws RemoteException {
        return server.getChat(gameID);
    }

    /**
     * Checks if a Nickname is available
     * @param nickname a String containing the Nickname
     * @return a boolean value indicating if the Nickname is available or not
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public boolean checkNickName(String nickname) throws RemoteException {
        return server.checkNickName(nickname);
    }

    /**
     * Blocks a Nickname, so that nobody else can use it
     * @param nickname a String containing the Nickname
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public void blockNickName(String nickname) throws RemoteException {
        server.blockNickName(nickname);
    }

    /**
     * Getter Method for the gameID
     * @return the gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public int getGameID() throws RemoteException {
        return gameID;
    }

    /**
     * Sets the Player as NOT DISCONNECTED, allowing it to play again
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void rejoinGame(int playerID) throws RemoteException {
        this.playerID = playerID;
        server.rejoinGame(gameID, playerID);
    }

    /**
     * Getter Method for the Common Objectives
     * @param cardID an int indicating which Card [1, 2]
     * @return the Objective Card
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ObjectiveCard getCommonObjective(int cardID) throws RemoteException {
        return server.getCommonObjective(gameID, cardID);
    }
}
