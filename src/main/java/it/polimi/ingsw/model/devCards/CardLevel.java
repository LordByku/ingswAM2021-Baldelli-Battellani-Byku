package it.polimi.ingsw.model.devCards;

/**
 * CardLevel represents all possible cards' levels
 */
public enum CardLevel {
    I("1"),
    II("2"),
    III("3");

    private final String CLIString;

    CardLevel(String CLIString) {
        this.CLIString = CLIString;
    }

    public CardLevel next() {
        if (this == I)
            return II;
        if (this == II)
            return III;
        return null;
    }

    public CardLevel prev() {
        if (this == II)
            return I;
        if (this == III)
            return II;
        return null;
    }

    public String getCLIString() {
        return CLIString;
    }
}