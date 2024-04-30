package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface NetworkController {
    void connect() throws RemoteException, NotBoundException;
    void disconnect();
    boolean isConnected();

    void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException;

    Game getGame();

    boolean kickPlayer(Player player);

    void nextTurn();

     void playCard(int handCard, int x, int y);

    void flipCard(Card card);

    void drawCard(Player player, CardType type);

    void grabCard(Player player, CardType type, int slot);

    void drawSecretObjectives();

    void addPlayer(Player player);

    void setCurrentStatus(GameStatus status);

    RemoteCollection getAvailableGames() throws RemoteException;

    void pickGame(int index) throws RemoteException;

    void getNewGameController() throws RemoteException;

    int getIndex() throws RemoteException;

    RemoteServer getServer() throws RemoteException;

    void removeListener(int playerID, int cardID, Listener listener) throws RemoteException;

    void setName(String name) throws RemoteException;

    Player getPlayer(int index);

    int getIndexOfPlayer(String nickName) throws RemoteException;

    int getNumberOfPlayers();

    void startGame();

    void setPlayerStatus(int playerID, GameStatus status);

    void setPlayerToken(int playerID, Token token);

    void setPlayerSecretObjective(int playerID, int pickedCard);

    void setPlayerStarterCard(int playerID);

    void flipStarterCard(int playerID);

}
