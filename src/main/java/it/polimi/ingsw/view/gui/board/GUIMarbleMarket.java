package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Market;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarbleImage;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarketTrayImage;
import it.polimi.ingsw.view.gui.windows.tokens.BoardToken;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.MarbleMarket;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIMarbleMarket implements LocalModelElementObserver {
    private final GUI gui;
    private final MarbleMarket marbleMarket;
    private final ArrayList<Player> players;
    private JPanel marketPanel;
    private JPanel marblePanel;
    private JPanel marketTrayPanel;
    private Client client;

    public GUIMarbleMarket(GUI gui, Client client, JPanel marketPanel) {
        this.gui = gui;
        this.client = client;
        this.marketPanel = marketPanel;
        marketTrayPanel = new MarketTrayImage();
        marblePanel = new JPanel(new GridBagLayout());

        marbleMarket = client.getModel().getGameZone().getMarbleMarket();
        marbleMarket.addObserver(this);

        players = client.getModel().getPlayers();
        for (Player player : players) {
            player.getCommandElement().addObserver(this, CommandType.MARKET);
        }
    }

    public void loadMarbleMarket() {
        JPanel marble;
        Image img;
        JButton button;
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                img = marbleMarket.get(i, j).getImage();
                marble = new MarbleImage(img);
                c.gridx = j;
                c.gridy = i;
                c.insets = new Insets(5, 5, 5, 5);
                marblePanel.add(marble, c);
            }
        }


        for (Player player : client.getModel().getPlayers()) {
            CommandBuffer commandBuffer = player.getCommandBuffer();
            if (commandBuffer == null) {
                continue;
            }
            if (commandBuffer.getCommandType() == CommandType.MARKET) {
                Market marketCommand = (Market) commandBuffer;
                if (marketCommand.getIndex() != -1) {
                    // TODO : move free marble
                } else if (player.getNickname().equals(client.getNickname())) {
                    int offset = 125;
                    for (int i = 0; i < 3; i++) {
                        button = new JButton("<-");

                        int finalI = i;
                        button.addMouseListener(new ButtonClickEvent((e) -> {
                            JsonObject value = new JsonObject();
                            value.addProperty("rowColSel", true);
                            value.addProperty("index", finalI);
                            JsonObject message = client.buildCommandMessage("selection", value);
                            gui.bufferWrite(message.toString());
                        }));

                        c.gridx = 0;
                        c.gridy = 0;
                        c.insets = new Insets(0, 500, 325 - (i * offset), 0);
                        marketPanel.add(button, c);
                    }
                    offset = 126;
                    for (int i = 0; i < 2; i++) {
                        button = new JButton("↑");
                        // TODO : fix button size
                        button.setPreferredSize(new Dimension(40, 50));

                        int finalI = i;
                        button.addMouseListener(new ButtonClickEvent((e) -> {
                            JsonObject value = new JsonObject();
                            value.addProperty("rowColSel", false);
                            value.addProperty("index", finalI);
                            JsonObject message = client.buildCommandMessage("selection", value);
                            gui.bufferWrite(message.toString());
                        }));

                        c.gridx = 0;
                        c.gridy = 0;
                        c.insets = new Insets(230, 0, 0, 40 + (i * offset));
                        marketPanel.add(button, c);
                    }
                    for (int i = 0; i < 2; i++) {
                        button = new JButton("↑");
                        // TODO : fix button size
                        button.setPreferredSize(new Dimension(40, 50));

                        int finalI = i+2;
                        button.addMouseListener(new ButtonClickEvent((e) -> {
                            JsonObject value = new JsonObject();
                            value.addProperty("rowColSel", false);
                            value.addProperty("index", finalI);
                            JsonObject message = client.buildCommandMessage("selection", value);
                            gui.bufferWrite(message.toString());
                        }));

                        c.gridx = 0;
                        c.gridy = 0;
                        c.insets = new Insets(230,86+ (i * offset), 0, 0);
                        marketPanel.add(button, c);
                    }
                }
            }
        }

        img = marbleMarket.getFreeMarble().getImage();
        marble = new MarbleImage(img);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 300, 210, 0);
        marketTrayPanel.add(marble, c);
        //marblePanel.setBorder(new LineBorder(Color.RED));
        marblePanel.setPreferredSize(new Dimension(60 * 4, 60 * 3));
        marblePanel.setOpaque(false);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 15, 0, 0);
        marketTrayPanel.add(marblePanel, c);
        marketTrayPanel.setBorder(new LineBorder(Color.BLACK));
        c.insets = new Insets(0, 2, 219, 0);
        marketPanel.add(marketTrayPanel, c);
    }

    @Override
    public void notifyObserver() {
        Player self = client.getModel().getPlayer(client.getNickname());
        CommandBuffer commandBuffer = self.getCommandBuffer();

        if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.MARKET) {
            Market marketCommand = (Market) commandBuffer;
            if (marketCommand.getIndex() != -1) {
                gui.switchGameWindow(new BoardToken(client.getNickname()));
                return;
            }
        }

        SwingUtilities.invokeLater(() -> {
            marketPanel.removeAll();
            loadMarbleMarket();
            marketPanel.revalidate();
            marketPanel.repaint();
        });
    }

    @Override
    public void clean() {
        marbleMarket.removeObserver(this);
        for (Player player : players) {
            player.getCommandElement().removeObserver(this, CommandType.MARKET);
        }
    }
}
