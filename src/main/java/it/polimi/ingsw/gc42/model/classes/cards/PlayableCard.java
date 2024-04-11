package it.polimi.ingsw.gc42.model.classes.cards;

public class PlayableCard extends Card {
    // Attributes
    private CardSide frontSide;
    private CardSide backSide;
    private int x;
    private int y;

    // Constructor Methods
    /**
     * Constructor Method
     *
     * @param frontSide:     group of Corners that make the front Side of the card
     * @param backSide:      group of empty Corners that make the back Side of the card, plus other features like permanent resources
     * @param isFrontFacing: true if the Card is showing the front Side, false if it is showing the back Side
     * @param id:            unique identifier for the specific Card
     * @param x:             horizontal coordinate for the Card's position on the table (null if not placed)
     * @param y:             vertical coordinate for the Card's position on the table (null if not placed)
     * @param frontImage:    String containing the name (+ extension) of the Image resource to display on the GUI
     * @param backImage:     String containing the name (+ extension) of the Image resource to display on the GUI
     */
    public PlayableCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing,
                        int id, int x, int y, String frontImage, String backImage) {
        super(isFrontFacing, id, frontImage, backImage);
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.x = x;
        this.y = y;
    }

    /**
     * Class Constructor without the x and y parameters, to generate Cards without having to place them already
     *
     * @param frontSide:     group of Corners that make the front Side of the card
     * @param backSide:      group of empty Corners that make the back Side of the card, plus other features like permanent resources
     * @param isFrontFacing: true if the Card is showing the front Side, false if it is showing the back Side
     * @param id:            unique identifier for the specific Card
     * @param frontImage:    String containing the name (+ extension) of the Image resource to display on the GUI
     * @param backImage:     String containing the name (+ extension) of the Image resource to display on the GUI
     */
    public PlayableCard(CardSide frontSide, CardSide backSide, boolean isFrontFacing,
                        int id, String frontImage, String backImage) {
        super(isFrontFacing, id, frontImage, backImage);
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.x = 0;
        this.y = 0;
    }

    // Getters and Setters
    /**
     * Getter Method for CardSide
     *
     * @return the appropriate CardSide based on which Side the Card is currently showing
     */
    public CardSide getShowingSide() {
        if (isFrontFacing) {
            return frontSide;
        } else {
            return backSide;
        }
    }

    /**
     * Getter Method for frontSide
     *
     * @return frontSide: Side shown on the front of the Card
     */
    public CardSide getFrontSide() {
        return frontSide;
    }

    /**
     * Setter Method for frontSide
     *
     * @param frontSide: group of Corners that make the front Side of the card
     */
    public void setFrontSide(CardSide frontSide) {
        this.frontSide = frontSide;
    }

    /**
     * Getter Method for backSide
     *
     * @return backSide: Side shown on the back of the Card
     */
    public CardSide getBackSide() {
        return backSide;
    }

    /**
     * Setter Method for backSide
     *
     * @param backSide: group of Corners that make the back Side of the card
     */
    public void setBackSide(CardSide backSide) {
        this.backSide = backSide;
    }

    /**
     * Getter method for x
     *
     * @return x: horizontal coordinate for the Card's position on the table (null if not placed)
     */
    public int getX() {
        return x;
    }

    /**
     * Setter Method for x
     *
     * @param x: horizontal coordinate for the Card's position on the table (null if not placed)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter Method for y
     *
     * @return y: vertical coordinate for the Card's position on the table (null if not placed)
     */
    public int getY() {
        return y;
    }

    /**
     * Setter Method for y
     *
     * @param y: vertical coordinate for the Card's position on the table (null if not placed)
     */
    public void setY(int y) {
        this.y = y;
    }
}
