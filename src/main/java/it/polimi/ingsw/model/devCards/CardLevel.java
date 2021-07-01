package it.polimi.ingsw.model.devCards;

import java.util.Random;

/**
 * CardLevel represents all possible cards' levels
 */
public enum CardLevel {
    I("1", 0),
    II("2", 16),
    III("3", 32);

    private final String CLIString;
    private final int fileOffset;
    private static final Random rng = new Random();

    CardLevel(String CLIString, int fileOffset) {
        this.CLIString = CLIString;
        this.fileOffset = fileOffset;
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

    public int getFileOffset() {
        return fileOffset + CardColour.values().length * rng.nextInt(4);
    }
}