package it.polimi.ingsw.gc42.view;

import it.polimi.ingsw.gc42.model.classes.cards.*;
import it.polimi.ingsw.gc42.view.Classes.Characters;
import it.polimi.ingsw.gc42.view.Classes.TerminalCharacters;

import java.util.ArrayList;

import static it.polimi.ingsw.gc42.model.classes.cards.KingdomResource.*;
import static it.polimi.ingsw.gc42.model.classes.cards.KingdomResource.INSECT;
import static it.polimi.ingsw.gc42.model.classes.cards.Resource.*;

public class TerminalCard {
    /**
     * Creates a formatted String representation of one of the Card's lines.
     * A Card has 5 lines, because it's printed in 5 different Lines.
     *
     * @param card  the Card to print
     * @param line  an int value (1-5) corresponding to the Line to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or use that one instead
     * @return the formatted String, in ASCII art or Emojis based on the Graphics Mode.
     */
    public static String getPrintCardLine(PlayableCard card, int line, boolean printColors, TerminalCharacters terminalCharacters) {
        String string = "";
        if (null != card) {
            if (card.getId() == -1){
                if (line == 3){
                    for (int i = 0; i < 3; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                    if (String.valueOf(card.getX()).length() == 1){
                        string += card.getX() + " ";
                    }
                    else{
                        string += String.valueOf(card.getX());
                    }
                    string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    if (String.valueOf(card.getY()).length() == 1){
                        string += card.getY() + " ";
                    }
                    else{
                        string += String.valueOf(card.getY());
                    }
                    for (int i = 0; i < 3; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                }
                else {
                    for (int i = 0; i < 9; i++) {
                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                    }
                }
            }
            else {
                switch (line) {
                    case 1 -> {
                        if (0 != card.getEarnedPoints()) {
                            // This Card has Points, that will be displayed on the texture
                            string = getCornerPrint(card, card.getShowingSide().topLeftCorner(), printColors, terminalCharacters);
                            for (int i = 0; i < 7; i++) {
                                if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else if (i == 3 && card.isFrontFacing()) {
                                    // Print points
                                    if (printColors) {
                                        string += color(card.getEarnedPoints() + " ", UiColors.YELLOW);
                                    } else {
                                        string += card.getEarnedPoints() + " ";
                                    }
                                } else {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().topRightCorner(), printColors, terminalCharacters);
                        } else if (card instanceof GoldCard && null != ((GoldCard) card).getObjective()) {
                            // This Card has some Points related to a Condition, that will be displayed on the texture
                            string = getCornerPrint(card, card.getShowingSide().topLeftCorner(), printColors, terminalCharacters);
                            for (int i = 0; i < 7; i++) {
                                if (i == 0 || i == 6) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else if (i == 2 && card.isFrontFacing()) {
                                    // Print points
                                    if (((GoldCard) card).getObjective() instanceof CornerCountObjective) {
                                        string += terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
                                    } else {
                                        switch (((ItemCountObjective) ((GoldCard) card).getObjective()).getItem()) {
                                            case FUNGI ->
                                                    string += terminalCharacters.getCharacter(Characters.FUNGI, printColors);
                                            case PLANT ->
                                                    string += terminalCharacters.getCharacter(Characters.PLANT, printColors);
                                            case ANIMAL ->
                                                    string += terminalCharacters.getCharacter(Characters.ANIMAL, printColors);
                                            case INSECT ->
                                                    string += terminalCharacters.getCharacter(Characters.INSECT, printColors);
                                            case POTION ->
                                                    string += terminalCharacters.getCharacter(Characters.POTION, printColors);
                                            case FEATHER ->
                                                    string += terminalCharacters.getCharacter(Characters.FEATHER, printColors);
                                            case SCROLL ->
                                                    string += terminalCharacters.getCharacter(Characters.SCROLL, printColors);
                                            default ->
                                                    throw new IllegalStateException("Unexpected value: " + ((ItemCountObjective) ((GoldCard) card).getObjective()).getItem());
                                        }
                                    }
                                } else if (i == 4 && card.isFrontFacing()) {
                                    // Print points
                                    if (printColors) {
                                        string += color(((GoldCard) card).getObjective().getPoints() + " ", UiColors.YELLOW);
                                    } else {
                                        string += ((GoldCard) card).getObjective().getPoints() + " ";
                                    }
                                } else {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().topRightCorner(), printColors, terminalCharacters);
                        } else {
                            string = getCornerPrint(card, card.getShowingSide().topLeftCorner(), printColors, terminalCharacters);
                            for (int i = 0; i < 7; i++) {
                                if ((i == 0 || i == 6) && card instanceof GoldCard) {
                                    string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                                } else {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                            }
                            string += getCornerPrint(card, card.getShowingSide().topRightCorner(), printColors, terminalCharacters);
                        }
                    }
                    case 2, 4 -> {
                        if (card instanceof GoldCard) {
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            for (int i = 0; i < 7; i++) {
                                string += getCardColor(card, printColors, terminalCharacters);
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                        } else {
                            for (int i = 0; i < 9; i++) {
                                string += getCardColor(card, printColors, terminalCharacters);
                            }
                        }
                    }
                    case 3 -> {
                        for (int i = 0; i < 3; i++) {
                            string += getCardColor(card, printColors, terminalCharacters);
                        }
                        if ((!(card instanceof StarterCard) && !card.isFrontFacing()) || (card.isFrontFacing() && card instanceof StarterCard)) {
                            switch (card.getPermanentResources().size()) {
                                case 1 -> {
                                    string += getCardColor(card, printColors, terminalCharacters) + getItemPrint(card.getPermanentResources().get(0), printColors, terminalCharacters) + getCardColor(card, printColors, terminalCharacters);
                                }
                                case 2 -> {
                                    string += getItemPrint(card.getPermanentResources().get(0), printColors, terminalCharacters) + getCardColor(card, printColors, terminalCharacters)
                                            + getItemPrint(card.getPermanentResources().get(1), printColors, terminalCharacters);
                                }
                                case 3 -> {
                                    string += getItemPrint(card.getPermanentResources().get(0), printColors, terminalCharacters)
                                            + getItemPrint(card.getPermanentResources().get(1), printColors, terminalCharacters)
                                            + getItemPrint(card.getPermanentResources().get(2), printColors, terminalCharacters);
                                }
                                default -> {
                                    for (int i = 0; i < 3; i++) {
                                        string += getCardColor(card, printColors, terminalCharacters);
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < 3; i++) {
                                string += getCardColor(card, printColors, terminalCharacters);
                            }
                        }
                        for (int i = 0; i < 3; i++) {
                            string += getCardColor(card, printColors, terminalCharacters);
                        }
                    }
                    case 5 -> {
                        string = getCornerPrint(card, card.getShowingSide().bottomLeftCorner(), printColors, terminalCharacters);
                        if (card instanceof GoldCard) {
                            // Read costs
                            ArrayList<Item> costs = new ArrayList<>();
                            for (int i = 0; i < ((GoldCard) card).getCost(FUNGI); i++) {
                                costs.add(FUNGI);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(PLANT); i++) {
                                costs.add(PLANT);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(ANIMAL); i++) {
                                costs.add(ANIMAL);
                            }
                            for (int i = 0; i < ((GoldCard) card).getCost(INSECT); i++) {
                                costs.add(INSECT);
                            }

                            if (!card.isFrontFacing()) {
                                costs = new ArrayList<>();
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            switch (costs.size()) {
                                case 0 -> {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                                case 1 -> {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getItemPrint(costs.getFirst(), printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);

                                }
                                case 2 -> {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getItemPrint(costs.getFirst(), printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(1), printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                                case 3 -> {
                                    string += getCardColor(card, printColors, terminalCharacters);
                                    string += getItemPrint(costs.getFirst(), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(1), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(2), printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                                case 4 -> {
                                    string += getItemPrint(costs.getFirst(), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(1), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(2), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(3), printColors, terminalCharacters);
                                    string += getCardColor(card, printColors, terminalCharacters);
                                }
                                case 5 -> {
                                    string += getItemPrint(costs.getFirst(), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(1), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(2), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(3), printColors, terminalCharacters);
                                    string += getItemPrint(costs.get(4), printColors, terminalCharacters);
                                }
                            }
                            string += terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
                            string += getCornerPrint(card, card.getShowingSide().bottomRightCorner(), printColors, terminalCharacters);
                        } else {
                            for (int i = 0; i < 7; i++) {
                                string += getCardColor(card, printColors, terminalCharacters);
                            }
                            string += getCornerPrint(card, card.getShowingSide().bottomRightCorner(), printColors, terminalCharacters);
                        }
                    }
                }
            }
        } else {
            if (true) {
                for (int i = 0; i < 20; i++) {
                    string += " ";
                }
            } else {
                for (int i = 0; i < 15; i++) {
                    string += " ";
                }
            }
        }
        return string;
    }

    /**
     * Creates a String containing a representation of an Item (KingdomResource or Resource)
     * @param item the item to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or not
     * @return the String representation of the Item, in Unicode characters or Emojis depending on the Graphics Mode.
     */
    public static String getItemPrint(Item item, boolean printColors, TerminalCharacters terminalCharacters) {
        String string = "  ";
        switch (item) {
            case FUNGI -> string = terminalCharacters.getCharacter(Characters.FUNGI, printColors);
            case ANIMAL -> string = terminalCharacters.getCharacter(Characters.ANIMAL, printColors);
            case INSECT -> string = terminalCharacters.getCharacter(Characters.INSECT, printColors);
            case PLANT -> string = terminalCharacters.getCharacter(Characters.PLANT, printColors);
            case FEATHER -> string = terminalCharacters.getCharacter(Characters.FEATHER, printColors);
            case POTION -> string = terminalCharacters.getCharacter(Characters.POTION, printColors);
            case SCROLL -> string = terminalCharacters.getCharacter(Characters.SCROLL, printColors);
            default -> {
                string = "  ";
            }
        }
        return string;
    }

    /**
     * Prints a Card in its entirety by printing all the 5 lines the Card is made of.
     * @param card the Card to print
     */
    public static void printCard(Card card, TerminalCharacters terminalCharacters) {
        if (card instanceof ObjectiveCard) {
            System.out.println(printObjectiveLine((ObjectiveCard) card, 1, terminalCharacters));
            System.out.println(printObjectiveLine((ObjectiveCard) card, 2, terminalCharacters));
            System.out.println(printObjectiveLine((ObjectiveCard) card, 3, terminalCharacters));
            System.out.println(printObjectiveLine((ObjectiveCard) card, 4, terminalCharacters));
            System.out.println(printObjectiveLine((ObjectiveCard) card, 5, terminalCharacters));
        } else {
            System.out.println(getPrintCardLine((PlayableCard) card, 1, terminalCharacters.isColorTerminalSupported(), terminalCharacters));
            System.out.println(getPrintCardLine((PlayableCard) card, 2, terminalCharacters.isColorTerminalSupported(), terminalCharacters));
            System.out.println(getPrintCardLine((PlayableCard) card, 3, terminalCharacters.isColorTerminalSupported(), terminalCharacters));
            System.out.println(getPrintCardLine((PlayableCard) card, 4, terminalCharacters.isColorTerminalSupported(), terminalCharacters));
            System.out.println(getPrintCardLine((PlayableCard) card, 5, terminalCharacters.isColorTerminalSupported(), terminalCharacters));
        }
    }

    /**
     * Creates a String containing a representation of a Corner, based on its type and the content inside.
     * @param card the Card that contains the Corner
     * @param corner the Corner to print
     * @param printColors a boolean value indicating if it needs to override the global isColorTerminalSupported or not
     * @return the String representation of the Corner, in Unicode characters or Emojis depending on the Graphics Mode.
     */
    private static String getCornerPrint(PlayableCard card, Corner corner, boolean printColors, TerminalCharacters terminalCharacters) {
        String string = "";
        if (null != corner) {
            if (null != corner.getItem()) {
                string = getItemPrint(corner.getItem(), printColors, terminalCharacters);
            } else {
                string = terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
            }
        } else string = getCardColor(card, printColors, terminalCharacters);
        return string;
    }
    /**
     * Creates a String containing the "Building Block" of a Card, the character used to visually create the Card when printing it.
     * @param card the Card to print
     * @param printColors a boolean value indicating it it needs to override the global isColorTerminalSupported or use that one instead
     * @return the Unicode Character or Emoji, depending on the Graphics Mode.
     */
    private static String getCardColor(PlayableCard card, boolean printColors, TerminalCharacters terminalCharacters) {
        String string = "";
        if (card instanceof ResourceCard || card instanceof GoldCard) {
            switch (card.getPermanentResources().getFirst()) {
                case PLANT -> string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE, printColors);
                case ANIMAL -> string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE, printColors);
                case FUNGI -> string = terminalCharacters.getCharacter(Characters.RED_SQUARE, printColors);
                case INSECT -> string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE, printColors);
                default -> string = "";
            }
        } else if (card instanceof StarterCard) {
            string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE, printColors);
        } else {
            string = terminalCharacters.getCharacter(Characters.EMPTY_CORNER, printColors);
        }
        return string;
    }
    /**
     * Creates and builds a formatted String containing a visual representation of one of the Lines that make up an Objective Card.
     * Like Playable Cards, Objective Cards are also made of 5 Lines, and the one to return is passed as a parameter.
     * The String is formatted in ASCII art or with Emojis, based on the Graphics Mode selected.
     * @param card the Objective Card to print
     * @param line the Line to print
     * @return the formatted String
     */
    public static String printObjectiveLine(ObjectiveCard card, int line, TerminalCharacters terminalCharacters) {
        String string = "";
        switch (card.getId()) {
            case 87:
                switch (line) {
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 2⚪🟥⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟥⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟥⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                }
                break;
            case 88:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟩⚪ 2⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟩⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;

                }
                break;
            case 89:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 2⚪🟦⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟦⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟦⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 90:
                switch (line){
                    case 1, 5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟪⚪ 2⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🟪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 91:
                switch (line){
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪🟥⚪ 3⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🟥⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪🟩⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 92:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪ 3⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪⚪🟩⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪🟪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 93:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪🟥⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🟦⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🟦⚪ 3⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 94:
                switch (line){
                    case 1,5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪🟦⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪ 3⚪🟪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 95:
                switch (line){
                    case 1, 5:
                        // 🟥🟨🟨🟨🟨🟨🟨🟨🟥
                        string = terminalCharacters.getCharacter(Characters.RED_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.RED_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🍄⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🍄⚪🍄⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FUNGI) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 96:
                switch (line){
                    case 1, 5:
                        // 🟩🟨🟨🟨🟨🟨🟨🟨🟩
                        string = terminalCharacters.getCharacter(Characters.GREEN_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.GREEN_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🌳⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🌳⚪🌳⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PLANT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 97:
                switch (line){
                    case 1, 5:
                        // 🟦🟨🟨🟨🟨🟨🟨🟨🟦
                        string = terminalCharacters.getCharacter(Characters.BLUE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.BLUE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🐺⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🐺⚪🐺⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.ANIMAL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 98:
                switch (line){
                    case 1, 5:
                        // 🟪🟨🟨🟨🟨🟨🟨🟨🟪
                        string = terminalCharacters.getCharacter(Characters.PURPLE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.PURPLE_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪⚪🦋⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪🦋⚪🦋⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.INSECT) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 99:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 3⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 3" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪📜🍷🪶⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 100:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);

                        break;
                    case 3:
                        // 🟨⚪⚪📜⚪📜⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.SCROLL) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 101:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🍷⚪🍷⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.POTION) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
            case 102:
                switch (line){
                    case 1, 5:
                        // 🟨🟨🟨🟨🟨🟨🟨🟨🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 2:
                        // 🟨⚪⚪⚪ 2⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                " 2" +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 3:
                        // 🟨⚪⚪🪶⚪🪶⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.FEATHER) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                    case 4:
                        // 🟨⚪⚪⚪⚪⚪⚪⚪🟨
                        string = terminalCharacters.getCharacter(Characters.YELLOW_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.WHITE_SQUARE) +
                                terminalCharacters.getCharacter(Characters.YELLOW_SQUARE);
                        break;
                }
                break;
        }
        return string;

    }

    /**
     * Formats a String using the Unicode Color codes to print a colored output.
     * @param str the Text that will be printed
     * @param fg a foreground color (text color) chosen from the UiColors enum
     * @return the formatted String to print in System.out
     */
    public static String color(String str, UiColors fg) {
        return fg.toString() + str + UiColors.RESET;
    }

    /**
     *      * Formats a String using the Unicode Color codes to print a colored output on a colored background
     * @param str the Text that will be printed
     * @param fg a foreground color (text color) chosen from the UiColors enum
     * @param bg a background color chosen from the UiColors enum
     * @return the formatted String to print in System.out
     */
    private static String color(String str, UiColors fg, UiColors bg) {
        return fg.toString() + bg.toString() + str + UiColors.RESET;
    }
}
