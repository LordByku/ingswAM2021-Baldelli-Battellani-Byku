package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.view.cli.BackGroundColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * CardColour represents all possible cards' colours
 */
public enum CardColour {
    GREEN(BackGroundColor.GREEN, "green"),
    BLUE(BackGroundColor.BLUE, "blue"),
    YELLOW(BackGroundColor.YELLOW, "yellow"),
    PURPLE(BackGroundColor.PURPLE, "purple");

    private final BackGroundColor colour;
    private final String filename;
    private Image image;

    CardColour(BackGroundColor colour, String filename) {
        this.colour = colour;
        this.filename = filename;
    }

    public BackGroundColor getColour() {
        return colour;
    }

    public String getCLIString() {
        return colour.escape() + "     " + BackGroundColor.RESET;
    }

    public void loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("CardColour/" + filename + ".png");
        image = ImageIO.read(resource);
    }

    public Image getImage() {
        return image;
    }
}

