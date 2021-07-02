package it.polimi.ingsw.model.devCards;

import it.polimi.ingsw.view.cli.BackGroundColor;
import it.polimi.ingsw.view.cli.CLIPrintable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * CardColour represents all possible cards' colours
 */
public enum CardColour implements CLIPrintable {
    /**
     * Green CardColour
     */
    GREEN(BackGroundColor.GREEN, "green", 0),
    /**
     * Blue CardColour
     */
    BLUE(BackGroundColor.BLUE, "blue", 2),
    /**
     * Yellow CardColour
     */
    YELLOW(BackGroundColor.YELLOW, "yellow", 3),
    /**
     * Purple CardColour
     */
    PURPLE(BackGroundColor.PURPLE, "purple", 1);

    /**
     * colour is the CLI background colour
     */
    private final BackGroundColor colour;
    /**
     * filename is the String referring to the card colour icon
     */
    private final String filename;
    /**
     * fileOffset is the offset in the DevCard pngs for this colour
     */
    private final int fileOffset;
    /**
     * image is the GUI element containing the CardColour icon
     */
    private Image image;

    /**
     * Default constructor
     * @param colour the CLI background colour
     * @param filename the name of the file containing of the CardColour icon
     * @param fileOffset the offset in the DevCard pngs for this colour
     */
    CardColour(BackGroundColor colour, String filename, int fileOffset) {
        this.colour = colour;
        this.filename = filename;
        this.fileOffset = fileOffset;
    }

    /**
     * getColour returns the CLI background colour
     * @return the CLI background colour
     */
    public BackGroundColor getColour() {
        return colour;
    }

    @Override
    public String getCLIString() {
        return colour.escape() + "     " + BackGroundColor.RESET;
    }

    /**
     * loadImage loads the CardColour icon from the filename
     */
    public void loadImage() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("CardColour/" + filename + ".png");
        try {
            image = ImageIO.read(resource);
        } catch (IOException e) {}
    }

    /**
     * getImage returns the image GUI element
     * @return the image GUI element
     */
    public Image getImage() {
        return image;
    }

    /**
     * getFileOffset returns the file offset in the DevCard pngs
     * @return the file offset in the DevCard pngs
     */
    public int getFileOffset() {
        return fileOffset;
    }
}

