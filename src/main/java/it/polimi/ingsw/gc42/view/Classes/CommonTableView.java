package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.controller.GameController;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.model.interfaces.GoldSlot1Listener;
import it.polimi.ingsw.gc42.model.interfaces.GoldSlot2Listener;
import it.polimi.ingsw.gc42.model.interfaces.ResourceSlot1Listener;
import it.polimi.ingsw.gc42.model.interfaces.ResourceSlot2Listener;
import it.polimi.ingsw.gc42.network.NetworkController;
import it.polimi.ingsw.gc42.view.GUIController;
import it.polimi.ingsw.gc42.view.Interfaces.DeckViewListener;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.Objects;

public class CommonTableView {
    private NetworkController gameController;
    private GUIController guiController;
    private DeckView resourceDeck;
    private DeckView goldDeck;
    private ImageView resourceDown1;
    private ImageView resourceDown2;
    private ImageView goldDown1;
    private ImageView goldDown2;
    private ImageView commonObjective1;
    private ImageView commonObjective2;
    private Text objName1;
    private Text objDescr1;
    private Text objName2;
    private Text objDescr2;
    private StackPane commonObjDescriptiionBox1;
    private StackPane commonObjDescriptiionBox2;

    private int selected = 0;
    private int lastSelected = 0;

    private int isShowingInfo = 0;

    public CommonTableView(NetworkController gameController, GUIController guiController, DeckView resourceDeck,
                           DeckView goldDeck, ImageView resourceDown1, ImageView resourceDown2,
                           ImageView goldDown1, ImageView goldDown2, ImageView commonObjective1, ImageView commonObjective2,
                           Text objName1, Text objDescr1, Text objName2, Text objDescr2, StackPane commonObjDescriptiionBox1, StackPane commonObjDescriptiionBox2) {
        this.gameController = gameController;
        this.guiController = guiController;
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.resourceDown1 = resourceDown1;
        this.resourceDown2 = resourceDown2;
        this.goldDown1 = goldDown1;
        this.goldDown2 = goldDown2;
        this.commonObjective1 = commonObjective1;
        this.commonObjective2 = commonObjective2;
        this.objName1 = objName1;
        this.objDescr1 = objDescr1;
        this.objName2 = objName2;
        this.objDescr2 = objDescr2;
        this.commonObjDescriptiionBox1 = commonObjDescriptiionBox1;
        this.commonObjDescriptiionBox2 = commonObjDescriptiionBox2;
        gameController.getGame().getResourcePlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                resourceDeck.refresh(gameController.getGame().getResourcePlayingDeck().getDeck().getCopy());

            }
        });
        // onClick
        resourceDeck.getContainer().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        resourceDeck.getContainer().setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(1);
            }
        });
        resourceDeck.getContainer().setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        gameController.getGame().getGoldPlayingDeck().getDeck().setListener(new DeckViewListener() {
            @Override
            public void onEvent() {
                goldDeck.refresh(gameController.getGame().getGoldPlayingDeck().getDeck().getCopy());
            }
        });
        // onClick
        goldDeck.getContainer().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        goldDeck.getContainer().setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(2);
            }
        });
        goldDeck.getContainer().setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        resourceDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getResourcePlayingDeck().getSlot(1).getFrontImage()))));
        gameController.getGame().setListener(new ResourceSlot1Listener() {
            @Override
            public void onEvent() {
                Card card = gameController.getGame().getResourcePlayingDeck().getSlot(1);
                if (null != card) {
                    resourceDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    resourceDown1.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        });
        // onClick
        resourceDown1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        resourceDown1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(3);
            }
        });
        resourceDown1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        resourceDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getResourcePlayingDeck().getSlot(2).getFrontImage()))));
        gameController.getGame().setListener(new ResourceSlot2Listener() {
            @Override
            public void onEvent() {
                Card card = gameController.getGame().getResourcePlayingDeck().getSlot(2);
                if (null != card) {
                    resourceDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    resourceDown2.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        });
        // onClick
        resourceDown2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        resourceDown2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(4);
            }
        });
        resourceDown2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        goldDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getGoldPlayingDeck().getSlot(1).getFrontImage()))));
        gameController.getGame().setListener(new GoldSlot1Listener() {
            @Override
            public void onEvent() {
                Card card = gameController.getGame().getGoldPlayingDeck().getSlot(1);
                if (null != card) {
                    goldDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    goldDown1.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        });
        // onClick
        goldDown1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        goldDown1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(5);
            }
        });
        goldDown1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        goldDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getGoldPlayingDeck().getSlot(2).getFrontImage()))));
        gameController.getGame().setListener(new GoldSlot2Listener() {
            @Override
            public void onEvent() {
                Card card = gameController.getGame().getGoldPlayingDeck().getSlot(2);
                if (null != card) {
                    goldDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    goldDown2.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        });
        // onClick
        goldDown2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onEnterPressed();
            }
        });
        goldDown2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(6);
            }
        });
        goldDown2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        commonObjective1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getObjectivePlayingDeck().getSlot(1).getFrontImage()))));
        objName1.setText(((ObjectiveCard) gameController.getGame().getObjectivePlayingDeck().getSlot(1)).getObjective().getName());
        objDescr1.setText(((ObjectiveCard) gameController.getGame().getObjectivePlayingDeck().getSlot(1)).getObjective().getDescription());
        commonObjective1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(7);
            }
        });
        commonObjective1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        commonObjective1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onEnterPressed();
            }
        });
        commonObjective2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gameController.getGame().getObjectivePlayingDeck().getSlot(2).getFrontImage()))));
        objName2.setText(((ObjectiveCard) gameController.getGame().getObjectivePlayingDeck().getSlot(2)).getObjective().getName());
        objDescr2.setText(((ObjectiveCard) gameController.getGame().getObjectivePlayingDeck().getSlot(2)).getObjective().getDescription());
        commonObjective2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(8);
            }
        });
        commonObjective2.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                select(0);
            }
        });
        commonObjective2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onEnterPressed();

            }
        });
    }

    public void select(int number) {
        if (number != 0) {
            if (!guiController.isShowingGlobalMap()) {
                guiController.blockInput();
                selected = number;
                DropShadow glowEffect = new DropShadow();
                glowEffect.setWidth(100);
                glowEffect.setHeight(100);
                glowEffect.setColor(Color.YELLOW);
                glowEffect.setBlurType(BlurType.GAUSSIAN);
                Node node = null;
                switch (number) {
                    case 1 -> node = resourceDeck.getContainer();
                    case 2 -> node = goldDeck.getContainer();
                    case 3 -> node = resourceDown1;
                    case 4 -> node = resourceDown2;
                    case 5 -> node = goldDown1;
                    case 6 -> node = goldDown2;
                    case 7 -> node = commonObjective1;
                    case 8 -> node = commonObjective2;
                }
                node.setEffect(glowEffect);
                ScaleTransition select1 = new ScaleTransition(Duration.millis(100), node);
                select1.setFromX(1);
                select1.setFromY(1);
                select1.setToX(1.3);
                select1.setToY(1.3);
                select1.setOnFinished(e -> guiController.unlockInput());
                select1.play();
                deselect(lastSelected);
            }
        } else {
            deselect(lastSelected);
            selected = 0;
        }
        lastSelected = selected;
    }

    public void deselect(int number) {
        if (number != 0) {
            guiController.blockInput();
            DropShadow shadow = new DropShadow();
            shadow.setWidth(50);
            shadow.setHeight(50);
            shadow.setBlurType(BlurType.GAUSSIAN);
            ;
            Node node = null;
            switch (number) {
                case 1 -> node = resourceDeck.getContainer();
                case 2 -> node = goldDeck.getContainer();
                case 3 -> node = resourceDown1;
                case 4 -> node = resourceDown2;
                case 5 -> node = goldDown1;
                case 6 -> node = goldDown2;
                case 7 -> node = commonObjective1;
                case 8 -> node = commonObjective2;
            }
            node.setEffect(shadow);
            ScaleTransition select1 = new ScaleTransition(Duration.millis(100), node);
            select1.setFromX(1.3);
            select1.setFromY(1.3);
            select1.setToX(1);
            select1.setToY(1);
            select1.setOnFinished(e -> guiController.unlockInput());
            select1.play();
            lastSelected = 0;
        }
    }

    public void onArrowKeyPressed(String key) {
        switch (selected) {
            case 0 -> select(1);
            case 1 -> {
                switch (key) {
                    case "RIGHT" -> select(7);
                    case "DOWN" -> select(3);
                }
            }
            case 2 -> {
                switch (key) {
                    case "LEFT" -> select(8);
                    case "DOWN" -> select(5);
                }
            }
            case 3 -> {
                switch (key) {
                    case "UP" -> select(1);
                    case "RIGHT" -> select(5);
                    case "DOWN" -> select(4);
                }
            }
            case 4 -> {
                switch (key) {
                    case "UP" -> select(3);
                    case "RIGHT" -> select(6);
                }
            }
            case 5 -> {
                switch (key) {
                    case "UP" -> select(2);
                    case "LEFT" -> select(3);
                    case "DOWN" -> select(6);
                }
            }
            case 6 -> {
                switch (key) {
                    case "UP" -> select(5);
                    case "LEFT" -> select(4);
                }
            }
            case 7 -> {
                switch (key) {
                    case "LEFT" -> select(1);
                    case "RIGHT" -> select(8);
                }
            }
            case 8 -> {
                switch (key) {
                    case "LEFT" -> select(7);
                    case "RIGHT" -> select(2);
                }
            }
        }
    }

    public void onEnterPressed() {
        if (guiController.canReadInput() && guiController.isCommonTableDown()
                && !guiController.isShowingDialog()) {
            if (guiController.isPlayerCanDrawOrGrab()) {
                switch (selected) {
                    case 1 -> {
                        gameController.drawCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getResourcePlayingDeck());
                    }
                    case 2 -> {
                        gameController.drawCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getGoldPlayingDeck());
                    }
                    case 3 -> {
                        gameController.grabCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getResourcePlayingDeck(), 1);
                    }
                    case 4 -> {
                        gameController.grabCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getResourcePlayingDeck(), 2);
                    }
                    case 5 -> {
                        gameController.grabCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getGoldPlayingDeck(), 1);
                    }
                    case 6 -> {
                        gameController.grabCard(gameController.getGame().getCurrentPlayer(), gameController.getGame().getGoldPlayingDeck(), 2);
                    }
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
                if (isShowingInfo != 0) {
                    commonObjDescriptiionBox1.setVisible(false);
                    commonObjDescriptiionBox2.setVisible(false);
                }
            } else if (selected == 7) {
                if (isShowingInfo == 1) {
                    commonObjDescriptiionBox1.setVisible(false);
                    commonObjDescriptiionBox2.setVisible(false);
                    isShowingInfo = 0;
                } else {
                    isShowingInfo = 1;
                    commonObjDescriptiionBox1.setVisible(true);
                    commonObjDescriptiionBox2.setVisible(false);
                }
            } else if (selected == 8) {
                if (isShowingInfo == 2) {
                    commonObjDescriptiionBox1.setVisible(false);
                    commonObjDescriptiionBox2.setVisible(false);
                    isShowingInfo = 0;
                } else {
                    isShowingInfo = 2;
                    commonObjDescriptiionBox1.setVisible(false);
                    commonObjDescriptiionBox2.setVisible(true);
                }
            }
        }
    }
}
