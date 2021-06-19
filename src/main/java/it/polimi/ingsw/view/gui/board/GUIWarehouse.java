package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;
import it.polimi.ingsw.view.localModel.Warehouse;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class GUIWarehouse implements LocalModelElementObserver {
    private final GUI gui;
    private final Player player;
    private final Warehouse warehouse;
    private Client client;
    private JPanel warehousePanel;
    private int numOfDepots;
    private ArrayList<Integer> depotSizes;

    public GUIWarehouse(GUI gui, Client client, JPanel warehousePanel, String nickname) {
        this.gui = gui;
        this.client = client;
        this.warehousePanel = warehousePanel;
        numOfDepots = LocalConfig.getInstance().getNumberOfDepots();
        depotSizes = LocalConfig.getInstance().getDepotSizes();
        warehousePanel.setLayout(new BoxLayout(warehousePanel, BoxLayout.X_AXIS));

        player = client.getModel().getPlayer(nickname);
        warehouse = player.getBoard().getWarehouse();

        player.getCommandElement().addObserver(this, CommandType.MARKET);
        player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);
        warehouse.addObserver(this);

        // TODO : check leader card depots (?)
    }

    public void loadWarehouse() {
        CommandBuffer commandBuffer = player.getCommandBuffer();
        AtomicReference<JPanel> selectedPanel = new AtomicReference<>();
        AtomicReference<ConcreteResource> selectedResource = new AtomicReference<>();

        AtomicReference<Integer> initResourcesCount = new AtomicReference<>(0);
        ConcreteResourceSet[] initDepots = new ConcreteResourceSet[numOfDepots];
        for(int k = 0; k < numOfDepots; ++k) {
            initDepots[k] = new ConcreteResourceSet();
        }

        JPanel depotsPanel = new JPanel(new GridBagLayout());
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
                    JPanel resourcePanel = new ResourceImage(concreteResource.getResourceImageType(), 30);
                    depotPanel.add(resourcePanel, c);

                    if (commandBuffer != null && !commandBuffer.isCompleted() && player.getNickname().equals(client.getNickname())) {
                        switch (commandBuffer.getCommandType()) {
                            case MARKET: {
                                Market marketCommand = (Market) commandBuffer;
                                if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                                    resourcePanel.addMouseListener(new ButtonClickEvent((e) -> {
                                        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();
                                        concreteResourceSet.addResource(concreteResource);

                                        JsonObject value = new JsonObject();
                                        value.addProperty("depotIndex", finalI);
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

                                        depots[finalI].addResource(concreteResource);

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

                                        depots[finalI].addResource(concreteResource);

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
                } else {
                    // TODO : load via Class Loader
                    Image empty = Toolkit.getDefaultToolkit().getImage("src/main/resources/Depots/emptyDepotSlot.png");

                    JLabel emptyResourceLabel = new JLabel();
                    emptyResourceLabel.setIcon(new ImageIcon(empty.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                    depotPanel.add(emptyResourceLabel, c);

                    if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.MARKET) {
                        Market marketCommand = (Market) commandBuffer;
                        if (marketCommand.getIndex() != -1 && !marketCommand.isCompleted()) {
                            emptyResourceLabel.addMouseListener(new ButtonClickEvent((e) -> {
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
                        if(!player.initResources() && player.getNickname().equals(client.getNickname())) {
                            emptyResourceLabel.addMouseListener(new ButtonClickEvent((e) -> {
                                if(selectedResource.get() != null) {
                                    initDepots[finalI].addResource(selectedResource.get());
                                    selectedResource.set(null);
                                    selectedPanel.set(null);

                                    initResourcesCount.set(initResourcesCount.get() + 1);
                                    if(initResourcesCount.get() == LocalConfig.getInstance().getInitialResources(player.getNickname())) {
                                        JsonObject request = client.buildRequestMessage(CommandType.INITRESOURCES);
                                        gui.bufferWrite(request.toString());
                                        JsonObject message = client.buildCommandMessage("resources", JsonUtil.getInstance().serialize(initDepots));
                                        gui.bufferWrite(message.toString());
                                    }
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

        if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.MARKET) {
            Market marketCommand = (Market) commandBuffer;
            if (marketCommand.getIndex() != -1) {
                JPanel obtainedPanel = new JPanel();
                obtainedPanel.setLayout(new BoxLayout(obtainedPanel, BoxLayout.Y_AXIS));

                ConcreteResourceSet toDiscard = marketCommand.getToDiscard();
                JPanel concretePanel = new JPanel();
                concretePanel.setLayout(new BoxLayout(concretePanel, BoxLayout.X_AXIS));

                // TODO : handle obtained faith points

                if (toDiscard == null) {
                    ChoiceResourceSet obtained = marketCommand.getObtainedResources();

                    for (ConcreteResource concreteResource : ConcreteResource.values()) {
                        ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                        for (int i = 0; i < obtained.getConcreteResources().getCount(concreteResource); ++i) {
                            JPanel container = new JPanel();
                            JPanel imagePanel = new ResourceImage(resourceImageType, 20);
                            container.add(imagePanel);
                            concretePanel.add(container);
                        }
                    }

                    JPanel choicePanel = new JPanel();
                    choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));

                    ArrayList<ChoiceResource> choiceResources = obtained.getChoiceResources();
                    for (int i = 0; i < choiceResources.size(); i++) {
                        ChoiceResource choiceResource = choiceResources.get(i);
                        ResourceImageType resourceImageType = choiceResource.getResourceImageType();

                        JPanel container = new JPanel();
                        JPanel imagePanel = new ResourceImage(resourceImageType, 30);

                        int finalI = i;
                        imagePanel.addMouseListener(new ButtonClickEvent((e) -> {
                            JPanel popupContent = new JPanel();
                            popupContent.setLayout(new BoxLayout(popupContent, BoxLayout.X_AXIS));

                            Popup popup = PopupFactory.getSharedInstance().getPopup(imagePanel, popupContent, imagePanel.getX(), imagePanel.getY());

                            for (ConcreteResource concreteResource : ConcreteResource.values()) {
                                if (choiceResource.canChoose(concreteResource)) {
                                    ResourceImageType concreteImageType = concreteResource.getResourceImageType();
                                    JPanel concreteResourcePanel = new ResourceImage(concreteImageType, 30);

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

                        container.add(imagePanel);
                        choicePanel.add(container);
                    }

                    obtainedPanel.add(concretePanel);
                    obtainedPanel.add(choicePanel);
                } else {
                    for (ConcreteResource concreteResource : ConcreteResource.values()) {
                        ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                        for (int i = 0; i < toDiscard.getCount(concreteResource); ++i) {
                            JPanel container = new JPanel(new GridBagLayout());
                            JPanel imagePanel = new ResourceImage(resourceImageType, 30);
                            if (imagePanel.equals(selectedPanel.get())) {
                                Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                imagePanel.setBorder(redBorder);
                            }

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

                            container.add(imagePanel);
                            concretePanel.add(container);

                        }
                    }

                    JPanel buttonPanel = new JPanel(new GridBagLayout());
                    GUIUtil.addButton("Confirm", buttonPanel, new ButtonClickEvent((e) -> {
                        JsonObject message = client.buildCommandMessage("confirmWarehouse", JsonNull.INSTANCE);
                        gui.bufferWrite(message.toString());
                    }));

                    obtainedPanel.add(concretePanel);
                    obtainedPanel.add(buttonPanel);
                }

                warehousePanel.add(obtainedPanel);
                warehousePanel.add(depotsPanel);

                AtomicReference<JButton> selectedButton = new AtomicReference<>();
                AtomicReference<Integer> selectedIndex = new AtomicReference<>();

                JPanel switchPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                for(int i =0; i<3; i++) {
                    int finalI = i;

                    JButton button = new JButton("switch");

                    button.addMouseListener(new ButtonClickEvent((e) -> {
                        if(selectedButton.get() == null) {
                            // TODO : fix border
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
                    gbc.insets = new Insets(2, 0, 2, 0);
                    switchPanel.add(button, gbc);
                }
                warehousePanel.add(switchPanel);
            } else {
                warehousePanel.add(depotsPanel);
            }
        } else {
            if(!player.initResources() && player.getNickname().equals(client.getNickname())) {
                JPanel initResourcesPanel = new JPanel(new GridBagLayout());

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                for(int i = 0; i < 2; ++i) {
                    for(int j = 0; j < 2; ++j) {
                        gbc.gridx = i;
                        gbc.gridy = j;

                        ConcreteResource concreteResource = ConcreteResource.values()[2 * i + j];
                        ResourceImageType resourceImageType = concreteResource.getResourceImageType();
                        ResourceImage resourceImage = new ResourceImage(resourceImageType, 30);

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
                warehousePanel.add(initResourcesPanel);
            }

            warehousePanel.add(depotsPanel);
        }
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            warehousePanel.removeAll();
            loadWarehouse();
            warehousePanel.revalidate();
            warehousePanel.repaint();
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
