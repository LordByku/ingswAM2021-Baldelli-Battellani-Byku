package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.view.cli.BackGroundColor;
import it.polimi.ingsw.view.cli.TextColour;

/**
 * CardColour represents all possible cards' colours
 */
public enum CardColour {
    GREEN(BackGroundColor.GREEN),
    BLUE(BackGroundColor.BLUE),
    YELLOW(BackGroundColor.YELLOW),
    PURPLE(BackGroundColor.PURPLE);

    private final BackGroundColor colour;

    CardColour(BackGroundColor colour) {
        this.colour = colour;
    }

    public BackGroundColor getColour() {
        return colour;
    }

    public String getCLIString() {
        return colour.escape() + "     " + BackGroundColor.RESET;
    }
}

