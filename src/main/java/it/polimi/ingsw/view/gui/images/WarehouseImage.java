package it.polimi.ingsw.view.gui.images;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class WarehouseImage extends JPanel {
    private Image img;
    private final int width=100;
    private final int height = 200;

    public WarehouseImage(String img) {
        this(new ImageIcon(img).getImage());
    }

    public WarehouseImage(Image img) {
        super(null);
        this.img = img;
        this.setPreferredSize(new Dimension(width, height));
        //this.setOpaque(false);
        this.setBorder(new LineBorder(Color.RED));
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
