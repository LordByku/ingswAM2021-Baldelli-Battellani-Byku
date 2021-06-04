package it.polimi.ingsw.view.gui.images.devCard;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductionOutPanel {
    private final JPanel panel;

    public ProductionOutPanel(ObtainableResourceSet obtainableResourceSet, int devCardWidth, int devCardHeight) {
        ArrayList<JPanel> resourcePanels = new ArrayList<>();

        ChoiceResourceSet choiceResourceSet = obtainableResourceSet.getResourceSet();

        ConcreteResourceSet concreteResourceSet = choiceResourceSet.getConcreteResources();

        for(ConcreteResource concreteResource: ConcreteResource.values()) {
            int count = concreteResourceSet.getCount(concreteResource);
            if(count > 0) {
                resourcePanels.add(ResourceImage.getResourcePanel(count, concreteResource.getResourceImageType(), devCardHeight / 25));
            }
        }

        int count = choiceResourceSet.getChoiceResources().size();
        if(count > 0) {
            resourcePanels.add(ResourceImage.getResourcePanel(count, ResourceImageType.CHOICE, devCardHeight / 25));
        }

        int faithPoints = obtainableResourceSet.getFaithPoints();
        if(faithPoints > 0) {
            resourcePanels.add(ResourceImage.getResourcePanel(faithPoints, ResourceImageType.FAITHPOINT, devCardHeight / 25));
        }

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBounds(devCardWidth * 16 / 31, devCardHeight * 16 / 31, devCardWidth * 7 / 20, devCardHeight * 3 / 11);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        int cols = resourcePanels.size() / 4;

        for (int i = 0; i < resourcePanels.size(); i++) {
            JPanel resourcePanel = resourcePanels.get(i);
            if(i == 4 && i == resourcePanels.size() - 1) {
                gbc.gridwidth = 2;
            }

            panel.add(resourcePanel, gbc);
            if (gbc.gridx == cols) {
                gbc.gridx = 0;
                gbc.gridy++;
            } else {
                gbc.gridx++;
            }
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
