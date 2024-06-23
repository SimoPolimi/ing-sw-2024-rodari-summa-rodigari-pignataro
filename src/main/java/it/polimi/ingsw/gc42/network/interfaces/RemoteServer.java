package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface describes the Server's behavior
 * and is used to operate on the Server from a Client through a network connection.
 * It contains all the necessary methods to execute actions and retrieve information.
 */
public interface RemoteServer extends Remote {

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
     * Sets the Player as DISCONNECTED, allowing it to be skipped by the Turn System
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void disconnectPlayer(int gameID, int playerID) throws RemoteException;

    /**
     * Sets the Player as NOT DISCONNECTED, allowing it to play again
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void rejoinGame(int gameID, int playerID) throws RemoteException;

    /**
     * Removes the Player from the Game entirely
     * @param gameID the Game's gameID
     * @param player the Player's playerID
     * @return a boolean value indicating if the removal was successful or not
     * @throws RemoteException in case of a Network Communication Error
     */
    boolean kickPlayer(int gameID, Player player) throws RemoteException;

    /**
     * Triggers a Turn Change
     * @param gameID the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    void nextTurn(int gameID) throws RemoteException;

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
    void playCard(int gameID, int playerID,  int cardID, int x, int y) throws RemoteException;

    /**
     * Flips one of the Player's Cards
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    void flipCard(int gameID, int playerID, int cardID) throws RemoteException;

    /**
     * Draws a Card from one of the 2 Decks and places it inside the Player's Hand
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Deck to draw [RESOURCECARD, GOLDCARD]
     * @throws RemoteException in case of a Network Communication Error
     */
    void drawCard(int gameID, int playerID, CardType type) throws RemoteException;

    /**
     * Grabs a Card from one of the 4 Slots and places it inside the Player's Hand
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Slots to grab [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot in particular [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    void grabCard(int gameID, int playerID, CardType type, int slot) throws RemoteException;

    /**
     * Getter Method for Game
     * @param gameID the Game's gameID
     * @return the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    Game getGame(int gameID) throws RemoteException;

    /**
     * Subscribes the Client inside a Game, so that it will automatically be notified when any event happens
     * @param gameID the Game's GameID
     * @param viewController the Client's RemoteViewController
     * @throws RemoteException in case of a Network Communication Error
     */
    void addView(int gameID, RemoteViewController viewController) throws RemoteException;

    /**
     * Adds a Player into a Game
     * @param gameID the Game's gameID
     * @param player the Player to add
     * @throws RemoteException in case of a Network Communication Error
     */
    void addPlayer(int gameID, Player player) throws RemoteException;

    /**
     * Setter Method for the Game's GameStatus
     * @param gameID the Game's gameID
     * @param status the GameStatus to set
     * @throws RemoteException in case of a Network Communication Error
     */
    void setCurrentStatus(int gameID, GameStatus status) throws RemoteException;

    /**
     * Getter Method for the Game's name
     * @param gameID the Game's gameID
     * @return a String containing the Game's name
     * @throws RemoteException in case of a Network Communication Error
     */
    String getName(int gameID) throws RemoteException;

    /**
     * Setter Method for the Game's name
     * @param gameID the Game's gameID
     * @param name a String containing the Name to set in the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    void setName(int gameID, String name) throws RemoteException;

    /**
     * Getter Method for the number of Players inside a Game
     * @param gameID the Game's gameID
     * @return the number of Players already in that Game
     * @throws RemoteException in case of a Network Communication Error
     */
    int getNumberOfPlayers(int gameID) throws RemoteException;

    /**
     * Getter Method for a Player
     * @param gameID the Game's gameID
     * @param index the Player's playerID
     * @return the Player
     * @throws RemoteException in case of a Network Communication Error
     */
    Player getPlayer(int gameID, int index) throws RemoteException;

    /**
     * Sends a message to the Server telling it to create a new Game
     * @return the new Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    int newGame() throws RemoteException;

    /**
     * Sends a message to the Server telling it to start a specific Game
     * @param gameID the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    void startGame(int gameID) throws RemoteException;

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
    void lookupClient(int gameID, String ip, int port, String clientID) throws RemoteException, NotBoundException;

    /**
     * Setter Method for the Player's GameStatus
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param status the GameStatus to set
     * @throws RemoteException in case of a Network Communication Error
     */
    void setPlayerStatus(int gameID, int playerID, GameStatus status) throws RemoteException;

    /**
     * Setter Method for the Player's Token
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param token the Token to set
     * @throws RemoteException in case of a Network Communication Error
     */
    void setPlayerToken(int gameID, int playerID, Token token) throws RemoteException;

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
    void setPlayerSecretObjective(int gameID, int playerID, int pickedCard) throws RemoteException;

    /**
     * Setter Method for the Player's Starter Card.
     * The Starter Card is already inside the Model, in the temporaryStarterCard field.
     * This method tells the Server to move it from there to the Player's PlayArea in (0, 0).
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void setPlayerStarterCard(int gameID, int playerID) throws RemoteException;

    /**
     * Flips the Player's Temporary Starter Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void flipStarterCard(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Decks.
     * @param gameID the Game's gameID
     * @param type a CardType indicating which Deck to return
     * @return an ArrayList of Card, containing all the Cards inside the Deck, in the same order
     * @throws RemoteException in case of a Network Communication Error
     */
    ArrayList<Card> getDeck(int gameID, CardType type) throws RemoteException;

    /**
     * Getter Method for the Slots
     * @param gameID the Game's gameID
     * @param type a CardType indicating which Slots to consider [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot to return [1, 2]
     * @return a copy of the Card inside that Slot
     * @throws RemoteException in case of a Network Communication Error
     */
    Card getSlotCard(int gameID, CardType type, int slot) throws RemoteException;

    /**
     * Getter Method for the Player's Secret Objective
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's ObjectiveCard
     * @throws RemoteException in case of a Network Communication Error
     */
    ObjectiveCard getSecretObjective(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Common Objectives
     * @param gameID the Game's gameID
     * @param slot an int indicating which Slot [1, 2]
     * @return the ObjectiveCard
     * @throws RemoteException in case of a Network Communication Error
     */
    ObjectiveCard getCommonObjective(int gameID, int slot) throws RemoteException;

    /**
     * Getter Method for the Current Turn
     * @param gameID the Game's gameID
     * @return the playerID corresponding to the Player who has the Turn
     * @throws RemoteException in case of a Network Communication Error
     */
    int getPlayerTurn(int gameID) throws RemoteException;

    /**
     * Getter Methods for the Temporary Secret Objectives
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList containing the Players Temporary Secret Objectives
     * @throws RemoteException in case of a Network Communication Error
     */
    ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Player's Temporary Starter Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's Temporary Starter Card
     * @throws RemoteException in case of a Network Communication Error
     */
    StarterCard getTemporaryStarterCard(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Player's GameStatus
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's GameStatus
     * @throws RemoteException in case of a Network Communication Error
     */
    GameStatus getPlayerStatus(int gameID, int playerID) throws RemoteException;

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
    ArrayList<HashMap<String, String>> getPlayersInfo(int gameID) throws RemoteException;

    /**
     * Getter Method for the Player's Hand's size
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the number of Cards inside the Player's Hand
     * @throws RemoteException in case of a Network Connection Error
     */
    int getPlayersHandSize(int gameID, int playerID) throws RemoteException;

    /**
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the Player is the first
     * @throws RemoteException in case of a Network Connection Error
     */
    boolean isPlayerFirst(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Available Placements.
     * Returns an ArrayList of Coordinates, each indicating a position where a Card can be played
     * inside the Player's PlayArea
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList of Coordinates
     * @throws RemoteException in case of a Network Connection Error
     */
    ArrayList<Coordinates> getAvailablePlacements(int gameID, int playerID) throws RemoteException;

    /**
     *
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return a boolean value indicating if the Card's cost requirements are satisfied and the Card can be played
     * @throws RemoteException in case of a Network Connection Error
     */
    boolean canCardBePlayed(int gameID, int playerID, int cardID) throws RemoteException;

    /**
     * Getter Method for the Player's Token
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Player's Token
     * @throws RemoteException in case of a Network Connection Error
     */
    Token getPlayerToken(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Player's last played Card
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return the Card last played by the Player
     * @throws RemoteException in case of a Network Connection Error
     */
    PlayableCard getPlayersLastPlayedCard(int gameID, int playerID) throws RemoteException;

    /**
     * Getter Method for the Player's Hand's Cards
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return the Card
     * @throws RemoteException in case of a Network Connection Error
     */
    PlayableCard getPlayersHandCard(int gameID, int playerID, int cardID) throws RemoteException;

    /**
     * Getter Method for the Player's playerID
     * @param gameID the Game's gameID
     * @param nickname the Player's Nickname
     * @return the Player's playerID
     * @throws RemoteException in case of a Network Connection Error
     */
    int getIndexOfPlayer(int gameID, String nickname) throws RemoteException;

    /**
     * Getter Method for the Player's PlayField
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @return an ArrayList containing all the Cards played by the Player
     * @throws RemoteException in case of a Network Connection Error
     */
    ArrayList<PlayableCard> getPlayersPlayfield(int gameID, int playerID) throws RemoteException;

    /**
     * Sends a message in the Game's Chat
     * @param gameID the Game's gameID
     * @param playerID the Player's playerID
     * @param message a String containing the Message content
     * @throws RemoteException in case of a Network Connection Error
     */
    void sendMessage(int gameID, int playerID, String message) throws RemoteException;

    /**
     * Getter Method for the Game's Chat
     * @param gameID the Game's gameID
     * @return an ArrayList of Message, containing the whole Chat
     * @throws RemoteException in case of a Network Connection Error
     */
    ArrayList<ChatMessage> getChat(int gameID) throws RemoteException;

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
}
