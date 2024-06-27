package it.polimi.ingsw.gc42.view.Classes.tui;

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
    private boolean isColorTerminalSupported;

    // Constructor Method

    /**
     * Constructor Method
     * @param isAdvancedGraphicsMode: a boolean value indicating if it's set in Advanced or Simple Mode
     */
    public TerminalCharacters(boolean isAdvancedGraphicsMode, boolean isColorTerminalSupported) {
        this.isAdvancedGraphicsMode = isAdvancedGraphicsMode;
        this.isColorTerminalSupported = isColorTerminalSupported;
    }

    // Getters and Setters

    /**
     * Getter Method for isAdvancedGraphicsMode
     * @return a boolean value indicating if it's set in Advanced or Enhanced/Standard Mode
     */
    public boolean isAdvancedGraphicsMode() {
        return isAdvancedGraphicsMode;
    }

    /**
     * Setter Method for isAdvancedGraphicsMode
     * @param advancedGraphicsMode: a boolean value indicating if it's set in Advanced or Enhanced/Standard Mode
     */
    public void setAdvancedGraphicsMode(boolean advancedGraphicsMode) {
        isAdvancedGraphicsMode = advancedGraphicsMode;
    }

    /**
     * Getter Method for isColorTerminalSupported
     * @return a boolean value indicating if it's set in Enhanced or Standard Mode
     */
    public boolean isColorTerminalSupported() {
        return isColorTerminalSupported;
    }

    /**
     * Setter Method for isColorTerminalSupported
     * @param colorTerminalSupported a boolean value indicating if it's set in Enhanced or Standard Mode
     */
    public void setColorTerminalSupported(boolean colorTerminalSupported) {
        isColorTerminalSupported = colorTerminalSupported;
    }

    // Methods

    /**
     * Getter Method for the Strings without option for color.
     * Uses the default value to decide if the String has to be colored or not.
     * @param character: a value from the Characters enum, indicating one of the supported Characters
     * @return a String representation of that Character, based on the currently selected mode (Advanced/Enhanced/Standard).
     */
    public String getCharacter(Characters character) {
        return getCharacter(character, isColorTerminalSupported);
    }

    /**
     * Getter Method for the Strings
     * @param character: a value from the Characters enum, indicating one of the supported Characters
     * @param color : a boolean value that specifies if the method has to return a formatted input including color or not
     * @return a String representation of that Character, based on the currently selected mode (Advanced/Enhanced/Standard).
     */
    public String getCharacter(Characters character, boolean color) {
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
        } else if (color) {
            // Colored Characters
            switch (character) {
                case FUNGI -> string = color("ନ ", UiColors.RED);
                case PLANT -> string = color("✿ ", UiColors.GREEN);
                case ANIMAL -> string = color("♘ ", UiColors.CYAN);
                case INSECT -> string = color("¥ ", UiColors.MAGENTA);
                case SCROLL -> string = color("∫ ", UiColors.YELLOW);
                case FEATHER -> string = color("ϡ ", UiColors.YELLOW);
                case POTION -> string = color("Ỗ ", UiColors.YELLOW);
                case RED_SQUARE -> string = color("▤ ", UiColors.RED);
                case BLUE_SQUARE -> string = color("▥ ", UiColors.CYAN);
                case GREEN_SQUARE -> string = color("▦ ", UiColors.GREEN);
                case PURPLE_SQUARE -> string = color("▧ ", UiColors.MAGENTA);
                case YELLOW_SQUARE -> string = color("▩ ", UiColors.YELLOW);
                case EMPTY_SPACE -> string = "  ";
                case WHITE_SQUARE -> string = color("■ ", UiColors.WHITE);
                case EMPTY_CORNER -> string = color("▢ ", UiColors.WHITE);
                case RED_CIRCLE -> string = color("● ", UiColors.RED);
                case BLUE_CIRCLE -> string = color("● ", UiColors.BLUE);
                case GREEN_CIRCLE -> string = color("● ", UiColors.GREEN);
                case YELLOW_CIRCLE -> string = color("● ", UiColors.YELLOW);
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

    private String color(String str, UiColors fg) {
        return fg.toString() + str + UiColors.RESET;
    }
}
