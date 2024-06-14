package it.polimi.ingsw.gc42.view.Classes;

/**
 * This class contains all the supported Characters that can be printed in TUI, along with their String representations.
 * Each Character has 2 forms: and Advanced and a Simple one.
 * Advanced Mode uses emojis to represent objects, while Simple Mode uses standard Unicode Characters.
 * Every Character String is 2 chars long.
 * A boolean value defines which Mode is selected.
 */
public class TerminalCharacters {
    // Attributes
    private boolean isAdvancedGraphicsMode;

    // Constructor Method

    /**
     * Constructor Method
     * @param isAdvancedGraphicsMode: a boolean value indicating if it's set in Advanced or Simple Mode
     */
    public TerminalCharacters(boolean isAdvancedGraphicsMode) {
        this.isAdvancedGraphicsMode = isAdvancedGraphicsMode;
    }

    // Getters and Setters

    /**
     * Getter Method for isAdvancedGraphicsMode
     * @return a boolean value indicating if it's set in Advanced or Simple Mode
     */
    public boolean isAdvancedGraphicsMode() {
        return isAdvancedGraphicsMode;
    }

    /**
     * Setter Method for isAdvancedGraphicsMode
     * @param advancedGraphicsMode: a boolean value indicating if it's set in Advanced or Simple Mode
     */
    public void setAdvancedGraphicsMode(boolean advancedGraphicsMode) {
        isAdvancedGraphicsMode = advancedGraphicsMode;
    }

    // Methods

    /**
     * Getter Method for the Strings
     * @param character: a value from the Characters enum, indicating one of the supported Characters
     * @return a String representation of that Character, based on the currently selected mode (Advanced/Simple).
     */
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
                case EMPTY_SPACE -> string = "  ";
                case WHITE_SQUARE -> string = "⚪";
                case EMPTY_CORNER -> string = "🟫";
                case RED_CIRCLE -> string = "🔴";
                case BLUE_CIRCLE -> string = "🔵";
                case GREEN_CIRCLE -> string = "🟢";
                case YELLOW_CIRCLE -> string = "🟡";
            }
        } else {
            // Standard Characters
            switch (character) {
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
                case EMPTY_SPACE -> string = "  ";
                case WHITE_SQUARE -> string = "■ ";
                case EMPTY_CORNER -> string = "▢ ";
                case RED_CIRCLE -> string = "● ";
                case BLUE_CIRCLE -> string = "○ ";
                case GREEN_CIRCLE -> string = "◍ ";
                case YELLOW_CIRCLE -> string = "◉ ";
            }
        }
        return string;
    }
}
