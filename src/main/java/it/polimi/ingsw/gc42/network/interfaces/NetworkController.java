package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.ClientController;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface NetworkController {
    void connect() throws RemoteException, NotBoundException, IOException;
    void disconnect();
    boolean isConnected();

    void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException;

    boolean kickPlayer(Player player);

    void nextTurn();

     void playCard(int handCard, int x, int y);

    void flipCard(int playerID, int cardID);

    void drawCard(int playerID, CardType type);

    void grabCard(int playerID, CardType type, int slot);

    void addPlayer(Player player);

    void setCurrentStatus(GameStatus status);

    ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException;

    void pickGame(int index) throws RemoteException;

    void getNewGameController() throws RemoteException;

    int getIndex() throws RemoteException;

    RemoteServer getServer() throws RemoteException;

    void removeListener(int playerID, int cardID, Listener listener) throws RemoteException;

    void setName(String name) throws RemoteException;

    int getIndexOfPlayer(String nickName) throws RemoteException;

    int getNumberOfPlayers();

    void startGame();

    void setPlayerStatus(int playerID, GameStatus status);

    void setPlayerToken(int playerID, Token token);

    void setPlayerSecretObjective(int playerID, int pickedCard);

    void setPlayerStarterCard(int playerID);

    void flipStarterCard(int playerID);


    ArrayList<String> getDeckTextures(CardType type);
    String getSlotCardTexture(CardType type, int slot);
    String getSecretObjectiveName(int playerID);
    String getSecretObjectiveDescription(int playerID);
    String getCommonObjectiveName(int slot);
    String getCommonObjectiveDescription(int slot);
    int getPlayerTurn();

    ArrayList<HashMap<String, String>> getTemporaryObjectiveTextures(int playerID);
    HashMap<String, String> getTemporaryStarterCardTextures(int playerID);
    HashMap<String, String> getSecretObjectiveTextures(int playerID);
    GameStatus getPlayerStatus(int playerID);
    ArrayList<HashMap<String, String>> getPlayersInfo();
    int getPlayersHandSize(int playerID);
    boolean isPlayerFirst(int playerID);
    ArrayList<Coordinates> getAvailablePlacements(int playerID);
    boolean canCardBePlayed(int playerID, int cardID);
    Token getPlayerToken(int playerID);
    PlayableCard getPlayersLastPlayedCard(int playerID);
    PlayableCard getPlayersHandCard(int playerID, int cardID);
}