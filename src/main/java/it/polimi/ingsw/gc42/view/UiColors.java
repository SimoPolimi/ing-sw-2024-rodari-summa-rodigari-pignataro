package it.polimi.ingsw.gc42.view;

public enum UiColors {

    // Must be inserted change back the color
    RESET("\033[0m"),

    // FG colors
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    MAGENTA("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    // BG colors
    BG_BLACK("\033[0;30m"),
    BG_RED("\033[0;31m"),
    BG_MAGENTA("\033[0;35m"),
    BG_CYAN("\033[0;36m"),
    BG_WHITE("\033[0;37m");

    private final String code;
    UiColors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
