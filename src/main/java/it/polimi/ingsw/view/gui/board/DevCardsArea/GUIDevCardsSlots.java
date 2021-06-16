package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.gui.images.devCardsArea.DevCardSlotImage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIDevCardsSlots {
    JPanel devCardsArea;
    int numOfSlots;
    Client client;

    public GUIDevCardsSlots(Client client, JPanel devCardsArea) {
        this.devCardsArea = devCardsArea;
        this.client = client;
        numOfSlots = LocalConfig.getInstance().getDevelopmentCardsSlots();
    }

    public void loadDevCardsSlots() {
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < numOfSlots; i++) {
            Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/devcardslot.jpg");
            JPanel slotPanel = new DevCardSlotImage(new ImageIcon(img).getImage());
            slotPanel.setBorder(new LineBorder(Color.BLACK));
            ArrayList<Integer> deck = client.getModel().getPlayer(client.getNickname()).getBoard().getDevCardDeck(i); //cards ID
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
