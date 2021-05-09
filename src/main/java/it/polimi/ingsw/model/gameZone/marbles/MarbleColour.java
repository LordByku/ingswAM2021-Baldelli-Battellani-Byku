package it.polimi.ingsw.model.gameZone.marbles;

import it.polimi.ingsw.view.cli.TextColour;

/**
 * MarbleColour represents all possible Marble colours
 */
public enum MarbleColour {
    WHITE(TextColour.WHITE),
    BLUE(TextColour.BLUE),
    GREY(TextColour.GREY),
    YELLOW(TextColour.YELLOW),
    PURPLE(TextColour.PURPLE),
    RED(TextColour.RED);

    TextColour colour;

    MarbleColour(TextColour colour) {
        this.colour = colour;
    }

    public TextColour getTextColour() {
        return colour;
    }

    public String getCLIString() {
        return colour.escape() + "\u25c9" + TextColour.RESET;
    }
}
