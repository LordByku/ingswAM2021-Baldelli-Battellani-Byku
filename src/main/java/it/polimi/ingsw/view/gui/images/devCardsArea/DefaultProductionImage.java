package it.polimi.ingsw.view.gui.images.devCardsArea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class DefaultProductionImage extends JPanel {
    private Image img;

    public DefaultProductionImage() {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Punchboard/scroll.png");
        try {
            this.img = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(250, 300));
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, 250, 300, null);
        }
    }
}
