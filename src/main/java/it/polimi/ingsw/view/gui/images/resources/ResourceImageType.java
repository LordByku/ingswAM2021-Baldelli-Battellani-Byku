package it.polimi.ingsw.view.gui.images.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public enum ResourceImageType {
    COIN("coin"),
    STONE("stone"),
    SHIELD("shield"),
    SERVANT("servant"),
    CHOICE("choice"),
    FAITHPOINT("faithpoint");

    private final String filename;
    private Image image;

    ResourceImageType(String filename) {
        this.filename = filename;
    }

    public void loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Resources/" + filename + ".png");
        image = ImageIO.read(resource);
    }

    public Image getImage() {
        return image;
    }
}
