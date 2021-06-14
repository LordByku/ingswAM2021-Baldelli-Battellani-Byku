package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.DevCardsParser;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.images.devCard.DevCardImage;
import it.polimi.ingsw.view.localModel.CardMarket;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;
import java.awt.*;

public class GUICardMarket implements LocalModelElementObserver {
    private final GUI gui;
    private final Client client;
    private final JPanel marketPanel;

    public GUICardMarket(GUI gui, Client client, JPanel marketPanel) {
        this.gui = gui;
        this.client = client;
        this.marketPanel = marketPanel;
    }

    public void loadCardMarket() {
        CardMarket cardMarket = client.getModel().getGameZone().getCardMarket();

        marketPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 0; i < 3; ++i) {
            gbc.gridy = i;
            for (int j = 0; j < 4; ++j) {
                gbc.gridx = j;

                JPanel container = new JPanel(new GridBagLayout());

                DevCardImage devCardImage = DevCardsParser.getInstance().getCard(cardMarket.getDevCard(2 - i, j)).getDevCardImage(150);

                container.add(devCardImage);

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
}
