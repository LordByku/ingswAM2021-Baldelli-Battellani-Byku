package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.StrongboxImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.view.localModel.Strongbox;

import javax.swing.*;
import java.awt.*;

public class GUIStrongbox implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private JPanel strongboxPanel;
    private Strongbox strongbox;
    private JPanel resourcePanel;
    private JLabel resourceQuantity;
    private Player player;
    private JPanel backgroundPanel;

    public GUIStrongbox(GUI gui, Client client, JPanel strongboxPanel, String nickname) {
        this.gui = gui;
        this.client = client;
        this.strongboxPanel = strongboxPanel;
        this.backgroundPanel = new StrongboxImage(150, 150);

        player = client.getModel().getPlayer(nickname);
        strongbox = player.getBoard().getStrongBox();

        strongbox.addObserver(this);
        player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);

        // TODO : production choice resources
    }

    public void loadStrongbox() {
        int quantity;
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;

        CommandBuffer commandBuffer = player.getCommandBuffer();
        for (ConcreteResource resource : ConcreteResource.values()) {
            quantity = strongbox.getContent().getCount(resource);
            resourcePanel = new ResourceImage(resource.getResourceImageType(), 30);
            resourceQuantity = new JLabel(Integer.toString(quantity));
            resourceQuantity.setForeground(Color.white);
            c.gridx = 0;
            backgroundPanel.add(resourceQuantity, c);
            c.gridx++;
            backgroundPanel.add(resourcePanel, c);

            if (commandBuffer != null && !commandBuffer.isCompleted() && player.getNickname().equals(client.getNickname())) {
                switch (commandBuffer.getCommandType()) {
                    case PURCHASE: {
                        Purchase purchaseCommand = (Purchase) commandBuffer;
                        if (purchaseCommand.getDeckIndex() != -1) {
                            resourcePanel.addMouseListener(new ButtonClickEvent((e) -> {
                                ConcreteResourceSet[] depots = new ConcreteResourceSet[player.getBoard().getWarehouse().getDepots().size()];
                                for (int k = 0; k < depots.length; ++k) {
                                    depots[k] = new ConcreteResourceSet();
                                }
                                ConcreteResourceSet strongBox = new ConcreteResourceSet();

                                strongBox.addResource(resource);

                                JsonObject value = new JsonObject();
                                value.add("warehouse", JsonUtil.getInstance().serialize(depots));
                                value.add("strongbox", JsonUtil.getInstance().serialize(strongBox));

                                JsonObject message = client.buildCommandMessage("spendResources", value);
                                gui.bufferWrite(message.toString());
                            }));
                        }
                        break;
                    }
                    case PRODUCTION: {
                        Production productionCommand = (Production) commandBuffer;
                        if (productionCommand.getProductionsToActivate() != null) {
                            resourcePanel.addMouseListener(new ButtonClickEvent((e) -> {
                                ConcreteResourceSet[] depots = new ConcreteResourceSet[player.getBoard().getWarehouse().getDepots().size()];
                                for (int k = 0; k < depots.length; ++k) {
                                    depots[k] = new ConcreteResourceSet();
                                }
                                ConcreteResourceSet strongBox = new ConcreteResourceSet();

                                strongBox.addResource(resource);

                                JsonObject value = new JsonObject();
                                value.add("warehouse", JsonUtil.getInstance().serialize(depots));
                                value.add("strongbox", JsonUtil.getInstance().serialize(strongBox));

                                JsonObject message = client.buildCommandMessage("spendResources", value);
                                gui.bufferWrite(message.toString());
                            }));
                        }
                        break;
                    }
                }
            }

            c.gridy++;
        }
        c.gridx=1;
        c.gridy=0;
        c.insets=new Insets(0,0,0,0);
        strongboxPanel.add(backgroundPanel,c);
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            strongboxPanel.removeAll();
            loadStrongbox();
            strongboxPanel.revalidate();
            strongboxPanel.repaint();
        });
    }

    @Override
    public void clean() {
        strongbox.removeObserver(this);
        player.getCommandElement().removeObserver(this, CommandType.PURCHASE);
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
    }
}
