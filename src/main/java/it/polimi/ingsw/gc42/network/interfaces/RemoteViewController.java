package it.polimi.ingsw.gc42.network.interfaces;


import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Token;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Remote Interface represents the Client as seen from the Controller.
 * It contains all the methods necessary to give command to a Client, and all the
 * "notify" methods to notify an event that happened in the Model, triggering a UI update
 */
public interface RemoteViewController extends Remote {
    /**
     * Getter Method for the Client's Owner
     * @return the playerID associated to this Client
     * @throws RemoteException in case of a Network Communication Error
     */
    int getOwner() throws RemoteException;

    /**
     * Tells the Client to show the Secret Objection Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    void showSecretObjectivesSelectionDialog() throws RemoteException;

    /**
     * Tells the Client to show the Starter Card Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    void showStarterCardSelectionDialog() throws RemoteException;

    /**
     * Tells the Client to show the Token Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    void showTokenSelectionDialog() throws RemoteException;

    /**
     * Tells the Client to show the Decks and Slot Cards, asking the Player to draw or grab one
     * @param playerID the Player's playerID that must draw/grab
     * @throws RemoteException in case of a Network Communication Error
     */
    void askToDrawOrGrab(int playerID) throws RemoteException;

    /**
     * Tells the Client that the Game is starting and it's time to build the UI
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyGameIsStarting() throws RemoteException;

    /**
     * Tells the Client that one of the Deck has changed, triggering a UI update
     * @param type the Deck's CardType
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyDeckChanged(CardType type) throws RemoteException;

    /**
     * Tells the Client that one of the Slot Cards has changed, triggering a UI update
     * @param type the Card's CardType
     * @param slot an int indicating which Slot [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifySlotCardChanged(CardType type, int slot) throws RemoteException;

    /**
     * Tells the Client that one Player's points have changed, triggering a UI update
     * @param token the Player's Token
     * @param newPoints the Player's updated Points
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException;

    /**
     * Tells the Client that the number of Players has changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyNumberOfPlayersChanged() throws RemoteException;

    /**
     * Tells the Client that one Player's Token has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyPlayersTokenChanged(int playerID) throws RemoteException;

    /**
     * Tells the Client that one Player's PlayArea has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException;

    /**
     * Tells the Client that one Player's Hand has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyPlayersHandChanged(int playerID) throws RemoteException;

    /**
     * Tells the Client that one Player's Hand's Card was flipped, triggering a UI update if needed
     * @param playedID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException;

    /**
     * Tells the Client that one Player's Secret Objective has changed, triggering a UI update if needed
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyPlayersObjectiveChanged(int playerID) throws RemoteException;

    /**
     * Tells the Client that the Common Objectives have changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyCommonObjectivesChanged() throws RemoteException;

    /**
     * Tells the Client that the Current Turn has changed, enabling or disabling the input handling
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyTurnChanged() throws RemoteException;

    /**
     * Tells the Client that the Game is starting
     * @param numberOfPlayers the number of Players inside the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    void getReady(int numberOfPlayers) throws RemoteException;

    /**
     * Tells the Client that the Last Turn has started
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyLastTurn() throws RemoteException;

    /**
     * Tells the Client that the Game has ended and it's time to show the Points.
     * Provides all the data to show inside an ArrayList, where each entry (one for each Player) contains
     * an HashMap of Strings for both Keys and Values.
     * Data can be retrieved using the following Keys:
     * - Nickname: the Player's Nickname
     * - Token: the Player's Token
     * - InitialPoints: the Player's Points before adding the Objective's ones
     * - SecretObjectivePoints: the Points acquired from the Secret Objective
     * - CommonObjective1Points: the Points acquired from the Common Objective 1
     * - CommonObjective2Points: the Points acquired from the Common Objective 2
     * - TotalPoints: the total Points at the end of the Game
     * - isWinner: a boolean value indicating if this Player is one of the winners
     * @param points the ArrayList containing the data
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException;

    /**
     * Tells the Client that a new Message has been sent, provides it and triggers a UI update
     * @param message the new Message
     * @throws RemoteException in case of a Network Communication Error
     */
    void notifyNewMessage(ChatMessage message) throws RemoteException;
}
