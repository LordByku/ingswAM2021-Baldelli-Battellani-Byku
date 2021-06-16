package it.polimi.ingsw.view.gui.images.leaderCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class LeaderCardsBackImage extends JPanel {
    private final int width;
    private final int height;
    private Image img;


    public LeaderCardsBackImage(String path, int width) throws IOException {
        super(null);
        this.width = width;
        this.height = (int) (1.5 * width);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        this.img = ImageIO.read(resource);
        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        }
    }
}
