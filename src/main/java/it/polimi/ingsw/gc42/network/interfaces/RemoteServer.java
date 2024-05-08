package it.polimi.ingsw.gc42.network.interfaces;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface RemoteServer extends Remote {
    int addGame(GameController game) throws RemoteException;
    RemoteCollection getGames() throws RemoteException;



    boolean kickPlayer(int gameID, Player player) throws RemoteException;

    void nextTurn(int gameID) throws RemoteException;

    void playCard(int gameID, int playerID,  int cardID, int x, int y) throws RemoteException;

    void flipCard(int gameID, int playerID, int cardID) throws RemoteException;

    void drawCard(int gameID, int playerID, CardType type) throws RemoteException;

    void grabCard(int gameID, int playerID, CardType type, int slot) throws RemoteException;

    void drawSecretObjectives(int gameID) throws RemoteException;

    Game getGame(int gameID) throws RemoteException;

    void addView(int gameID, RemoteViewController viewController) throws RemoteException;

    void addPlayer(int gameID, Player player) throws RemoteException;

    void setCurrentStatus(int gameID, GameStatus status) throws RemoteException;

    String getName(int gameID) throws RemoteException;
    void setName(int gameID, String name) throws RemoteException;

    int getNumberOfPlayers(int gameID) throws RemoteException;

    Player getPlayer(int gameID, int index) throws RemoteException;

    void removeCardListener(int gameID, int playerID, int cardID, Listener listener) throws RemoteException;

    int newGame() throws RemoteException;

    void startGame(int gameID) throws RemoteException;

    void lookupClient(int gameID, String ip, int port, String clientID) throws RemoteException, NotBoundException;

    void setPlayerStatus(int gameID, int playerID, GameStatus status) throws RemoteException;

    void setPlayerToken(int gameID, int playerID, Token token) throws RemoteException;

    void setPlayerSecretObjective(int gameID, int playerID, int pickedCard) throws RemoteException;

    void setPlayerStarterCard(int gameID, int playerID) throws RemoteException;

    void flipStarterCard(int gameID, int playerID) throws RemoteException;

    ArrayList<String> getDeckTextures(int gameID, CardType type) throws RemoteException;

    String getSlotCardTexture(int gameID, CardType type, int slot) throws RemoteException;

    String getSecretObjectiveName(int gameID, int playerID) throws RemoteException;

    String getSecretObjectiveDescription(int gameID, int playerID) throws RemoteException;

    String getCommonObjectiveName(int gameID, int slot) throws RemoteException;

    String getCommonObjectiveDescription(int gameID, int slot) throws RemoteException;

    int getPlayerTurn(int gameID) throws RemoteException;

    ArrayList<HashMap<String, String>> getTemporaryObjectiveTextures(int gameID, int playerID) throws RemoteException;

    HashMap<String, String> getTemporaryStarterCardTextures(int gameID, int playerID) throws RemoteException;

    HashMap<String, String> getSecretObjectiveTextures(int gameID, int playerID) throws RemoteException;

    GameStatus getPlayerStatus(int gameID, int playerID) throws RemoteException;

    ArrayList<HashMap<String, String>> getPlayersInfo(int gameID) throws RemoteException;

    int getPlayersHandSize(int gameID, int playerID) throws RemoteException;

    boolean isPlayerFirst(int gameID, int playerID) throws RemoteException;

    ArrayList<Coordinates> getAvailablePlacements(int gameID, int playerID) throws RemoteException;

    boolean canCardBePlayed(int gameID, int playerID, int cardID) throws RemoteException;

    Token getPlayerToken(int gameID, int playerID) throws RemoteException;

    PlayableCard getPlayersLastPlayedCard(int gameID, int playerID) throws RemoteException;

    PlayableCard getPlayersHandCard(int gameID, int playerID, int cardID) throws RemoteException;
}
