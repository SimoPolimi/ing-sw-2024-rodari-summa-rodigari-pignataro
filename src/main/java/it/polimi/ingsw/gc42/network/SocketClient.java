package it.polimi.ingsw.gc42.network;

import com.google.gson.Gson;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.network.interfaces.RemoteServer;
import it.polimi.ingsw.gc42.network.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Message pendingMessage = null;
    private boolean responseLock = false;

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
        receiveMessage();

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

    private void receiveMessage(){
        ExecutorService pool = Executors.newCachedThreadPool(); // Create a thread pool to handle the message flow between the server and every client
        pool.submit(() -> {
            while (true) {
                pool.submit(() -> {
                    try {
                        Scanner in = new Scanner(server.getInputStream());
                        PrintWriter out = new PrintWriter(server.getOutputStream()); // May not be necessary
                        while (true) {
                            String line = in.nextLine();
                            // By creating a thread it is possible to receive and translate a new message
                            // even if the previous translation isn't finished, though this approach
                            // can be risky because there could be a chain of "order sensitive" operations.
                            // Another approach could be putting the messages in a queue and running translate()
                            // every time the queue is not empty
                            if(!line.isEmpty()){
                                System.out.println(new Gson().fromJson(line, Message.class));
                            pool.submit(() -> {
                                try {
                                    translate(new Gson().fromJson(line, Message.class));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                            });}
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    private void translate(Message message) throws RemoteException {
        switch (message.getType()){
            case NEW_GAME -> this.gameID = ((GameMessage)message).getGameID();
            case GET_AVAILABLE_GAMES, GET_NUMBER_OF_PLAYERS  -> {
                // Response from server
                pendingMessage = message;
                responseLock = true;
            }


            case SHOW_SECRET_OBJECTIVES_SELECTION_DIALOG -> clientController.showSecretObjectivesSelectionDialog();
            case SHOW_STARTER_CARD_SELECTION_DIALOG -> clientController.showStarterCardSelectionDialog();
            case SHOW_TOKEN_SELECTION_DIALOG -> clientController.showTokenSelectionDialog();
            case GET_OWNER -> {}//TODO: Return owner
            case ASK_TO_DRAW_OR_GRAB -> clientController.askToDrawOrGrab();
            case NOTIFY_GAME_IS_STARTING -> clientController.notifyGameIsStarting();
            case NOTIFY_DECK_CHANGED -> clientController.notifyDeckChanged(((DeckChangedMessage) message).getCardType());
            case NOTIFY_SLOT_CARD_CHANGED -> clientController.notifySlotCardChanged(((SlotCardMessage) message).getCardType(), ((SlotCardMessage) message).getSlot());
            case NOTIFY_NUMBER_OF_PLAYERS_CHANGED -> clientController.notifyNumberOfPlayersChanged();
            case NOTIFY_PLAYERS_TOKEN_CHANGED -> clientController.notifyPlayersTokenChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_PLAYERS_PLAY_AREA_CHANGED -> clientController.notifyPlayersPlayAreaChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_PLAYERS_HAND_CHANGED -> clientController.notifyPlayersHandChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_HAND_CARD_WAS_FLIPPED -> clientController.notifyHandCardWasFlipped(((HandCardMessage) message).getPlayerID(), ((HandCardMessage) message).getSlot());
            case NOTIFY_PLAYERS_OBJECTIVE_CHANGED -> clientController.notifyPlayersObjectiveChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_TURN_CHANGED -> clientController.notifyTurnChanged();
            case GET_READY -> clientController.getReady();
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
    public boolean kickPlayer(Player player) {
        //TODO: kick player
        sendMessage(new GameMessage(MessageType.KICK_PLAYER, gameID));
        return false;
    }

    @Override
    public void nextTurn() {
        sendMessage(new GameMessage(MessageType.NEXT_TURN, gameID));
    }

    @Override
    public void playCard(int handCard, int x, int y) {
        sendMessage(new PlayCardMessage(MessageType.PLAY_CARD, gameID, playerID, handCard, x, y));
    }

    @Override
    public void flipCard(int playerID, int cardID) {
        sendMessage(new FlipCardMessage(MessageType.FLIP_CARD, gameID, playerID, cardID));
    }

    @Override
    public void drawCard(int playerID, CardType type) {
        sendMessage(new DrawCardMessage(MessageType.DRAW_CARD, gameID, playerID, type));
    }

    @Override
    public void grabCard(int playerID, CardType type, int slot) {
        sendMessage(new GrabCardMessage(MessageType.GRAB_CARD, gameID, playerID, type, slot));
    }

    @Override
    public void addPlayer(Player player) {
        sendMessage(new AddPlayerMessage(MessageType.ADD_PLAYER, gameID, player));
    }

    @Override
    public void setCurrentStatus(GameStatus status) {
        sendMessage(new Message(MessageType.SET_CURRENT_STATUS));
    }

    @Override
    public ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException {
        sendMessage(new Message(MessageType.GET_AVAILABLE_GAMES));
        responseLock = false;
        while (!responseLock) {}
        return new Gson().fromJson(((StringMessage)pendingMessage).getString(), ArrayList.class);
    }

    @Override
    public void pickGame(int index) throws RemoteException {

    }

    @Override
    public void getNewGameController() throws RemoteException {

    }

    @Override
    public int getIndex() throws RemoteException {
        // Lock until response
        return 0;
    }

    @Override
    public RemoteServer getServer() throws RemoteException {
        out.println("D");
        out.flush();
        return new ServerManager(port);
    }

    @Override
    public void removeListener(int playerID, int cardID, Listener listener) throws RemoteException {

    }

    @Override
    public void setName(String name) throws RemoteException {
        sendMessage(new SetNameMessage(MessageType.SET_NAME, gameID, name));
    }

    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        return 0;
    }

    @Override
    public int getNumberOfPlayers() {
        // TODO
        sendMessage(new GameMessage(MessageType.GET_NUMBER_OF_PLAYERS, gameID));
        return 0;
    }

    @Override
    public void startGame() {
        sendMessage(new GameMessage(MessageType.START_GAME, gameID));
    }

    @Override
    public void setPlayerStatus(int playerID, GameStatus status) {
        sendMessage(new SetPlayerStatusMessage(MessageType.SET_PLAYER_STATUS, gameID, playerID, status));
    }

    @Override
    public void setPlayerToken(int playerID, Token token) {
        sendMessage(new SetPlayerTokenMessage(MessageType.SET_PLAYER_TOKEN, gameID, playerID, token));
    }

    @Override
    public void setPlayerSecretObjective(int playerID, int pickedCard) {
        sendMessage(new SetPlayerSecretObjectiveMessage(MessageType.SET_PLAYER_SECRET_OBJECTIVE, gameID, playerID, pickedCard));
    }

    @Override
    public void setPlayerStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.SET_PLAYER_STARTER_CARD, gameID, playerID));
    }

    @Override
    public void flipStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.FLIP_STARTER_CARD, gameID, playerID));
    }

    public void sendMessage(Message message){
        out.println(new Gson().toJson(message));
        out.flush();
    }

    //TODO: Implement all

    @Override
    public ArrayList<String> getDeckTextures(CardType type) {
        sendMessage(new StringMessage(MessageType.GET_DECK_TEXTURES, new Gson().toJson(type)));
        return null;
    }

    @Override
    public String getSlotCardTexture(CardType type, int slot) {
        return "";
    }

    @Override
    public String getSecretObjectiveName(int playerID) {
        return "";
    }

    @Override
    public String getSecretObjectiveDescription(int playerID) {
        return "";
    }

    @Override
    public String getCommonObjectiveName(int slot) {
        return "";
    }

    @Override
    public String getCommonObjectiveDescription(int slot) {
        return "";
    }

    @Override
    public int getPlayerTurn() {
        return 0;
    }

    @Override
    public ArrayList<HashMap<String, String>> getTemporaryObjectiveTextures(int playerID) {
        return null;
    }

    @Override
    public HashMap<String, String> getTemporaryStarterCardTextures(int playerID) {
        return null;
    }

    @Override
    public HashMap<String, String> getSecretObjectiveTextures(int playerID) {
        return null;
    }

    @Override
    public GameStatus getPlayerStatus(int playerID) {
        return null;
    }

    @Override
    public ArrayList<HashMap<String, String>> getPlayersInfo() {
        return null;
    }

    @Override
    public int getPlayersHandSize(int playerID) {
        return 0;
    }

    @Override
    public boolean isPlayerFirst(int playerID) {
        return false;
    }

    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int playerID) {
        return null;
    }

    @Override
    public boolean canCardBePlayed(int playerID, int cardID) {
        return false;
    }

    @Override
    public Token getPlayerToken(int playerID) {
        return null;
    }

    @Override
    public PlayableCard getPlayersLastPlayedCard(int playerID) {
        return null;
    }

    @Override
    public PlayableCard getPlayersHandCard(int playerID, int cardID) {
        return null;
    }
}
