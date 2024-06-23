package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.view.GameTerminal;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.RemoteException;

/**
 * This class acts as a Container for a GameTerminal, a specific type of ViewController.
 * This is needed because GameTerminal can't handle methods in real time, but needs to receive them in a queue,
 * so that they can be handled appropriately inside.
 * This class translates all the methods in Runnables, and adds them in the GameTerminal's queue.
 */
public class QueuedClientController extends ClientController {
    private GameTerminal terminal;

    /**
     * Constructor Method
     * @param viewController the View class
     * @throws RemoteException in case of a Network Communication Error
     */
    public QueuedClientController(ViewController viewController) throws RemoteException {
        super(viewController);
        terminal = (GameTerminal) viewController;
    }

    /**
     * Tells the Client to show the Secret Objection Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showSecretObjectivesSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showSecretObjectivesSelectionDialog());
    }

    /**
     * Tells the Client to show the Starter Card Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showStarterCardSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showStarterCardSelectionDialog());
    }

    /**
     * Tells the Client to show the Token Selection Dialog/Menu
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void showTokenSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showTokenSelectionDialog());
    }

    /**
     * Tells the Client to show the Decks and Slot Cards, asking the Player to draw or grab one
     * @param playerID the Player's playerID that must draw/grab
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void askToDrawOrGrab(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.askToDrawOrGrab());
    }

    /**
     * Tells the Client that the Game is starting and it's time to build the UI
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyGameIsStarting() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyGameIsStarting());
    }

    /**
     * Tells the Client that one of the Deck has changed, triggering a UI update
     * @param type the Deck's CardType
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyDeckChanged(CardType type) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyDeckChanged(type));
    }

    /**
     * Tells the Client that one of the Slot Cards has changed, triggering a UI update
     * @param type the Card's CardType
     * @param slot an int indicating which Slot [1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifySlotCardChanged(type, slot));
    }

    /**
     * Tells the Client that one Player's points have changed, triggering a UI update
     * @param token the Player's Token
     * @param newPoints the Player's updated Points
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersPointsChanged(token, newPoints));
    }

    /**
     * Tells the Client that the number of Players has changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyNumberOfPlayersChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyNumberOfPlayersChanged());
    }

    /**
     * Tells the Client that one Player's Token has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersTokenChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersTokenChanged(playerID));
    }

    /**
     * Tells the Client that one Player's PlayArea has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersPlayAreaChanged(playerID));
    }

    /**
     * Tells the Client that one Player's Hand has changed, triggering a UI update
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersHandChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersHandChanged(playerID));
    }

    /**
     * Tells the Client that one Player's Hand's Card was flipped, triggering a UI update if needed
     * @param playedID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyHandCardWasFlipped(playedID, cardID));
    }

    /**
     * Tells the Client that one Player's Secret Objective has changed, triggering a UI update if needed
     * @param playerID the Player's playerID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersObjectiveChanged(playerID));
    }

    /**
     * Tells the Client that the Common Objectives have changed, triggering a UI update
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyCommonObjectivesChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyCommonObjectivesChanged());
    }

    /**
     * Tells the Client that the Current Turn has changed, enabling or disabling the input handling
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void notifyTurnChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyTurnChanged());
    }

    /**
     * Tells the Client that the Game is starting
     * @param numberOfPlayers the number of Players inside the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void getReady(int numberOfPlayers) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.getReady(numberOfPlayers));
    }
}
