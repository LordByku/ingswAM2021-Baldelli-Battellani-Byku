package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
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
        self.addObserver(this);
    }

    public void loadCommandsPanel() {
        boolean canCommand = self.canMainAction(client.getModel());

        JButton marketCommand = GUIUtil.addButton("Collect resources", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.MARKET);
            gui.bufferWrite(message.toString());
            gui.switchGameWindow(new MarbleMarketToken());
        }));
        marketCommand.setEnabled(canCommand);

        JButton purchaseCommand = GUIUtil.addButton("Purchase card", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.PURCHASE);
            gui.bufferWrite(message.toString());
            gui.switchGameWindow(new CardMarketToken());
        }));
        purchaseCommand.setEnabled(canCommand);

        JButton productionCommand = GUIUtil.addButton("Activate productions", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildRequestMessage(CommandType.PRODUCTION);
            gui.bufferWrite(message.toString());
        }));
        productionCommand.setEnabled(canCommand);

        JButton endTurnCommand = GUIUtil.addButton("End turn", commandsPanel, new ButtonClickEvent((e) -> {
            JsonObject message = client.buildCancelMessage();
            gui.bufferWrite(message.toString());
        }));
        endTurnCommand.setEnabled(self.canEndTurn(client.getModel()));
        endTurnCommand.setForeground(Color.RED);
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            commandsPanel.removeAll();
            loadCommandsPanel();
            commandsPanel.revalidate();
            commandsPanel.repaint();
        });
    }

    @Override
    public void clean() {
        self.removeObserver(this);
    }
}
