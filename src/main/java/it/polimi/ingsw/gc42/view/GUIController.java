package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.ChatMessage;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.network.ClientController;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.Classes.*;
import it.polimi.ingsw.gc42.view.Dialog.*;
import it.polimi.ingsw.gc42.view.Interfaces.CardPickerListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.*;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GUIController implements ViewController {
    // Imports from the GUI
    @FXML
    public AnchorPane uiContainer;
    @FXML
    private StackPane root;
    @FXML
    private AnchorPane mainArea;
    @FXML
    private VBox dialog;
    @FXML
    private VBox miniScoreboardContainer;
    @FXML
    private AnchorPane playerTableContainer;
    @FXML
    private AnchorPane rightPlayerTableContainer;
    @FXML
    private AnchorPane topPlayerTableContainer;
    @FXML
    private AnchorPane leftPlayerTableContainer;
    @FXML
    private StackPane backgroundContainer;
    @FXML
    private Text commonTableTxt;
    @FXML
    private StackPane resourceDeckContainer;
    @FXML
    private StackPane goldDeckContainer;
    @FXML
    private ImageView resourceDown1;
    @FXML
    private ImageView resourceDown2;
    @FXML
    private ImageView goldDown1;
    @FXML
    private ImageView goldDown2;
    @FXML
    private ImageView commonObjective1;
    @FXML
    private ImageView commonObjective2;
    @FXML
    private Text objName1;
    @FXML
    private Text objDescr1;
    @FXML
    private Text objName2;
    @FXML
    private Text objDescr2;
    @FXML
    private StackPane commonObjDescriptiionBox1;
    @FXML
    private StackPane commonObjDescriptiionBox2;
    @FXML
    private ImageView fullTableButton;
    @FXML
    private ImageView redToken;
    @FXML
    private ImageView blueToken;
    @FXML
    private ImageView yellowToken;
    @FXML
    private ImageView greenToken;
    @FXML
    private StackPane chatContainer;
    @FXML
    private StackPane chatBoxContainer;
    @FXML
    private TextField chatTextField;
    @FXML
    private Button sendButton;
    @FXML
    private Text chatHintTxt;

    // Attributes
    private Dialog showingDialog;
    private NetworkController controller;
    private int gameID;
    private boolean canReadInput = true;
    private boolean isShowingDialog = false;
    private final ArrayList<Dialog> dialogQueue = new ArrayList<>();
    private boolean isCommonTableDown = false;
    private TableView table;
    private boolean playerCanDrawOrGrab = false;
    private boolean isShowingGlobalMap = false;
    private CommonTableView commonTable;
    private int playerID;
    private String playerNickname;

    private TableView rightTable = null;
    private TableView topTable = null;
    private TableView leftTable = null;

    private ScoreBoardView scoreBoard;
    private ChatView chat;

    private double currentUIScale = 1;


    public void build() {
        table = new TableView(false, this, controller);
        playerTableContainer.getChildren().addAll(table.getPane());
    }

    public void setGameController(NetworkController controller, int gameID) throws RemoteException {
        this.controller = controller;
        this.gameID = gameID;
        try {
            controller.setViewController(new ClientController(this));
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
        commonTable = new CommonTableView(controller, this,
                new DeckView(resourceDeckContainer), new DeckView(goldDeckContainer), resourceDown1,
                resourceDown2, goldDown1, goldDown2, commonObjective1, commonObjective2, objName1, objDescr1,
                objName2, objDescr2, commonObjDescriptiionBox1, commonObjDescriptiionBox2);

        scoreBoard = new ScoreBoardView(redToken, blueToken, greenToken, yellowToken);
        chat = new ChatView(true, chatContainer, chatBoxContainer, chatTextField, sendButton, chatHintTxt, this);
        root.requestFocus();
    }

    public NetworkController getNetworkController() {
        return controller;
    }

    public int getGameId() {
        return gameID;
    }

    public boolean isPlayerCanDrawOrGrab() {
        return playerCanDrawOrGrab;
    }

    public void setPlayerCanDrawOrGrab(boolean playerCanDrawOrGrab) {
        this.playerCanDrawOrGrab = playerCanDrawOrGrab;
    }

    public StackPane getRoot() {
        return root;
    }

    public void setPlayer(int playerID) throws RemoteException {
        this.playerID = playerID;
        this.playerNickname = controller.getPlayersInfo().get(playerID-1).get("Nickname");
        table.setPlayer(playerID);
        chat.refresh();
        root.requestFocus();
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    private void setOtherPlayers(int numberOfPlayers) throws RemoteException {
        boolean isRightTableEmpty = true;
        boolean isTopTableEmpty = true;
        boolean isLeftTableEmpty = true;
        ArrayList<Integer> players = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            if (i != playerID && !players.contains(i)) {
                players.add(i);
                if (numberOfPlayers == 2 && isTopTableEmpty) {
                    isTopTableEmpty = false;
                    topTable = new TableView(true, this, controller);
                    topTable.setPlayer(i);
                    topPlayerTableContainer.getChildren().add(topTable.getPane());
                } else if (numberOfPlayers > 2 && isRightTableEmpty) {
                    isRightTableEmpty = false;
                    rightTable = new TableView(true, this, controller);
                    rightTable.setPlayer(i);
                    rightPlayerTableContainer.getChildren().add(rightTable.getPane());
                } else if (numberOfPlayers == 4 && isTopTableEmpty) {
                    isTopTableEmpty = false;
                    topTable = new TableView(true, this, controller);
                    topTable.setPlayer(i);
                    topPlayerTableContainer.getChildren().add(topTable.getPane());
                } else if (numberOfPlayers >= 3 && isLeftTableEmpty){
                    isLeftTableEmpty = false;
                    leftTable = new TableView(true, this, controller);
                    leftTable.setPlayer(i);
                    leftPlayerTableContainer.getChildren().add(leftTable.getPane());
                }
            }
        }
        notifyCommonObjectivesChanged();
        notifySlotCardChanged(CardType.RESOURCECARD, 1);
        notifySlotCardChanged(CardType.RESOURCECARD, 2);
        notifySlotCardChanged(CardType.GOLDCARD, 1);
        notifySlotCardChanged(CardType.GOLDCARD, 2);
    }

    public boolean isShowingDialog() {
        return isShowingDialog;
    }

    public void setShowingDialog(boolean status) {
        isShowingDialog = status;
        if (!status && !dialogQueue.isEmpty()) {
            showDialog(dialogQueue.removeFirst());
        }
    }

    public boolean canReadInput() {
        return canReadInput;
    }

    public void blockInput() {
        canReadInput = false;
    }

    public void unlockInput() {
        canReadInput = true;
    }

    public boolean isCommonTableDown() {
        return isCommonTableDown;
    }

    public void moveDown() {
        if (!isShowingDialog && !isCommonTableDown) {
            table.getHand().moveDown();
        } else if (isCommonTableDown && !isShowingGlobalMap) {
            commonTable.onArrowKeyPressed("DOWN");
        }
    }

    public void moveUp() {
        if (!isShowingDialog && !isCommonTableDown) {
            table.getHand().moveUp();
        } else if (isCommonTableDown && !isShowingGlobalMap) {
            commonTable.onArrowKeyPressed("UP");
        }
    }

    public void moveRight() {
        if (isShowingDialog() && !isCommonTableDown) {
            onDialogKeyboardPressed("RIGHT");
        } else if (isCommonTableDown && !isShowingGlobalMap) {
            commonTable.onArrowKeyPressed("RIGHT");
        }
    }

    public void moveLeft() {
        if (isShowingDialog() && !isCommonTableDown) {
            onDialogKeyboardPressed("LEFT");
        } else if (isCommonTableDown && !isShowingGlobalMap) {
            commonTable.onArrowKeyPressed("LEFT");
        }
    }

    public void onFKeyPressed() {
        table.getHand().onFKeyPressed();
    }


    public void toggleHand() {
        if (!isShowingDialog && canReadInput && !isCommonTableDown) {
            table.toggleHand();
        }
    }

    public void flipObjective() {
        if (canReadInput() && !isShowingDialog && !isCommonTableDown) {
            blockInput();
            table.rotateObjective();
        }
    }

    public void showDialog(Dialog content) {
        if (!isShowingDialog) {
            showingDialog = content;
            dialog.getChildren().clear();
            dialog.getChildren().add(content.build());

            blockInput();
            ScaleTransition transition = new ScaleTransition(Duration.millis(150), dialog);
            transition.setFromX(0);
            transition.setToX(1.1);
            transition.setFromY(0);
            transition.setToY(1.1);
            transition.setInterpolator(Interpolator.TANGENT(Duration.millis(150), 1));
            ScaleTransition bounceBack = new ScaleTransition(Duration.millis(80), dialog);
            bounceBack.setFromX(1.1);
            bounceBack.setToX(1);
            bounceBack.setFromY(1.1);
            bounceBack.setToY(1);

            transition.setOnFinished(e -> bounceBack.play());
            bounceBack.setOnFinished(e -> unlockInput());

            table.getHand().deselectAllCards(true);
            setShowingDialog(true);
            backgroundContainer.setEffect(new GaussianBlur(10));
            if (content.isDismissible()) {
                backgroundContainer.setOnMouseClicked((e) -> {
                    hideDialog();
                });
            }
            dialog.setVisible(true);
            transition.play();
        } else {
            dialogQueue.add(content);
        }
    }

    public void hideDialog() {
        blockInput();
        ScaleTransition bounce = new ScaleTransition(Duration.millis(80), dialog);
        bounce.setFromX(1);
        bounce.setToX(1.1);
        bounce.setFromY(1);
        bounce.setToY(1.1);
        ScaleTransition transition = new ScaleTransition(Duration.millis(150), dialog);
        transition.setFromX(1.1);
        transition.setToX(0);
        transition.setFromY(1.1);
        transition.setToY(0);

        bounce.setOnFinished(e -> transition.play());
        transition.setOnFinished(e -> {
            dialog.setVisible(false);
            dialog.getChildren().clear();
            unlockInput();
            setShowingDialog(false);
        });
        backgroundContainer.setEffect(null);
        backgroundContainer.setOnMouseClicked(null);
        bounce.play();
    }

    public void onDialogKeyboardPressed(String key) {
        showingDialog.onKeyPressed(key);
    }

    @Override
    public void showSecretObjectivesSelectionDialog() {
        CardPickerDialog dialog = new CardPickerDialog("Choose a Secret Objective!", false, false, this);
        ArrayList<ObjectiveCard> cards = controller.getTemporaryObjectiveCards(playerID);
        for (ObjectiveCard card : cards) {
            dialog.addCard(card);
        }
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                controller.setPlayerSecretObjective(playerID, dialog.getPickedCardNumber());
                hideDialog();
                controller.setPlayerStatus(playerID, GameStatus.READY_TO_CHOOSE_STARTER_CARD);
            }
        });
        Platform.runLater(() -> showDialog(dialog));
    }

    @Override
    public void showStarterCardSelectionDialog() {
        CardPickerDialog dialog = new CardPickerDialog("This is your Starter Card, choose a Side!", false
                , true, this);
        StarterCard card = controller.getTemporaryStarterCard(playerID);
        dialog.addCard(card);
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                Card card = dialog.getPickedCard();
                if (!card.isFrontFacing()) {
                    controller.flipStarterCard(playerID);
                }
                controller.setPlayerStarterCard(playerID);
                hideDialog();
                controller.setPlayerStatus(playerID, GameStatus.READY_TO_DRAW_STARTING_HAND);
            }
        });
        Platform.runLater(() -> showDialog(dialog));
    }

    @Override
    public void showTokenSelectionDialog() {
        SharedTokenPickerDialog dialog = new SharedTokenPickerDialog("Pick your Token!", false, this);
        dialog.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                controller.setPlayerToken(playerID, dialog.getPickedToken());
                hideDialog();
                controller.setPlayerStatus(playerID, GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
            }
        });
        Platform.runLater(() -> showDialog(dialog));
    }

    public void onEnterPressed() {
        if (!isCommonTableDown) {
            if (controller.getPlayerStatus(playerID) == GameStatus.MY_TURN) {
                table.playCard();
            }
        } else if (!isShowingGlobalMap){
            try {
                commonTable.onEnterPressed();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initMiniScoreBoard() throws RemoteException {
        ArrayList<Player> notOrderedPlayers = new ArrayList<>();
        int number = controller.getNumberOfPlayers();
        ArrayList<HashMap<String, String>> allPlayers = controller.getPlayersInfo();
        for (HashMap<String, String> player: allPlayers) {
            Player newPlayer = new Player(player.get("Nickname"));
            newPlayer.setPoints(Integer.parseInt(player.get("Points")));
            Token token = null;
            switch (player.get("Token")) {
                case "Blue" -> token = Token.BLUE;
                case "Red" -> token = Token.RED;
                case "Yellow" -> token = Token.YELLOW;
                case "Green" -> token = Token.GREEN;
            }
            newPlayer.setToken(token);
            notOrderedPlayers.add(newPlayer);
        }
        Platform.runLater(() -> {
            ArrayList<Player> players = new ArrayList<>();
            ArrayList<Integer> alreadySeen = new ArrayList<>();
            // Players are ordered by their points
            while (alreadySeen.size() != number) {
                int currentMax = 0;
                int currentMaxPoints = 0;
                for (int i = 0; i < number; i++) {
                    if (notOrderedPlayers.get(i).getPoints() >= currentMaxPoints && !alreadySeen.contains(i)) {
                        currentMax = i;
                        currentMaxPoints = notOrderedPlayers.get(i).getPoints();
                    }
                }
                players.add(notOrderedPlayers.get(currentMax));
                alreadySeen.add(currentMax);
            }

            miniScoreboardContainer.getChildren().clear();
            for (Player player : players) {
                if (null != player.getToken()) {
                    ImageView tokenView = null;
                    switch (player.getToken()) {
                        case BLUE ->
                                tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blueToken.png"))));
                        case RED ->
                                tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/redToken.png"))));
                        case YELLOW ->
                                tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/yellowToken.png"))));
                        case GREEN ->
                                tokenView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/greenToken.png"))));
                    }
                    tokenView.setPreserveRatio(true);
                    tokenView.setFitWidth(15);

                    Label name = new Label(player.getNickname());
                    name.setFont(Font.font("Tahoma Regular", 15));
                    name.setTextOverrun(OverrunStyle.ELLIPSIS);
                    name.setTextAlignment(TextAlignment.LEFT);
                    name.setMaxWidth(100);
                    name.setMinWidth(50);

                    if (players.indexOf(player) == 0) {
                        name.setTextFill(Paint.valueOf("gold"));
                    } else {
                        name.setTextFill(Paint.valueOf("white"));
                    }
                    name.setTextAlignment(TextAlignment.LEFT);

                    Text points = new Text(String.valueOf(player.getPoints()));
                    points.setFont(Font.font("Tahoma Bold", 12));
                    points.setStrokeWidth(25);
                    if (players.indexOf(player) == 0) {
                        points.setFill(Paint.valueOf("gold"));
                    } else {
                        points.setFill(Paint.valueOf("white"));
                    }

                    HBox container = new HBox(tokenView, name, points);
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setSpacing(5);

                    miniScoreboardContainer.getChildren().add(container);
                }
            }
        });
    }

    public void refreshScoreBoard() {
        try {
            initMiniScoreBoard();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void toggleCommonTable() {
        if (canReadInput() && !isShowingDialog && !isShowingGlobalMap) {
            if (!isCommonTableDown) {
                bringCommonTableDown();
            } else {
                bringCommonTableUp();
            }
        }
    }

    public void bringCommonTableDown() {
        blockInput();
        isCommonTableDown = true;

        TranslateTransition transition = new TranslateTransition(Duration.millis(400), mainArea);
        transition.setByY((mainArea.getHeight()+500)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("Go back to your Table");
            fullTableButton.setScaleX(0);
            fullTableButton.setScaleY(0);
            fullTableButton.setVisible(true);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), fullTableButton);
            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
        transition.play();

    }

    public void bringCommonTableUp() {
        if (!isShowingGlobalMap) {
            blockInput();
            isCommonTableDown = false;
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), fullTableButton);
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(0);
            scaleTransition.setToY(0);
            scaleTransition.setOnFinished((e) -> {
                fullTableButton.setVisible(false);
                fullTableButton.setScaleX(1);
                fullTableButton.setScaleY(1);
            });
            scaleTransition.play();

            TranslateTransition transition = new TranslateTransition(Duration.millis(400), mainArea);
            transition.setByY(-(mainArea.getHeight() + 500) / 2);
            transition.setOnFinished((e) -> {
                unlockInput();
                commonTableTxt.setText("See the Common Table");
                if (null != table.getHand().getHandCardView(1).getModelCard()
                        && null != table.getHand().getHandCardView(2).getModelCard()
                        && null != table.getHand().getHandCardView(3).getModelCard()) {
                }
            });
            transition.play();
        }
    }

    @Override
    public int getOwner(){
        return playerID;
    }

    @Override
    public void askToDrawOrGrab() {
        setPlayerCanDrawOrGrab(true);
        if (!isCommonTableDown) {
            bringCommonTableDown();
        }
    }

    @Override
    public void notifyGameIsStarting() {
        // Don't need
    }

    @Override
    public void notifyDeckChanged(CardType type) {
        switch (type) {
            case RESOURCECARD -> commonTable.refreshResourceDeck();
            case GOLDCARD -> commonTable.refreshGoldDeck();
        }
    }

    @Override
    public void notifySlotCardChanged(CardType type, int slot) {
        switch (type) {
            case RESOURCECARD -> commonTable.refreshResourceSlot(slot);
            case GOLDCARD -> commonTable.refreshGoldSlot(slot);
        }
    }

    @Override
    public void notifyPlayersPointsChanged(Token token, int newPoints) {
        refreshScoreBoard();
        Platform.runLater(() -> scoreBoard.animatePath(token, newPoints));
    }

    @Override
    public void notifyNumberOfPlayersChanged() {
        // Don't need
    }

    @Override
    public void notifyPlayersTokenChanged(int playerID) {
        if (this.playerID == playerID) {
            table.refreshToken();
        } else if (null != rightTable && playerID == rightTable.getPlayer()) {
            rightTable.refreshToken();
        } else if (null != topTable && playerID == topTable.getPlayer()) {
            topTable.refreshToken();
        } else if (null != leftTable && playerID == leftTable.getPlayer()) {
            leftTable.refreshToken();
        }
        Token token = controller.getPlayerToken(playerID);
        if (this.playerID != playerID && isShowingDialog && showingDialog instanceof SharedTokenPickerDialog) {
            ((SharedTokenPickerDialog) showingDialog).grayToken(token);
        }
        scoreBoard.setTokenInPosition(token, 0);
    }

    @Override
    public void notifyPlayersPlayAreaChanged(int playerID) {
        if (this.playerID == playerID) {
            table.refreshPlayArea();
        } else if (null != rightTable && playerID == rightTable.getPlayer()) {
            rightTable.refreshPlayArea();
        } else if (null != topTable && playerID == topTable.getPlayer()) {
            topTable.refreshPlayArea();
        } else if (null != leftTable && playerID == leftTable.getPlayer()) {
            leftTable.refreshPlayArea();
        }
    }

    @Override
    public void notifyPlayersHandChanged(int playerID) {
        if (this.playerID == playerID) {
            table.refreshHand();
        } else if (null != rightTable && playerID == rightTable.getPlayer()) {
            rightTable.refreshHand();
        } else if (null != topTable && playerID == topTable.getPlayer()) {
            topTable.refreshHand();
        } else if (null != leftTable && playerID == leftTable.getPlayer()) {
            leftTable.refreshHand();
        }
    }

    @Override
    public void notifyHandCardWasFlipped(int playedID, int cardID) {
        if (this.playerID == playedID) {
            table.flipCard(cardID+1);
        }
    }

    @Override
    public void notifyPlayersObjectiveChanged(int playerID) {
        if (this.playerID == playerID) {
            table.refreshSecretObjective();
        } else if (null != rightTable && playerID == rightTable.getPlayer()) {
            rightTable.refreshSecretObjective();
        } else if (null != topTable && playerID == topTable.getPlayer()) {
            topTable.refreshSecretObjective();
        } else if (null != leftTable && playerID == leftTable.getPlayer()) {
            leftTable.refreshSecretObjective();
        }
    }

    @Override
    public void notifyCommonObjectivesChanged() {
        commonTable.refreshCommonObjectives();
    }

    @Override
    public void notifyTurnChanged() {
        if (controller.getPlayerTurn() == playerID) {
            table.setCanPlayCards(true);
            InnerShadow glow = new InnerShadow();
            glow.setBlurType(BlurType.GAUSSIAN);
            glow.setColor(Color.GOLD);
            glow.setWidth(200);
            glow.setHeight(200);
            root.setEffect(glow);
        } else {
            table.setCanPlayCards(false);
            root.setEffect(null);
        }
    }

    @Override
    public void showWaitingForServerDialog() {
        TextDialog dialog = new TextDialog("Waiting for Server...", false);
        showDialog(dialog);
        controller.setPlayerStatus(playerID, GameStatus.WAITING_FOR_SERVER);
    }

    @Override
    public void getReady(int numberOfPlayers) {
        if (isShowingDialog) {
            hideDialog();
        }
        Platform.runLater(() -> {
            try {
                setOtherPlayers(numberOfPlayers);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        controller.setPlayerStatus(playerID, GameStatus.READY);
    }

    @FXML
    public void toggleGlobalMap() {
        if (canReadInput && !isShowingDialog && isCommonTableDown) {
            if (isShowingGlobalMap) {
                hideGlobalMap();
            } else {
                showGlobalMap();
            }
        }
    }

    private void showGlobalMap() {
        if (!table.getHand().isHidden()) {
            table.getHand().deselectAllCards(false);
            table.getHand().hide();
        }
        if (table.getSecretObjective().isShowingDetails()) {
            table.getSecretObjective().rotate();
        }

        blockInput();
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), uiContainer);
        transition.setFromX(currentUIScale);
        transition.setFromY(currentUIScale);
        transition.setToX(0.4);
        transition.setToY(0.4);
        transition.setOnFinished((e) -> {
            isShowingGlobalMap = true;
            unlockInput();
        });
        transition.play();
    }

    private void hideGlobalMap() {
        blockInput();
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), uiContainer);
        transition.setFromX(0.4);
        transition.setFromY(0.4);
        transition.setToX(currentUIScale);
        transition.setToY(currentUIScale);
        transition.setOnFinished((e) -> {
            isShowingGlobalMap = false;
            unlockInput();
        });
        transition.play();
    }

    @FXML
    public void toggleChat() {
        if (canReadInput) {
            chat.toggle();
        }
        if (!chat.isShowing()) {
            root.requestFocus();
        }
    }

    public boolean isShowingGlobalMap() {
        return isShowingGlobalMap;
    }

    @Override
    public void notifyLastTurn() throws RemoteException {
        // TODO: Implement
        System.out.println("Last Turn!");
    }

    @Override
    public void notifyEndGame(ArrayList<HashMap<String, String>> points) throws RemoteException {
        root.setEffect(null);
        EndGameDialog dialog = new EndGameDialog("The End", false, points, this);
        Platform.runLater(() -> {
            showDialog(dialog);
            dialog.animate();
        });
    }

    @Override
    public void notifyNewMessage(ChatMessage message) {
        chat.addMessage(message);
    }

    @FXML
    public void toggleSettingsDialog() {
        if (!isShowingGlobalMap) {
            SettingsDialog dialog = new SettingsDialog("Settings", false, this);
            dialog.setListener(new Listener() {
                @Override
                public void onEvent() {
                    hideDialog();
                }
            });
            showDialog(dialog);
        }
    }

    public void resizeUI(double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), uiContainer);
        transition.setFromX(currentUIScale);
        transition.setFromY(currentUIScale);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
        TranslateTransition chatTranslate = new TranslateTransition(Duration.millis(250), chatContainer);
        chatTranslate.setFromX(chatContainer.getTranslateX());
        chatTranslate.setToX(chatContainer.getTranslateX() - ((currentUIScale-scale)*500));
        chatTranslate.play();
        currentUIScale = scale;
    }

    public double getCurrentUIScale() {
        return currentUIScale;
    }
}