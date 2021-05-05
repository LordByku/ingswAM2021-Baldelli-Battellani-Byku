package it.polimi.ingsw.view.cli;

public enum TextColour {
    GREY("\u001B[30;1m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    WHITE("\u001b[37m");

    public static final String RESET = "\u001B[0m";

    private final String escape;

    TextColour(String escape) {
        this.escape = escape;
    }

    public String escape() {
        return escape;
    }
}
