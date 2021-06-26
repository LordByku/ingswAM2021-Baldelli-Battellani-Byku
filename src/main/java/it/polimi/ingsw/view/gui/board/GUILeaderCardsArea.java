package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.leaderCard.DepotLeaderCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.ResourceDepotImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.PlayedLeaderCardsArea;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.view.localModel.Warehouse;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class GUILeaderCardsArea implements LocalModelElementObserver {
    private final GUI gui;
    private final PlayedLeaderCardsArea playedLeaderCards;
    private final Client client;
    private final JPanel leaderCardsArea;
    private final Player player;

    public GUILeaderCardsArea(GUI gui, Client client, JPanel leaderCardsArea, String nickname) {
        this.gui = gui;
        this.client = client;
        this.leaderCardsArea = leaderCardsArea;
        player = client.getModel().getPlayer(nickname);
        playedLeaderCards = player.getBoard().getPlayedLeaderCards();

        playedLeaderCards.addObserver(this);
        player.getBoard().getWarehouse().addObserver(this);
        player.getCommandElement().addObserver(this, CommandType.MARKET);
        player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);
    }

    public void loadLeaderCardsArea() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);

        CommandBuffer commandBuffer = player.getCommandBuffer();

        int productionIndex = 0;
        int depotCardIndex = 0;
        for (int playedLeaderCardID : playedLeaderCards.getLeaderCards()) {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCardID);
            JPanel cardPanel = leaderCard.getLeaderCardImage(150);

            container.add(cardPanel);

            if (leaderCard.isType(LeaderCardType.PRODUCTION)) {
                if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
                    Production productionCommand = (Production) commandBuffer;

                    int[] currentSelection = productionCommand.getProductionsToActivate();
                    int n = currentSelection != null ? currentSelection.length : 0;

                    boolean selected = false;
                    if (currentSelection != null) {
                        for (int selection : currentSelection) {
                            if (4 + productionIndex == selection) {
                                selected = true;
                            }
                        }
                    }
                    boolean finalSelected = selected;
                    int finalProductionIndex = productionIndex;
                    JButton button = GUIUtil.addButton("select", container, new ButtonClickEvent((e) -> {
                        if (finalSelected) {
                            int[] selection = new int[n - 1];
                            for (int j = 0, k = 0; j < n; ++j) {
                                if (currentSelection[j] != 4 + finalProductionIndex) {
                                    selection[k++] = currentSelection[j];
                                }
                            }
                            JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                            gui.bufferWrite(message.toString());
                        } else {
                            int[] selection = new int[n + 1];
                            for (int j = 0; j < n; ++j) {
                                selection[j] = currentSelection[j];
                            }
                            selection[n] = 4 + finalProductionIndex;
                            JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                            gui.bufferWrite(message.toString());
                        }
                    }));

                    if (selected) {
                        Border redBorder = BorderFactory.createLineBorder(Color.RED);
                        button.setBorder(redBorder);
                    } else {
                        button.setBorder(null);
                    }
                }

                ++productionIndex;
            }

            if (leaderCard.isType(LeaderCardType.DEPOT)) {
                DepotLeaderCard depotLeaderCard = (DepotLeaderCard) leaderCard;
                DepotLeaderCardImage depotLeaderCardImage = (DepotLeaderCardImage) cardPanel;

                ArrayList<ResourceDepotImage> resourceDepotImages = depotLeaderCardImage.getResourceDepotImages();
                int depotIndex = LocalConfig.getInstance().getNumberOfDepots() + depotCardIndex;

                Warehouse warehouse = player.getBoard().getWarehouse();
                ConcreteResourceSet depot = warehouse.getDepots().get(depotIndex);

                ConcreteResource resource = depot.getResourceType();

                for (int i = 0; i < depot.size(); ++i) {
                    resourceDepotImages.get(i).setImage(resource.getResourceImageType());

                    if (commandBuffer != null && !commandBuffer.isCompleted()) {
                        switch (commandBuffer.getCommandType()) {
                            case MARKET: {
                                Market marketCommand = (Market) commandBuffer;
                                if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                                    resourceDepotImages.get(i).addMouseListener(new ButtonClickEvent((e) -> {
                                        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                                        concreteResourceSet.addResource(resource);

                                        JsonObject value = new JsonObject();
                                        value.addProperty("depotIndex", depotIndex);
                                        value.add("resources", JsonUtil.getInstance().serialize(concreteResourceSet));
                                        JsonObject message = client.buildCommandMessage("removeFromDepot", value);
                                        gui.bufferWrite(message.toString());
                                    }));
                                }
                                break;
                            }
                            case PURCHASE: {
                                Purchase purchaseCommand = (Purchase) commandBuffer;
                                if (purchaseCommand.getDeckIndex() != -1) {
                                    resourceDepotImages.get(i).addMouseListener(new ButtonClickEvent((e) -> {
                                        ConcreteResourceSet[] depots = new ConcreteResourceSet[warehouse.getDepots().size()];
                                        for (int k = 0; k < depots.length; ++k) {
                                            depots[k] = new ConcreteResourceSet();
                                        }
                                        ConcreteResourceSet strongBox = new ConcreteResourceSet();

                                        depots[depotIndex].addResource(resource);

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
                                    resourceDepotImages.get(i).addMouseListener(new ButtonClickEvent((e) -> {
                                        ConcreteResourceSet[] depots = new ConcreteResourceSet[warehouse.getDepots().size()];
                                        for (int k = 0; k < depots.length; ++k) {
                                            depots[k] = new ConcreteResourceSet();
                                        }
                                        ConcreteResourceSet strongBox = new ConcreteResourceSet();

                                        depots[depotIndex].addResource(resource);

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
                }

                if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.MARKET) {
                    Market marketCommand = (Market) commandBuffer;
                    if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                        for (int i = depot.size(); i < depotLeaderCard.getDepot().getSlots(); ++i) {
                            resourceDepotImages.get(i).addMouseListener(new ButtonClickEvent((e) -> {
                                if (GUIWarehouse.getSelectedResourceReference().get() != null) {
                                    ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                                    concreteResourceSet.addResource(GUIWarehouse.getSelectedResourceReference().get());
                                    GUIWarehouse.getSelectedResourceReference().set(null);
                                    GUIWarehouse.getSelectedPanelReference().set(null);

                                    JsonObject value = new JsonObject();
                                    value.addProperty("depotIndex", depotIndex);
                                    value.add("resources", JsonUtil.getInstance().serialize(concreteResourceSet));
                                    JsonObject message = client.buildCommandMessage("addToDepot", value);
                                    gui.bufferWrite(message.toString());
                                }
                            }));
                        }

                        JPanel buttonPanel = new JPanel(new GridBagLayout());
                        JButton button = new JButton("switch");
                        button.setPreferredSize(new Dimension(80, 20));
                        button.setFont(new Font("Arial", Font.PLAIN, 10));

                        button.addActionListener(new ButtonClickEvent((e) -> {
                            if (GUIWarehouse.getSelectedButtonReference().get() == null) {
                                Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                button.setBorder(redBorder);

                                GUIWarehouse.getSelectedButtonReference().set(button);
                                GUIWarehouse.getSelectedIndexReference().set(depotIndex);
                            } else {
                                int indexA = GUIWarehouse.getSelectedIndexReference().get(), indexB = depotIndex;

                                GUIWarehouse.getSelectedButtonReference().get().setBorder(null);
                                GUIWarehouse.getSelectedButtonReference().set(null);
                                GUIWarehouse.getSelectedIndexReference().set(null);

                                JsonObject value = new JsonObject();
                                value.addProperty("depotIndexA", indexA);
                                value.addProperty("depotIndexB", indexB);
                                JsonObject message = client.buildCommandMessage("swapFromDepots", value);
                                gui.bufferWrite(message.toString());
                            }
                        }));

                        buttonPanel.add(button);
                        container.add(buttonPanel);
                    }
                }
            }

            leaderCardsArea.add(container, c);
            c.gridx++;
        }
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            leaderCardsArea.removeAll();
            loadLeaderCardsArea();
            leaderCardsArea.revalidate();
            leaderCardsArea.repaint();
        });
    }

    @Override
    public void clean() {
        playedLeaderCards.removeObserver(this);
        player.getBoard().getWarehouse().removeObserver(this);
        player.getCommandElement().removeObserver(this, CommandType.MARKET);
        player.getCommandElement().removeObserver(this, CommandType.PURCHASE);
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
    }
}
