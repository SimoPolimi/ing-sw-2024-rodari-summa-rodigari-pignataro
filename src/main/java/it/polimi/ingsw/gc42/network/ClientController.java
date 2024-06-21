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

public class ClientController extends UnicastRemoteObject implements RemoteViewController {
    private final ViewController viewController;

    public ClientController(ViewController viewController) throws RemoteException {
        this.viewController = viewController;
    }

    @Override
    public int getOwner() throws RemoteException {
        return viewController.getOwner();
    }

    @Override
    public void showSecretObjectivesSelectionDialog() throws RemoteException {
        viewController.showSecretObjectivesSelectionDialog();
    }

    @Override
    public void notifyNewMessage(ChatMessage message) throws RemoteException {
        viewController.notifyNewMessage(message);
    }

    @Override
    public void showStarterCardSelectionDialog() throws RemoteException {
        viewController.showStarterCardSelectionDialog();
    }

    @Override
    public void showTokenSelectionDialog() throws RemoteException {
        viewController.showTokenSelectionDialog();
    }

    @Override
    public void askToDrawOrGrab(int playerID) throws RemoteException {
        if (playerID == viewController.getOwner()) {
            viewController.askToDrawOrGrab();
        }
    }

    @Override
    public void notifyGameIsStarting() throws RemoteException {
        viewController.notifyGameIsStarting();
    }

    @Override
    public void notifyDeckChanged(CardType type) throws RemoteException {
        viewController.notifyDeckChanged(type);
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) throws RemoteException {
        viewController.notifySlotCardChanged(type, slot);
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) throws RemoteException {
        viewController.notifyPlayersPointsChanged(token, newPoints);
    }

    @Override
    public void notifyNumberOfPlayersChanged() throws RemoteException {
        viewController.notifyNumberOfPlayersChanged();
    }

    @Override
    public void notifyPlayersTokenChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersTokenChanged(playerID);
    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersPlayAreaChanged(playerID);
    }

    @Override
    public void notifyPlayersHandChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersHandChanged(playerID);
    }

    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) throws RemoteException {
        viewController.notifyHandCardWasFlipped(playedID, cardID);
    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersObjectiveChanged(playerID);
    }

    @Override
    public void notifyCommonObjectivesChanged() throws RemoteException {
        viewController.notifyCommonObjectivesChanged();
    }

    @Override
    public void notifyTurnChanged() throws RemoteException {
        viewController.notifyTurnChanged();
    }

    @Override
    public void getReady(int numberOfPlayers) throws RemoteException {
        viewController.getReady(numberOfPlayers);
    }

    @Override
    public void notifyLastTurn() throws RemoteException {
        viewController.notifyLastTurn();
    }

    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        viewController.notifyEndGame(points);
    }
}
