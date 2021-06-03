package it.polimi.ingsw.view.gui.images.resources;

import javax.swing.*;
import java.awt.*;

public class ResourceImage extends JPanel {
    private final Image image;
    private final int size;

    public ResourceImage(ResourceImageType resourceImageType, int size) {
        super(null);
        image = resourceImageType.getImage();
        this.size = size;

        this.setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
            g.drawImage(image, 0, 0, size, size, null);
        }
    }
}
