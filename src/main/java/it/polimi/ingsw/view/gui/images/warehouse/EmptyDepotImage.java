package it.polimi.ingsw.view.gui.images.warehouse;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.synth.SynthScrollBarUI;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class EmptyDepotImage extends JPanel {
    private Image img;

    public EmptyDepotImage() {
        super(new GridBagLayout());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("Depots/emptyDepotSlot.png");
        try {
            this.img = ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(25, 25));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, 25, 25, null);
        }
    }
}
