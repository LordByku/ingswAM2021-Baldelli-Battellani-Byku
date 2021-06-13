package it.polimi.ingsw.view.gui.images.marbleMarket;

import javax.swing.*;
import java.awt.*;

public class MarbleImage extends JPanel {
    private Image img;

    public MarbleImage(String img) {
        this(new ImageIcon(img).getImage());
    }

    public MarbleImage(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
