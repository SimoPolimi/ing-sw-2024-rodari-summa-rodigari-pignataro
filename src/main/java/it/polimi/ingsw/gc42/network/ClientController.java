package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.interfaces.RemoteViewController;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class acts as a Container for a ViewController, a View class.
 * It implements UnicastRemoteObject, and allows the View to be used as a RemoteObject in RMI.
 */
public class ClientController extends UnicastRemoteObject implements RemoteViewController {
    private final ViewController viewController;

    /**
     * Constructor Method
     * @param viewController the View class
     * @throws RemoteException in case of a Network Communication Error
     */
    public ClientController(ViewController viewController) throws RemoteException {
        this.viewController = viewController;
    }

    /**
     * Getter Method for the Client's Owner
     * @return the playerID associated to this Client
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public int getOwner() throws RemoteException {
        return viewController.getOwner();
    }

    /**
     * Tells the Client to show the Secret Objection Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showSecretObjectivesSelectionDialog() throws RemoteException {
        viewController.showSecretObjectivesSelectionDialog();
    }

    /**
     * Tells the Client that a new Message has been sent, provides it and triggers a UI update
     * @param message the new Message
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        viewController.notifyNewMessage(message);
    }

    /**
     * Tells the Client to show the Starter Card Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showStarterCardSelectionDialog() throws RemoteException {
        viewController.showStarterCardSelectionDialog();
    }

    /**
     * Tells the Client to show the Token Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showTokenSelectionDialog() throws RemoteException {
        viewController.showTokenSelectionDialog();
    }

    /**
     * Tells the Client to show the Decks and Slot Cards, asking the Player to draw or grab one
     * @param playerID the Player's playerID that must draw/grab
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void askToDrawOrGrab(int playerID) throws RemoteException {
        if (playerID == viewController.getOwner()) {
            viewController.askToDrawOrGrab();
        }
    }

    /**
     * Tells the Client that the Game is starting and it's time to build the UI
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyGameIsStarting() throws RemoteException {
        viewController.notifyGameIsStarting();
    }

    /**
     * Tells the Client that one of the Deck has changed, triggering a UI update
     * @param type the Deck's CardType
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyDeckChanged(CardType type) throws RemoteException {
        viewController.notifyDeckChanged(type);
    }

    /**
     * Tells the Client that one of the Slot Cards has changed, triggering a UI update
     * @param type the Card's CardType
     * @param slot an int indicating which Slot [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {
        viewController.notifySlotCardChanged(type, slot);
    }

    /**
     * Tells the Client that one Player's points have changed, triggering a UI update
     * @param token the Player's Token
     * @param newPoints the Player's updated Points
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {
        viewController.notifyPlayersPointsChanged(token, newPoints);
    }

    /**
     * Tells the Client that the number of Players has changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyNumberOfPlayersChanged() throws RemoteException {
        viewController.notifyNumberOfPlayersChanged();
    }

    /**
     * Tells the Client that one Player's Token has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersTokenChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersTokenChanged(playerID);
    }

    /**
     * Tells the Client that one Player's PlayArea has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersPlayAreaChanged(playerID);
    }

    /**
     * Tells the Client that one Player's Hand has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersHandChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersHandChanged(playerID);
    }

    /**
     * Tells the Client that one Player's Hand's Card was flipped, triggering a UI update if needed
     * @param playedID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {
        viewController.notifyHandCardWasFlipped(playedID, cardID);
    }

    /**
     * Tells the Client that one Player's Secret Objective has changed, triggering a UI update if needed
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersObjectiveChanged(playerID);
    }

    /**
     * Tells the Client that the Common Objectives have changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyCommonObjectivesChanged() throws RemoteException {
        viewController.notifyCommonObjectivesChanged();
    }

    /**
     * Tells the Client that the Current Turn has changed, enabling or disabling the input handling
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyTurnChanged() throws RemoteException {
        viewController.notifyTurnChanged();
    }

    /**
     * Tells the Client that the Game is starting
     * @param numberOfPlayers the number of Players inside the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void getReady(int numberOfPlayers) throws RemoteException {
        viewController.getReady(numberOfPlayers);
    }

    /**
     * Tells the Client that the Last Turn has started
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyLastTurn() throws RemoteException {
        viewController.notifyLastTurn();
    }

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
    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        viewController.notifyEndGame(points);
    }
}
