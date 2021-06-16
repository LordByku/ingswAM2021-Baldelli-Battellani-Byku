package it.polimi.ingsw.view.gui.images.devCardsArea;

import javax.swing.*;
import java.awt.*;

public class DevCardSlotImage extends JPanel {
    private Image img;

    public DevCardSlotImage(Image img) {
        super(null);
        this.img = img;
        this.setPreferredSize(new Dimension(200, 300));
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, 200, 300, null);
        }
    }
}

