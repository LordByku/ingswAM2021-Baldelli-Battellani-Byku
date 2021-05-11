package it.polimi.ingsw.view.cli;

public enum BackGroundColor {
    RED("\u001b[41m"),
    GREEN("\u001b[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    WHITE("\u001b[47m");

    public static final String RESET = "\u001B[0m";

    private final String escape;

    BackGroundColor(String escape) {
        this.escape = escape;
    }

    public String escape() {
        return escape;
    }
}
