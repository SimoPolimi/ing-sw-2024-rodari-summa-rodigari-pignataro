package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.network.interfaces.NetworkController;
import it.polimi.ingsw.gc42.view.Interfaces.ExistingGameListener;
import it.polimi.ingsw.gc42.view.Interfaces.NewGameListener;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class handles the entire GUI, from the Login View to the Game Window.
 */
public class GameWindow extends Application {

    private boolean isFullScreen = false;
    private LoginViewController loginController;
    private Player player;

    /**
     * Creates and shows the Login View scene/window, then it handles its behavior.
     * When the Scene notifies that the Login is completed, it calls goToGameSelectionScreen().
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if the view file can't be found
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("/login_view.fxml"));
        Scene loginSCene = new Scene(fxmlLoaderLogin.load());

        stage.setTitle("Codex Naturalis");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        stage.setResizable(false);

        loginController = fxmlLoaderLogin.getController();
        loginController.setListener(new Listener() {
            @Override
            public void onEvent() {
                try {
                    player = new Player(loginController.getNickName());
                    goToGameSelectionScreen(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        loginSCene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    loginController.onEnterPressed();
                }
            }
        });
        loginController.init();
        stage.setScene(loginSCene);
        stage.show();
    }

    /**
     * Launches the Application
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Creates and shows the Scene for the Game Selection Screen.
     * It handles its behaviors.
     * If the Scene notifies that the User wants to join a Game, it calls goToPlayScreen().
     * If the Scene notifies that the User wants to create a new Game, it calls goToNewGameScreen().
     * @param stage the currently shown Stage
     * @throws IOException if the view file can't be found
     */
    private void goToGameSelectionScreen(Stage stage) throws IOException {
        FXMLLoader gamesSelection = new FXMLLoader(getClass().getResource("/games-list-view.fxml"));
        Scene gamesSelectionScene = new Scene(gamesSelection.load());

        GamesListViewController gamesListViewController = gamesSelection.getController();
        gamesListViewController.setServer(loginController.getNetworkController());
        gamesListViewController.setPlayer(player);
        gamesListViewController.refresh();

        gamesListViewController.setListener(new NewGameListener() {
            @Override
            public void onEvent() {
                try {
                    goToNewGameScreen(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        gamesListViewController.setListener(new ExistingGameListener() {
            @Override
            public void onEvent() {
                try {
                    goToPlayScreen(stage, loginController.getNickName(), false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.setScene(gamesSelectionScene);
    }

    /**
     * Creates and shows the Scene for the Game Creation Screen.
     * In this Screen the User can see who joins his Game, can give it a Name, and can access the in-game Chat.
     * When he is ready he can start the Game, in which case the Scene notifies this method, who then calls
     * goToPlayScreen().
     * @param stage the currently shown Stage
     * @throws IOException if the view file can't be found.
     */
    private void goToNewGameScreen(Stage stage) throws IOException {
        FXMLLoader newGameLoader = new FXMLLoader(getClass().getResource("/new-game-view.fxml"));
        Scene newGameScene = new Scene(newGameLoader.load());
        stage.setScene(newGameScene);

        NewGameViewController newGameController = newGameLoader.getController();
        NetworkController network = loginController.getNetworkController();
        network.getNewGameController();
        newGameController.setServer(network, network.getIndex());
        newGameController.setPlayer(player);

        newGameController.setListener(new NewGameListener() {
            @Override
            public void onEvent() {
                try {
                    goToPlayScreen(stage, player.getNickname(), newGameController.getStartGame());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        stage.show();
    }

    /**
     * Creates and shows the Playing Screen.
     * It sets the Window to be resizable, and to set the minimum and maximum sizes to some pre-determined values.
     * If the User created the Game, it sets its status to WAITING_FOR_SERVER just before actually entering the Screen.
     * @param stage the currently shown Stage
     * @param nickName the User's NickName
     * @param startGame a boolean value indicating if this method needs to tell the Server to start the Game (if the User created it) or not
     * @throws IOException if the view file can't be found
     */
    private void goToPlayScreen(Stage stage, String nickName, boolean startGame) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GUIController controller = fxmlLoader.getController();
        NetworkController gameController = loginController.getNetworkController();
        stage.setScene(scene);
        stage.setResizable(true);
        //stage.setMinHeight(670);
        //stage.setMinWidth(800);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        controller.setGameController(gameController, gameController.getIndex());
        controller.build();
        controller.setPlayer(gameController.getIndexOfPlayer(nickName));

        if (startGame) {
            gameController.setCurrentStatus(GameStatus.WAITING_FOR_SERVER);
        }

        scene.setOnKeyPressed(e -> {
            if (controller.canReadInput()) {
                switch (e.getCode()) {
                    case F -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("F");
                        }
                        controller.onFKeyPressed();
                    }
                    case DOWN -> {
                        controller.moveDown();
                    }
                    case UP -> {
                        controller.moveUp();
                    }
                    case E -> {
                        controller.toggleHand();
                    }
                    case O -> {
                        controller.flipObjective();
                    }
                    case LEFT -> {
                        controller.moveLeft();
                    }
                    case RIGHT -> {
                        controller.moveRight();
                    }
                    case ENTER -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("ENTER");
                        } else {
                            controller.onEnterPressed();
                        }
                    }
                    case T -> {
                        controller.toggleCommonTable();
                    }
                    case M -> {
                        controller.toggleGlobalMap();
                    }
                    case C -> {
                        controller.toggleChat();
                    }
                }
            }
            scene.setOnKeyReleased(e1 -> {
                switch (e.getCode()) {
                    case F11 -> {
                        isFullScreen = !isFullScreen;
                        stage.setFullScreen(isFullScreen);
                    }
                }
            });
        });

        stage.show();
        controller.showWaitingForServerDialog();
    }
}
