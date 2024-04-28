package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface NetworkController {
    void connect() throws RemoteException, NotBoundException;
    void disconnect();
    boolean isConnected();

    Game getGame();

    boolean kickPlayer(Player player);

    void nextTurn();

     void playCard(PlayableCard card, int x, int y);

    void flipCard(Card card);

    void drawCard(Player player, CardType type);

    void grabCard(Player player, CardType type, int slot);

    void drawSecretObjectives();

    void addView();

    void addPlayer(Player player);

    void setCurrentStatus(GameStatus status);

    RemoteCollection getAvailableGames() throws RemoteException;

    void pickGame(int index) throws RemoteException;

    void getNewGameController() throws RemoteException;

    int getIndex() throws RemoteException;

    RemoteServer getServer() throws RemoteException;

    StarterCard drawStarterCard() throws RemoteException;

    void setListener(int gameID, Listener listener) throws RemoteException;


}
