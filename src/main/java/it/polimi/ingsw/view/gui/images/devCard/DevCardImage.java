package it.polimi.ingsw.view.gui.images.devCard;

import it.polimi.ingsw.model.devCards.DevCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class DevCardImage extends JPanel {
    private final Image image;
    private final int width;
    private final int height;

    public DevCardImage(String filename, int width, DevCard devCard) throws IOException {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("DevCardPNGs/" + filename + ".png");
        image = ImageIO.read(resource);
        this.width = width;
        this.height = (int) (1.5 * width);

        this.add(new RequirementsPanel(devCard.getReqResources(), width, height).getPanel());

        JPanel points = new JPanel();
        points.setLayout(new GridBagLayout());
        points.setBounds(width * 4 / 9, height * 6 / 7,width / 9, height / 12);

        JLabel pointsLabel = new JLabel("" + devCard.getPoints(), SwingConstants.CENTER);
        pointsLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
        pointsLabel.setFont(pointsLabel.getFont().deriveFont((float) height / 20));
        points.add(pointsLabel);

        this.add(points);

        this.add(new ProductionInPanel(devCard.getProductionPower().getInput(), width, height).getPanel());

        this.add(new ProductionOutPanel(devCard.getProductionPower().getOutput(), width, height).getPanel());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
}
