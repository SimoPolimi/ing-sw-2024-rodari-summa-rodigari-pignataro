package it.polimi.ingsw.gc42.view.Dialog;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * Dialog that shows the Final Points acquired by each Player.
 * For each Player it will show the following Points:
 * - Initial Points (the ones accumulated throughout the match)
 * - Secret Objective Points
 * - Common Objective 1 Points
 * - Common Objective 2 Points
 * - Total Points (the total sum).
 * <p>
 * One or more Players are chosen as Winner(s) by the Server.
 * These Players will be highlighted in a yellow, slightly bigger card/box, and will show a
 * "WINNER!" tag under their Total Points.
 * Additionally, if the GUIController that created this Dialog belongs to one of the Winner Players,
 * this Dialog will handle a winning animation, consisting of a number of confetti falling through the Screen.
 */
public class EndGameDialog extends Dialog implements Observable {
    // Attributes
    private final ArrayList<HashMap<String, String>> points;
    private final GUIController controller;
    private final ArrayList<Listener> listeners = new ArrayList<>();

    private boolean animate = false;

    // Constructor Method

    /**
     * Constructor Method
     * @param title a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     * @param points an ArrayList containing the info to show in this Dialog
     * @param controller the GUIController that created this Dialog
     */
    public EndGameDialog(String title, boolean isDismissible, ArrayList<HashMap<String, String>> points, GUIController controller) {
        super(title, isDismissible);
        this.points = points;
        this.controller = controller;
    }

    /**
     * Builds the Dialog's UI.
     * @return the Node (JavaFx element) that contains the whole UI.
     */
    @Override
    public Node build() {
        HBox container = new HBox();
        container.setSpacing(20);

        for (HashMap<String, String> player : points) {
            // Card
            VBox card = new VBox();
            card.setSpacing(10);
            card.setStyle("-fx-background-color: lightgrey; -fx-background-radius: 10");
            card.setPadding(new Insets(5, 5, 5, 5));
            DropShadow shadow = new DropShadow();
            shadow.setBlurType(BlurType.GAUSSIAN);
            shadow.setHeight(10);
            shadow.setWidth(10);
            shadow.setRadius(4.5);
            card.setEffect(shadow);

            boolean isWinner = Boolean.parseBoolean(player.get("IsWinner"));

            // Nickname
            HBox nameBox = new HBox();
            nameBox.setAlignment(Pos.CENTER);
            Text name = new Text(player.get("Nickname"));
            name.setFont(Font.font("Tahoma Bold", 15));
            nameBox.getChildren().add(name);

            // Token
            HBox tokenBox = new HBox();
            tokenBox.setAlignment(Pos.CENTER);
            ImageView token = new ImageView();
            switch (player.get("Token")) {
                case "RED" -> token.setImage(new Image("/redToken.png"));
                case "BLUE" -> token.setImage(new Image("/blueToken.png"));
                case "GREEN" -> token.setImage(new Image("/greenToken.png"));
                case "YELLOW" -> token.setImage(new Image("/yellowToken.png"));
            }
            token.setFitWidth(40);
            token.setPreserveRatio(true);
            DropShadow effect = new DropShadow();
            effect.setBlurType(BlurType.GAUSSIAN);
            effect.setWidth(20);
            effect.setHeight(20);
            effect.setRadius(24.5);
            token.setEffect(effect);
            tokenBox.getChildren().add(token);

            // Initial Points
            HBox initialBox = initTextBox("Initial Points: ", player.get("InitialPoints"));

            // Secret Objective Points
            HBox secretObjectiveBox = initTextBox("Secret Objective: ", player.get("SecretObjectivePoints"));

            // Common Objective 1
            HBox common1Box = initTextBox("Common Objective 1: ", player.get("CommonObjective1Points"));

            // Common Objective 2
            HBox common2Box = initTextBox("Common Objective 2: ", player.get("CommonObjective2Points"));

            // Final Points
            int total = Integer.parseInt(player.get("TotalPoints"));
            HBox finalBox = new HBox();
            finalBox.setAlignment(Pos.CENTER_LEFT);
            finalBox.setSpacing(5);
            Text text1 = new Text("Final Points: ");
            text1.setFont(Font.font("Tahoma Bold", 14));
            text1.setUnderline(true);
            Text text2 = new Text(String.valueOf(total));
            text2.setFont(Font.font("Tahoma Regular", 14));
            finalBox.getChildren().addAll(text1, text2);

            // Winner text
            HBox winnerBox = new HBox();
            winnerBox.setAlignment(Pos.CENTER);
            Text winnerText = new Text("WINNER!");
            winnerText.setFont(Font.font("Comic Sans MS Bold", 20));
            winnerText.setTextAlignment(TextAlignment.CENTER);
            winnerText.setFill(Paint.valueOf("gold"));
            winnerText.setStroke(Paint.valueOf("saddlebrown"));
            winnerText.setUnderline(true);
            winnerBox.getChildren().add(winnerText);
            winnerBox.setVisible(false);


            // Highlighting winner
            if (isWinner) {
                card.setStyle("-fx-background-color: #ccb619; -fx-background-radius: 10");
                card.setScaleX(1.1);
                card.setScaleY(1.1);
                winnerBox.setVisible(true);
                if (player.get("Nickname").equals(controller.getPlayerNickname())) {
                    animate = true;
                }
            }

            card.getChildren().addAll(nameBox, tokenBox, initialBox, secretObjectiveBox, common1Box,
                    common2Box, finalBox, winnerBox);

            container.getChildren().add(card);
        }

        super.container.getChildren().add(container);
        return super.container;
    }

    /**
     * Creates and initializes the Textboxes for the different entries of this Dialog.
     * @param text1 the entry's text or title
     * @param text2 the number of Points associated to this entry
     * @return an HBox (JavaFx element) containing the UI for this element
     */
    private HBox initTextBox(String text1, String text2) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(5);

        Text textbox1 = new Text(text1);
        textbox1.setFont(Font.font("Tahoma Bold", 11));

        Text textbox2 = new Text(text2);
        textbox2.setFont(Font.font("Tahoma Regular", 11));

        box.getChildren().addAll(textbox1, textbox2);
        return box;
    }

    /**
     * Handles the Keyboard's input events
     * @param key a String containing the Key's name.
     */
    @Override
    public void onKeyPressed(String key) {

    }

    /**
     * Animates the Confetti animation for the Winner Player
     */
    public void animate() {
        if (animate) {
            ArrayList<Image> images = new ArrayList<>();
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/red_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/orange_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/yellow_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pink_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/magenta_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/purple_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/green_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/light_green_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/blue_Confetti.png"))));
            images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/light_blue_Confetti.png"))));

            StackPane pane = controller.getRoot();

            addConfetti(0, images, pane);
        }
    }

    /**
     * Creates and adds to the UI a single Confetti element
     * @param count an int value representing how many Confetti have already been created
     * @param images the ArrayList of available textures, where a random one is chosen
     * @param pane the Pane (KJavaFx element) where the Confetti element will be added.
     */
    private void addConfetti(int count, ArrayList<Image> images, StackPane pane) {
        Random random = new Random();
        if (count < 1500) {
            ImageView image = new ImageView();
            image.setImage(images.get(random.nextInt(images.size() - 1)));
            image.setPreserveRatio(true);
            image.setFitWidth(10);
            image.setTranslateY(random.nextInt(-1500, -700));
            image.setTranslateX(random.nextInt((int) -(pane.getWidth()/2), (int) pane.getWidth()/2));
            image.setRotate(random.nextInt(360));
            double scale = random.nextDouble(0.9, 1.2);
            image.setScaleX(scale);
            image.setScaleY(scale);
            pane.getChildren().add(image);
            TranslateTransition transition = new TranslateTransition(Duration.millis(5000), image);
            transition.setByY(3000);
            transition.play();

            TranslateTransition timeout = new TranslateTransition(Duration.millis(1), pane);
            timeout.setByY(0);
            timeout.setOnFinished(e -> addConfetti(count+1, images, pane));
            timeout.play();
        }
    }

    // Observable Methods

    // TODO: Javadoc
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    // TODO: Javadoc
    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    // TODO: Javadoc
    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}
