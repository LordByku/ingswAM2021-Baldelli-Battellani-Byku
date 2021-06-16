package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.images.devCardsArea.DefaultProductionImage;
import it.polimi.ingsw.view.gui.images.resources.ProductionInPanel;
import it.polimi.ingsw.view.gui.images.resources.ProductionOutPanel;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class GUIDefaultProductionPower {
    private JPanel devCardsArea;
    private ProductionDetails defaultProductionPower;
    private JPanel defProdPower;

    public GUIDefaultProductionPower(JPanel devCardsArea) {
        this.devCardsArea = devCardsArea;
        defaultProductionPower = LocalConfig.getInstance().getDefaultProductionPower();
        try {
            defProdPower = new DefaultProductionImage("Punchboard/scroll.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultProductionPower() {
        JPanel productionPanel = new JPanel();
        productionPanel.setLayout(new BoxLayout(productionPanel, BoxLayout.X_AXIS));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        for(ConcreteResource concreteResource: ConcreteResource.values()) {
            for(int i = 0; i < 10; ++i) {
                choiceResourceSet.addResource(concreteResource);
            }
        }
        for(int i = 0; i < 10; ++i) {
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        }

        JPanel inputPanel = new ProductionInPanel(defaultProductionPower.getInput(), null, 18).getPanel();
        productionPanel.add(inputPanel, gbc);

        gbc.gridy++;

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("}", SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(1, 1, 1, 1));
        label.setFont(label.getFont().deriveFont((float) 70));
        labelPanel.add(label);

        productionPanel.add(labelPanel, gbc);

        gbc.gridy++;

        JPanel outputPanel = new ProductionOutPanel(defaultProductionPower.getOutput(), null, 18).getPanel();
        productionPanel.add(outputPanel, gbc);

        defProdPower.add(productionPanel);
        devCardsArea.add(defProdPower);
    }
}
