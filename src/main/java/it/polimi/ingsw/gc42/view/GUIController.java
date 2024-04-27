package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import it.polimi.ingsw.gc42.network.NetworkController;
import it.polimi.ingsw.gc42.view.Classes.*;
import it.polimi.ingsw.gc42.view.Dialog.SharedTokenPickerDialog;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.*;
import it.polimi.ingsw.gc42.view.Dialog.CardPickerDialog;
import it.polimi.ingsw.gc42.view.Dialog.Dialog;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class GUIController implements ViewController {
    // Imports from the GUI
    @FXML
    private StackPane root;
    @FXML
    private AnchorPane mainArea;
    //private StackPane mainArea;
    @FXML
    private VBox dialog;
    @FXML
    private VBox miniScoreboardContainer;
    @FXML
    private StackPane commonTableContainer;
    @FXML
    private AnchorPane playerTableContainer;
    @FXML
    private AnchorPane rightPlayerTableContainer;
    @FXML
    private AnchorPane topPlayerTableContainer;
    @FXML
    private AnchorPane leftPlayerTableContainer;
    @FXML
    private AnchorPane backgroundContainer;
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

    // Attributes
    private Player player;
    private Dialog showingDialog;
    private NetworkController gameController;
    private boolean canReadInput = true;
    private boolean isShowingDialog = false;
    private final ArrayList<Dialog> dialogQueue = new ArrayList<>();
    private boolean isCommonTableDown = false;
    private TableView table;
    private boolean playerCanDrawOrGrab = false;
    private boolean isShowingGlobalMap = false;
    private CommonTableView commonTable;


    public void build() {
        table = new TableView(false, this);
        playerTableContainer.getChildren().addAll(table.getPane());
    }

    public void setGameController(NetworkController gameController) {
        this.gameController = gameController;
        gameController.getGame().setListener(new Listener() {
            @Override
            public void onEvent() {
                refreshScoreBoard();
            }
        });
        commonTable = new CommonTableView(gameController, this,
                new DeckView(resourceDeckContainer), new DeckView(goldDeckContainer), resourceDown1,
                resourceDown2, goldDown1, goldDown2, commonObjective1, commonObjective2, objName1, objDescr1,
                objName2, objDescr2, commonObjDescriptiionBox1, commonObjDescriptiionBox2);
    }

    public NetworkController getGameController() {
        return gameController;
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

    public void setPlayer(Player player) {
        this.player = player;
        table.setPlayer(player);
        setOtherPlayers();
    }

    private void setOtherPlayers() {
        boolean isRightTableEmpty = true;
        boolean isTopTableEmpty = true;
        boolean isLeftTableEmpty = true;
        TableView rightTable;
        TableView topTable;
        TableView leftTable;
        ArrayList<Player> players = new ArrayList<>();
        int numberOfPlayers = gameController.getGame().getNumberOfPlayers();
        for (int i = 1; i <= numberOfPlayers; i++) {
            if (!gameController.getGame().getPlayer(i).equals(player) && !players.contains(gameController.getGame().getPlayer(i))) {
                players.add(gameController.getGame().getPlayer(i));
                if (numberOfPlayers == 2 && isTopTableEmpty) {
                    isTopTableEmpty = false;
                    topTable = new TableView(true, this);
                    topTable.setPlayer(gameController.getGame().getPlayer(i));
                    topPlayerTableContainer.getChildren().add(topTable.getPane());
                } else if (numberOfPlayers > 2 && isRightTableEmpty) {
                    isRightTableEmpty = false;
                    rightTable = new TableView(true, this);
                    rightTable.setPlayer(gameController.getGame().getPlayer(i));
                    rightPlayerTableContainer.getChildren().add(rightTable.getPane());
                } else if (numberOfPlayers == 4 && isTopTableEmpty) {
                    isTopTableEmpty = false;
                    topTable = new TableView(true, this);
                    topTable.setPlayer(gameController.getGame().getPlayer(i));
                    topPlayerTableContainer.getChildren().add(topTable.getPane());
                } else if (numberOfPlayers >= 3 && isLeftTableEmpty){
                    isLeftTableEmpty = false;
                    leftTable = new TableView(true, this);
                    leftTable.setPlayer(gameController.getGame().getPlayer(i));
                    leftPlayerTableContainer.getChildren().add(leftTable.getPane());
                }
            }
        }
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
            if (content.isDismissable()) {
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
        ArrayList<ObjectiveCard> cards = player.getTemporaryObjectiveCards();
        for (ObjectiveCard card : cards) {
            dialog.addCard(card);
        }
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                player.setSecretObjective((ObjectiveCard) dialog.getPickedCard());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_CHOOSE_STARTER_CARD);
            }
        });
        showDialog(dialog);
    }

    @Override
    public void showStarterCardSelectionDialog() {
        Card card = gameController.getGame().getStarterDeck().draw();
        CardPickerDialog dialog = new CardPickerDialog("This is your Starter Card, choose a Side!", false
                , true, this);
        dialog.addCard(card);
        dialog.setListener(new CardPickerListener() {
            @Override
            public void onEvent() {
                player.setStarterCard((StarterCard) dialog.getPickedCard());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_DRAW_STARTING_HAND);
            }
        });
        showDialog(dialog);
    }

    @Override
    public void showTokenSelectionDialog() {
        SharedTokenPickerDialog dialog = new SharedTokenPickerDialog("Pick your Token!", false, this);
        dialog.setListener(new TokenListener() {
            @Override
            public void onEvent() {
                player.setToken(dialog.getPickedToken());
                hideDialog();
                player.setStatus(GameStatus.READY_TO_CHOOSE_SECRET_OBJECTIVE);
            }
        });
        showDialog(dialog);
    }

    public void onEnterPressed() {
        if (!isCommonTableDown) {
            table.playCard();
        } else if (!isShowingGlobalMap){
            commonTable.onEnterPressed();
        }
    }

    private void initMiniScoreBoard() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Integer> alreadySeen = new ArrayList<>();
        // Players are ordered by their points
        while (alreadySeen.size() != gameController.getGame().getNumberOfPlayers()) {
            int currentMax = 1;
            for (int i = 1; i <= gameController.getGame().getNumberOfPlayers(); i++) {
                if (gameController.getGame().getPlayer(i).getPoints() >= gameController.getGame().getPlayer(currentMax).getPoints() && !alreadySeen.contains(i)) {
                    currentMax = i;
                }
            }
            players.add(gameController.getGame().getPlayer(currentMax));
            alreadySeen.add(currentMax);
        }

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
    }

    public void refreshScoreBoard() {
        miniScoreboardContainer.getChildren().clear();
        initMiniScoreBoard();
    }

    @FXML
    public void toggleCommonTable() {
        if (canReadInput() && !isShowingGlobalMap) {
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
        transition.setByY(-(mainArea.getHeight()+500)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("See the Common Table");
            if (null != table.getHand().getHandCardView(1).getModelCard()
                    && null != table.getHand().getHandCardView(2).getModelCard()
                    && null != table.getHand().getHandCardView(3).getModelCard()) {
                setPlayerCanPlayCards(true);
            }
        });
        transition.play();

    }

    @Override
    public Player getOwner(){
        return player;
    }

    @Override
    public void askToDrawOrGrab() {
        setPlayerCanDrawOrGrab(true);
        if (!isCommonTableDown) {
            bringCommonTableDown();
        }
    }

    public void setPlayerCanPlayCards(boolean value) {
        table.setCanPlayCards(value);
    }

    @FXML
    public void toggleGlobalMap() {
        if (canReadInput && isCommonTableDown) {
            if (isShowingGlobalMap) {
                hideGlobalMap();
            } else {
                showGlobalMap();
            }
        }
    }

    private void showGlobalMap() {
        blockInput();
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), backgroundContainer);
        transition.setFromX(1);
        transition.setFromY(1);
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
        ScaleTransition transition = new ScaleTransition(Duration.millis(250), backgroundContainer);
        transition.setFromX(0.4);
        transition.setFromY(0.4);
        transition.setToX(1);
        transition.setToY(1);
        transition.setOnFinished((e) -> {
            isShowingGlobalMap = false;
            unlockInput();
        });
        transition.play();
    }

    public boolean isShowingGlobalMap() {
        return isShowingGlobalMap;
    }
}