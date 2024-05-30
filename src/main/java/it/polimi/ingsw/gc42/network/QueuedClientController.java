package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.view.GameTerminal;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public class QueuedClientController extends ClientController {
    private GameTerminal terminal;

    public QueuedClientController(ViewController viewController) throws RemoteException {
        super(viewController);
        terminal = (GameTerminal) viewController;
    }

    @Override
    public void showSecretObjectivesSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showSecretObjectivesSelectionDialog());
    }

    @Override
    public void showStarterCardSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showStarterCardSelectionDialog());
    }

    @Override
    public void showTokenSelectionDialog() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.showTokenSelectionDialog());
    }

    @Override
    public void askToDrawOrGrab(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.askToDrawOrGrab());
    }

    @Override
    public void notifyGameIsStarting() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyGameIsStarting());
    }

    @Override
    public void notifyDeckChanged(CardType type) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyDeckChanged(type));
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifySlotCardChanged(type, slot));
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersPointsChanged(token, newPoints));
    }

    @Override
    public void notifyNumberOfPlayersChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyNumberOfPlayersChanged());
    }

    @Override
    public void notifyPlayersTokenChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersTokenChanged(playerID));
    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersPlayAreaChanged(playerID));
    }

    @Override
    public void notifyPlayersHandChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersHandChanged(playerID));
    }

    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyHandCardWasFlipped(playedID, cardID));
    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyPlayersObjectiveChanged(playerID));
    }

    @Override
    public void notifyCommonObjectivesChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyCommonObjectivesChanged());
    }

    @Override
    public void notifyTurnChanged() throws RemoteException {
        terminal.addToActionQueue(() -> terminal.notifyTurnChanged());
    }

    @Override
    public void getReady(int numberOfPlayers) throws RemoteException {
        terminal.addToActionQueue(() -> terminal.getReady(numberOfPlayers));
    }
}
