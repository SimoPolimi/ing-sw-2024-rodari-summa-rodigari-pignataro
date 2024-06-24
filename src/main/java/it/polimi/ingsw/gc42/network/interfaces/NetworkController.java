package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
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
    void rejoinGame(int playerID) throws RemoteException;
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

    void setName(String name) throws RemoteException;

    int getIndexOfPlayer(String nickName) throws RemoteException;

    int getNumberOfPlayers();

    void startGame();

    void setPlayerStatus(int playerID, GameStatus status);

    void setPlayerToken(int playerID, Token token);

    void setPlayerSecretObjective(int playerID, int pickedCard);

    void setPlayerStarterCard(int playerID);

    void flipStarterCard(int playerID);


    ArrayList<Card> getDeck(CardType type);
    Card getSlotCard(CardType type, int slot);
    ObjectiveCard getSecretObjective(int playerID);
    int getPlayerTurn();

    ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int playerID);
    StarterCard getTemporaryStarterCard(int playerID);
    GameStatus getPlayerStatus(int playerID);
    ArrayList<HashMap<String, String>> getPlayersInfo();
    int getPlayersHandSize(int playerID);
    boolean isPlayerFirst(int playerID);
    ArrayList<Coordinates> getAvailablePlacements(int playerID);
    boolean canCardBePlayed(int playerID, int cardID);
    Token getPlayerToken(int playerID);
    PlayableCard getPlayersLastPlayedCard(int playerID);
    PlayableCard getPlayersHandCard(int playerID, int cardID);

    ArrayList<PlayableCard> getPlayersPlayfield(int playerID);

    void sendMessage(int playerID, String message) throws RemoteException;
    ArrayList<ChatMessage> getFullChat() throws RemoteException;
    boolean checkNickName(String nickname) throws RemoteException;
    void blockNickName(String nickname) throws RemoteException;
    ObjectiveCard getCommonObjective(int cardID) throws RemoteException;
}