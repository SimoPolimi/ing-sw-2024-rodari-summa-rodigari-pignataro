package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.network.interfaces.RemoteViewController;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;
import it.polimi.ingsw.gc42.network.messages.*;
import it.polimi.ingsw.gc42.network.messages.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

public class SocketControllerServer implements ServerNetworkController {
    private String ipAddress;
    private int port = 23690;
    private Runnable onReady;
    private GameCollection games;
    private ServerManager server;
    final private HashMap<Socket, BlockingDeque<Message>> messagesQueue = new HashMap<>();
    private Scanner in;
    private PrintWriter out;
    private HashMap<Socket, ObjectInputStream> inMap = new HashMap<>();
    private HashMap<Socket, ObjectOutputStream> outMap = new HashMap<>();
    private HashMap<Socket, KeepAliveTimer> aliveSockets = new HashMap<>();

    private synchronized void receiveMessage(Socket socket) throws RemoteException {
        try {
            Message temp = messagesQueue.get(socket).take();
            switch (temp.getType()) {
                case ADD_PLAYER:
                    server.addPlayer(((AddPlayerMessage) temp).getGameID(), ((AddPlayerMessage) temp).getPlayer());
                    sendMessage(socket, new IntResponse(MessageType.ADD_PLAYER, server.getIndexOfPlayer(((AddPlayerMessage) temp).getGameID(), (((AddPlayerMessage) temp).getPlayer().getNickname()))));
                    break;
                case KICK_PLAYER:
                    server.kickPlayer(((KickPlayerMessage) temp).getGameID(), ((KickPlayerMessage) temp).getPlayer());
                    break;
                case NEXT_TURN:
                    server.nextTurn(((GameMessage) temp).getGameID());
                    break;
                case PLAY_CARD:
                    server.playCard(((PlayCardMessage) temp).getGameID(), ((PlayCardMessage) temp).getPlayerID(), ((PlayCardMessage) temp).getHandCard(), ((PlayCardMessage) temp).getX(), ((PlayCardMessage) temp).getY());
                    break;
                case FLIP_CARD:
                    server.flipCard(((FlipCardMessage) temp).getGameID(), ((FlipCardMessage) temp).getPlayerID(), ((FlipCardMessage) temp).getCardID());
                    break;
                case DRAW_CARD:
                    server.drawCard(((DrawCardMessage) temp).getGameID(), ((DrawCardMessage) temp).getPlayerID(), ((DrawCardMessage) temp).getCardType());
                    break;
                case GRAB_CARD:
                    server.grabCard(((GrabCardMessage) temp).getGameID(), ((GrabCardMessage) temp).getPlayerID(), ((GrabCardMessage) temp).getCardType(), ((GrabCardMessage) temp).getSlot());
                    break;
                case SET_CURRENT_STATUS:
                    server.setCurrentStatus(((SetCurrentStatusMessage) temp).getGameID(), ((SetCurrentStatusMessage) temp).getStatus());
                    break;
                case GET_AVAILABLE_GAMES:
                    sendMessage(socket, new ListMapStrStrResponse(MessageType.GET_AVAILABLE_GAMES, server.getAvailableGames()));
                    break;
                case NEW_GAME:
                    // Send GameID to client
                    sendMessage(socket, new IntResponse(MessageType.NEW_GAME, server.newGame()));
                    break;
                case SET_NAME:
                    server.setName(((SetNameMessage) temp).getGameID(), ((SetNameMessage) temp).getName());
                    break;
                case GET_INDEX_OF_PLAYER:
                    sendMessage(socket, new IntResponse(MessageType.GET_INDEX_OF_PLAYER, server.getIndexOfPlayer(((GetNameMessage) temp).getGameID(), ((GetNameMessage) temp).getName())));
                     break;
                case GET_NUMBER_OF_PLAYERS:
                    sendMessage(socket, new IntResponse(MessageType.GET_NUMBER_OF_PLAYERS, server.getNumberOfPlayers(((GameMessage) temp).getGameID())));
                    break;
                case START_GAME:
                    server.startGame((((GameMessage) temp).getGameID()));
                    break;
                case SET_PLAYER_STATUS:
                    server.setPlayerStatus((((SetPlayerStatusMessage) temp).getGameID()), (((SetPlayerStatusMessage) temp).getPlayerID()), (((SetPlayerStatusMessage) temp).getStatus()));
                    break;
                case SET_PLAYER_TOKEN:
                    server.setPlayerToken(((SetPlayerTokenMessage) temp).getGameID(), ((SetPlayerTokenMessage) temp).getPlayerID(), ((SetPlayerTokenMessage) temp).getToken());
                    break;
                case SET_PLAYER_SECRET_OBJECTIVE:
                    server.setPlayerSecretObjective(((SetPlayerSecretObjectiveMessage) temp).getGameID(), ((SetPlayerSecretObjectiveMessage) temp).getPlayerID(), ((SetPlayerSecretObjectiveMessage) temp).getPickedCard());
                    break;
                case SET_PLAYER_STARTER_CARD:
                    server.setPlayerStarterCard(((PlayerMessage) temp).getGameID(), ((PlayerMessage) temp).getPlayerID());
                    break;
                case FLIP_STARTER_CARD:
                    server.flipStarterCard(((PlayerMessage) temp).getGameID(), ((PlayerMessage) temp).getPlayerID());
                    break;
                case GET_DECK:
                    sendMessage(socket, new DeckResponse(MessageType.GET_DECK, server.getDeck(((GetDeckTexturesMessage) temp).getGameID(), ((GetDeckTexturesMessage) temp).getCardType())));
                    break;
                case GET_SLOT_CARD:
                    Card response = server.getSlotCard(((GetSlotCardTextureMessage) temp).getGameID(), ((GetSlotCardTextureMessage) temp).getCardType(), ((GetSlotCardTextureMessage) temp).getSlot());
                    sendMessage(socket, new CardResponse(MessageType.GET_SLOT_CARD, response, response.isFrontFacing()));
                    break;
                case GET_SECRET_OBJECTIVE:
                    sendMessage(socket, new ObjectiveCardResponse(MessageType.GET_SECRET_OBJECTIVE, server.getSecretObjective(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case GET_PLAYER_TURN:
                    sendMessage(socket, new IntResponse(MessageType.GET_PLAYER_TURN, server.getPlayerTurn(((GameMessage) temp).getGameID())));
                    break;
                case GET_TEMPORARY_OBJECTIVE_CARDS:
                    sendMessage(socket, new ObjCardListResponse(MessageType.GET_TEMPORARY_OBJECTIVE_CARDS, server.getTemporaryObjectiveCards(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case GET_TEMPORARY_STARTER_CARD:
                    sendMessage(socket, new StarterCardResponse(MessageType.GET_TEMPORARY_STARTER_CARD, server.getTemporaryStarterCard(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case GET_PLAYER_STATUS:
                    sendMessage(socket, new GameStatusResponse(MessageType.GET_PLAYER_STATUS, server.getPlayerStatus(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case GET_PLAYERS_INFO:
                    sendMessage(socket, new ListMapStrStrResponse(MessageType.GET_PLAYERS_INFO, server.getPlayersInfo(((GameMessage)temp).getGameID())));
                    break;
                case GET_PLAYERS_HAND_SIZE:
                    sendMessage(socket, new IntResponse(MessageType.GET_PLAYERS_HAND_SIZE, server.getPlayersHandSize(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case IS_PLAYER_FIRST:
                    sendMessage(socket, new BoolResponse(MessageType.IS_PLAYER_FIRST, server.isPlayerFirst(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case GET_AVAILABLE_PLACEMENT:
                    sendMessage(socket, new ListCoordResponse(MessageType.GET_AVAILABLE_PLACEMENT, server.getAvailablePlacements(((PlayerMessage)temp).getGameID(), ((PlayerMessage)temp).getPlayerID())));
                    break;
                case CAN_CARD_BE_PLAYED:
                    sendMessage(socket, new BoolResponse(MessageType.CAN_CARD_BE_PLAYED, server.canCardBePlayed(((CanCardBePlayedMessage)temp).getGameID(), ((CanCardBePlayedMessage)temp).getPlayerID(), ((CanCardBePlayedMessage)temp).getCardID())));
                    break;
                case GET_PLAYER_TOKEN:
                    sendMessage(socket, new TokenResponse(MessageType.GET_PLAYER_TOKEN, server.getPlayerToken(((PlayerMessage) temp).getGameID(), ((((PlayerMessage) temp).getPlayerID())))));
                    break;
                case GET_PLAYERS_LAST_PLAYED_CARD:
                    PlayableCard card = server.getPlayersLastPlayedCard(((PlayerMessage) temp).getGameID(), ((((PlayerMessage) temp).getPlayerID())));
                            sendMessage(socket, new PlayableCardResponse(MessageType.GET_PLAYERS_LAST_PLAYED_CARD, card, card.getX(), card.getY(), card.isFrontFacing()));
                    break;
                case GET_PLAYERS_HAND_CARD:
                    PlayableCard tempCard = server.getPlayersHandCard(((GetPlayersHandCardMessage) temp).getGameID(), ((GetPlayersHandCardMessage) temp).getPlayerID(), ((GetPlayersHandCardMessage) temp).getCardID());
                    boolean isFrontFacing = true;
                    if (null != tempCard) {
                        isFrontFacing = tempCard.isFrontFacing();
                    }
                    sendMessage(socket, new PlayableCardResponse(MessageType.GET_PLAYERS_HAND_CARD, tempCard, 0, 0, isFrontFacing));
                    break;
                case ADD_VIEW:
                    // Creates a Virtual View and hooks it to the specific Game the user is playing
                    server.addView(((PlayerMessage) temp).getGameID(), new RemoteViewController() {
                        // The Virtual View (or Remote View) implements the same methods as a regular View, but inside
                        // of them has the code to SEND MESSAGES via Socket

                        @Override
                        public void showSecretObjectivesSelectionDialog() {
                            sendMessage(socket, new Message(MessageType.SHOW_SECRET_OBJECTIVES_SELECTION_DIALOG));
                        }

                        @Override
                        public void showStarterCardSelectionDialog() {
                            sendMessage(socket, new Message(MessageType.SHOW_STARTER_CARD_SELECTION_DIALOG));
                        }

                        @Override
                        public void showTokenSelectionDialog() {
                            sendMessage(socket, new Message(MessageType.SHOW_TOKEN_SELECTION_DIALOG));
                        }

                        @Override
                        public void askToDrawOrGrab(int playerID) {
                            sendMessage(socket, new PlayerMessage(MessageType.ASK_TO_DRAW_OR_GRAB, ((PlayerMessage)temp).getGameID(), playerID));
                        }

                        @Override
                        public void notifyGameIsStarting() {
                            sendMessage(socket, new Message(MessageType.NOTIFY_GAME_IS_STARTING));
                        }

                        @Override
                        public void notifyDeckChanged(CardType type) {
                            sendMessage(socket, new DeckChangedMessage(MessageType.NOTIFY_DECK_CHANGED, type));
                        }

                        @Override
                        public void notifySlotCardChanged(CardType type, int slot) {
                            sendMessage(socket, new SlotCardMessage(MessageType.NOTIFY_SLOT_CARD_CHANGED, type, slot));
                        }

                        @Override
                        public void notifyPlayersPointsChanged() {
                            sendMessage(socket, new Message(MessageType.NOTIFY_PLAYERS_POINTS_CHANGED));
                        }

                        @Override
                        public void notifyNumberOfPlayersChanged() {
                            sendMessage(socket, new Message(MessageType.NOTIFY_NUMBER_OF_PLAYERS_CHANGED));
                        }

                        @Override
                        public void notifyPlayersTokenChanged(int playerID) {
                            sendMessage(socket, new PlayerMessage(MessageType.NOTIFY_PLAYERS_TOKEN_CHANGED, ((PlayerMessage) temp).getGameID(), playerID));
                        }

                        @Override
                        public void notifyPlayersPlayAreaChanged(int playerID) {
                            sendMessage(socket, new PlayerMessage(MessageType.NOTIFY_PLAYERS_PLAY_AREA_CHANGED, ((PlayerMessage) temp).getGameID(), playerID));
                        }

                        @Override
                        public void notifyPlayersHandChanged(int playerID) {
                            sendMessage(socket, new PlayerMessage(MessageType.NOTIFY_PLAYERS_HAND_CHANGED, ((PlayerMessage) temp).getGameID(), playerID));
                        }

                        @Override
                        public void notifyHandCardWasFlipped(int playedID, int cardID) {
                            sendMessage(socket, new FlipCardMessage(MessageType.NOTIFY_HAND_CARD_WAS_FLIPPED, ((PlayerMessage) temp).getGameID(), playedID, cardID));
                        }

                        @Override
                        public void notifyPlayersObjectiveChanged(int playerID) {
                            sendMessage(socket, new PlayerMessage(MessageType.NOTIFY_PLAYERS_OBJECTIVE_CHANGED, ((PlayerMessage) temp).getGameID(), playerID));
                        }

                        @Override
                        public void notifyCommonObjectivesChanged() {
                            sendMessage(socket, new Message(MessageType.NOTIFY_COMMON_OBJECTIVES_CHANGED));
                        }

                        @Override
                        public void notifyTurnChanged() {
                            sendMessage(socket, new Message(MessageType.NOTIFY_TURN_CHANGED));
                        }

                        @Override
                        public void getReady(int numberOfPlayers) {
                            sendMessage(socket, new IntResponse(MessageType.GET_READY, numberOfPlayers));
                        }
                    });
                case CLIENT_STATE:
                    aliveSockets.get(socket).setAlive(ClientState.ALIVE);
                    break;
                default:
                    //TODO
                    break;
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getPort() {
        return String.valueOf(port);
    }

    @Override
    public void setWhenReady(Runnable runnable) {
        this.onReady = runnable;
    }

    @Override
    public void start() throws IOException {
        // TODO: Create connection
        ServerSocket serverSocket;
        try {
            //serverSocket = new ServerSocket(0);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        //port = serverSocket.getLocalPort();
        System.out.println("Server socket created");

        // Creates its own ServerManager
        server = new ServerManager(port);
        // Puts the shared GameCollection inside the ServerManager
        server.setCollection(games);

        // Executes the GUI refresh Code to show the IP and Port in Server's GUI
        onReady.run();

        // Listens on its port for new connections
        ExecutorService pool = Executors.newCachedThreadPool(); // Create a thread pool to handle the message flow between the server and every client
        pool.submit(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Received a connection");
                    pool.submit(() -> {
                        try {
                            //in = new Scanner(socket.getInputStream());
                            //out = new PrintWriter(socket.getOutputStream()); // May not be necessary
                            messagesQueue.put(socket, new LinkedBlockingDeque<>());
                            inMap.put(socket, new ObjectInputStream(socket.getInputStream()));
                            outMap.put(socket, new ObjectOutputStream(socket.getOutputStream()));
                            outMap.get(socket).flush();
                            aliveSockets.put(socket, new KeepAliveTimer());
                            ScheduledExecutorService checkAlive = Executors.newSingleThreadScheduledExecutor();
                            checkAlive.scheduleAtFixedRate(aliveSockets.get(socket), 5, 1, TimeUnit.SECONDS);
                            while (true) {
                                Message message = (Message) inMap.get(socket).readObject();
                                // By creating a thread it is possible to receive and translate a new message
                                // even if the previous translation isn't finished, though this approach
                                // can be risky because there could be a chain of "order sensitive" operations.
                                // Another approach could be putting the messages in a queue and running translate()
                                // every time the queue is not empty
                                pool.submit(() -> {
                                    messagesQueue.get(socket).add(message);
                                    try {
                                        receiveMessage(socket);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    break; // Server is closed
                }
            }
        });
    }

    @Override
    public void stop() {
        // TODO: Close connection
    }

    @Override
    public void setCollection(GameCollection collection) {
        this.games = collection;
    }

    private synchronized void sendMessage(Socket socket, Message message){
        //out.println(new Gson().toJson(message));
        try{
            outMap.get(socket).writeObject(message);
            outMap.get(socket).reset();
        }catch (IOException e){
            e.printStackTrace();
        }
        //out.flush();
    }

    // TODO: Write Socket Methods to send and receive messages.
    // When a message is received, the same action is executed on server.
    // When it has to call the addView() Method, a new RemoteViewController is created HERE.
    // Inside the @Override methods of this new RemoteViewController there will be calls to
    // SocketControllerServer's methods to send messages to Client.
    // The newly created RemoteViewController is saved inside the GameController, the same way
    // RMI does: server.addView(...).
}
