package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientController extends UnicastRemoteObject implements RemoteViewController{
    private final ViewController viewController;

    public ClientController(ViewController viewController) throws RemoteException {
        this.viewController = viewController;
    }

    @Override
    public void showSecretObjectivesSelectionDialog() throws RemoteException {
        viewController.showSecretObjectivesSelectionDialog();
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
    public Player getOwner() throws RemoteException {
        return viewController.getOwner();
    }

    @Override
    public void askToDrawOrGrab() throws RemoteException {
        viewController.askToDrawOrGrab();
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
    public void notifyPlayersPointsChanged() throws RemoteException {
        viewController.notifyPlayersPointsChanged();
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
    public void notifyPlayersObjectiveChanged(int playerID) throws RemoteException {
        viewController.notifyPlayersObjectiveChanged(playerID);
    }

    @Override
    public void notifyCommonObjectivesChanged() throws RemoteException {
        viewController.notifyCommonObjectivesChanged();
    }

    @Override
    public void getReady() throws RemoteException {
        viewController.getReady();
    }
}
