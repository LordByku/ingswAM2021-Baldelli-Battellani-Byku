package it.polimi.ingsw.model.gameZone.marbles;

import it.polimi.ingsw.view.cli.TextColour;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * MarbleColour represents all possible Marble colours
 */
public enum MarbleColour {
    WHITE(TextColour.WHITE, "white"),
    BLUE(TextColour.BLUE, "blue"),
    GREY(TextColour.GREY, "grey"),
    YELLOW(TextColour.YELLOW, "yellow"),
    PURPLE(TextColour.PURPLE, "purple"),
    RED(TextColour.RED, "red");

    private final TextColour colour;
    private final String filename;
    private Image marbleColourImage;

    MarbleColour(TextColour colour, String filename) {
        this.colour = colour;
        this.filename = filename;
    }

    public void loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Marbles/" + filename + ".png");
        marbleColourImage = ImageIO.read(resource);
    }

    public Image getImage() {
        return marbleColourImage;
    }

    public TextColour getTextColour() {
        return colour;
    }

    public String getCLIString() {
        return colour.escape() + "\u25c9" + TextColour.RESET;
    }
}
