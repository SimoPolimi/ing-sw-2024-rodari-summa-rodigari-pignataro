package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.ClientController;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface NetworkController {
    /**
     * Connects to the Server
     * @throws RemoteException in case of a Network Communication Error
     * @throws NotBoundException in case of a Registry Error
     */
    void connect() throws RemoteException, NotBoundException, IOException;

    /**
     * Closes the Network Connection and sends one last message to the Server
     */
    void disconnect();

    /**
     * Sets the Player as NOT DISCONNECTED, allowing it to play again
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void rejoinGame(int playerID) throws RemoteException;

    /**
     * Getter Method for isConnected
     * @return a boolean value indicating if it's connected or not
     */
    boolean isConnected();

    /**
     * Saves the View class on which to execute actions, and sends a message to subscribe
     * to a specific Game to receive messages from
     * @param viewController the View class
     * @throws AlreadyBoundException if the Class is already connected
     * @throws RemoteException in case of a Network Communication Error
     */
    void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException;

    /**
     * Removes the Player from the Game entirely
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the removal was successful or not
     */
    boolean kickPlayer(int playerID);

    /**
     * Triggers a Turn Change
     */
    void nextTurn();

    /**
     * Plays a Card from a certain Player in a specific set of Coordinates
     * The Card to play is defined as the index of the Card in the Player's Hand.
     * The Coordinates need to be in the Model's format
     * @param handCard the index of the Card inside the Player's Hand [1, 2, 3]
     * @param x the x Coordinate where the Card will be placed
     * @param y the y Coordinate where the Card will be placed
     */
     void playCard(int handCard, int x, int y);

    /**
     * Flips one of the Player's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     */
    void flipCard(int playerID, int cardID);

    /**
     * Draws a Card from one of the 2 Decks and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Deck to draw [RESOURCECARD, GOLDCARD]
     */
    void drawCard(int playerID, CardType type);

    /**
     * Grabs a Card from one of the 4 Slots and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Slots to grab [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot in particular [1, 2]
     */
    void grabCard(int playerID, CardType type, int slot);

    /**
     * Adds a Player into a Game
     * @param player the Player to add
     */
    void addPlayer(Player player);

    /**
     * Setter Method for the Game's GameStatus
     * @param status the GameStatus to set
     */
    void setCurrentStatus(GameStatus status);

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
    ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException;

    /**
     * Picks a Game and saves its gameID
     * @param index the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    void pickGame(int index) throws RemoteException;

    /**
     * Sends a message to the Server telling it to create a new Game.
     * Saves the newly obtained gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    void createNewGame() throws RemoteException;

    /**
     * Getter Method for the gameID
     * @return the gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    int getGameID() throws RemoteException;

    /**
     * Setter Method for the Game's name
     * @param name a String containing the Name to set in the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    void setName(String name) throws RemoteException;

    /**
     * Getter Method for the Player's playerID
     * @param nickName the Player's Nickname
     * @return the Player's playerID
     * @throws RemoteException in case of a Network Connection Error
     */
    int getIndexOfPlayer(String nickName) throws RemoteException;

    /**
     * Getter Method for the number of Players inside a Game
     * @return the number of Players already in that Game
     */
    int getNumberOfPlayers();

    /**
     * Setter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @param status the GameStatus to set
     */
    void setPlayerStatus(int playerID, GameStatus status);

    /**
     * Setter Method for the Player's Token
     * @param playerID the Player's playerID
     * @param token the Token to set
     */
    void setPlayerToken(int playerID, Token token);

    /**
     * Setter Method for the Player's Secret Objective.
     * The Secret Objective is already stored inside the Model and IS NOT sent in here.
     * The Player has 2 Temporary Secret Objectives: this method contains either 1 or 2, indicating
     * which one to keep.
     * @param playerID the Player's playerID
     * @param pickedCard an int representing the User's choice [1, 2]
     */
    void setPlayerSecretObjective(int playerID, int pickedCard);

    /**
     * Setter Method for the Player's Starter Card.
     * The Starter Card is already inside the Model, in the temporaryStarterCard field.
     * This method tells the Server to move it from there to the Player's PlayArea in (0, 0).
     * @param playerID the Player's playerID
     */
    void setPlayerStarterCard(int playerID);

    /**
     * Flips the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     */
    void flipStarterCard(int playerID);

    /**
     * Getter Method for the Decks.
     * @param type a CardType indicating which Deck to return
     * @return an ArrayList of Card, containing all the Cards inside the Deck, in the same order
     */
    ArrayList<Card> getDeck(CardType type);

    /**
     * Getter Method for the Slots
     * @param type a CardType indicating which Slots to consider [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot to return [1, 2]
     * @return a copy of the Card inside that Slot
     */
    Card getSlotCard(CardType type, int slot);

    /**
     * Getter Method for the Player's Secret Objective
     * @param playerID the Player's playerID
     * @return the Player's ObjectiveCard
     */
    ObjectiveCard getSecretObjective(int playerID);

    /**
     * Getter Method for the Current Turn
     * @return the playerID corresponding to the Player who has the Turn
     */
    int getPlayerTurn();

    /**
     * Getter Methods for the Temporary Secret Objectives
     * @param playerID the Player's playerID
     * @return an ArrayList containing the Players Temporary Secret Objectives
     */
    ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int playerID);

    /**
     * Getter Method for the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     * @return the Player's Temporary Starter Card
     */
    StarterCard getTemporaryStarterCard(int playerID);

    /**
     * Getter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @return the Player's GameStatus
     */
    GameStatus getPlayerStatus(int playerID);

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
    ArrayList<HashMap<String, String>> getPlayersInfo();

    /**
     * Getter Method for the Player's Hand's size
     * @param playerID the Player's playerID
     * @return the number of Cards inside the Player's Hand
     */
    int getPlayersHandSize(int playerID);

    /**
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the Player is the first
     */
    boolean isPlayerFirst(int playerID);

    /**
     * Getter Method for the Available Placements.
     * Returns an ArrayList of Coordinates, each indicating a position where a Card can be played
     * inside the Player's PlayArea
     * @param playerID the Player's playerID
     * @return an ArrayList of Coordinates
     */
    ArrayList<Coordinates> getAvailablePlacements(int playerID);

    /**
     *
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return a boolean value indicating if the Card's cost requirements are satisfied and the Card can be played
     */
    boolean canCardBePlayed(int playerID, int cardID);

    /**
     * Getter Method for the Player's Token
     * @param playerID the Player's playerID
     * @return the Player's Token
     */
    Token getPlayerToken(int playerID);

    /**
     * Getter Method for the Player's last played Card
     * @param playerID the Player's playerID
     * @return the Card last played by the Player
     */
    PlayableCard getPlayersLastPlayedCard(int playerID);

    /**
     * Getter Method for the Player's Hand's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return the Card
     */
    PlayableCard getPlayersHandCard(int playerID, int cardID);

    /**
     * Getter Method for the Player's PlayField
     * @param playerID the Player's playerID
     * @return an ArrayList containing all the Cards played by the Player
     */
    ArrayList<PlayableCard> getPlayersPlayfield(int playerID);

    /**
     * Sends a message in the Game's Chat
     * @param playerID the Player's playerID
     * @param message a String containing the Message content
     * @throws RemoteException in case of a Network Connection Error
     */
    void sendChatMessage(int playerID, String message) throws RemoteException;

    /**
     * Getter Method for the Game's Chat
     * @return an ArrayList of Message, containing the whole Chat
     * @throws RemoteException in case of a Network Connection Error
     */
    ArrayList<ChatMessage> getFullChat() throws RemoteException;

    /**
     * Checks if a Nickname is available
     * @param nickname a String containing the Nickname
     * @return a boolean value indicating if the Nickname is available or not
     * @throws RemoteException in case of a Network Connection Error
     */
    boolean checkNickName(String nickname) throws RemoteException;

    /**
     * Blocks a Nickname, so that nobody else can use it
     * @param nickname a String containing the Nickname
     * @throws RemoteException in case of a Network Connection Error
     */
    void blockNickName(String nickname) throws RemoteException;

    /**
     * Getter Method for the Common Objectives
     * @param cardID an int indicating which Card [1, 2]
     * @return the Objective Card
     * @throws RemoteException in case of a Network Connection Error
     */
    ObjectiveCard getCommonObjective(int cardID) throws RemoteException;
}