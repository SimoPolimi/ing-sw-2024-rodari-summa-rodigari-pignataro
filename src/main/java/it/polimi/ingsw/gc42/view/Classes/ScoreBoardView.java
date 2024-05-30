package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.game.Token;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class ScoreBoardView {
    // Attributes
    private final ImageView redToken;
    private final ImageView blueToken;
    private final ImageView greenToken;
    private final ImageView yellowToken;

    private final ArrayList<ArrayList<Token>> tokensInPosition = new ArrayList<>(NUMBER_OF_POSITIONS);
    private final ArrayList<Coordinates> positions = new ArrayList<>(NUMBER_OF_POSITIONS);

    private static final int OVERLAPPING_PADDING = -5;
    private static final int NUMBER_OF_POSITIONS = 30;
    // Positions are 0-29, 30 in total
    private static final int JUMP_ANIMATION_DURATION = 100;

    // Constructor Method
    public ScoreBoardView(ImageView redToken, ImageView blueToken, ImageView greenToken, ImageView yellowToken) {
        this.redToken = redToken;
        this.blueToken = blueToken;
        this.greenToken = greenToken;
        this.yellowToken = yellowToken;
        // Initializes the Coordinates needed to animate the Transitions
        initCoordinates();
        // Sets all the Tokens in Position 0
    }

    // Methods
    public void animatePath(Token token, int finalPosition) {
        ImageView tokenImage = null;
        switch (token) {
            case RED -> tokenImage = redToken;
            case BLUE -> tokenImage = blueToken;
            case GREEN -> tokenImage = greenToken;
            case YELLOW -> tokenImage = yellowToken;
        }
        if (tokenImage != null) {
            int startingPosition = 0;
            for (ArrayList<Token> position : tokensInPosition) {
                if (position.contains(token)) {
                    startingPosition = tokensInPosition.indexOf(position);
                }
            }
            if (finalPosition > 29) {
                finalPosition = 29;
            }
            animate(tokenImage, startingPosition, finalPosition);
            setTokenInPosition(token, finalPosition);
        }
    }

    private void animate(ImageView token, int startingPosition, int finalPosition) {
        if (startingPosition != finalPosition && finalPosition != 0) {
            // Check if the jump is the last one or if it's just an intermediate one
            int nextPosition;
            if (finalPosition - startingPosition == 1) {
                nextPosition = finalPosition;
            } else {
                nextPosition = startingPosition + 1;
            }

            // Start to calculate the transition
            Coordinates startingCoordinates = positions.get(startingPosition);
            Coordinates finalCoordinates = positions.get(nextPosition);
            Coordinates halfWayCoordinates = new Coordinates();
            switch (startingPosition) {
                case 0, 1, 3, 4, 5, 7, 8, 9, 11, 12, 13, 15, 16, 17 -> {
                    // Horizontal Jump
                    halfWayCoordinates.setX((startingCoordinates.getX() + finalCoordinates.getX()) / 2);
                    halfWayCoordinates.setY(startingCoordinates.getY() - 50);
                }
                case 6, 10, 14, 18, 21, 22, 27 -> {
                    // Vertical Jump
                    halfWayCoordinates.setX(startingCoordinates.getX() - 50);
                    halfWayCoordinates.setY((startingCoordinates.getY() + finalCoordinates.getY()) / 2);
                }
                default -> {
                    // Other Jumps
                    halfWayCoordinates.setX((startingCoordinates.getX() + finalCoordinates.getX()) / 2);
                    halfWayCoordinates.setY((startingCoordinates.getY() + finalCoordinates.getY()) / 2);
                }
            }

            // Handles the case when other Tokens are already in that Position when the Position is not final
            if (nextPosition != finalPosition) {
                finalCoordinates.setY(finalCoordinates.getY() - (tokensInPosition.get(nextPosition).size() * OVERLAPPING_PADDING));
            }

            TranslateTransition halfJump1 = new TranslateTransition(Duration.millis(JUMP_ANIMATION_DURATION), token);
            halfJump1.setFromX(startingCoordinates.getX());
            halfJump1.setToX(halfWayCoordinates.getX());
            halfJump1.setFromY(token.getTranslateY());
            halfJump1.setToY(halfWayCoordinates.getY());

            TranslateTransition halfJump2 = new TranslateTransition(Duration.millis(JUMP_ANIMATION_DURATION), token);
            halfJump2.setFromX(halfWayCoordinates.getX());
            halfJump2.setToX(finalCoordinates.getX());
            halfJump2.setFromY(halfWayCoordinates.getY());
            halfJump2.setToY(finalCoordinates.getY());

            halfJump1.setOnFinished((e) -> halfJump2.play());
            halfJump2.setOnFinished((e) -> animate(token, nextPosition, finalPosition));

            ScaleTransition zoom = new ScaleTransition(Duration.millis(JUMP_ANIMATION_DURATION), token);
            zoom.setByX(1.2);
            zoom.setByY(1.2);
            zoom.setAutoReverse(true);
            zoom.setCycleCount(2);

            halfJump1.play();
            zoom.play();
        }
    }

    private void refreshPosition(int position) {
        for (Token token : tokensInPosition.get(position)) {
            setTokenImageHeight(token, positions.get(position).getY()
                    + (tokensInPosition.get(position).indexOf(token) * OVERLAPPING_PADDING));
        }
    }

    private void setTokenImageHeight(Token token, int y) {
        ImageView tokenImage = null;
        switch (token) {
            case RED -> tokenImage = redToken;
            case BLUE -> tokenImage = blueToken;
            case GREEN -> tokenImage = greenToken;
            case YELLOW -> tokenImage = yellowToken;
        }
        if (tokenImage != null) {
            TranslateTransition transition = new TranslateTransition(Duration.millis(50), tokenImage);
            transition.setFromY(tokenImage.getTranslateY());
            transition.setToY(y);
            transition.play();
        }
    }

    public void setTokenInPosition(Token token, int position) {
        ImageView tokenImage = null;
        switch (token) {
            case RED -> tokenImage = redToken;
            case BLUE -> tokenImage = blueToken;
            case GREEN -> tokenImage = greenToken;
            case YELLOW -> tokenImage = yellowToken;
        }
        if (tokenImage != null) {
            tokenImage.setVisible(true);
        }

        for (ArrayList<Token> positionList : tokensInPosition) {
            if (positionList.contains(token)) {
                positionList.remove(token);
                refreshPosition(tokensInPosition.indexOf(positionList));
            }
        }
        tokensInPosition.get(position).add(token);
        refreshPosition(position);
    }

    private void initCoordinates() {
        // Initializes the ArrayLists
        for (int i = 0; i < NUMBER_OF_POSITIONS; i++) {
            positions.add(new Coordinates());
            tokensInPosition.add(new ArrayList<>());
        }
        // Initializes the Values
        positions.get(0).setX(-60);
        positions.get(0).setY(215);
        positions.get(1).setX(0);
        positions.get(1).setY(215);
        positions.get(2).setX(60);
        positions.get(2).setY(215);
        positions.get(3).setX(90);
        positions.get(3).setY(160);
        positions.get(4).setX(30);
        positions.get(4).setY(160);
        positions.get(5).setX(-30);
        positions.get(5).setY(160);
        positions.get(6).setX(-90);
        positions.get(6).setY(160);
        positions.get(7).setX(-90);
        positions.get(7).setY(105);
        positions.get(8).setX(-30);
        positions.get(8).setY(105);
        positions.get(9).setX(30);
        positions.get(9).setY(105);
        positions.get(10).setX(90);
        positions.get(10).setY(105);
        positions.get(11).setX(90);
        positions.get(11).setY(50);
        positions.get(12).setX(30);
        positions.get(12).setY(50);
        positions.get(13).setX(-30);
        positions.get(13).setY(50);
        positions.get(14).setX(-90);
        positions.get(14).setY(50);
        positions.get(15).setX(-90);
        positions.get(15).setY(-5);
        positions.get(16).setX(-30);
        positions.get(16).setY(-5);
        positions.get(17).setX(30);
        positions.get(17).setY(-5);
        positions.get(18).setX(90);
        positions.get(18).setY(-5);
        positions.get(19).setX(90);
        positions.get(19).setY(-60);
        positions.get(20).setX(0);
        positions.get(20).setY(-80);
        positions.get(21).setX(-90);
        positions.get(21).setY(-60);
        positions.get(22).setX(-90);
        positions.get(22).setY(-110);
        positions.get(23).setX(-90);
        positions.get(23).setY(-165);
        positions.get(24).setX(-55);
        positions.get(24).setY(-210);
        positions.get(25).setX(0);
        positions.get(25).setY(-220);
        positions.get(26).setX(55);
        positions.get(26).setY(-210);
        positions.get(27).setX(90);
        positions.get(27).setY(-165);
        positions.get(28).setX(90);
        positions.get(28).setY(-110);
        positions.get(29).setX(0);
        positions.get(29).setY(-150);
    }
}
