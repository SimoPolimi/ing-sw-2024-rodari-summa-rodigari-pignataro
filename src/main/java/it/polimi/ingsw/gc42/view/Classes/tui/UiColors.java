package it.polimi.ingsw.gc42.view.Classes.tui;

/**
 * This enum contains all the Unicode Colors that can be used to print in different colors in the Terminal
 */
public enum UiColors {

    // Must be inserted to change back the color
    RESET("\033[0m"),

    // FG standard colors
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    MAGENTA("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    // FG bright colors
    BRIGHT_BLACK("\033[0;90m"),
    BRIGHT_RED("\033[0;91m"),
    BRIGHT_GREEN("\033[0;92m"),
    BRIGHT_YELLOW("\033[0;93m"),
    BRIGHT_BLUE("\033[0;94m"),
    BRIGHT_MAGENTA("\033[0;95m"),
    BRIGHT_CYAN("\033[0;96m"),
    BRIGHT_WHITE("\033[0;97m"),

    // BG standard colors
    BG_BLACK("\033[0;40m"),
    BG_RED("\033[0;41m"),
    BG_GREEN("\033[0;42m"),
    BG_YELLOW("\033[0;43m"),
    BG_BLUE("\033[0;44m"),
    BG_MAGENTA("\033[0;45m"),
    BG_CYAN("\033[0;46m"),
    BG_WHITE("\033[0;47m"),

    // BG bright colors
    BG_BRIGHT_BLACK("\033[0;100m"),
    BG_BRIGHT_RED("\033[0;101m"),
    BG_BRIGHT_GREEN("\033[0;102m"),
    BG_BRIGHT_YELLOW("\033[0;103m"),
    BG_BRIGHT_BLUE("\033[0;104m"),
    BG_BRIGHT_MAGENTA("\033[0;105m"),
    BG_BRIGHT_CYAN("\033[0;106m"),
    BG_BRIGHT_WHITE("\033[0;107m");

    private final String code;
    UiColors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
