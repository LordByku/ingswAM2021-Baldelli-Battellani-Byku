package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.leaderCards.DiscountLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;
import it.polimi.ingsw.view.gui.images.warehouse.EmptyDepotImage;
import it.polimi.ingsw.view.gui.images.warehouse.WarehouseImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.view.localModel.Warehouse;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GUIWarehouse implements LocalModelElementObserver {
    private static AtomicReference<JPanel> selectedPanel = new AtomicReference<>();
    private static AtomicReference<ConcreteResource> selectedResource = new AtomicReference<>();
    private static AtomicReference<JButton> selectedButton = new AtomicReference<>();
    private static AtomicReference<Integer> selectedIndex = new AtomicReference<>();
    private final GUI gui;
    private final Player player;
    private final Warehouse warehouse;
    private final Client client;
    private final JPanel warehousePanel;
    private final int numOfDepots;
    private final ArrayList<Integer> depotSizes;
    private final JPanel backgroundPanel;

    public GUIWarehouse(GUI gui, Client client, JPanel warehousePanel, String nickname) {
        this.gui = gui;
        this.client = client;
        this.warehousePanel = warehousePanel;
        this.backgroundPanel = new WarehouseImage(150, 150);
        numOfDepots = LocalConfig.getInstance().getNumberOfDepots();
        depotSizes = LocalConfig.getInstance().getDepotSizes();

        player = client.getModel().getPlayer(nickname);
        warehouse = player.getBoard().getWarehouse();

        player.getCommandElement().addObserver(this, CommandType.MARKET);
        player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);
        warehouse.addObserver(this);
    }

    protected static AtomicReference<JPanel> getSelectedPanelReference() {
        return selectedPanel;
    }

    protected static AtomicReference<ConcreteResource> getSelectedResourceReference() {
        return selectedResource;
    }

    protected static AtomicReference<JButton> getSelectedButtonReference() {
        return selectedButton;
    }

    protected static AtomicReference<Integer> getSelectedIndexReference() {
        return selectedIndex;
    }

    public void loadWarehouse() {
        selectedPanel = new AtomicReference<>();
        selectedResource = new AtomicReference<>();
        selectedButton = new AtomicReference<>();
        selectedIndex = new AtomicReference<>();

        CommandBuffer commandBuffer = player.getCommandBuffer();

        JPanel depotsPanel = new JPanel(new GridBagLayout());
        depotsPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        for (int i = 0; i < numOfDepots; i++) {
            int finalI = i;

            JPanel depotPanel = new JPanel(new GridBagLayout());
            ConcreteResourceSet depot = warehouse.getDepots().get(i);
            int count = depot.size();
            ConcreteResource concreteResource = depot.getResourceType();
            for (int j = 0; j < depotSizes.get(i); ++j) {
                if (j < count && concreteResource != null) {
                    depotPanel.add(addResourcePanel(concreteResource, commandBuffer, finalI), c);
                } else {
                    JPanel emptyPanel = new EmptyDepotImage();
                    depotPanel.add(emptyPanel, c);

                    if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.MARKET && player.equals(client.getModel().getPlayer(client.getNickname()))) {
                        Market marketCommand = (Market) commandBuffer;
                        if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                            emptyPanel.addMouseListener(new ButtonClickEvent((e) -> {
                                if (selectedResource.get() != null) {
                                    ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                                    concreteResourceSet.addResource(selectedResource.get());
                                    selectedResource.set(null);
                                    selectedPanel.set(null);

                                    JsonObject value = new JsonObject();
                                    value.addProperty("depotIndex", finalI);
                                    value.add("resources", JsonUtil.getInstance().serialize(concreteResourceSet));
                                    JsonObject message = client.buildCommandMessage("addToDepot", value);
                                    gui.bufferWrite(message.toString());
                                }
                            }));
                        }
                    } else {
                        if (!player.initResources() && player.getNickname().equals(client.getNickname())) {
                            emptyPanel.addMouseListener(new ButtonClickEvent((e) -> {
                                if (selectedResource.get() != null) {
                                    ConcreteResourceSet[] depots = new ConcreteResourceSet[numOfDepots];
                                    for (int k = 0; k < numOfDepots; ++k) {
                                        depots[k] = new ConcreteResourceSet();
                                    }
                                    depots[finalI].addResource(selectedResource.get());
                                    selectedResource.set(null);
                                    selectedPanel.set(null);

                                    if (commandBuffer == null) {
                                        JsonObject request = client.buildRequestMessage(CommandType.INITRESOURCES);
                                        gui.bufferWrite(request.toString());
                                    }
                                    JsonObject message = client.buildCommandMessage("resources", JsonUtil.getInstance().serialize(depots));
                                    gui.bufferWrite(message.toString());
                                }
                            }));
                        }
                    }
                }
                c.gridx++;
            }
            c.gridx = 0;
            c.gridy = i;
            depotsPanel.add(depotPanel, c);
        }

        c.gridx = c.gridy = 0;
        if (!player.initResources() && player.getNickname().equals(client.getNickname())) {
            JPanel initResourcesPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < 2; ++j) {
                    gbc.gridx = i;
                    gbc.gridy = j;

                    ConcreteResource concreteResource = ConcreteResource.values()[2 * i + j];
                    ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                    ResourceImage resourceImage = new ResourceImage(resourceImageType, 25);

                    JPanel imagePanel = new JPanel(new GridBagLayout());
                    imagePanel.add(resourceImage);

                    resourceImage.addMouseListener(new ButtonClickEvent((e) -> {
                        if (imagePanel.equals(selectedPanel.get())) {
                            imagePanel.setBorder(null);
                            selectedPanel.set(null);
                            selectedResource.set(null);
                        } else {
                            if (selectedPanel.get() != null) {
                                selectedPanel.get().setBorder(null);
                            }
                            Border redBorder = BorderFactory.createLineBorder(Color.RED);
                            imagePanel.setBorder(redBorder);

                            selectedPanel.set(imagePanel);
                            selectedResource.set(concreteResource);
                        }
                    }));

                    initResourcesPanel.add(imagePanel, gbc);
                }
            }
            backgroundPanel.add(depotsPanel);
            warehousePanel.add(initResourcesPanel, c);
            c.gridx++;
            warehousePanel.add(backgroundPanel, c);
        } else if (commandBuffer != null && !commandBuffer.isCompleted()) {
            switch (commandBuffer.getCommandType()) {
                case MARKET: {
                    Market marketCommand = (Market) commandBuffer;
                    if (marketCommand.getIndex() != -1) {
                        JPanel obtainedPanel = new JPanel();
                        obtainedPanel.setLayout(new GridBagLayout());

                        ConcreteResourceSet toDiscard = marketCommand.getToDiscard();
                        JPanel concretePanel = new JPanel();
                        concretePanel.setLayout(new GridBagLayout());

                        // TODO : handle obtained faith points

                        int count = 0;
                        if (toDiscard == null) {
                            ChoiceResourceSet obtained = marketCommand.getObtainedResources();

                            for (ConcreteResource concreteResource : ConcreteResource.values()) {
                                ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                                for (int i = 0; i < obtained.getConcreteResources().getCount(concreteResource); ++i) {
                                    JPanel container = new JPanel();
                                    JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                                    container.add(imagePanel);
                                    c.gridx = count / 3;
                                    c.gridy = count % 3;
                                    concretePanel.add(container, c);
                                    count++;
                                }
                            }
                            count = 0;
                            JPanel choicePanel = new JPanel();
                            choicePanel.setLayout(new GridBagLayout());

                            ArrayList<ChoiceResource> choiceResources = obtained.getChoiceResources();
                            for (int i = 0; i < choiceResources.size(); i++) {
                                ChoiceResource choiceResource = choiceResources.get(i);
                                ResourceImageType resourceImageType = choiceResource.getResourceImageType();

                                JPanel container = new JPanel();
                                JPanel imagePanel = new ResourceImage(resourceImageType, 25);

                                int finalI = i;
                                if(player.equals(client.getModel().getPlayer(client.getNickname()))) {
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
                                                    JsonObject message = client.buildCommandMessage("conversion", JsonUtil.getInstance().serialize(resourcesArray));
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
                                }

                                container.add(imagePanel);
                                c.gridx = count / 3;
                                c.gridy = count % 3;
                                choicePanel.add(container, c);
                                count++;
                            }
                            c.gridx = 0;
                            c.gridy = 0;
                            obtainedPanel.add(concretePanel, c);
                            c.gridy = 1;
                            obtainedPanel.add(choicePanel, c);
                        } else {
                            for (ConcreteResource concreteResource : ConcreteResource.values()) {
                                ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                                for (int i = 0; i < toDiscard.getCount(concreteResource); ++i) {
                                    JPanel container = new JPanel(new GridBagLayout());
                                    JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                                    if (imagePanel.equals(selectedPanel.get())) {
                                        Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                        imagePanel.setBorder(redBorder);
                                    }

                                    if(player.equals(client.getModel().getPlayer(client.getNickname()))) {
                                        imagePanel.addMouseListener(new ButtonClickEvent((e) -> {
                                            if (imagePanel.equals(selectedPanel.get())) {
                                                imagePanel.setBorder(null);
                                                selectedPanel.set(null);
                                                selectedResource.set(null);
                                            } else {
                                                if (selectedPanel.get() != null) {
                                                    selectedPanel.get().setBorder(null);
                                                }
                                                Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                                imagePanel.setBorder(redBorder);

                                                selectedPanel.set(imagePanel);
                                                selectedResource.set(concreteResource);
                                            }
                                        }));
                                    }

                                    container.add(imagePanel);
                                    c.gridx = count / 3;
                                    c.gridy = count % 3;
                                    concretePanel.add(container, c);
                                    count++;

                                }
                            }

                            JPanel buttonPanel = new JPanel(new GridBagLayout());

                            if(player.equals(client.getModel().getPlayer(client.getNickname()))) {
                                JButton button = GUIUtil.addButton("Confirm", buttonPanel, new ButtonClickEvent((e) -> {
                                    JsonObject message = client.buildCommandMessage("confirmWarehouse", JsonNull.INSTANCE);
                                    gui.bufferWrite(message.toString());
                                }));
                                button.setPreferredSize(new Dimension(80, 20));
                                button.setFont(new Font("Arial", Font.PLAIN, 10));
                            }

                            c.gridx = 0;
                            c.gridy = 0;
                            obtainedPanel.add(concretePanel, c);
                            c.gridy = 1;
                            obtainedPanel.add(buttonPanel, c);
                        }

                        JPanel switchPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        if(player.equals(client.getModel().getPlayer(client.getNickname()))) {
                            for (int i = 0; i < LocalConfig.getInstance().getNumberOfDepots(); i++) {
                                int finalI = i;

                                JButton button = new JButton("switch");
                                button.setPreferredSize(new Dimension(80, 20));
                                button.setFont(new Font("Arial", Font.PLAIN, 10));

                                button.addActionListener(new ButtonClickEvent((e) -> {
                                    if (selectedButton.get() == null) {
                                        Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                        button.setBorder(redBorder);

                                        selectedButton.set(button);
                                        selectedIndex.set(finalI);
                                    } else {
                                        int indexA = selectedIndex.get(), indexB = finalI;

                                        selectedButton.get().setBorder(null);
                                        selectedButton.set(null);
                                        selectedIndex.set(null);

                                        JsonObject value = new JsonObject();
                                        value.addProperty("depotIndexA", indexA);
                                        value.addProperty("depotIndexB", indexB);
                                        JsonObject message = client.buildCommandMessage("swapFromDepots", value);
                                        gui.bufferWrite(message.toString());
                                    }
                                }));

                                gbc.gridx = 0;
                                gbc.gridy = i;
                                gbc.insets = new Insets(4, 0, 4, 0);
                                switchPanel.add(button, gbc);
                            }
                        }

                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);

                        warehousePanel.add(obtainedPanel, c);
                        ++c.gridx;
                        warehousePanel.add(backgroundPanel, c);
                        ++c.gridx;
                        warehousePanel.add(switchPanel, c);
                    } else {
                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);

                        warehousePanel.add(backgroundPanel);
                    }
                    break;
                }
                case PURCHASE: {
                    Purchase purchaseCommand = (Purchase) commandBuffer;
                    if (purchaseCommand.getDeckIndex() != -1) {
                        int row = purchaseCommand.getMarketRow(), col = purchaseCommand.getMarketCol();
                        Integer devCardId = client.getModel().getGameZone().getCardMarket().getDevCard(row, col);

                        JPanel toSpendPanel = new JPanel(new GridBagLayout());
                        if (devCardId != null && !player.getBoard().getDevCardsArea().getDecks().get(purchaseCommand.getDeckIndex()).contains(devCardId)) {
                            ConcreteResourceSet toSpend = DevCardsParser.getInstance().getCard(devCardId).getReqResources();

                            for (int cardId : player.getBoard().getPlayedLeaderCards().getLeaderCards()) {
                                LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(cardId);
                                if (leaderCard.isType(LeaderCardType.DISCOUNT)) {
                                    DiscountLeaderCard discountLeaderCard = (DiscountLeaderCard) leaderCard;
                                    toSpend = discountLeaderCard.getDiscountEffect().applyDiscount(toSpend);
                                }
                            }

                            int count = 0;
                            for (ConcreteResource concreteResource : ConcreteResource.values()) {
                                ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                                for (int i = 0; i < toSpend.getCount(concreteResource); ++i) {
                                    JPanel container = new JPanel();
                                    JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                                    container.add(imagePanel);
                                    c.gridx = count / 3;
                                    c.gridy = count % 3;
                                    toSpendPanel.add(container, c);
                                    count++;
                                }
                            }
                        }

                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);
                        warehousePanel.add(backgroundPanel, c);
                        c.gridx++;
                        warehousePanel.add(toSpendPanel, c);
                    } else {
                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);
                        warehousePanel.add(backgroundPanel);
                    }
                    break;
                }
                case PRODUCTION: {
                    Production productionCommand = (Production) commandBuffer;

                    if (productionCommand.getProductionsToActivate() != null && productionCommand.getObtainedResources() == null) {
                        HashMap<Integer, ProductionDetails> map = player.getBoard().activeProductionDetails();

                        SpendableResourceSet toSpend = new SpendableResourceSet();
                        for (int productionIndex : productionCommand.getProductionsToActivate()) {
                            toSpend = toSpend.union(map.get(productionIndex).getInput());
                        }

                        ConcreteResourceSet concreteResources = toSpend.getResourceSet().getConcreteResources();
                        ArrayList<ChoiceResource> choiceResources = toSpend.getResourceSet().getChoiceResources();
                        JPanel toSpendPanel = new JPanel(new GridBagLayout());
                        int count = 0;
                        for (ConcreteResource concreteResource : ConcreteResource.values()) {
                            ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                            for (int i = 0; i < concreteResources.getCount(concreteResource); ++i) {
                                JPanel container = new JPanel();
                                JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                                container.add(imagePanel);
                                c.gridx = count / 3;
                                c.gridy = count % 3;
                                toSpendPanel.add(container, c);
                                count++;
                            }
                        }
                        for (ChoiceResource choiceResource : choiceResources) {
                            ResourceImageType resourceImageType = choiceResource.getResourceImageType();
                            JPanel container = new JPanel();
                            JPanel imagePanel = new ResourceImage(resourceImageType, 25);
                            container.add(imagePanel);
                            c.gridx = count / 3;
                            c.gridy = count % 3;
                            toSpendPanel.add(container, c);
                            count++;
                        }

                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);
                        warehousePanel.add(backgroundPanel, c);
                        c.gridx++;
                        warehousePanel.add(toSpendPanel, c);
                    } else {
                        c.gridx = 0;
                        c.gridy = 0;
                        backgroundPanel.add(depotsPanel, c);
                        warehousePanel.add(backgroundPanel);
                    }
                    break;
                }
                default: {
                    c.gridx = 0;
                    c.gridy = 0;
                    backgroundPanel.add(depotsPanel);
                    warehousePanel.add(backgroundPanel, c);
                }
            }
        } else {
            backgroundPanel.add(depotsPanel);
            warehousePanel.add(backgroundPanel, c);
        }
    }

    private JPanel addResourcePanel(ConcreteResource concreteResource, CommandBuffer commandBuffer, int depotIndex) {
        JPanel resourcePanel = new ResourceImage(concreteResource.getResourceImageType(), 25);

        if (commandBuffer != null && !commandBuffer.isCompleted() && player.getNickname().equals(client.getNickname())) {
            switch (commandBuffer.getCommandType()) {
                case MARKET: {
                    Market marketCommand = (Market) commandBuffer;
                    if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                        resourcePanel.addMouseListener(new ButtonClickEvent((e) -> {
                            ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                            concreteResourceSet.addResource(concreteResource);

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
                        resourcePanel.addMouseListener(new ButtonClickEvent((e) -> {
                            ConcreteResourceSet[] depots = new ConcreteResourceSet[warehouse.getDepots().size()];
                            for (int k = 0; k < depots.length; ++k) {
                                depots[k] = new ConcreteResourceSet();
                            }
                            ConcreteResourceSet strongBox = new ConcreteResourceSet();

                            depots[depotIndex].addResource(concreteResource);

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
                            ConcreteResourceSet[] depots = new ConcreteResourceSet[warehouse.getDepots().size()];
                            for (int k = 0; k < depots.length; ++k) {
                                depots[k] = new ConcreteResourceSet();
                            }
                            ConcreteResourceSet strongBox = new ConcreteResourceSet();

                            depots[depotIndex].addResource(concreteResource);

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

        return resourcePanel;
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        System.out.println("WAREHOUSE UPDATE");
        SwingUtilities.invokeLater(() -> {
            synchronized (client.getModel()) {
                backgroundPanel.removeAll();
                warehousePanel.removeAll();
                loadWarehouse();

                warehousePanel.revalidate();
                backgroundPanel.revalidate();
                warehousePanel.repaint();
                backgroundPanel.repaint();
            }
        });
    }

    @Override
    public void clean() {
        player.getCommandElement().removeObserver(this, CommandType.MARKET);
        player.getCommandElement().removeObserver(this, CommandType.PURCHASE);
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
        warehouse.removeObserver(this);
    }
}
