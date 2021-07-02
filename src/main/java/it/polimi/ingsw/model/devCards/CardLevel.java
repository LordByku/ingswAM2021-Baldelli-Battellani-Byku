package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.view.cli.CLIPrintable;

/**
 * CardLevel represents all possible cards' levels
 */
public enum CardLevel implements CLIPrintable {
    /**
     * Level I DevCard
     */
    I("1", 0),
    /**
     * Level II DevCard
     */
    II("2", 16),
    /**
     * Level III DevCard
     */
    III("3", 32);

    /**
     * CLIString is the CLI String for this CardLevel
     */
    private final String CLIString;
    /**
     * fileOffset is the offset in the DevCard pngs for this level
     */
    private final int fileOffset;

    /**
     * Default constructor
     * @param CLIString the CLI String
     * @param fileOffset the offset in the DevCard pngs
     */
    CardLevel(String CLIString, int fileOffset) {
        this.CLIString = CLIString;
        this.fileOffset = fileOffset;
    }

    /**
     * next returns the next level of this CardLevel
     * @return the next level or null if this is the highest level
     */
    public CardLevel next() {
        if (this == I)
            return II;
        if (this == II)
            return III;
        return null;
    }

    /**
     * prev returns the previous level of this CardLevel
     * @return the previous level or null if this is the lowest level
     */
    public CardLevel prev() {
        if (this == II)
            return I;
        if (this == III)
            return II;
        return null;
    }

    /**
     * getCLIString gets the CLI String of this CardLevel
     * @return the CLI String
     */
    @Override
    public String getCLIString() {
        return CLIString;
    }

    /**
     * getFileOffset returns the file offset in the DevCard pngs
     * @return the file offset in the DevCard pngs
     */
    public int getFileOffset() {
        return fileOffset;
    }
}