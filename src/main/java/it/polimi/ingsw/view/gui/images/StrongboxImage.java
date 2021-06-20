package it.polimi.ingsw.view.gui.images;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class StrongboxImage extends JPanel {
    private final int width;
    private final int height;
    private Image img;

    public StrongboxImage(int width, int height) {
        super(null);
        this.width=width;
        this.height=height;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Punchboard/strongbox.jpg");
        try {
            this.img = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(width, height));
        //this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        }
    }
}
