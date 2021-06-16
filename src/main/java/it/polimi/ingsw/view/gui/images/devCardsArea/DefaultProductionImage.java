package it.polimi.ingsw.view.gui.images.devCardsArea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class DefaultProductionImage extends JPanel {
    private Image img;

    public DefaultProductionImage(String path) throws IOException {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        this.img = ImageIO.read(resource);
        this.setPreferredSize(new Dimension(150, 300));
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, 150, 300, null);
        }
    }
}
