package it.polimi.ingsw.view.gui.images.marbleMarket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MarketTrayImage extends JPanel {
    private final int width = 375;
    private final int height = 306;
    private Image img;

    public MarketTrayImage() {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Punchboard/marketTray.png");
        try {
            this.img = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        }
    }
}
