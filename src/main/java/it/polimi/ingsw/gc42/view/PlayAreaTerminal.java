package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.classes.cards.Coordinates;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.cards.ResourceCard;
import it.polimi.ingsw.gc42.model.classes.cards.StarterCard;
import it.polimi.ingsw.gc42.view.Classes.Characters;
import it.polimi.ingsw.gc42.view.Classes.TerminalCharacters;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayAreaTerminal {
    private HashMap<String, Integer> printingExtremes = new HashMap<>();
    private String[][] playArea;
    private final GameTerminal terminal;
    public PlayAreaTerminal(GameTerminal terminal) {
        this.terminal = terminal;
    }
    public void setPrintingExtremes(String key, int value){
        printingExtremes.put(key, value);
    }
    /**
     * Converts a Card's Coordinates from the custom 45° angled system used in Model to the standard xy system representation.
     * @param coordinates the Model's Coordinates
     * @return a Coordinates object containing the converted values.
     */
    private Coordinates convertToAbsoluteCoordinates(Coordinates coordinates) {
        int x = coordinates.getX() - coordinates.getY();
        int y = coordinates.getX() + coordinates.getY();
        return new Coordinates(x, y);
    }

    /**
     * Converts the Coordinates to a format specifically tailored to work for the matrices used to represent the PlayArea.
     * This format uses two offset values, calculated from the matrix's width and height to move the (0,0) position from the
     * top left corner to the matrix's center, and then allows all the other normalized Coordinates to be converted in matrix's
     * Coordinates.
     * IMPORTANT: This method requires normalized Coordinates, as in using the standard xy representation.
     * Model's Coordinates need to be converted to Absolute Coordinates before being passed here.
     * @param coordinates the Absolute Coordinates to convert to Matrix's Coordinates
     * @param matrixSizeX the Matrix's width
     * @param matrixSizeY the Matrix's height
     * @return the converted Coordinates
     */
    private Coordinates convertMatrixCoordinates(Coordinates coordinates, int matrixSizeX, int matrixSizeY) {
        int shiftX = (matrixSizeX) / 2;
        int shiftY = (matrixSizeY) / 2;
        int x = coordinates.getX() + shiftX;
        int y = shiftY - coordinates.getY();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    /**
     * Initializes the Matrix used to represent the PlayArea, and puts the StarterCard already in there.
     * @param starter the Player's Starter Card
     * @param playerID the Player's playerID, used to initialize its Matrix's printing extremes values
     */
    public void createCardMatrix(StarterCard starter, int playerID, TerminalCharacters terminalCharacters){
        String[][] playArea = new String[800][1440];
        String line = "";
        int firstColumn = 716;
        int firstLine = 398 ;
        for (int j = 1; j <= 5; j++) {
            line = TerminalCard.getPrintCardLine(starter,j, false, terminalCharacters);
            for (int i = 0; i < line.length(); i += 2) {
                String string = new String(String.valueOf(line.charAt(i)));
                try {
                    string += line.charAt(i+1);
                } catch (IndexOutOfBoundsException e) {
                    //string += line.charAt(i);
                }
                playArea[firstLine][firstColumn] = string;
                firstColumn++;
            }
            firstLine++;
            firstColumn = 716;
        }
        for (int i = 0; i < 800; i++){
            for (int j = 0; j < 1440; j++){
                if (playArea[i][j] == null){
                    playArea[i][j] = "  ";
                }
            }
        }
        //extreme DX
        printingExtremes.replace("RIGHT", 725);
        //extreme SX
        printingExtremes.replace("LEFT", 717);
        //extreme UP
        printingExtremes.replace("UP", 399);
        //extreme DOWN
        printingExtremes.replace("DOWN", 403);
        this.playArea = playArea;
    }

    /**
     * Updates the Matrix by adding a Card in a specific position, identified by 3 Matrix's Coordinates
     * @param matrix the Matrix to update
     * @param card the Card to add inside the Matrix (PlayArea)
     * @param firstLine the y Coordinate of the top left Corner of the Card
     * @param firstColumn the x Coordinate of the top left Corner of the Card
     * @param centerX the x Coordinate of the center of the Card
     * @return the updated Matrix
     */
    public String[][] addCardToMatrix(String[][] matrix, PlayableCard card, int firstLine, int firstColumn, int centerX, TerminalCharacters terminalCharacters){
        String line = "";
        for (int j = 1; j <= 5; j++) {
            line = TerminalCard.getPrintCardLine(card,j, false, terminalCharacters);
            int i = 0;
            while (i < line.length()) {
                if (terminal.isAdvancedGraphicsMode()) {
                    // Emoji Mode
                    String string = new String(String.valueOf(line.charAt(i)));
                    if (string.equals("🟫")) {
                        matrix[firstLine][firstColumn] = "🟫";
                        i--;
                        firstColumn++;
                    } else {
                        string += line.charAt(i + 1);
                        matrix[firstLine][firstColumn] = string;
                        firstColumn++;
                    }
                    i += 2;
                } else {
                    // Character Mode
                    String string = new String(String.valueOf(line.charAt(i)));
                    string += line.charAt(i + 1);
                    matrix[firstLine][firstColumn] = string;
                    firstColumn++;
                    i += 2;
                }
            }
            firstLine++;
            firstColumn = centerX - 4;
        }
        return matrix;
    }

    /**
     * Calculates the Coordinates where a Card has to be placed inside the playArea, then updates the Matrix
     * @param card the Card to add inside the Matrix
     * @param cards the Player's PlayField
     * @param playerID the Player's playerID
     */
    public void updateCardMatrix(PlayableCard card, ArrayList<PlayableCard> cards, int playerID, TerminalCharacters terminalCharacters){
        Coordinates coordinates = convertMatrixCoordinates(convertToAbsoluteCoordinates(card.getCoordinates()), 1440,800);
        Coordinates coordinates1 = convertToAbsoluteCoordinates(card.getCoordinates());
        //card down sx
        if(isThereACardIn(card.getX()-1,card.getY(), cards)){
            int centerX = coordinates.getX() + (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() + 8;
            }*/
            int centerY = coordinates.getY() - (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() - 4;
            }*/

            centerX -= coordinates1.getX();
            centerY+= coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY -2;

            playArea = addCardToMatrix(playArea,card,firstLine,firstColumn,centerX, terminalCharacters);

            if (centerX + 4 > printingExtremes.get("RIGHT")) {
                printingExtremes.replace("RIGHT", centerX + 4);
            }
            if (centerY - 2 < printingExtremes.get("UP")) {
                printingExtremes.replace("UP", centerY - 2);
            }
        }
        //card up dx
        if(isThereACardIn(card.getX()+1,card.getY(), cards)){
            int centerX = coordinates.getX() - (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() - 8;
            }*/
            int centerY = coordinates.getY() + (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() + 4;
            }*/

            centerX -= coordinates1.getX();
            centerY+= coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY - 2;
            playArea = addCardToMatrix(playArea,card,firstLine,firstColumn,centerX, terminalCharacters);

            if (centerX - 4 < printingExtremes.get("LEFT")) {
                printingExtremes.replace("LEFT", centerX - 4);
            }
            if (centerY + 2 > printingExtremes.get("DOWN")) {
                printingExtremes.replace("DOWN", centerY + 2);
            }
        }
        //card up sx
        if(isThereACardIn(card.getX(),card.getY()+1, cards)){
            int centerX = coordinates.getX() + (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() + 8;
            }*/
            int centerY = coordinates.getY() + (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() + 4;
            }*/

            centerX -= coordinates1.getX();
            centerY += coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY - 2;
            playArea = addCardToMatrix(playArea,card,firstLine,firstColumn,centerX, terminalCharacters);

            if (centerX + 4 > printingExtremes.get("RIGHT")) {
                printingExtremes.replace("RIGHT", centerX + 4);
            }
            if (centerY + 2 > printingExtremes.get("DOWN")) {
                printingExtremes.replace("DOWN", centerY + 2);
            }
        }
        //card down dx
        if(isThereACardIn(card.getX(),card.getY()-1, cards)){
            int centerX = coordinates.getX() - (8 * Math.abs(coordinates1.getX()));
            /*if (card.getX() == 0) {
                centerX = coordinates.getX() - 8;
            }*/
            int centerY = coordinates.getY() - (4 * Math.abs(coordinates1.getY()));
            /*if (card.getY() == 0) {
                centerY= coordinates.getY() - 4;
            }*/

            centerX -= coordinates1.getX();
            centerY += coordinates1.getY();

            int firstColumn = centerX - 4;
            int firstLine = centerY -2;
            playArea = addCardToMatrix(playArea,card,firstLine,firstColumn,centerX, terminalCharacters);

            if (centerX - 4 < printingExtremes.get("LEFT")) {
                printingExtremes.replace("LEFT", centerX - 4);
            }
            if (centerY - 2 < printingExtremes.get("UP")) {
                printingExtremes.replace("UP", centerY - 2);
            }
        }
    }

    /**
     * Refreshes the entire Matrix (PlayArea), fetching every single String and re-writing all of them.
     * This is intended to re-generate the Matrix after switching Graphics Mode: in this situation every String, still
     * formatted for the old Mode, is replaced by the same String but in the new Mode representation.
     * This works both ways: STANDARD -> FANCY and FANCY -> STANDARD.
     * This method can also be used to re-generate a Matrix while keeping the same Graphics Mode, in the case of errors.
     */
    public void recreatePlayArea(TerminalCharacters terminalCharacters) {
        for (int i = 0; i < playArea.length; i++) {
            for (int j = 0; j < playArea[i].length; j++) {
                switch (playArea[i][j]) {
                    case "ନ ", "🍄" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.FUNGI);
                    case "✿ ", "🌳" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.PLANT);
                    case "♘ ", "🐺" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.ANIMAL);
                    case "¥ ", "🦋" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.INSECT);
                    case "∫ ", "📜" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.SCROLL);
                    case "ϡ ", "🪶" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.FEATHER);
                    case "Ỗ ", "🍷" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.POTION);
                    case "▤ ", "🟥" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.RED_SQUARE);
                    case "▥ ", "🟦" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                    case "▦ ", "🟩" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                    case "▧ ", "🟪" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                    case "▩ ", "🟨" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                    case "■ ", "⚪" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.WHITE_SQUARE);
                    case "  ", "⬛" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.EMPTY_SPACE);
                    case "▢ ", "🟫" -> playArea[i][j] = terminalCharacters.getCharacter(Characters.EMPTY_CORNER);

                }
            }
        }
    }

    /**
     * Prints a portion of the Matrix (PlayArea).
     * Printing is limited to a portion because the whole Matrix is too big.
     * Printing extremes are used to determine where to start and end printing: they are
     * updated every time a new Card is added inside the Matrix.
     * Printing extremes leave a few empty Strings around the Cards as a margin.
     * @param playerID the Player's playerID associated to this Matrix
     */
    public void printPlayArea(int playerID, TerminalCharacters terminalCharacters){
        int extremeUP = printingExtremes.get("UP");
        int extremeDOWN = printingExtremes.get("DOWN");
        int extremeLEFT = printingExtremes.get("LEFT");
        int extremeRIGHT = printingExtremes.get("RIGHT");
        for (int i = extremeUP - 5; i <= extremeDOWN + 5; i++){
            String line = "";
            for (int j = extremeLEFT - 5; j <= extremeRIGHT + 5; j++){
                if (!terminal.isAdvancedGraphicsMode() && terminal.isColorTerminalSupported()) {
                    // Enhanced Mode (here colors are injected in the String, because the Matrix doesn't store them
                    String symbol = "";
                    switch (playArea[i][j]) {
                        case "ନ " -> symbol = terminalCharacters.getCharacter(Characters.FUNGI);
                        case "✿ " -> symbol = terminalCharacters.getCharacter(Characters.PLANT);
                        case "♘ " -> symbol = terminalCharacters.getCharacter(Characters.ANIMAL);
                        case "¥ " -> symbol = terminalCharacters.getCharacter(Characters.INSECT);
                        case "∫ " -> symbol = terminalCharacters.getCharacter(Characters.SCROLL);
                        case "ϡ " -> symbol = terminalCharacters.getCharacter(Characters.FEATHER);
                        case "Ỗ " -> symbol = terminalCharacters.getCharacter(Characters.POTION);
                        case "▤ " -> symbol = terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        case "▥ " -> symbol = terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        case "▦ " -> symbol = terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        case "▧ " -> symbol = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        case "▩ " -> symbol = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        case "■ " -> symbol = terminalCharacters.getCharacter(Characters.WHITE_SQUARE);
                        case "  " -> symbol = terminalCharacters.getCharacter(Characters.EMPTY_SPACE);
                        case "▢ " -> symbol = terminalCharacters.getCharacter(Characters.EMPTY_CORNER);
                        default -> symbol = playArea[i][j];
                    }
                    line += symbol;
                } else {
                    // Fancy and Standard Mode (no character modifications required
                    line += playArea[i][j];
                }
            }
            System.out.println(line);
        }
    }
    /**
     * Checks if a Card is present in a specific set of Coordinates (Model) inside an ArrayList<Card>,
     * such as a Player's PlayField.
     * @param x the x Coordinate to check
     * @param y the y Coordinate to check
     * @param cards the PlayField to check into
     * @return a boolean value indicating if those Coordinates belong to any Card or not.
     */
    private boolean isThereACardIn(int x, int y, ArrayList<PlayableCard> cards) {
        for (PlayableCard card : cards) {
            if (card.getX() == x && card.getY() == y) {
                return true;
            }
        }
        return false;
    }
    public String[][] getCopy(){
        String[][] matrix = new String[800][1440];
        for (int i = 0; i < 800; i++){
            for (int j = 0; j < 1440; j++){
                matrix[i][j] = playArea[i][j];
            }
        }
        return matrix;
    }

    public void setPlayArea(String[][] playArea) {
        this.playArea = playArea;
    }

    public int getPrintingExtreme(String key){
        return printingExtremes.get(key);
    }

    public boolean isInitialized() {
        return null == playArea;
    }
}
