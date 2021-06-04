package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.devCards.CardType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardTypeRequirementsPanel {
    private final JPanel panel;

    public CardTypeRequirementsPanel(HashMap<CardType, Integer> cardTypes, int leaderCardWidth, int leaderCardHeight) {
        ArrayList<JPanel> cardTypePanels = new ArrayList<>();

        for(Map.Entry<CardType, Integer> entry: cardTypes.entrySet()) {
            CardType cardType = entry.getKey();
            int quantity = entry.getValue();

            JPanel requirementsPanel = new JPanel();
            requirementsPanel.setLayout(new BoxLayout(requirementsPanel, BoxLayout.X_AXIS));

            JPanel quantityPanel = new JPanel();
            quantityPanel.setLayout(new GridBagLayout());

            JLabel quantityLabel = new JLabel("" + quantity, SwingConstants.CENTER);
            quantityLabel.setFont(quantityLabel.getFont().deriveFont((float) leaderCardHeight / 16));
            quantityLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
            quantityPanel.add(quantityLabel);

            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new GridBagLayout());

            JPanel cardTypePanel = new CardTypeImage(cardType, leaderCardWidth / 11);
            containerPanel.add(cardTypePanel);

            requirementsPanel.add(quantityPanel);
            requirementsPanel.add(containerPanel);

            cardTypePanels.add(requirementsPanel);
        }

        panel = new JPanel();
        panel.setBounds(leaderCardWidth / 20, leaderCardHeight / 30, leaderCardWidth * 9 / 10, leaderCardHeight / 7);
        panel.setLayout(new GridLayout(1, cardTypePanels.size()));

        for(JPanel cardTypePanel: cardTypePanels) {
            panel.add(cardTypePanel);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
