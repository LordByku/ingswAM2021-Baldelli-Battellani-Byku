package it.polimi.ingsw.view.gui.images.resources;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductionOutPanel {
    private final JPanel panel;

    public ProductionOutPanel(ObtainableResourceSet obtainableResourceSet, Rectangle bounds, int font) {
        ArrayList<JPanel> resourcePanels = new ArrayList<>();

        ChoiceResourceSet choiceResourceSet = obtainableResourceSet.getResourceSet();

        ConcreteResourceSet concreteResourceSet = choiceResourceSet.getConcreteResources();

        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            int count = concreteResourceSet.getCount(concreteResource);
            if (count > 0) {
                resourcePanels.add(ResourceImage.getResourcePanel(count, concreteResource.getResourceImageType(), font));
            }
        }

        int count = choiceResourceSet.getChoiceResources().size();
        if (count > 0) {
            resourcePanels.add(ResourceImage.getResourcePanel(count, ResourceImageType.CHOICE, font));
        }

        int faithPoints = obtainableResourceSet.getFaithPoints();
        if (faithPoints > 0) {
            resourcePanels.add(ResourceImage.getResourcePanel(faithPoints, ResourceImageType.FAITHPOINT, font));
        }

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        if (bounds != null) {
            panel.setBounds(bounds);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        int cols = resourcePanels.size() / 4;

        for (int i = 0; i < resourcePanels.size(); i++) {
            JPanel resourcePanel = resourcePanels.get(i);
            if (i == 4 && i == resourcePanels.size() - 1) {
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
