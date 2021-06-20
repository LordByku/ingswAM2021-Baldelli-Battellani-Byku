package it.polimi.ingsw.view.gui.images.faithTrack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CrossImage extends JPanel {
    private Image img;

    public CrossImage(String path) {
        super(new GridBagLayout());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        try {
            this.img = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(30, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, 30, 30, null);
        }
    }
}
