package it.polimi.ingsw.gc42.view;

public enum UiColors {

    // Must be inserted change back the color
    RESET("\033[0m"),

    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    MAGENTA("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m");

    private final String code;
    UiColors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
