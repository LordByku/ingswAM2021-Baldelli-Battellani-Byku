package it.polimi.ingsw.view.gui.images.resources;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    public static JPanel getResourcePanel(int count, ResourceImageType resourceImageType, int height) {
        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.X_AXIS));

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new GridBagLayout());

        JLabel quantityLabel = new JLabel("" + count, SwingConstants.CENTER);
        quantityLabel.setFont(quantityLabel.getFont().deriveFont((float) height));
        quantityLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
        quantityPanel.add(quantityLabel);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridBagLayout());

        JPanel imagePanel = new ResourceImage(resourceImageType, height);
        containerPanel.add(imagePanel);

        resourcePanel.add(quantityPanel);
        resourcePanel.add(containerPanel);

        return resourcePanel;
    }
}
