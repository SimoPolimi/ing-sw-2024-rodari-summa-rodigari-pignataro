package it.polimi.ingsw.gc42.view.Dialog;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsDialog extends Dialog implements Observable {
    // Attributes
    private final GUIController controller;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    /**
     * Constructor Method
     *
     * @param title         a String that will be shown at the top of the Dialog, as a Title
     * @param isDismissible a boolean value that defines if the Dialog can be closed without picking a Card
     */
    public SettingsDialog(String title, boolean isDismissible, GUIController controller) {
        super(title, isDismissible);
        this.controller = controller;
    }

    @Override
    public Node build() {
        VBox container = new VBox();
        container.setSpacing(10);

        Text text1 = new Text("UI Size:");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Tahoma Bold", 18));

        HBox hbox = new HBox();
        hbox.setSpacing(20);

        VBox mode1 = new VBox();
        mode1.setSpacing(10);
        mode1.setAlignment(Pos.TOP_CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setWidth(20);
        shadow.setHeight(20);
        shadow.setBlurType(BlurType.GAUSSIAN);


        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);

        ImageView mode1Image = new ImageView();
        mode1Image.setPreserveRatio(true);
        mode1Image.setFitWidth(100);
        mode1Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/normal_GUI_Icon.png"))));
        mode1Image.setEffect(shadow);
        mode1Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(1);
            }
        });
        mode1Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode1Image.setEffect(glowEffect);
            }
        });
        mode1Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode1Image.setEffect(shadow);
            }
        });
        mode1Image.setCursor(Cursor.HAND);

        Text mode1Text = new Text("Normal");
        mode1Text.setFill(Color.WHITE);
        mode1Text.setFont(Font.font("Tahoma Regular", 15));

        mode1.getChildren().addAll(mode1Image, mode1Text);

        VBox mode2 = new VBox();
        mode2.setSpacing(10);
        mode2.setAlignment(Pos.TOP_CENTER);

        ImageView mode2Image = new ImageView();
        mode2Image.setPreserveRatio(true);
        mode2Image.setFitWidth(100);
        mode2Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/big_GUI_Icon.png"))));
        mode2Image.setEffect(shadow);
        mode2Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(0.9);
            }
        });
        mode2Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode2Image.setEffect(glowEffect);
            }
        });
        mode2Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode2Image.setEffect(shadow);
            }
        });
        mode2Image.setCursor(Cursor.HAND);

        Text mode2Text = new Text("Big");
        mode2Text.setFill(Color.WHITE);
        mode2Text.setFont(Font.font("Tahoma Regular", 15));

        mode2.getChildren().addAll(mode2Image, mode2Text);

        VBox mode3 = new VBox();
        mode3.setSpacing(10);
        mode3.setAlignment(Pos.TOP_CENTER);

        ImageView mode3Image = new ImageView();
        mode3Image.setPreserveRatio(true);
        mode3Image.setFitWidth(100);
        mode3Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huge_GUI_Icon.png"))));
        mode3Image.setEffect(shadow);
        mode3Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(0.8);
            }
        });
        mode3Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode3Image.setEffect(glowEffect);
            }
        });
        mode3Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mode3Image.setEffect(shadow);
            }
        });
        mode3Image.setCursor(Cursor.HAND);

        Text mode3Text = new Text("Huge");
        mode3Text.setFill(Color.WHITE);
        mode3Text.setFont(Font.font("Tahoma Regular", 15));

        mode3.getChildren().addAll(mode3Image, mode3Text);

        hbox.getChildren().addAll(mode1, mode2, mode3);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);

        Button closeButton = new Button();
        closeButton.setText("Close");
        closeButton.setFont(Font.font("Tahoma Bold", 20));
        closeButton.setTextFill(Color.WHITE);
        closeButton.setStyle("-fx-background-color: forestgreen; -fx-background-radius: 15");
        closeButton.setTextAlignment(TextAlignment.CENTER);
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.hideDialog();
            }
        });

        buttonContainer.getChildren().add(closeButton);

        container.getChildren().addAll(text1, hbox, buttonContainer);

        super.container.getChildren().add(container);
        return super.container;
    }

    @Override
    public void onKeyPressed(String key) {

    }

    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}
