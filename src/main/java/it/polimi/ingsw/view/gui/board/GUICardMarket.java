package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.devCardsArea.devCard.DevCardImage;
import it.polimi.ingsw.view.gui.windows.tokens.BoardToken;
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
    private final ArrayList<Player> players;

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

                Integer devCardId = cardMarket.getDevCard(2 - i, j);
                // TODO : handle null devCardId
                DevCard devCard = DevCardsParser.getInstance().getCard(devCardId);
                DevCardImage devCardImage = devCard.getDevCardImage(150);
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
                            int finalI = i;
                            int finalJ = j;

                            ConcreteResourceSet requirements = devCard.getReqResources();
                            ConcreteResourceSet boardResources = player.getBoard().getResources();

                            GUIUtil.addButton("Purchase", container, new ButtonClickEvent((e) -> {
                                JsonObject value = new JsonObject();
                                value.addProperty("row", 2 - finalI);
                                value.addProperty("column", finalJ);
                                JsonObject message = client.buildCommandMessage("cardSelection", value);
                                gui.bufferWrite(message.toString());
                            })).setEnabled(boardResources.contains(requirements));
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
        Player self = client.getModel().getPlayer(client.getNickname());
        CommandBuffer commandBuffer = self.getCommandBuffer();

        if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.PURCHASE) {
            Purchase purchaseCommand = (Purchase) commandBuffer;
            if (purchaseCommand.getMarketRow() != -1 && purchaseCommand.getMarketCol() != -1) {
                gui.switchGameWindow(new BoardToken(client.getNickname()));
                return;
            }
        }

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
        for (Player player : players) {
            player.getCommandElement().removeObserver(this, CommandType.PURCHASE);
        }
    }
}
