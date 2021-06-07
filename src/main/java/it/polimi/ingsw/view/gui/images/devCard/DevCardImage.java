package it.polimi.ingsw.view.gui.images.devCard;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.view.gui.images.resources.ProductionInPanel;
import it.polimi.ingsw.view.gui.images.resources.ProductionOutPanel;

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

        this.setPreferredSize(new Dimension(width, height));

        this.add(new RequirementsPanel(devCard.getReqResources(), width, height).getPanel());

        JPanel points = new JPanel();
        points.setLayout(new GridBagLayout());
        points.setBounds(width * 4 / 9, height * 6 / 7,width / 9, height / 12);

        JLabel pointsLabel = new JLabel("" + devCard.getPoints(), SwingConstants.CENTER);
        pointsLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
        pointsLabel.setFont(pointsLabel.getFont().deriveFont((float) height / 20));
        points.add(pointsLabel);

        this.add(points);

        Rectangle productionInBorder = new Rectangle(width / 9, height * 16 / 31, width * 3 / 10, height * 3 / 11);
        this.add(new ProductionInPanel(devCard.getProductionPower().getInput(), productionInBorder, height / 25).getPanel());

        Rectangle productionOutBorder = new Rectangle(width * 16 / 31, height * 16 / 31, width * 7 / 20, height * 3 / 11);
        this.add(new ProductionOutPanel(devCard.getProductionPower().getOutput(), productionOutBorder, height / 25).getPanel());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
}
