package it.polimi.ingsw.view.gui.images.devCard;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RequirementsPanel {
    private final JPanel panel;

    public RequirementsPanel(ConcreteResourceSet concreteResourceSet, int devCardWidth, int devCardHeight) {
        ArrayList<JPanel> resourcePanels = new ArrayList<>();

        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            int count = concreteResourceSet.getCount(concreteResource);
            if (count > 0) {
                resourcePanels.add(ResourceImage.getResourcePanel(count, concreteResource.getResourceImageType(), devCardHeight / 25));
            }
        }

        panel = new JPanel();
        panel.setBounds(devCardWidth * 3 / 10, devCardHeight / 30, devCardWidth * 4 / 10, devCardHeight / 9);

        if (resourcePanels.size() == 4) {
            panel.setLayout(new GridLayout(2, 2));
        } else {
            panel.setLayout(new GridLayout(1, resourcePanels.size()));
        }

        for (JPanel resourcePanel : resourcePanels) {
            panel.add(resourcePanel);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
