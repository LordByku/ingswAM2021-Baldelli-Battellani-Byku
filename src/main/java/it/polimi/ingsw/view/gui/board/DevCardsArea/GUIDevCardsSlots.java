package it.polimi.ingsw.view.gui.board.DevCardsArea;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.DiscardLeader;
import it.polimi.ingsw.controller.Purchase;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.devCardsArea.DevCardSlotImage;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIDevCardsSlots {
    private final GUI gui;
    private JPanel devCardsArea;
    private int numOfSlots;
    private Client client;

    public GUIDevCardsSlots(GUI gui, Client client, JPanel devCardsArea) {
        this.gui = gui;
        this.devCardsArea = devCardsArea;
        this.client = client;
        numOfSlots = LocalConfig.getInstance().getDevelopmentCardsSlots();
    }

    public void loadDevCardsSlots() {
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < numOfSlots; i++) {
            // TODO : load via Class Loader
            Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/devcardslot.jpg");
            JPanel slotPanel = new DevCardSlotImage(new ImageIcon(img).getImage());
            slotPanel.setBorder(new LineBorder(Color.BLACK));

            // TODO : change player to load
            Player player = client.getModel().getPlayer(client.getNickname());

            if(player.getNickname().equals(client.getNickname())) {
                CommandBuffer commandBuffer = player.getCommandBuffer();
                if(commandBuffer != null && commandBuffer.getCommandType() == CommandType.PURCHASE) {
                    Purchase purchaseCommand = (Purchase) commandBuffer;
                    if(purchaseCommand.getDeckIndex() == -1) {
                        int finalI = i;
                        slotPanel.addMouseListener(new ButtonClickEvent((e) -> {
                            JsonObject message = client.buildCommandMessage("deckSelection", new JsonPrimitive(finalI));
                            gui.bufferWrite(message.toString());
                        }));
                    }
                }
            }

            ArrayList<Integer> deck = player.getBoard().getDevCardDeck(i); //cards ID
            for (int j = deck.size() - 1; j >= 0; --j) {
                DevCard devCard = DevCardsParser.getInstance().getCard(deck.get(i));
                JPanel card = devCard.getDevCardImage(50);
                card.setBorder(new LineBorder(Color.BLACK));
                c.gridx = 0;
                c.gridy = 0;
                c.insets = new Insets(0, 0, j * 10, 0);
                slotPanel.add(card, c);
            }
            c.gridx = i + 1;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            c.weightx = 0.5;
            devCardsArea.add(slotPanel, c);
        }
    }
}
