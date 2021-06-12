package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ResourcesRequirementsPanel {
    private final JPanel panel;

    public ResourcesRequirementsPanel(ConcreteResourceSet concreteResourceSet, int leaderCardWidth, int leaderCardHeight) {
        ArrayList<JPanel> resourcePanels = new ArrayList<>();

        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            int count = concreteResourceSet.getCount(concreteResource);
            if (count > 0) {
                resourcePanels.add(ResourceImage.getResourcePanel(count, concreteResource.getResourceImageType(), leaderCardHeight / 16));
            }
        }

        panel = new JPanel();
        panel.setBounds(leaderCardWidth / 20, leaderCardHeight / 30, leaderCardWidth * 9 / 10, leaderCardHeight / 7);


        panel.setLayout(new GridLayout(1, resourcePanels.size()));

        for (JPanel resourcePanel : resourcePanels) {
            panel.add(resourcePanel);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
