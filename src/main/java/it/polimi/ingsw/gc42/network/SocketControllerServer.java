package it.polimi.ingsw.gc42.network;

import com.google.gson.Gson;
import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.interfaces.ServerNetworkController;
import it.polimi.ingsw.gc42.network.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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
    //TODO: move BlockingQueue to game controller, it will contains lambda. I shoould add them from here after translating the message

    private void receiveMessage() throws RemoteException {
        // TODO: message parsing and method calling
        // ex: Message_type: arg1, arg2, ...
        /*ArrayList<String> temp = new ArrayList<>(Arrays.asList(messagesQueue.poll().split(":")));
        String command = temp.removeFirst();
        ArrayList<String> args = new ArrayList<>(Arrays.asList(temp.getFirst().split(", ")));
        //TODO: are thoso ok here?? variables for switch Enum
        GameStatus gameStatus = null;
        CardType cardType = null;
        Token token = null;

        switch (command) {
            case "START_GAME":
                server.getGame(Integer.parseInt(args.removeFirst()));
                break;
            case "SET_PLAYER_STATUS":
                // String to Enum
                for(GameStatus gameStatus1 : GameStatus.values()){
                    if(gameStatus1.toString().equals(args.getLast())){
                        gameStatus = gameStatus1;
                    }
                }
                args.removeLast();
                server.setPlayerStatus(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), gameStatus);
                    break;
            case "NEW_GAME":
                break;
            case "GET_GAME":
                break;
            case "GRAB_CARD":
                // String to Enum
                for(CardType cardType1 : CardType.values()){
                    if(cardType1.toString().equals(args.getLast())){
                        cardType = cardType1;
                    }
                }
                args.removeLast();
                server.grabCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), cardType, Integer.parseInt(args.removeFirst()));
                break;
            case   "PLAY_CARD":
                server.playCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()));
                break;
            case    "KICK_PLAYER":
                break;
            case    "NEXT_TURN":
                server.nextTurn(Integer.parseInt(args.removeFirst()));
                break;
            case    "ADD_PLAYER":
                break;
            case    "DRAW_CARD":
                // String to Enum
                for(CardType cardType1 : CardType.values()){
                    if(cardType1.toString().equals(args.getLast())){
                        cardType = cardType1;
                    }
                }
                args.removeLast();
                server.drawCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), cardType);
                break;
            case    "GET_NUMBER_OF_PLAYERS":
                break;
            case    "SET_NAME":
                server.setName(Integer.parseInt(args.removeFirst()), args.removeFirst());
                break;
            case    "SET_PLAYER_SECRET_OBJECTIVE":
                server.setPlayerSecretObjective(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()));
                break;
            case    "SET_PLAYER_STARTER_CARD":
                server.setPlayerStarterCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()));
                break;
            case    "SET_PLAYER_TOKEN":
                // String to Enum
                for(Token token1 : Token.values()){
                    if(token1.toString().equals(args.getLast())){
                        token = token1;
                    }
                }
                args.removeLast();
                server.setPlayerToken(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), token);
                break;
            case    "DRAW_SECRET_OBJECTIVES":
                server.drawSecretObjectives(Integer.parseInt(args.removeFirst()));
                break;
            case    "FLIP_CARD":
                server.flipCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()));
                break;
            case    "FLIP_STARTER_CARD":
                server.flipStarterCard(Integer.parseInt(args.removeFirst()), Integer.parseInt(args.removeFirst()));
                break;
            case    "SET_CURRENT_STATUS":
                // String to Enum
                for(GameStatus gameStatus1 : GameStatus.values()){
                    if(gameStatus1.toString().equals(args.getLast())){
                        gameStatus = gameStatus1;
                    }
                }
                args.removeLast();
                server.setCurrentStatus(Integer.parseInt(args.removeFirst()), gameStatus);
                break;
            case    "GET_NAME":
                break;
            case    "GET_PLAYER":
                break;
        }*/
        Message temp = messagesQueue.poll();
        switch (temp.getType()){
            case START_GAME:
                server.startGame((((GameMessage)temp).getGameID()));
                break;
            case SET_PLAYER_STATUS:
                server.setPlayerStatus((((SetPlayerStatusMessage)temp).getGameID()), (((SetPlayerStatusMessage)temp).getPlayerID()), (((SetPlayerStatusMessage)temp).getStatus()));
                break;
            case NEW_GAME:
                break;
            case GET_GAME:
                break;
            case GRAB_CARD:
                server.grabCard(((GrabCardMessage)temp).getGameID(), ((GrabCardMessage)temp).getPlayerID(), ((GrabCardMessage)temp).getCardType(), ((GrabCardMessage)temp).getSlot());
                break;
            case PLAY_CARD:
                server.playCard(((PlayCardMessage)temp).getGameID(), ((PlayCardMessage)temp).getPlayerID(), ((PlayCardMessage)temp).getHandCard(), ((PlayCardMessage)temp).getX(), ((PlayCardMessage)temp).getY());
                break;
            case KICK_PLAYER:
                break;
            case NEXT_TURN:
                server.nextTurn(((GameMessage)temp).getGameID());
                break;
            case ADD_PLAYER:
                break;
            case DRAW_CARD:
                server.drawCard(((DrawCardMessage)temp).getGameID(), ((DrawCardMessage)temp).getPlayerID(), ((DrawCardMessage)temp).getCardType());
                break;
            case GET_NUMBER_OF_PLAYERS:
                break;
            case SET_NAME:
                server.setName(((SetNameMessage)temp).getGameID(), ((SetNameMessage)temp).getName());
                break;
            case SET_PLAYER_SECRET_OBJECTIVE:
                server.setPlayerSecretObjective(((SetPlayerSecretObjectiveMessage)temp).getGameID(), ((SetPlayerSecretObjectiveMessage)temp).getPlayerID(), ((SetPlayerSecretObjectiveMessage)temp).getPickedCard());
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
            case GET_NAME:
            case GET_PLAYER:

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
                            Scanner in = new Scanner(socket.getInputStream());
                            PrintWriter out = new PrintWriter(socket.getOutputStream()); // May not be necessary
                            while (true) {
                                String line = in.nextLine();
                                // By creating a thread it is possible to receive and translate a new message
                                // even if the previous translation isn't finished, though this approach
                                // can be risky because there could be a chain of "order sensitive" operations.
                                // Another approach could be putting the messages in a queue and running translate()
                                // every time the queue is not empty

                                pool.submit(() -> messagesQueue.add(new Gson().fromJson(line, Message.class)));
                                receiveMessage();
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

    // TODO: Write Socket Methods to send and receive messages.
    // When a message is received, the same action is executed on server.
    // When it has to call the addView() Method, a new RemoteViewController is created HERE.
    // Inside the @Override methods of this new RemoteViewController there will be calls to
    // SocketControllerServer's methods to send messages to Client.
    // The newly created RemoteViewController is saved inside the GameController, the same way
    // RMI does: server.addView(...).
}
