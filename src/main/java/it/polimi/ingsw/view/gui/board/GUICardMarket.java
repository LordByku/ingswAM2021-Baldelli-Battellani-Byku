package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.images.devCardsArea.devCard.DevCardImage;
import it.polimi.ingsw.view.localModel.CardMarket;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class GUICardMarket implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final JPanel marketPanel;
    private final CardMarket cardMarket;
    private ArrayList<Player> players;

    public GUICardMarket(GUI gui, Client client, JPanel marketPanel) {
        this.gui = gui;
        this.client = client;
        this.marketPanel = marketPanel;

        cardMarket = client.getModel().getGameZone().getCardMarket();
        cardMarket.addObserver(this);

        players = client.getModel().getPlayers();
        for (Player player : players) {
            player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        }
    }

    public void loadCardMarket() {
        marketPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 0; i < 3; ++i) {
            gbc.gridy = i;
            for (int j = 0; j < 4; ++j) {
                gbc.gridx = j;

                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

                DevCardImage devCardImage = DevCardsParser.getInstance().getCard(cardMarket.getDevCard(2 - i, j)).getDevCardImage(150);
                container.add(devCardImage);

                for (Player player : players) {
                    CommandBuffer commandBuffer = player.getCommandBuffer();
                    if (commandBuffer == null) {
                        continue;
                    }
                    if (commandBuffer.getCommandType() == CommandType.PURCHASE) {
                        Purchase purchaseCommand = (Purchase) commandBuffer;
                        if (purchaseCommand.getMarketRow() != -1 && purchaseCommand.getMarketCol() != -1) {
                            if (purchaseCommand.getMarketRow() == 2 - i && purchaseCommand.getMarketCol() == j) {
                                Border redBorder = BorderFactory.createLineBorder(Color.RED);
                                devCardImage.setBorder(redBorder);
                            }
                        } else if (player.getNickname().equals(client.getNickname())) {
                            GUIUtil.addButton("Purchase", container, null);
                        }
                    }
                }

                gbc.insets = new Insets(5, 5, 5, 5);
                marketPanel.add(container, gbc);
            }
        }
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            marketPanel.removeAll();
            loadCardMarket();
            marketPanel.revalidate();
            marketPanel.repaint();
        });
    }

    @Override
    public void clean() {
        cardMarket.removeObserver(this);
    }
}
