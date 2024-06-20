package it.polimi.ingsw.gc42.view.Dialog;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsDialog extends Dialog implements Observable {
    // Attributes
    private final GUIController controller;

    private final ArrayList<Listener> listeners = new ArrayList<>();

    private ImageView currentScalingMode = null;
    private Text currentScalingModeText = null;

    private ImageView currentSpeedMode = null;
    private Text currentSpeedModeText = null;

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

        HBox scalingModesContainer = new HBox();
        scalingModesContainer.setSpacing(20);

        VBox scalingMode1 = new VBox();
        scalingMode1.setSpacing(10);
        scalingMode1.setAlignment(Pos.TOP_CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setWidth(20);
        shadow.setHeight(20);
        shadow.setBlurType(BlurType.GAUSSIAN);


        DropShadow glowEffect = new DropShadow();
        glowEffect.setWidth(100);
        glowEffect.setHeight(100);
        glowEffect.setColor(Color.YELLOW);
        glowEffect.setBlurType(BlurType.GAUSSIAN);

        ImageView scalingMode1Image = new ImageView();
        scalingMode1Image.setPreserveRatio(true);
        scalingMode1Image.setFitWidth(100);
        scalingMode1Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/normal_GUI_Icon.png"))));
        scalingMode1Image.setEffect(shadow);

        Text scalingMode1Text = new Text("Normal");
        scalingMode1Text.setFill(Color.WHITE);
        scalingMode1Text.setFont(Font.font("Tahoma Regular", 15));

        scalingMode1Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(1);
                animateSelection("Scaling",scalingMode1Image, scalingMode1Text);
            }
        });
        scalingMode1Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode1Image.setEffect(glowEffect);
            }
        });
        scalingMode1Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode1Image.setEffect(shadow);
            }
        });
        scalingMode1Image.setCursor(Cursor.HAND);



        scalingMode1.getChildren().addAll(scalingMode1Image, scalingMode1Text);

        VBox scalingMode2 = new VBox();
        scalingMode2.setSpacing(10);
        scalingMode2.setAlignment(Pos.TOP_CENTER);

        ImageView scalingMode2Image = new ImageView();
        scalingMode2Image.setPreserveRatio(true);
        scalingMode2Image.setFitWidth(100);
        scalingMode2Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/big_GUI_Icon.png"))));
        scalingMode2Image.setEffect(shadow);

        Text scalingMode2Text = new Text("Big");
        scalingMode2Text.setFill(Color.WHITE);
        scalingMode2Text.setFont(Font.font("Tahoma Regular", 15));

        scalingMode2Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(0.9);
                animateSelection("Scaling",scalingMode2Image, scalingMode2Text);
            }
        });
        scalingMode2Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode2Image.setEffect(glowEffect);
            }
        });
        scalingMode2Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode2Image.setEffect(shadow);
            }
        });
        scalingMode2Image.setCursor(Cursor.HAND);


        scalingMode2.getChildren().addAll(scalingMode2Image, scalingMode2Text);

        VBox scalingMode3 = new VBox();
        scalingMode3.setSpacing(10);
        scalingMode3.setAlignment(Pos.TOP_CENTER);

        ImageView scalingMode3Image = new ImageView();
        scalingMode3Image.setPreserveRatio(true);
        scalingMode3Image.setFitWidth(100);
        scalingMode3Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huge_GUI_Icon.png"))));
        scalingMode3Image.setEffect(shadow);

        Text scalingMode3Text = new Text("Huge");
        scalingMode3Text.setFill(Color.WHITE);
        scalingMode3Text.setFont(Font.font("Tahoma Regular", 15));

        scalingMode3Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.resizeUI(0.8);
                animateSelection("Scaling",scalingMode3Image, scalingMode3Text);
            }
        });
        scalingMode3Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode3Image.setEffect(glowEffect);
            }
        });
        scalingMode3Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scalingMode3Image.setEffect(shadow);
            }
        });
        scalingMode3Image.setCursor(Cursor.HAND);

        scalingMode3.getChildren().addAll(scalingMode3Image, scalingMode3Text);

        scalingModesContainer.getChildren().addAll(scalingMode1, scalingMode2, scalingMode3);

        Text text2 = new Text("Animation Speed:");
        text2.setFill(Color.WHITE);
        text2.setFont(Font.font("Tahoma Bold", 18));

        HBox animationSpeedModeContainer = new HBox();
        animationSpeedModeContainer.setSpacing(20);

        VBox speedMode1 = new VBox();
        speedMode1.setSpacing(10);
        speedMode1.setAlignment(Pos.TOP_CENTER);

        ImageView speedMode1Image = new ImageView();
        speedMode1Image.setPreserveRatio(true);
        speedMode1Image.setFitWidth(100);
        speedMode1Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/normal_GUI_Icon.png"))));
        speedMode1Image.setEffect(shadow);

        Text speedMode1Text = new Text("Slow");
        speedMode1Text.setFill(Color.WHITE);
        speedMode1Text.setFont(Font.font("Tahoma Regular", 15));

        speedMode1Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.setCurrentAnimationSpeed(400);
                animateSelection("Animation Speed",speedMode1Image, speedMode1Text);
            }
        });
        speedMode1Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode1Image.setEffect(glowEffect);
            }
        });
        speedMode1Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode1Image.setEffect(shadow);
            }
        });
        speedMode1Image.setCursor(Cursor.HAND);



        speedMode1.getChildren().addAll(speedMode1Image, speedMode1Text);

        VBox speedMode2 = new VBox();
        speedMode2.setSpacing(10);
        speedMode2.setAlignment(Pos.TOP_CENTER);

        ImageView speedMode2Image = new ImageView();
        speedMode2Image.setPreserveRatio(true);
        speedMode2Image.setFitWidth(100);
        speedMode2Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/big_GUI_Icon.png"))));
        speedMode2Image.setEffect(shadow);

        Text speedMode2Text = new Text("Normal");
        speedMode2Text.setFill(Color.WHITE);
        speedMode2Text.setFont(Font.font("Tahoma Regular", 15));

        speedMode2Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.setCurrentAnimationSpeed(250);
                animateSelection("Animation Speed",speedMode2Image, speedMode2Text);
            }
        });
        speedMode2Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode2Image.setEffect(glowEffect);
            }
        });
        speedMode2Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode2Image.setEffect(shadow);
            }
        });
        speedMode2Image.setCursor(Cursor.HAND);


        speedMode2.getChildren().addAll(speedMode2Image, speedMode2Text);

        VBox speedMode3 = new VBox();
        speedMode3.setSpacing(10);
        speedMode3.setAlignment(Pos.TOP_CENTER);

        ImageView speedMode3Image = new ImageView();
        speedMode3Image.setPreserveRatio(true);
        speedMode3Image.setFitWidth(100);
        speedMode3Image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/huge_GUI_Icon.png"))));
        speedMode3Image.setEffect(shadow);

        Text speedMode3Text = new Text("Fast");
        speedMode3Text.setFill(Color.WHITE);
        speedMode3Text.setFont(Font.font("Tahoma Regular", 15));

        speedMode3Image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.setCurrentAnimationSpeed(100);
                animateSelection("Animation Speed",speedMode3Image, speedMode3Text);
            }
        });
        speedMode3Image.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode3Image.setEffect(glowEffect);
            }
        });
        speedMode3Image.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                speedMode3Image.setEffect(shadow);
            }
        });
        speedMode3Image.setCursor(Cursor.HAND);

        speedMode3.getChildren().addAll(speedMode3Image, speedMode3Text);

        animationSpeedModeContainer.getChildren().addAll(speedMode1, speedMode2, speedMode3);

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

        container.getChildren().addAll(text1, scalingModesContainer, text2, animationSpeedModeContainer, buttonContainer);

        if (controller.getCurrentUIScale() == 1.0) {
            animateSelection("Scaling",scalingMode1Image, scalingMode1Text);
        } else if (controller.getCurrentUIScale() == 0.9) {
            animateSelection("Scaling",scalingMode2Image, scalingMode2Text);
        } else if (controller.getCurrentUIScale() == 0.8) {
            animateSelection("Scaling",scalingMode3Image, scalingMode3Text);
        }

        if (controller.getCurrentAnimationSpeed() == 400) {
            animateSelection("Animation Speed",speedMode1Image, speedMode1Text);
        } else if (controller.getCurrentAnimationSpeed() == 250) {
            animateSelection("Animation Speed",speedMode2Image, speedMode2Text);
        } else if (controller.getCurrentAnimationSpeed() == 100) {
            animateSelection("Animation Speed",speedMode3Image, speedMode3Text);
        }

        super.container.getChildren().add(container);
        return super.container;
    }

    private void animateSelection(String mode, ImageView newMode, Text newModeText) {
        if (mode.equals("Scaling")) {
            if (null != currentScalingMode) {
                ScaleTransition zoomOut = new ScaleTransition(Duration.millis(250), currentScalingMode);
                zoomOut.setFromX(1.1);
                zoomOut.setFromY(1.1);
                zoomOut.setToX(1);
                zoomOut.setToY(1);
                zoomOut.play();

                currentScalingModeText.setFill(Color.WHITE);
            }

            currentScalingMode = newMode;
            currentScalingModeText = newModeText;
            ScaleTransition zoomIn = new ScaleTransition(Duration.millis(250), currentScalingMode);
            zoomIn.setFromX(1);
            zoomIn.setFromY(1);
            zoomIn.setToX(1.1);
            zoomIn.setToY(1.1);
            zoomIn.play();

            currentScalingModeText.setFill(Color.YELLOW);
        } else if (mode.equals("Animation Speed")) {
            if (null != currentSpeedMode) {
                ScaleTransition zoomOut = new ScaleTransition(Duration.millis(250), currentSpeedMode);
                zoomOut.setFromX(1.1);
                zoomOut.setFromY(1.1);
                zoomOut.setToX(1);
                zoomOut.setToY(1);
                zoomOut.play();

                currentSpeedModeText.setFill(Color.WHITE);
            }

            currentSpeedMode = newMode;
            currentSpeedModeText = newModeText;
            ScaleTransition zoomIn = new ScaleTransition(Duration.millis(250), currentSpeedMode);
            zoomIn.setFromX(1);
            zoomIn.setFromY(1);
            zoomIn.setToX(1.1);
            zoomIn.setToY(1.1);
            zoomIn.play();

            currentSpeedModeText.setFill(Color.YELLOW);
        }
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
