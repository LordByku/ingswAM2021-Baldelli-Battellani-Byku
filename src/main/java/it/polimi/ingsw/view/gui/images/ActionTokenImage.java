package it.polimi.ingsw.view.gui.images;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ActionTokenImage extends JPanel {
    private final int width;
    private final int height;
    private Image img;

    public ActionTokenImage(Image img, int dim) {
        super(null);
        this.width = dim;
        this.height = dim;
        this.img = img;
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
