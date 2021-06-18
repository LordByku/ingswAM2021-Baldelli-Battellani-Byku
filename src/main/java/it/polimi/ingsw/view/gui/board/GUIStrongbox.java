package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;
import java.awt.*;

public class GUIStrongbox implements LocalModelElementObserver {
    JPanel strongboxPanel;
    Client client;
    ConcreteResourceSet strongbox;
    JPanel resourcePanel;
    JLabel resourceQuantity;


    public GUIStrongbox(Client client, JPanel strongboxPanel) {
        this.client = client;
        this.strongboxPanel = strongboxPanel;
        strongbox = client.getModel().getPlayer(client.getNickname()).getBoard().getStrongBox();
    }

    public void loadStrongbox() {
        int quantity;
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;
        for (ConcreteResource resource : ConcreteResource.values()) {
            quantity = strongbox.getCount(resource);
            resourcePanel = new ResourceImage(resource.getResourceImageType(), 30);
            resourceQuantity = new JLabel(Integer.toString(quantity));
            c.gridx = 0;
            strongboxPanel.add(resourceQuantity, c);
            c.gridx++;
            strongboxPanel.add(resourcePanel, c);
            c.gridy++;
        }
    }

    @Override
    public void notifyObserver() {

    }

    @Override
    public void clean() {

    }
}
