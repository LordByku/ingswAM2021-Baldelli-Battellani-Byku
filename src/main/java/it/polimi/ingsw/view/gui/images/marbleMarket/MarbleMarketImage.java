package it.polimi.ingsw.view.gui.images.marbleMarket;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MarbleMarketImage extends JPanel {
    private Image img;
    private final int width = 550;
    private final int height = 700;

    public MarbleMarketImage(String img) {
        this(new ImageIcon(img).getImage());
    }

    public MarbleMarketImage(Image img) {
        super(null);
        this.img = img;
        this.setPreferredSize(new Dimension(width,height));
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
