package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.game.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RmiController {
    private ArrayList<Player> users;
    // GameController will be the stub
    private final RemoteObject gameController = new GameController();

    public RemoteObject getGameController() {
        return gameController;
    }

    public RmiController() throws RemoteException {

    }

    private void registerUser(Player player) {
        users.add(player);
    }
}
