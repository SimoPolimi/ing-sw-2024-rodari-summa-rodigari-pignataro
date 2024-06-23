package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GameCollection implements Serializable {
    private final ArrayList<GameController> gameControllers = new ArrayList<>();
    private final ArrayList<String> blockedNicknames = new ArrayList<>();

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

    public boolean isNickNameAvailable(String nickname) throws RemoteException {
        if (!nickname.equals("Server")) {
            return !blockedNicknames.contains(nickname);
        } else return false;
    }

    public void blockNickname(String nickname) throws RemoteException {
        blockedNicknames.add(nickname);
    }

    public void unlockNickname(String nickname) throws RemoteException {
        blockedNicknames.remove(nickname);
    }

    public GameCollection getCopy() throws RemoteException {
        return new GameCollection(gameControllers);
    }

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
