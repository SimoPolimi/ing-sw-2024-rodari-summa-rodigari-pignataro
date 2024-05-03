package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.CardType;
import it.polimi.ingsw.gc42.model.classes.cards.ObjectiveCard;
import it.polimi.ingsw.gc42.controller.network.NetworkController;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.Objects;

public class CommonTableView {
    private NetworkController controller;
    private int gameID;
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

    public CommonTableView(NetworkController controller, int gameID, GUIController guiController, DeckView resourceDeck,
                           DeckView goldDeck, ImageView resourceDown1, ImageView resourceDown2,
                           ImageView goldDown1, ImageView goldDown2, ImageView commonObjective1, ImageView commonObjective2,
                           Text objName1, Text objDescr1, Text objName2, Text objDescr2, StackPane commonObjDescriptiionBox1, StackPane commonObjDescriptiionBox2) throws RemoteException {
        this.controller = controller;
        this.gameID = gameID;
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

        // onClick
        resourceDeck.getContainer().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
        // onClick
        goldDeck.getContainer().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
        // onClick
        resourceDown1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
        // onClick
        resourceDown2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
        // onClick
        goldDown1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
        // onClick
        goldDown2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
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
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
                try {
                    onEnterPressed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

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

    public void onEnterPressed() throws RemoteException {
        if (guiController.canReadInput() && guiController.isCommonTableDown()
                && !guiController.isShowingDialog()) {
            if (guiController.isPlayerCanDrawOrGrab()) {
                switch (selected) {
                    case 1 -> {
                        controller.drawCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.RESOURCECARD);
                    }
                    case 2 -> {
                        controller.drawCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.GOLDCARD);
                    }
                    case 3 -> {
                        controller.grabCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.RESOURCECARD, 1);
                    }
                    case 4 -> {
                        controller.grabCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.RESOURCECARD, 2);
                    }
                    case 5 -> {
                        controller.grabCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.GOLDCARD, 1);
                    }
                    case 6 -> {
                        controller.grabCard(controller.getIndexOfPlayer(guiController.getOwner().getNickname()), CardType.GOLDCARD, 2);
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

    public void refreshResourceDeck() {
        ArrayList<Card> deck = controller.getGame().getResourcePlayingDeck().getDeck().getCopy();
        Platform.runLater(() -> resourceDeck.refresh(deck));
    }

    public void refreshGoldDeck() {
        ArrayList<Card> deck = controller.getGame().getGoldPlayingDeck().getDeck().getCopy();
        Platform.runLater(() -> goldDeck.refresh(deck));
    }

    public void refreshResourceSlot(int slot) {
        switch (slot) {
            case 1 -> {
                Card card = null;
                card = controller.getGame().getResourcePlayingDeck().getSlot(1);
                if (null != card) {
                    resourceDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    resourceDown1.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
            case 2 -> {
                Card card = null;
                card = controller.getGame().getResourcePlayingDeck().getSlot(2);
                if (null != card) {
                    resourceDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    resourceDown2.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        }
    }

    public void refreshGoldSlot(int slot) {
        switch (slot) {
            case 1 -> {
                Card card = null;
                card = controller.getGame().getGoldPlayingDeck().getSlot(1);
                if (null != card) {
                    goldDown1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    goldDown1.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
            case 2 -> {
                Card card = null;
                card = controller.getGame().getGoldPlayingDeck().getSlot(2);
                if (null != card) {
                    goldDown2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getFrontImage()))));
                } else {
                    goldDown2.setVisible(false);
                }
                if (guiController.isCommonTableDown()) {
                    guiController.bringCommonTableUp();
                }
            }
        }
    }

    public void refreshCommonObjectives() {
        commonObjective1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(controller
                .getGame().getObjectivePlayingDeck().getSlot(1).getFrontImage()))));
        objName1.setText(((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getSlot(1)).getObjective().getName());
        objDescr1.setText(((ObjectiveCard) controller.getGame().getObjectivePlayingDeck().getSlot(1)).getObjective().getDescription());
        commonObjective2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(controller
                .getGame().getObjectivePlayingDeck().getSlot(2).getFrontImage()))));
        objName2.setText(((ObjectiveCard) controller.getGame().getObjectivePlayingDeck()
                .getSlot(2)).getObjective().getName());
        objDescr2.setText(((ObjectiveCard) controller.getGame().getObjectivePlayingDeck()
                .getSlot(2)).getObjective().getDescription());
    }
}
