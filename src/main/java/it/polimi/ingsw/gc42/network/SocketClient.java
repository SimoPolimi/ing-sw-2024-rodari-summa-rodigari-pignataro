package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.network.interfaces.RemoteServer;
import it.polimi.ingsw.gc42.network.messages.*;
import it.polimi.ingsw.gc42.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

public class SocketClient implements NetworkController {
    private String ipAddress;
    private int port = 23690;
    private int gameID;
    private boolean isConnected = false;
    private int playerID;
    private Player owner;
    private Socket server;
    private Scanner in;
    private PrintWriter out;
    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;
    private Message pendingMessage = null;
    private BlockingDeque<Message> queue = new LinkedBlockingDeque<>();
    private boolean alreadySetRemoteView = false;
    private boolean alive = true;

    private ClientController clientController;

    public SocketClient(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public void connect() throws IOException {
        // TODO: Implement
        server = new Socket(ipAddress, port);
        isConnected = true;
        streamOut = new ObjectOutputStream(server.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(server.getInputStream());
        ScheduledExecutorService sendAlive = Executors.newScheduledThreadPool(2);
        sendAlive.scheduleAtFixedRate(() -> sendMessage(new ClientStateMessage(MessageType.CLIENT_STATE, ClientState.ALIVE)), 0, 500, TimeUnit.MILLISECONDS);
        // TODO: implement unresponsiveness handling
        sendAlive.scheduleAtFixedRate(() -> {if (alive) alive = false; else System.out.println("BBBBBB")/*send UNRESPONSIVE*/;}, 10, 5, TimeUnit.SECONDS);
        receiveMessage();

    }

    @Override
    public void disconnect() {
        sendMessage(new PlayerMessage(MessageType.DISCONNECT_PLAYER, gameID, playerID));

        // TODO: Implement
        /*in.close();
        out.close();
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    private synchronized void receiveMessage(){
        ExecutorService pool = Executors.newCachedThreadPool(); // Create a thread pool to handle the message flow between the server and every client
        pool.submit(() -> {
            while (true) {
                try {
                    Message message = (Message) streamIn.readObject();
                    System.out.println(message);
                    pool.submit(() -> {
                        try {
                            translate(message);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void translate(Message message) throws RemoteException {
        switch (message.getType()){
            case SHOW_SECRET_OBJECTIVES_SELECTION_DIALOG -> clientController.showSecretObjectivesSelectionDialog();
            case SHOW_STARTER_CARD_SELECTION_DIALOG -> clientController.showStarterCardSelectionDialog();
            case SHOW_TOKEN_SELECTION_DIALOG -> clientController.showTokenSelectionDialog();
            case ASK_TO_DRAW_OR_GRAB -> clientController.askToDrawOrGrab(((PlayerMessage) message).getPlayerID());
            case NOTIFY_GAME_IS_STARTING -> clientController.notifyGameIsStarting();
            case NOTIFY_DECK_CHANGED -> clientController.notifyDeckChanged(((DeckChangedMessage) message).getCardType());
            case NOTIFY_SLOT_CARD_CHANGED -> clientController.notifySlotCardChanged(((SlotCardMessage) message).getCardType(), ((SlotCardMessage) message).getSlot());
            case NOTIFY_PLAYERS_POINTS_CHANGED -> clientController.notifyPlayersPointsChanged(((PlayersPointsChangedMessage) message).getToken(), ((PlayersPointsChangedMessage) message).getNewPoints());
            case NOTIFY_NUMBER_OF_PLAYERS_CHANGED -> clientController.notifyNumberOfPlayersChanged();
            case NOTIFY_PLAYERS_TOKEN_CHANGED -> clientController.notifyPlayersTokenChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_PLAYERS_PLAY_AREA_CHANGED -> clientController.notifyPlayersPlayAreaChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_PLAYERS_HAND_CHANGED -> clientController.notifyPlayersHandChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_HAND_CARD_WAS_FLIPPED -> clientController.notifyHandCardWasFlipped(((FlipCardMessage) message).getPlayerID(), ((FlipCardMessage) message).getCardID());
            case NOTIFY_PLAYERS_OBJECTIVE_CHANGED -> clientController.notifyPlayersObjectiveChanged(((PlayerMessage) message).getPlayerID());
            case NOTIFY_COMMON_OBJECTIVES_CHANGED -> clientController.notifyCommonObjectivesChanged();
            case NOTIFY_TURN_CHANGED -> clientController.notifyTurnChanged();
            case GET_READY -> clientController.getReady(((IntResponse) message).getResponse());
            case NOTIFY_LAST_TURN -> clientController.notifyLastTurn();
            case NOTIFY_END_GAME -> clientController.notifyEndGame(((ListMapStrStrResponse) message).getResponse());
            case NOTIFY_NEW_MESSAGE -> clientController.notifyNewMessage(((ChatMessageMessage) message).getResponse());
            // Response from server
            default -> queue.add(message);
        }
    }

    @Override
    public ArrayList<PlayableCard> getPlayersPlayfield(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYERS_PLAY_FIELD, gameID, playerID));
        return ((PlayableCardListResponse) waitResponse(MessageType.GET_PLAYERS_PLAY_FIELD)).getResponse();
    }

    private synchronized Message waitResponse(MessageType messageType) {
        Message temp = null;
        try{
            temp = queue.take();
            while (!temp.getType().equals(messageType)){
                queue.addLast(temp);
                temp = queue.take();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return temp;
    }

    public synchronized void sendMessage(Message message) {
        try{
            streamOut.writeObject(message);
            streamOut.reset();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (message.getType() != MessageType.CLIENT_STATE) alive = true;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void setViewController(ClientController viewController) throws AlreadyBoundException, RemoteException {
        if (isConnected) {
            this.clientController = viewController;
            if (!alreadySetRemoteView) {
                sendMessage(new PlayerMessage(MessageType.ADD_VIEW, gameID, playerID));
                alreadySetRemoteView = true;
            }
        }
    }

    @Override
    public void addPlayer(Player player) {
        sendMessage(new AddPlayerMessage(MessageType.ADD_PLAYER, gameID, player));
        // Set owner
        this.owner = player;
        playerID = ((IntResponse) waitResponse(MessageType.ADD_PLAYER)).getResponse();
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
    public void setCurrentStatus(GameStatus status) {
        sendMessage(new SetCurrentStatusMessage(MessageType.SET_CURRENT_STATUS, gameID, status));
    }

    @Override
    public ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException {
        sendMessage(new Message(MessageType.GET_AVAILABLE_GAMES));
        return ((ListMapStrStrResponse) waitResponse(MessageType.GET_AVAILABLE_GAMES)).getResponse();
    }

    @Override
    public void pickGame(int index) throws RemoteException {
        gameID = index;
    }

    @Override
    public void getNewGameController() throws RemoteException {
        sendMessage(new Message(MessageType.NEW_GAME));
        gameID = ((IntResponse) waitResponse(MessageType.NEW_GAME)).getResponse();
    }

    @Override
    public int getIndex() throws RemoteException {
        return gameID;
    }

    @Override
    public RemoteServer getServer() throws RemoteException {
        out.println("D");
        out.flush();
        return new ServerManager(port);
    }

    @Override
    public void removeListener(int playerID, int cardID, Listener listener) throws RemoteException {
//TODO
    }

    @Override
    public void setName(String name) throws RemoteException {
        sendMessage(new SetNameMessage(MessageType.SET_NAME, gameID, name));
    }

    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        sendMessage(new GetNameMessage(MessageType.GET_INDEX_OF_PLAYER, gameID, nickName));
        return ((IntResponse) waitResponse(MessageType.GET_INDEX_OF_PLAYER)).getResponse();
    }

    @Override
    public int getNumberOfPlayers() {
        sendMessage(new GameMessage(MessageType.GET_NUMBER_OF_PLAYERS, gameID));
        return ((IntResponse) waitResponse(MessageType.GET_NUMBER_OF_PLAYERS)).getResponse();
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

    @Override
    public ArrayList<Card> getDeck(CardType type) {
        sendMessage(new GetDeckTexturesMessage(MessageType.GET_DECK, gameID, type));
        return ((DeckResponse) waitResponse(MessageType.GET_DECK)).getResponse();
    }

    @Override
    public Card getSlotCard(CardType type, int slot) {
        sendMessage(new GetSlotCardTextureMessage(MessageType.GET_SLOT_CARD, gameID, type, slot));
        return ((CardResponse) waitResponse(MessageType.GET_SLOT_CARD)).getResponse();
    }

    @Override
    public ObjectiveCard getSecretObjective(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_SECRET_OBJECTIVE, gameID, playerID));
        return ((ObjectiveCardResponse) waitResponse(MessageType.GET_SECRET_OBJECTIVE)).getResponse();
    }

    @Override
    public int getPlayerTurn() {
        sendMessage(new GameMessage(MessageType.GET_PLAYER_TURN, gameID));
        return ((IntResponse) waitResponse(MessageType.GET_PLAYER_TURN)).getResponse();
    }

    @Override
    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_TEMPORARY_OBJECTIVE_CARDS, gameID, playerID));
        return ((ObjCardListResponse) waitResponse(MessageType.GET_TEMPORARY_OBJECTIVE_CARDS)).getResponse();
    }

    @Override
    public StarterCard getTemporaryStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_TEMPORARY_STARTER_CARD, gameID, playerID));
        return ((StarterCardResponse) waitResponse(MessageType.GET_TEMPORARY_STARTER_CARD)).getResponse();
    }

    @Override
    public GameStatus getPlayerStatus(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYER_STATUS, gameID, playerID));
        return ((GameStatusResponse) waitResponse(MessageType.GET_PLAYER_STATUS)).getResponse();
    }

    @Override
    public ArrayList<HashMap<String, String>> getPlayersInfo() {
        sendMessage(new GameMessage(MessageType.GET_PLAYERS_INFO, gameID));
        return ((ListMapStrStrResponse) waitResponse(MessageType.GET_PLAYERS_INFO)).getResponse();
    }

    @Override
    public int getPlayersHandSize(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYERS_HAND_SIZE, gameID, playerID));
        return ((IntResponse) waitResponse(MessageType.GET_PLAYERS_HAND_SIZE)).getResponse();
    }

    @Override
    public boolean isPlayerFirst(int playerID) {
        sendMessage(new PlayerMessage(MessageType.IS_PLAYER_FIRST, gameID, playerID));
        return ((BoolResponse) waitResponse(MessageType.IS_PLAYER_FIRST)).getResponse();
    }

    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_AVAILABLE_PLACEMENT, gameID, playerID));
        return ((ListCoordResponse) waitResponse(MessageType.GET_AVAILABLE_PLACEMENT)).getResponse();
    }

    @Override
    public boolean canCardBePlayed(int playerID, int cardID) {
        sendMessage(new CanCardBePlayedMessage(MessageType.CAN_CARD_BE_PLAYED, gameID, playerID, cardID));
        return ((BoolResponse) waitResponse(MessageType.CAN_CARD_BE_PLAYED)).getResponse();
    }

    @Override
    public Token getPlayerToken(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYER_TOKEN, gameID, playerID));
        return ((TokenResponse) waitResponse(MessageType.GET_PLAYER_TOKEN)).getResponse();
    }

    @Override
    public PlayableCard getPlayersLastPlayedCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYERS_LAST_PLAYED_CARD, gameID, playerID));
        PlayableCardResponse temp = (PlayableCardResponse) waitResponse(MessageType.GET_PLAYERS_LAST_PLAYED_CARD);
        PlayableCard card = temp.getResponse();
        if (null != card) {
            card.setX(temp.getX());
            card.setY(temp.getY());
            if (card.isFrontFacing() != temp.isFrontFacing()) {
                card.flip();
            }
        }
        return card;
    }

    @Override
    public PlayableCard getPlayersHandCard(int playerID, int cardID) {
        sendMessage(new GetPlayersHandCardMessage(MessageType.GET_PLAYERS_HAND_CARD, gameID, playerID, cardID));
        return ((PlayableCardResponse) waitResponse(MessageType.GET_PLAYERS_HAND_CARD)).getResponse();
    }

    @Override
    public void sendMessage(int playerID, String message) throws RemoteException {
        sendMessage(new SendMessageMessage(MessageType.SEND_MESSAGE, gameID, playerID, message));
    }

    @Override
    public ArrayList<ChatMessage> getFullChat() throws RemoteException {
        sendMessage(new GameMessage(MessageType.GET_FULL_CHAT, gameID));
        return ((ChatResponse) waitResponse(MessageType.GET_FULL_CHAT)).getResponse();
    }

    @Override
    public boolean checkNickName(String nickname) throws RemoteException {
        sendMessage(new StrResponse(MessageType.CHECK_NICKNAME, nickname));
        return ((BoolResponse) waitResponse(MessageType.CHECK_NICKNAME)).getResponse();
    }

    @Override
    public void blockNickName(String nickname) throws RemoteException {
        sendMessage(new StrResponse(MessageType.BLOCK_NICKNAME, nickname));
    }
}
