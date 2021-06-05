package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.view.gui.images.resources.ProductionInPanel;
import it.polimi.ingsw.view.gui.images.resources.ProductionOutPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class ProductionLeaderCardImage extends LeaderCardImage {
    public ProductionLeaderCardImage(ProductionLeaderCard leaderCard, int width) throws IOException {
        super("12", leaderCard, width);

        JPanel productionPanel = new JPanel();
        productionPanel.setLayout(new BoxLayout(productionPanel, BoxLayout.X_AXIS));
        productionPanel.setBounds(width / 12, height * 3 / 4, width * 5 / 6, height / 5);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel inputPanel = new ProductionInPanel(leaderCard.getProductionPower().getInput(), null, height / 25).getPanel();
        productionPanel.add(inputPanel, gbc);

        gbc.gridy++;

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("}", SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(1, 1, 1, 1));
        label.setFont(label.getFont().deriveFont((float) height / 7));
        labelPanel.add(label);

        productionPanel.add(labelPanel, gbc);

        gbc.gridy++;

        JPanel outputPanel = new ProductionOutPanel(leaderCard.getProductionPower().getOutput(), null, height / 25).getPanel();
        productionPanel.add(outputPanel, gbc);

        this.add(productionPanel);
    }
}
