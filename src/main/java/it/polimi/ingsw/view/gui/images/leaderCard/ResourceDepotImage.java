package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.leaderCards.LeaderCardDepot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ResourceDepotImage extends JPanel {
    private final Image image;
    private final int size;

    public ResourceDepotImage(LeaderCardDepot depot, int size) throws IOException {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Depots/" + depot.getFilename() + ".png");
        image = ImageIO.read(resource);
        this.size = size;

        this.setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, size, size, null);
        }
    }
}
