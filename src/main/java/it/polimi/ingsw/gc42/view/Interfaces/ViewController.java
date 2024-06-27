package it.polimi.ingsw.gc42.view.Interfaces;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ViewController extends Serializable {
    /**
     * Getter Method for the Client's Owner
     * @return the playerID associated to this Client
     */
    int getOwner();

    /**
     * Tells the Client to show the Secret Objection Selection Dialog/Menu
     */
    void showSecretObjectivesSelectionDialog();

    /**
     * Tells the Client to show the Starter Card Selection Dialog/Menu
     */
    void showStarterCardSelectionDialog();

    /**
     * Tells the Client to show the Token Selection Dialog/Menu
     */
    void showTokenSelectionDialog();

    /**
     * @return the User's Nickname
     */
    String getPlayerNickname();

    /**
     * @return the Network Controller used for communication
     */
    NetworkController getNetworkController();

    /**
     * Blocks inputs from the User
     */
    void blockInput();

    /**
     * Unlocks inputs from the User
     */
    void unlockInput();

    /**
     * Tells the Client to show the Decks and Slot Cards, asking the Player to draw or grab one
     */
    void askToDrawOrGrab();

    /**
     * Tells the Client that the Game is starting and it's time to build the UI
     */
    void notifyGameIsStarting();

    /**
     * Tells the Client that one of the Deck has changed, triggering a UI update
     * @param type the Deck's CardType
     */
    void notifyDeckChanged(CardType type);

    /**
     * Tells the Client that one of the Slot Cards has changed, triggering a UI update
     * @param type the Card's CardType
     * @param slot an int indicating which Slot [1, 2]
     */
    void notifySlotCardChanged(CardType type, int slot);

    /**
     * Tells the Client that one Player's Token has changed, triggering a UI update
     */
    void notifyPlayersPointsChanged(Token token, int newPoints);

    /**
     * Tells the Client that the number of Players has changed, triggering a UI update
     */
    void notifyNumberOfPlayersChanged();

    /**
     * Tells the Client that one Player's Token has changed, triggering a UI update
     * @param playerID the Player's playerID
     */
    void notifyPlayersTokenChanged(int playerID);

    /**
     * Tells the Client that one Player's PlayArea has changed, triggering a UI update
     * @param playerID the Player's playerID
     */
    void notifyPlayersPlayAreaChanged(int playerID);

    /**
     * Tells the Client that one Player's Hand has changed, triggering a UI update
     * @param playerID the Player's playerID
     */
    void notifyPlayersHandChanged(int playerID);

    /**
     * Tells the Client that one Player's Hand's Card was flipped, triggering a UI update if needed
     * @param playedID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     */
    void notifyHandCardWasFlipped(int playedID, int cardID);

    /**
     * Tells the Client that one Player's Secret Objective has changed, triggering a UI update if needed
     * @param playerID the Player's playerID
     */
    void notifyPlayersObjectiveChanged(int playerID);

    /**
     * Tells the Client that the Common Objectives have changed, triggering a UI update
     */
    void notifyCommonObjectivesChanged();

    /**
     * Tells the Client that the Current Turn has changed, enabling or disabling the input handling
     */
    void notifyTurnChanged();

    /**
     * Shows a Dialog saying "Waiting for Server"
     */
    void showWaitingForServerDialog();

    /**
     * Tells the Client that the Game is starting
     * @param numberOfPlayers the number of Players inside the Game
     */
    void getReady(int numberOfPlayers);

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
     */
    void notifyNewMessage(ChatMessage message);
}
