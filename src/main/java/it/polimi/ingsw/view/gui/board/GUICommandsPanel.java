package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Market;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.windows.tokens.CardMarketToken;
import it.polimi.ingsw.view.gui.windows.tokens.MarbleMarketToken;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import java.awt.*;

public class GUICommandsPanel implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final JPanel commandsPanel;
    private final Player self;

    public GUICommandsPanel(GUI gui, Client client, JPanel commandsPanel) {
        this.gui = gui;
        this.client = client;
        this.commandsPanel = commandsPanel;

        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.X_AXIS));

        self = client.getModel().getPlayer(client.getNickname());
        for(Player player: client.getModel().getPlayers()) {
            player.addObserver(this);
        }

        self.getCommandElement().addObserver(this, CommandType.MARKET);
        self.getCommandElement().addObserver(this, CommandType.PURCHASE);
    }

    public void loadCommandsPanel() {
        boolean canCommand = self.canMainAction(client.getModel());

        JButton marketCommand = GUIUtil.addButton("Collect resources", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.MARKET);
            gui.bufferWrite(message.toString());
        }));
        marketCommand.setEnabled(canCommand);

        JButton purchaseCommand = GUIUtil.addButton("Purchase card", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.PURCHASE);
            gui.bufferWrite(message.toString());
        }));
        purchaseCommand.setEnabled(canCommand);

        JButton productionCommand = GUIUtil.addButton("Activate productions", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.PRODUCTION);
            gui.bufferWrite(message.toString());
        }));
        productionCommand.setEnabled(canCommand);

        JButton endTurnCommand = GUIUtil.addButton("End turn", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildEndTurnMessage();
            gui.bufferWrite(message.toString());
        }));
        endTurnCommand.setEnabled(self.canEndTurn(client.getModel()));
        endTurnCommand.setForeground(Color.RED);
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        if(notificationSource == NotificationSource.COMMANDELEMENT) {
            CommandBuffer commandBuffer = self.getCommandBuffer();

            if (commandBuffer != null) {
                System.out.println(commandBuffer.getCommandType().name());
                switch (commandBuffer.getCommandType()) {
                    case MARKET: {
                        Market marketCommand = (Market) commandBuffer;
                        if (marketCommand.getIndex() == -1) {
                            gui.switchGameWindow(new MarbleMarketToken());
                        }
                        break;
                    }
                    case PURCHASE: {
                        Purchase purchaseCommand = (Purchase) commandBuffer;
                        if (purchaseCommand.getMarketRow() == -1 || purchaseCommand.getMarketCol() == -1) {
                            gui.switchGameWindow(new CardMarketToken());
                        }
                        break;
                    }
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            commandsPanel.removeAll();
            loadCommandsPanel();
            commandsPanel.revalidate();
            commandsPanel.repaint();
        });
    }

    @Override
    public void clean() {
        for(Player player: client.getModel().getPlayers()) {
            player.removeObserver(this);
        }
        self.getCommandElement().removeObserver(this, CommandType.MARKET);
        self.getCommandElement().removeObserver(this, CommandType.PURCHASE);
    }
}
