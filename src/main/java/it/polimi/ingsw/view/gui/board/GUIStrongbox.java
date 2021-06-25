package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.StrongboxImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.view.localModel.Strongbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUIStrongbox implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final JPanel strongboxPanel;
    private final Strongbox strongbox;
    private final Player player;
    private final JPanel backgroundPanel;
    private JPanel resourcePanel;
    private JLabel resourceQuantity;

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
    }

    public void loadStrongbox() {
        int quantity;
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.gridy = 0;

        CommandBuffer commandBuffer = player.getCommandBuffer();

        if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
            Production productionCommand = (Production) commandBuffer;
            if (productionCommand.getObtainedResources() != null) {
                JPanel choicePanel = new JPanel();
                ArrayList<ChoiceResource> choiceResources = productionCommand.getObtainedResources().getChoiceResources();
                for (int i = 0; i < choiceResources.size(); i++) {
                    ChoiceResource choiceResource = choiceResources.get(i);
                    ResourceImageType resourceImageType = choiceResource.getResourceImageType();

                    JPanel container = new JPanel();
                    JPanel imagePanel = new ResourceImage(resourceImageType, 25);

                    int finalI = i;
                    imagePanel.addMouseListener(new ButtonClickEvent((e) -> {
                        JPanel popupContent = new JPanel();
                        popupContent.setLayout(new BoxLayout(popupContent, BoxLayout.X_AXIS));

                        MouseEvent mouseEvent = (MouseEvent) e;
                        Popup popup = PopupFactory.getSharedInstance().getPopup(imagePanel, popupContent, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen());

                        for (ConcreteResource concreteResource : ConcreteResource.values()) {
                            if (choiceResource.canChoose(concreteResource)) {
                                ResourceImageType concreteImageType = concreteResource.getResourceImageType();
                                JPanel concreteResourcePanel = new ResourceImage(concreteImageType, 25);

                                concreteResourcePanel.addMouseListener(new ButtonClickEvent((event) -> {
                                    ConcreteResource[] resourcesArray = new ConcreteResource[choiceResources.size()];
                                    resourcesArray[finalI] = concreteResource;
                                    JsonObject message = client.buildCommandMessage("choiceSelection", JsonUtil.getInstance().serialize(resourcesArray));
                                    gui.bufferWrite(message.toString());
                                    popup.hide();
                                }, true));

                                popupContent.add(concreteResourcePanel);
                            }
                        }

                        GUIUtil.addButton("x", popupContent, new ButtonClickEvent((event) -> {
                            popup.hide();
                        }, true));

                        popup.show();
                    }));

                    container.add(imagePanel);
                    choicePanel.add(container, c);
                }
                strongboxPanel.add(choicePanel);
            }
        }

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

        if (commandBuffer != null && !commandBuffer.isCompleted()) {
            switch (commandBuffer.getCommandType()) {
                case PURCHASE: {
                    Purchase purchaseCommand = (Purchase) commandBuffer;
                    if (purchaseCommand.getDeckIndex() != -1) {
                        addSpentPanel(c, purchaseCommand.getCurrentTotalToSpend());
                    } else {
                        c.gridx = 1;
                        c.gridy = 0;
                        c.insets = new Insets(0, 0, 0, 0);
                        strongboxPanel.add(backgroundPanel, c);
                    }
                    break;
                }
                case PRODUCTION: {
                    Production productionCommand = (Production) commandBuffer;
                    if (productionCommand.getProductionsToActivate() != null && productionCommand.getObtainedResources() == null) {
                        addSpentPanel(c, productionCommand.getCurrentTotalToSpend());
                    } else {
                        c.gridx = 1;
                        c.gridy = 0;
                        c.insets = new Insets(0, 0, 0, 0);
                        strongboxPanel.add(backgroundPanel, c);
                    }
                    break;
                }
                default: {
                    c.gridx = 1;
                    c.gridy = 0;
                    c.insets = new Insets(0, 0, 0, 0);
                    strongboxPanel.add(backgroundPanel, c);
                }
            }
        } else {
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            strongboxPanel.add(backgroundPanel, c);
        }
    }

    private void addSpentPanel(GridBagConstraints c, ConcreteResourceSet currentSpent) {
        JPanel spentPanel = new JPanel(new GridBagLayout());
        int count = 0;
        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            ResourceImageType resourceImageType = concreteResource.getResourceImageType();
            for (int i = 0; i < currentSpent.getCount(concreteResource); ++i) {
                JPanel container = new JPanel();
                JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                container.add(imagePanel);
                c.gridx = count / 2;
                c.gridy = count % 2;
                spentPanel.add(container, c);
                count++;
            }
        }
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        strongboxPanel.add(backgroundPanel, c);

        c.gridx++;
        strongboxPanel.add(spentPanel, c);
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            backgroundPanel.removeAll();
            strongboxPanel.removeAll();
            loadStrongbox();
            backgroundPanel.revalidate();
            backgroundPanel.repaint();
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
