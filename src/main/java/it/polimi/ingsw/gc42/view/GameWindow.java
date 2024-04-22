package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Listener;
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

public class GameWindow extends Application {

    private boolean isFullScreen = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("/login_view.fxml"));
        Scene loginSCene = new Scene(fxmlLoaderLogin.load());

        stage.setTitle("Codex Naturalis");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png"))));
        stage.setResizable(false);

        LoginViewController loginController = fxmlLoaderLogin.getController();
        loginController.setListener(new Listener() {
            @Override
            public void onEvent() {
                try {
                    goToPlayScreen(stage, loginController.getNickName());
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



    public static void main(String[] args) {
        launch();
    }

    private void goToPlayScreen(Stage stage, String nickName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GUIController controller = fxmlLoader.getController();
        GameController gameController = new GameController();
        controller.build();
        stage.setScene(scene);
        stage.setResizable(true);
        //stage.setMinHeight(670);
        //stage.setMinWidth(800);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        controller.setGameController(gameController);

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
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("LEFT");
                        }
                    }
                    case RIGHT -> {
                        if (controller.isShowingDialog()) {
                            controller.onDialogKeyboardPressed("RIGHT");
                        }
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

        gameController.addView(controller);


        Player player = new Player(nickName);
        player.setFirst(true);
        controller.setPlayer(player);
        gameController.addPlayer(player);

        gameController.setCurrentStatus(GameStatus.READY);
        player.setStatus(GameStatus.READY);
    }
}
