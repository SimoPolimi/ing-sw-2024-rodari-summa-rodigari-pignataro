package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.network.interfaces.RemoteCollection;
import it.polimi.ingsw.gc42.network.interfaces.RemoteServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketClient implements NetworkController {
    private String ipAddress;
    private int port;
    private int gameID;
    private boolean isConnected = false;
    private int playerID;
    private Player owner;
    private Socket server;
    private Scanner in;
    private PrintWriter out;

    private ClientController clientController;

    public SocketClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void connect() throws IOException {
        // TODO: Implement
        server = new Socket(ipAddress, port);
        isConnected = true;
        in = new Scanner(server.getInputStream());
        out = new PrintWriter(server.getOutputStream());
    }

    @Override
    public void disconnect() {
        // TODO: Implement
        in.close();
        out.close();
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException {
        if (isConnected) {
            this.clientController = viewController;
            // TODO: Send message to do addView() to SocketControllerServer
        }
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public boolean kickPlayer(Player player) {
        return false;
    }

    @Override
    public void nextTurn() {

    }

    @Override
    public void playCard(int handCard, int x, int y) {

    }

    @Override
    public void flipCard(int playerID, int cardID) {

    }

    @Override
    public void drawCard(int playerID, CardType type) {

    }

    @Override
    public void grabCard(int playerID, CardType type, int slot) {

    }

    @Override
    public void drawSecretObjectives() {

    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void setCurrentStatus(GameStatus status) {

    }

    @Override
    public RemoteCollection getAvailableGames() throws RemoteException {
        return null;
    }

    @Override
    public void pickGame(int index) throws RemoteException {

    }

    @Override
    public void getNewGameController() throws RemoteException {

    }

    @Override
    public int getIndex() throws RemoteException {
        return 0;
    }

    @Override
    public RemoteServer getServer() throws RemoteException {
        out.println("D");
        return new ServerManager(port);
    }

    @Override
    public void removeListener(int playerID, int cardID, Listener listener) throws RemoteException {

    }

    @Override
    public void setName(String name) throws RemoteException {

    }

    @Override
    public Player getPlayer(int index) {
        return null;
    }

    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        return 0;
    }

    @Override
    public int getNumberOfPlayers() {
        return 0;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void setPlayerStatus(int playerID, GameStatus status) {

    }

    @Override
    public void setPlayerToken(int playerID, Token token) {

    }

    @Override
    public void setPlayerSecretObjective(int playerID, int pickedCard) {

    }

    @Override
    public void setPlayerStarterCard(int playerID) {

    }

    @Override
    public void flipStarterCard(int playerID) {

    }
}
