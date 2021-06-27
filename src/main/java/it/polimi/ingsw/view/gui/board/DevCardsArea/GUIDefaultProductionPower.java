package it.polimi.ingsw.view.gui.board.DevCardsArea;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.model.devCards.ProductionDetails;
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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIDefaultProductionPower implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final Player player;
    private final JPanel devCardsArea;
    private final ProductionDetails defaultProductionPower;
    private final JPanel defProdPower;
    private final String nickname;

    public GUIDefaultProductionPower(GUI gui, Client client, JPanel devCardsArea, String nickname) {
        this.gui = gui;
        this.client = client;
        this.devCardsArea = devCardsArea;
        this.nickname = nickname;
        defaultProductionPower = LocalConfig.getInstance().getDefaultProductionPower();

        player = client.getModel().getPlayer(nickname);
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

        // tests
        /*ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            for (int i = 0; i < 10; ++i) {
                choiceResourceSet.addResource(concreteResource);
            }
        }
        for (int i = 0; i < 10; ++i) {
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        }*/

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
        if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
            Production productionCommand = (Production) commandBuffer;
            JPanel container = new JPanel();

            int[] currentSelection = productionCommand.getProductionsToActivate();
            int n = currentSelection != null ? currentSelection.length : 0;

            boolean selected = false;
            if (currentSelection != null) {
                for (int selection : currentSelection) {
                    if (0 == selection) {
                        selected = true;
                    }
                }
            }
            boolean finalSelected = selected;
            JButton button = GUIUtil.addButton("select", container, new ButtonClickEvent((e) -> {
                if (finalSelected) {
                    int[] selection = new int[n - 1];
                    for (int j = 0, k = 0; j < n; ++j) {
                        if (currentSelection[j] != 0) {
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
                    selection[n] = 0;
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

            container.setOpaque(false);
            gbc.gridy++;
            defProdPower.add(container, gbc);
        }
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            synchronized (client.getModel()) {
                defProdPower.removeAll();
                loadDefaultProductionPower();
                defProdPower.revalidate();
                defProdPower.repaint();
            }
        });
    }

    @Override
    public void clean() {
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
    }
}
