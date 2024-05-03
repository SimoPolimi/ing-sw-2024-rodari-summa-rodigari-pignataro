package it.polimi.ingsw.gc42.controller.network;

import it.polimi.ingsw.gc42.controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameCollection implements RemoteCollection, Serializable {
    private final ArrayList<GameController> gameControllers = new ArrayList<>();

    public GameCollection() throws RemoteException {}

    public GameCollection(ArrayList<GameController> gameControllers) throws RemoteException{
        this.gameControllers.addAll(gameControllers);
    }

    public void add(GameController gameController) throws RemoteException {
        gameControllers.add(gameController);
    }

    public int size() throws RemoteException {
        return gameControllers.size();
    }

    public GameController get(int index) throws RemoteException {
        return gameControllers.get(index);
    }

    @Override
    public RemoteCollection getCopy() throws RemoteException {
        return new GameCollection(gameControllers);
    }

    @Override
    public GameController createNewGame() throws RemoteException {
        add(new GameController(null));
        // Returns the current size: it will become the index of the new GameController, once created.
        return gameControllers.getLast();
    }

    public void remove(GameController gameController) throws RemoteException {
        gameControllers.remove(gameController);
    }

    public void empty() {
        gameControllers.clear();
    }
}
