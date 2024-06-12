package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.view.UiColors;

public class TerminalCharacters {
    // Attributes
    private boolean isAdvancedGraphicsMode;

    // Constructor Method
    public TerminalCharacters(boolean isAdvancedGraphicsMode) {
        this.isAdvancedGraphicsMode = isAdvancedGraphicsMode;
    }

    // Getters and Setters

    public boolean isAdvancedGraphicsMode() {
        return isAdvancedGraphicsMode;
    }

    public void setAdvancedGraphicsMode(boolean advancedGraphicsMode) {
        isAdvancedGraphicsMode = advancedGraphicsMode;
    }

    // Methods
    public String getCharacter(Characters character) {
        String string = "";
        if (isAdvancedGraphicsMode) {
            // Emojis
            switch (character) {
                case FUNGI -> string = "🍄";
                case PLANT -> string = "🌳";
                case ANIMAL -> string = "🐺";
                case INSECT -> string = "🦋";
                case SCROLL -> string = "📜";
                case FEATHER -> string = "🪶";
                case POTION -> string = "🍷";
                case RED_SQUARE -> string = "🟥";
                case BLUE_SQUARE -> string = "🟦";
                case GREEN_SQUARE -> string = "🟩";
                case PURPLE_SQUARE -> string = "🟪";
                case YELLOW_SQUARE -> string = "🟨";
                case BLACK_SQUARE -> string = "⬛";
                case WHITE_SQUARE -> string = "⬜";
                case EMPTY_CORNER -> string = "🟫";
                case RED_CIRCLE -> string = "🔴";
                case BLUE_CIRCLE -> string = "🔵";
                case GREEN_CIRCLE -> string = "🟢";
                case YELLOW_CIRCLE -> string = "🟡";
            }
        } else {
            // Standard Characters
            switch (character) {
                /*case FUNGI -> string = color("ନ ", UiColors.RED);
                case PLANT -> string = color("⚜ ", UiColors.GREEN);
                case ANIMAL -> string = color("♘ ", UiColors.BLUE);
                case INSECT -> string = color("¥ ", UiColors.MAGENTA);
                case SCROLL -> string = color("∫ ", UiColors.YELLOW);
                case FEATHER -> string = color("ϡ ", UiColors.YELLOW);
                case POTION -> string = color("Ỗ ", UiColors.YELLOW);
                case RED_SQUARE -> string = color("■ ", UiColors.RED);
                case BLUE_SQUARE -> string = color("■ ",UiColors.BLUE);
                case GREEN_SQUARE -> string = color("■ ",UiColors.GREEN);
                case PURPLE_SQUARE -> string = color("■ ",UiColors.MAGENTA);
                case YELLOW_SQUARE -> string = color("■ ",UiColors.YELLOW);
                case BLACK_SQUARE -> string = color("■ ",UiColors.BLACK);
                case WHITE_SQUARE -> string = color("■ ", UiColors.WHITE);
                case EMPTY_CORNER -> string = color("▢ ", UiColors.YELLOW);
                case RED_CIRCLE -> string = color("●", UiColors.RED);
                case BLUE_CIRCLE -> string = color("●",UiColors.BLUE);
                case GREEN_CIRCLE -> string = color("●",UiColors.GREEN);
                case YELLOW_CIRCLE -> string = color("●",UiColors.YELLOW);*/

                case FUNGI -> string = "ନ ";
                case PLANT -> string = "✿ ";
                case ANIMAL -> string = "♘ ";
                case INSECT -> string = "¥ ";
                case SCROLL -> string = "∫ ";
                case FEATHER -> string = "ϡ ";
                case POTION -> string = "Ỗ ";
                case RED_SQUARE -> string = "▤ ";
                case BLUE_SQUARE -> string = "▥ ";
                case GREEN_SQUARE -> string = "▦ ";
                case PURPLE_SQUARE -> string = "▧ ";
                case YELLOW_SQUARE -> string = "▩ ";
                case BLACK_SQUARE -> string = "■ ";
                case WHITE_SQUARE -> string = "■ ";
                case EMPTY_CORNER -> string = "▢ ";
                case RED_CIRCLE -> string = "●";
                case BLUE_CIRCLE -> string = "●";
                case GREEN_CIRCLE -> string = "●";
                case YELLOW_CIRCLE -> string = "●";
            }
        }
        return string;
    }

    private String color(String str, UiColors fg) {
        return fg.toString() + str + UiColors.RESET;
    }

}
