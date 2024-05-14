package it.polimi.ingsw.gc42.network;

import com.google.gson.Gson;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;
import it.polimi.ingsw.gc42.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class SocketControllerServer implements ServerNetworkController {
    private String ipAddress;
    private int port;
    private Runnable onReady;
    private GameCollection games;
    private ServerManager server;
    final private BlockingDeque<Message> messagesQueue = new LinkedBlockingDeque<>();
    private Scanner in;
    private PrintWriter out;
    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;

    private synchronized void receiveMessage() throws RemoteException {
        try {
            Message temp = messagesQueue.take();
            switch (temp.getType()) {
                case START_GAME:
                    server.startGame((((GameMessage) temp).getGameID()));
                    break;
                case SET_PLAYER_STATUS:
                    server.setPlayerStatus((((SetPlayerStatusMessage) temp).getGameID()), (((SetPlayerStatusMessage) temp).getPlayerID()), (((SetPlayerStatusMessage) temp).getStatus()));
                    break;
                case NEW_GAME:
                    // Send GameID to client
                    sendMessage(new GameMessage(MessageType.NEW_GAME, server.newGame()));
                    break;
                case GRAB_CARD:
                    server.grabCard(((GrabCardMessage) temp).getGameID(), ((GrabCardMessage) temp).getPlayerID(), ((GrabCardMessage) temp).getCardType(), ((GrabCardMessage) temp).getSlot());
                    break;
                case PLAY_CARD:
                    server.playCard(((PlayCardMessage) temp).getGameID(), ((PlayCardMessage) temp).getPlayerID(), ((PlayCardMessage) temp).getHandCard(), ((PlayCardMessage) temp).getX(), ((PlayCardMessage) temp).getY());
                    break;
                case KICK_PLAYER:
                    server.kickPlayer(((KickPlayerMessage) temp).getGameID(), ((KickPlayerMessage) temp).getPlayer());
                    break;
                case NEXT_TURN:
                    server.nextTurn(((GameMessage) temp).getGameID());
                    break;
                case ADD_PLAYER:
                    server.addPlayer(((AddPlayerMessage) temp).getGameID(), ((AddPlayerMessage) temp).getPlayer());
                    break;
                case DRAW_CARD:
                    server.drawCard(((DrawCardMessage) temp).getGameID(), ((DrawCardMessage) temp).getPlayerID(), ((DrawCardMessage) temp).getCardType());
                    break;
                case GET_NUMBER_OF_PLAYERS:
                    // Send Number of players to client
                    sendMessage(new StringMessage((MessageType.GET_NUMBER_OF_PLAYERS), String.valueOf(server.getNumberOfPlayers(((GameMessage) temp).getGameID()))));
                    break;
                case SET_NAME:
                    server.setName(((SetNameMessage) temp).getGameID(), ((SetNameMessage) temp).getName());
                    break;
                case SET_PLAYER_SECRET_OBJECTIVE:
                    server.setPlayerSecretObjective(((SetPlayerSecretObjectiveMessage) temp).getGameID(), ((SetPlayerSecretObjectiveMessage) temp).getPlayerID(), ((SetPlayerSecretObjectiveMessage) temp).getPickedCard());
                    break;
                case SET_PLAYER_STARTER_CARD:
                    server.setPlayerStarterCard(((PlayerMessage) temp).getGameID(), ((PlayerMessage) temp).getPlayerID());
                    break;
                case SET_PLAYER_TOKEN:
                    server.setPlayerToken(((SetPlayerTokenMessage) temp).getGameID(), ((SetPlayerTokenMessage) temp).getPlayerID(), ((SetPlayerTokenMessage) temp).getToken());
                    break;
                case FLIP_CARD:
                    server.flipCard(((FlipCardMessage) temp).getGameID(), ((FlipCardMessage) temp).getPlayerID(), ((FlipCardMessage) temp).getCardID());
                    break;
                case FLIP_STARTER_CARD:
                    server.flipStarterCard(((PlayerMessage) temp).getGameID(), ((PlayerMessage) temp).getPlayerID());
                    break;
                case SET_CURRENT_STATUS:
                    server.setCurrentStatus(((SetCurrentStatusMessage) temp).getGameID(), ((SetCurrentStatusMessage) temp).getStatus());
                    break;
                // Responses
                case GET_AVAILABLE_GAMES:
                    sendMessage(new StringMessage((MessageType.GET_AVAILABLE_GAMES), new Gson().toJson(server.getAvailableGames())));
                    break;
                case GET_DECK_TEXTURES:
                    // Send Controller's name to client
                    sendMessage(new StringMessage((MessageType.GET_DECK_TEXTURES), new Gson().toJson(server.getDeckTextures(((GameMessage) temp).getGameID(), new Gson().fromJson(((StringMessage) temp).getString(), CardType.class)))));
                    break;
                case GET_PLAYER_TURN:

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
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        port = serverSocket.getLocalPort();
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
                            streamIn = new ObjectInputStream(socket.getInputStream());
                            streamOut = new ObjectOutputStream(socket.getOutputStream());
                            streamOut.flush();
                            while (true) {
                                //String line = in.nextLine();
                                Message message = (Message)streamIn.readObject();
                                // By creating a thread it is possible to receive and translate a new message
                                // even if the previous translation isn't finished, though this approach
                                // can be risky because there could be a chain of "order sensitive" operations.
                                // Another approach could be putting the messages in a queue and running translate()
                                // every time the queue is not empty

                                /*pool.submit(() -> {
                                    messagesQueue.add(new Gson().fromJson(line, Message.class));
                                    try {
                                        receiveMessage();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });*/
                                pool.submit(() -> {
                                    messagesQueue.add(message);
                                    try {
                                        receiveMessage();
                                    } catch (RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    System.err.println(e.getMessage());
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

    private void sendMessage(Message message){
        //out.println(new Gson().toJson(message));
        try{
            streamOut.writeObject(message);
            streamOut.flush();
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
