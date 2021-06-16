package it.polimi.ingsw.view.gui.images.marbleMarket;

import javax.swing.*;
import java.awt.*;

public class MarbleImage extends JPanel {
    private final int width = 48;
    private final int height = 48;
    private Image img;

    public MarbleImage(String img) {
        this(new ImageIcon(img).getImage());
    }

    public MarbleImage(Image img) {
        super(null);
        this.img = img;
        this.setPreferredSize(new Dimension(width, height));
        this.setOpaque(false);
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
