package it.polimi.ingsw.view.gui.board.DevCardsArea;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.devCardsArea.DefaultProductionImage;
import it.polimi.ingsw.view.gui.images.resources.ProductionInPanel;
import it.polimi.ingsw.view.gui.images.resources.ProductionOutPanel;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIDefaultProductionPower implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final Player player;
    private JPanel devCardsArea;
    private ProductionDetails defaultProductionPower;
    private JPanel defProdPower;

    public GUIDefaultProductionPower(GUI gui, Client client, JPanel devCardsArea) {
        this.gui = gui;
        this.client = client;
        this.devCardsArea = devCardsArea;
        defaultProductionPower = LocalConfig.getInstance().getDefaultProductionPower();

        // TODO : load player according to nickname
        player = client.getModel().getPlayer(client.getNickname());
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);

        defProdPower = new DefaultProductionImage();
        devCardsArea.add(defProdPower);
    }

    public void loadDefaultProductionPower() {
        JPanel productionPanel = new JPanel();
        productionPanel.setLayout(new BoxLayout(productionPanel, BoxLayout.X_AXIS));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            for (int i = 0; i < 10; ++i) {
                choiceResourceSet.addResource(concreteResource);
            }
        }
        for (int i = 0; i < 10; ++i) {
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        }

        JPanel inputPanel = new ProductionInPanel(defaultProductionPower.getInput(), null, 18).getPanel();
        productionPanel.add(inputPanel);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("}", SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(1, 1, 1, 1));
        label.setFont(label.getFont().deriveFont((float) 70));
        labelPanel.add(label);

        productionPanel.add(labelPanel);

        JPanel outputPanel = new ProductionOutPanel(defaultProductionPower.getOutput(), null, 18).getPanel();
        productionPanel.add(outputPanel);

        defProdPower.add(productionPanel, gbc);

        CommandBuffer commandBuffer = player.getCommandBuffer();
        if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
            Production productionCommand = (Production) commandBuffer;
            int[] currentSelection = productionCommand.getProductionsToActivate();
            int n = currentSelection != null ? currentSelection.length : 0;
            JPanel container = new JPanel();
            GUIUtil.addButton("select", container, new ButtonClickEvent((e) -> {
                int[] selection = new int[n + 1];
                for (int i = 0; i < n; ++i) {
                    selection[i] = currentSelection[i];
                }
                selection[n] = 0;
                JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                gui.bufferWrite(message.toString());
            }));
            container.setOpaque(false);
            gbc.gridy++;
            defProdPower.add(container, gbc);
        }
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            defProdPower.removeAll();
            loadDefaultProductionPower();
            defProdPower.revalidate();
            defProdPower.repaint();
        });
    }

    @Override
    public void clean() {
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
    }
}
