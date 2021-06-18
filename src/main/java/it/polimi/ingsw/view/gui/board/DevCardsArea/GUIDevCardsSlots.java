package it.polimi.ingsw.view.gui.board.DevCardsArea;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.devCardsArea.DevCardSlotImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIDevCardsSlots implements LocalModelElementObserver {
    private final GUI gui;
    private final JPanel slotsPanel;
    private final Player player;
    private JPanel devCardsArea;
    private int numOfSlots;
    private Client client;
    private String nickname;

    public GUIDevCardsSlots(GUI gui, Client client, JPanel devCardsArea, String nickname) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
        this.nickname = nickname;
        numOfSlots = LocalConfig.getInstance().getDevelopmentCardsSlots();

        player = client.getModel().getPlayer(nickname);

        player.getCommandElement().addObserver(this, CommandType.PURCHASE);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);
        player.getBoard().getDevCardsArea().addObserver(this);

        slotsPanel = new JPanel(new GridBagLayout());
        devCardsArea.add(slotsPanel);
    }

    public void loadDevCardsSlots() {
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < numOfSlots; i++) {
            int finalI = i;

            JPanel slotPanel = new DevCardSlotImage();
            slotPanel.setBorder(new LineBorder(Color.BLACK));

            // TODO : change player to load
            Player player = client.getModel().getPlayer(nickname);

            if (player.getNickname().equals(client.getNickname())) {
                CommandBuffer commandBuffer = player.getCommandBuffer();
                if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.PURCHASE) {
                    Purchase purchaseCommand = (Purchase) commandBuffer;
                    if (purchaseCommand.getDeckIndex() == -1) {
                        slotPanel.addMouseListener(new ButtonClickEvent((e) -> {
                            JsonObject message = client.buildCommandMessage("deckSelection", new JsonPrimitive(finalI));
                            gui.bufferWrite(message.toString());
                        }));
                    }
                }
            }
            c.gridx = c.gridy = 0;

            ArrayList<Integer> deck = new ArrayList<>(player.getBoard().getDevCardsArea().getDecks().get(i)); //cards ID
            CommandBuffer commandBuffer = player.getCommandBuffer();
            if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.PURCHASE) {
                Purchase purchaseCommand = (Purchase) commandBuffer;
                if (i == purchaseCommand.getDeckIndex()) {
                    int row = purchaseCommand.getMarketRow(), col = purchaseCommand.getMarketCol();
                    int devCard = client.getModel().getGameZone().getCardMarket().getDevCard(row, col);
                    deck.add(devCard);
                }
            }

            // for testing:
            /*for(int j = 0; j <= i; ++j) {
                deck.add(j);
            }*/

            for (int j = deck.size() - 1; j >= 0; --j) {
                // TODO : adjust size ?
                DevCard devCard = DevCardsParser.getInstance().getCard(deck.get(j));
                JPanel card = devCard.getDevCardImage(128);
                card.setBorder(new LineBorder(Color.BLACK));
                card.setOpaque(false);

                c.insets = new Insets(32 * (deck.size() - 1 - j), 0, 32 * j, 0);
                slotPanel.add(card, c);
            }

            if (commandBuffer != null && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
                if (!deck.isEmpty()) {
                    Production productionCommand = (Production) commandBuffer;
                    int[] currentSelection = productionCommand.getProductionsToActivate();
                    int n = currentSelection != null ? currentSelection.length : 0;
                    JPanel container = new JPanel();
                    GUIUtil.addButton("select", container, new ButtonClickEvent((e) -> {
                        int[] selection = new int[n + 1];
                        for (int j = 0; j < n; ++j) {
                            selection[j] = currentSelection[j];
                        }
                        selection[n] = finalI + 1;
                        JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                        gui.bufferWrite(message.toString());
                    }));
                    container.setOpaque(false);
                    c.gridy++;
                    c.insets = new Insets(0, 0, 0, 0);
                    slotPanel.add(container, c);
                }
            }

            c.gridx = i + 1;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            c.weightx = 0.5;
            slotsPanel.add(slotPanel, c);
        }
    }

    @Override
    public void notifyObserver() {
        SwingUtilities.invokeLater(() -> {
            slotsPanel.removeAll();
            loadDevCardsSlots();
            slotsPanel.revalidate();
            slotsPanel.repaint();
        });
    }

    @Override
    public void clean() {
        player.getCommandElement().removeObserver(this, CommandType.PURCHASE);
        player.getCommandElement().removeObserver(this, CommandType.PRODUCTION);
        player.getBoard().getDevCardsArea().removeObserver(this);
    }
}
