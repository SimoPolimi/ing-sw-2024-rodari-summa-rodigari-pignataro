package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import it.polimi.ingsw.gc42.view.Classes.*;
import it.polimi.ingsw.gc42.view.Dialog.SharedTokenPickerDialog;
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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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
    private AnchorPane backgroundContainer;
    @FXML
    private Text commonTableTxt;

    // Attributes
    private Player player;
    private Dialog showingDialog;
    private GameController gameController;
    private boolean canReadInput = true;
    private ObjectiveCardView objectiveCardView;
    private boolean isShowingDialog = false;
    private final ArrayList<Dialog> dialogQueue = new ArrayList<>();
    private boolean isCommonTableDown = false;
    private TableView table;


    public void build() {
        table = new TableView(true, 0, this);
        playerTableContainer.getChildren().addAll(table.getPane());
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.getGame().setListener(new Listener() {
            @Override
            public void onEvent() {
                refreshScoreBoard();
            }
        });
    }

    public GameController getGameController() {
        return gameController;
    }

    public StackPane getRoot() {
        return root;
    }

    public void setPlayer(Player player) {
        this.player = player;
        table.setPlayer(player);
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
        }
    }

    public void moveUp() {
        if (!isShowingDialog && !isCommonTableDown) {
            table.getHand().moveUp();
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
        player.drawSecretObjectives(gameController.getGame().getObjectivePlayingDeck());
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
        table.playCard();
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

                if (player.isFirst()) {
                    name.setTextFill(Paint.valueOf("gold"));
                } else {
                    name.setTextFill(Paint.valueOf("white"));
                }
                name.setTextAlignment(TextAlignment.LEFT);

                Text points = new Text(String.valueOf(player.getPoints()));
                points.setFont(Font.font("Tahoma Bold", 12));
                points.setStrokeWidth(25);
                if (player.isFirst()) {
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
        if (canReadInput()) {
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
        transition.setByY((mainArea.getHeight()+400)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("Go back to your Table");
        });
        transition.play();

    }

    public void bringCommonTableUp() {
        blockInput();
        isCommonTableDown = false;

        TranslateTransition transition = new TranslateTransition(Duration.millis(400), mainArea);
        transition.setByY(-(mainArea.getHeight()+400)/2);
        transition.setOnFinished((e) -> {
            unlockInput();
            commonTableTxt.setText("See the Common Table");
        });
        transition.play();

    }
}