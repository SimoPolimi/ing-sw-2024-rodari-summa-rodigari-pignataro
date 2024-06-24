package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.classes.game.Token;
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
import java.util.concurrent.*;

/**
 * This Class handles the Socket Client
 */
public class SocketClient implements NetworkController {
    // Attributes
    private final String ipAddress;
    private final int port = 23690;
    private int gameID;
    private boolean isConnected = false;
    private int playerID;
    private Player owner;
    private Socket server;
    private PrintWriter out;
    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;
    private final BlockingDeque<Message> queue = new LinkedBlockingDeque<>();
    private boolean alreadySetRemoteView = false;
    private boolean alive = true;

    private ClientController clientController;

    /**
     * Constructor Method
     * @param ipAddress the Client's IP Address
     */
    public SocketClient(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Connects to the Server, creating a Socket
     * @throws IOException in case of a Network Communication Error
     */
    @Override
    public void connect() throws IOException {
        server = new Socket(ipAddress, port);
        isConnected = true;
        streamOut = new ObjectOutputStream(server.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(server.getInputStream());
        receiveMessage();

    }

    /**
     * Closes the Socket Connection and sends one last message to the Server
     */
    @Override
    public void disconnect() {
        sendMessage(new PlayerMessage(MessageType.DISCONNECT_PLAYER, gameID, playerID));
    }

    /**
     * Translates a Message into an action to execute on the View
     */
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

    /**
     * Translates the Message
     * @param message the Message to translate
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Getter Method for the Player's PlayField
     * @param playerID the Player's playerID
     * @return an ArrayList containing all the Cards played by the Player
     */
    @Override
    public ArrayList<PlayableCard> getPlayersPlayfield(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYERS_PLAY_FIELD, gameID, playerID));
        return ((PlayableCardListResponse) waitResponse(MessageType.GET_PLAYERS_PLAY_FIELD)).getResponse();
    }

    /**
     * Waits for a response after a GET message
     * @param messageType the Get MessageType to wait for
     * @return the Message containing the response
     */
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

    /**
     * Sends a Message to the Server through the Socket Connection
     * @param message the Message to send
     */
    public synchronized void sendMessage(Message message) {
        try{
            streamOut.writeObject(message);
            streamOut.reset();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter Method for isConnected
     * @return a boolean value indicating if it's connected or not
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Saves the View class on which to execute actions, and sends a message to subscribe
     * to a specific Game to receive messages from
     * @param viewController the View class
     * @throws AlreadyBoundException if the Class is already connected
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Adds a Player into a Game
     * @param player the Player to add
     */
    @Override
    public void addPlayer(Player player) {
        sendMessage(new AddPlayerMessage(MessageType.ADD_PLAYER, gameID, player));
        // Set owner
        this.owner = player;
        playerID = ((IntResponse) waitResponse(MessageType.ADD_PLAYER)).getResponse();
    }

    /**
     * Removes the Player from the Game entirely
     * @param player the Player's playerID
     * @return a boolean value indicating if the removal was successful or not
     */
    @Override
    public boolean kickPlayer(Player player) {
        sendMessage(new GameMessage(MessageType.KICK_PLAYER, gameID));
        return false;
    }

    /**
     * Triggers a Turn Change
     */
    @Override
    public void nextTurn() {
        sendMessage(new GameMessage(MessageType.NEXT_TURN, gameID));
    }

    /**
     * Plays a Card from a certain Player in a specific set of Coordinates
     * The Card to play is defined as the index of the Card in the Player's Hand.
     * The Coordinates need to be in the Model's format
     * @param handCard the index of the Card inside the Player's Hand [1, 2, 3]
     * @param x the x Coordinate where the Card will be placed
     * @param y the y Coordinate where the Card will be placed
     */
    @Override
    public void playCard(int handCard, int x, int y) {
        sendMessage(new PlayCardMessage(MessageType.PLAY_CARD, gameID, playerID, handCard, x, y));
    }

    /**
     * Flips one of the Player's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     */
    @Override
    public void flipCard(int playerID, int cardID) {
        sendMessage(new FlipCardMessage(MessageType.FLIP_CARD, gameID, playerID, cardID));
    }

    /**
     * Draws a Card from one of the 2 Decks and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Deck to draw [RESOURCECARD, GOLDCARD]
     */
    @Override
    public void drawCard(int playerID, CardType type) {
        sendMessage(new DrawCardMessage(MessageType.DRAW_CARD, gameID, playerID, type));
    }

    /**
     * Grabs a Card from one of the 4 Slots and places it inside the Player's Hand
     * @param playerID the Player's playerID
     * @param type a CardType indicating from which Slots to grab [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot in particular [1, 2]
     */
    @Override
    public void grabCard(int playerID, CardType type, int slot) {
        sendMessage(new GrabCardMessage(MessageType.GRAB_CARD, gameID, playerID, type, slot));
    }

    /**
     * Setter Method for the Game's GameStatus
     * @param status the GameStatus to set
     */
    @Override
    public void setCurrentStatus(GameStatus status) {
        sendMessage(new SetCurrentStatusMessage(MessageType.SET_CURRENT_STATUS, gameID, status));
    }

    /**
     * Getter Method for the List of Available Games that a Player can join.
     * The data is returned in an HashMap that uses String as a key and as a value:
     * all data is written in the String format, including numbers, and all keys are String as well.
     * Each HashMap contains the data of a single Game, and they are all contained in an ArrayList of
     * HashMaps, so that one HashMap for every Game can be found.
     * The data can be retrieved using the following Keys:
     * - Name: the Game's name
     * - NumberOfPlayers: the number of Players already inside it
     * - Status: a String representation of the Game's GameStatus
     * - NumberOfDisconnectedPlayers: the number of Players in the Game but disconnected,
     * used to iterate through the next elements
     * - DisconnectedPlayerX: the Disconnected Players' Nickname, so that the Client can determine if the User
     * can re-join this Game or not. NOTE: There are multiple of them, starting from DisconnectedPlayer0 all the way to
     * DisconnectedPlayerN, where N = NumberOfDisconnectedPlayers - 1.
     * @return the List of Games
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public ArrayList<HashMap<String, String>> getAvailableGames() throws RemoteException {
        sendMessage(new Message(MessageType.GET_AVAILABLE_GAMES));
        return ((ListMapStrStrResponse) waitResponse(MessageType.GET_AVAILABLE_GAMES)).getResponse();
    }

    /**
     * Picks a Game and saves its gameID
     * @param index the Game's gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void pickGame(int index) throws RemoteException {
        gameID = index;
    }

    /**
     * Sends a message to the Server telling it to create a new Game.
     * Saves the newly obtained gameID
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void getNewGameController() throws RemoteException {
        sendMessage(new Message(MessageType.NEW_GAME));
        gameID = ((IntResponse) waitResponse(MessageType.NEW_GAME)).getResponse();
    }

    /**
     * Getter Method for the gameID
     * @return the gameID
     * @throws RemoteException in case of a Network Communication Error
     */
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

    /**
     * Setter Method for the Game's name
     * @param name a String containing the Name to set in the Game
     * @throws RemoteException in case of a Network Communication Error
     */
    @Override
    public void setName(String name) throws RemoteException {
        sendMessage(new SetNameMessage(MessageType.SET_NAME, gameID, name));
    }

    /**
     * Getter Method for the Player's playerID
     * @param nickName the Player's Nickname
     * @return the Player's playerID
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public int getIndexOfPlayer(String nickName) throws RemoteException {
        sendMessage(new GetNameMessage(MessageType.GET_INDEX_OF_PLAYER, gameID, nickName));
        return ((IntResponse) waitResponse(MessageType.GET_INDEX_OF_PLAYER)).getResponse();
    }

    /**
     * Getter Method for the number of Players inside a Game
     * @return the number of Players already in that Game
     */
    @Override
    public int getNumberOfPlayers() {
        sendMessage(new GameMessage(MessageType.GET_NUMBER_OF_PLAYERS, gameID));
        return ((IntResponse) waitResponse(MessageType.GET_NUMBER_OF_PLAYERS)).getResponse();
    }

    /**
     * Sends a message to the Server telling it to start a specific Game
     */
    @Override
    public void startGame() {
        sendMessage(new GameMessage(MessageType.START_GAME, gameID));
    }

    /**
     * Setter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @param status the GameStatus to set
     */
    @Override
    public void setPlayerStatus(int playerID, GameStatus status) {
        sendMessage(new SetPlayerStatusMessage(MessageType.SET_PLAYER_STATUS, gameID, playerID, status));
    }

    /**
     * Setter Method for the Player's Token
     * @param playerID the Player's playerID
     * @param token the Token to set
     */
    @Override
    public void setPlayerToken(int playerID, Token token) {
        sendMessage(new SetPlayerTokenMessage(MessageType.SET_PLAYER_TOKEN, gameID, playerID, token));
    }

    /**
     * Setter Method for the Player's Secret Objective.
     * The Secret Objective is already stored inside the Model and IS NOT sent in here.
     * The Player has 2 Temporary Secret Objectives: this method contains either 1 or 2, indicating
     * which one to keep.
     * @param playerID the Player's playerID
     * @param pickedCard an int representing the User's choice [1, 2]
     */
    @Override
    public void setPlayerSecretObjective(int playerID, int pickedCard) {
        sendMessage(new SetPlayerSecretObjectiveMessage(MessageType.SET_PLAYER_SECRET_OBJECTIVE, gameID, playerID, pickedCard));
    }

    /**
     * Setter Method for the Player's Starter Card.
     * The Starter Card is already inside the Model, in the temporaryStarterCard field.
     * This method tells the Server to move it from there to the Player's PlayArea in (0, 0).
     * @param playerID the Player's playerID
     */
    @Override
    public void setPlayerStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.SET_PLAYER_STARTER_CARD, gameID, playerID));
    }

    /**
     * Flips the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     */
    @Override
    public void flipStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.FLIP_STARTER_CARD, gameID, playerID));
    }

    /**
     * Getter Method for the Decks.
     * @param type a CardType indicating which Deck to return
     * @return an ArrayList of Card, containing all the Cards inside the Deck, in the same order
     */
    @Override
    public ArrayList<Card> getDeck(CardType type) {
        sendMessage(new GetDeckTexturesMessage(MessageType.GET_DECK, gameID, type));
        return ((DeckResponse) waitResponse(MessageType.GET_DECK)).getResponse();
    }

    /**
     * Getter Method for the Slots
     * @param type a CardType indicating which Slots to consider [RESOURCECARD, GOLDCARD]
     * @param slot an int indicating which Slot to return [1, 2]
     * @return a copy of the Card inside that Slot
     */
    @Override
    public Card getSlotCard(CardType type, int slot) {
        sendMessage(new GetSlotCardTextureMessage(MessageType.GET_SLOT_CARD, gameID, type, slot));
        return ((CardResponse) waitResponse(MessageType.GET_SLOT_CARD)).getResponse();
    }

    /**
     * Getter Method for the Player's Secret Objective
     * @param playerID the Player's playerID
     * @return the Player's ObjectiveCard
     */
    @Override
    public ObjectiveCard getSecretObjective(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_SECRET_OBJECTIVE, gameID, playerID));
        return ((ObjectiveCardResponse) waitResponse(MessageType.GET_SECRET_OBJECTIVE)).getResponse();
    }

    /**
     * Getter Method for the Current Turn
     * @return the playerID corresponding to the Player who has the Turn
     */
    @Override
    public int getPlayerTurn() {
        sendMessage(new GameMessage(MessageType.GET_PLAYER_TURN, gameID));
        return ((IntResponse) waitResponse(MessageType.GET_PLAYER_TURN)).getResponse();
    }

    /**
     * Getter Methods for the Temporary Secret Objectives
     * @param playerID the Player's playerID
     * @return an ArrayList containing the Players Temporary Secret Objectives
     */
    @Override
    public ArrayList<ObjectiveCard> getTemporaryObjectiveCards(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_TEMPORARY_OBJECTIVE_CARDS, gameID, playerID));
        return ((ObjCardListResponse) waitResponse(MessageType.GET_TEMPORARY_OBJECTIVE_CARDS)).getResponse();
    }

    /**
     * Getter Method for the Player's Temporary Starter Card
     * @param playerID the Player's playerID
     * @return the Player's Temporary Starter Card
     */
    @Override
    public StarterCard getTemporaryStarterCard(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_TEMPORARY_STARTER_CARD, gameID, playerID));
        return ((StarterCardResponse) waitResponse(MessageType.GET_TEMPORARY_STARTER_CARD)).getResponse();
    }

    /**
     * Getter Method for the Player's GameStatus
     * @param playerID the Player's playerID
     * @return the Player's GameStatus
     */
    @Override
    public GameStatus getPlayerStatus(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYER_STATUS, gameID, playerID));
        return ((GameStatusResponse) waitResponse(MessageType.GET_PLAYER_STATUS)).getResponse();
    }

    /**
     * Getter Method for the Player's info.
     * Each Player's info are contained inside an HashMap, and both Keys and Values are String.
     * The HashMap are contained inside an ArrayList, so that there is an HashMap for each Player.
     * The data can be retrieved with the following Keys:
     * - Nickname: the Player's Nickname
     * - Points: the Player's points
     * - Token: a String representation of the Player's Token [RED, BLUE, GREEN, YELLOW]
     * @return the Collection of data
     */
    @Override
    public ArrayList<HashMap<String, String>> getPlayersInfo() {
        sendMessage(new GameMessage(MessageType.GET_PLAYERS_INFO, gameID));
        return ((ListMapStrStrResponse) waitResponse(MessageType.GET_PLAYERS_INFO)).getResponse();
    }

    /**
     * Getter Method for the Player's Hand's size
     * @param playerID the Player's playerID
     * @return the number of Cards inside the Player's Hand
     */
    @Override
    public int getPlayersHandSize(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYERS_HAND_SIZE, gameID, playerID));
        return ((IntResponse) waitResponse(MessageType.GET_PLAYERS_HAND_SIZE)).getResponse();
    }

    /**
     * @param playerID the Player's playerID
     * @return a boolean value indicating if the Player is the first
     */
    @Override
    public boolean isPlayerFirst(int playerID) {
        sendMessage(new PlayerMessage(MessageType.IS_PLAYER_FIRST, gameID, playerID));
        return ((BoolResponse) waitResponse(MessageType.IS_PLAYER_FIRST)).getResponse();
    }

    /**
     * Getter Method for the Available Placements.
     * Returns an ArrayList of Coordinates, each indicating a position where a Card can be played
     * inside the Player's PlayArea
     * @param playerID the Player's playerID
     * @return an ArrayList of Coordinates
     */
    @Override
    public ArrayList<Coordinates> getAvailablePlacements(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_AVAILABLE_PLACEMENT, gameID, playerID));
        return ((ListCoordResponse) waitResponse(MessageType.GET_AVAILABLE_PLACEMENT)).getResponse();
    }

    /**
     *
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return a boolean value indicating if the Card's cost requirements are satisfied and the Card can be played
     */
    @Override
    public boolean canCardBePlayed(int playerID, int cardID) {
        sendMessage(new CanCardBePlayedMessage(MessageType.CAN_CARD_BE_PLAYED, gameID, playerID, cardID));
        return ((BoolResponse) waitResponse(MessageType.CAN_CARD_BE_PLAYED)).getResponse();
    }

    /**
     * Getter Method for the Player's Token
     * @param playerID the Player's playerID
     * @return the Player's Token
     */
    @Override
    public Token getPlayerToken(int playerID) {
        sendMessage(new PlayerMessage(MessageType.GET_PLAYER_TOKEN, gameID, playerID));
        return ((TokenResponse) waitResponse(MessageType.GET_PLAYER_TOKEN)).getResponse();
    }

    /**
     * Getter Method for the Player's last played Card
     * @param playerID the Player's playerID
     * @return the Card last played by the Player
     */
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

    /**
     * Getter Method for the Player's Hand's Cards
     * @param playerID the Player's playerID
     * @param cardID the index of the Card inside the Player's Hand [0, 1, 2]
     * @return the Card
     */
    @Override
    public PlayableCard getPlayersHandCard(int playerID, int cardID) {
        sendMessage(new GetPlayersHandCardMessage(MessageType.GET_PLAYERS_HAND_CARD, gameID, playerID, cardID));
        return ((PlayableCardResponse) waitResponse(MessageType.GET_PLAYERS_HAND_CARD)).getResponse();
    }

    /**
     * Sends a message in the Game's Chat
     * @param playerID the Player's playerID
     * @param message a String containing the Message content
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public void sendMessage(int playerID, String message) throws RemoteException {
        sendMessage(new SendMessageMessage(MessageType.SEND_MESSAGE, gameID, playerID, message));
    }

    /**
     * Getter Method for the Game's Chat
     * @return an ArrayList of Message, containing the whole Chat
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public ArrayList<ChatMessage> getFullChat() throws RemoteException {
        sendMessage(new GameMessage(MessageType.GET_FULL_CHAT, gameID));
        return ((ChatResponse) waitResponse(MessageType.GET_FULL_CHAT)).getResponse();
    }

    /**
     * Checks if a Nickname is available
     * @param nickname a String containing the Nickname
     * @return a boolean value indicating if the Nickname is available or not
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public boolean checkNickName(String nickname) throws RemoteException {
        sendMessage(new StrResponse(MessageType.CHECK_NICKNAME, nickname));
        return ((BoolResponse) waitResponse(MessageType.CHECK_NICKNAME)).getResponse();
    }

    /**
     * Blocks a Nickname, so that nobody else can use it
     * @param nickname a String containing the Nickname
     * @throws RemoteException in case of a Network Connection Error
     */
    @Override
    public void blockNickName(String nickname) throws RemoteException {
        sendMessage(new StrResponse(MessageType.BLOCK_NICKNAME, nickname));
    }

    /**
     * Sets the Player as NOT DISCONNECTED, allowing it to play again
     * @param playerID the Player's playerID
     */
    @Override
    public void rejoinGame(int playerID) {
        sendMessage(new PlayerMessage(MessageType.REJOIN_GAME, gameID, playerID));
    }

    @Override
    public ObjectiveCard getCommonObjective(int cardID) throws RemoteException {
        sendMessage(new GetSlotCardTextureMessage(MessageType.GET_COMMON_OBJECTIVE, gameID, CardType.OBJECTIVECARD, cardID));
        return ((ObjectiveCardResponse) waitResponse(MessageType.GET_COMMON_OBJECTIVE)).getResponse();
    }
}
