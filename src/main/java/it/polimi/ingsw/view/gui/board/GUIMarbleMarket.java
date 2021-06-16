package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarbleImage;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarketTrayImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.MarbleMarket;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GUIMarbleMarket implements LocalModelElementObserver {
    private JPanel marketPanel;
    private JPanel marblePanel;
    private JPanel marketTrayPanel;
    private Client client;

    public GUIMarbleMarket(Client client, JPanel marketPanel) {
        this.client = client;
        this.marketPanel = marketPanel;
        marblePanel = new JPanel(new GridBagLayout());
        Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/marketTray.png");
        marketTrayPanel = new MarketTrayImage(new ImageIcon(img).getImage());
    }

    public void loadMarbleMarket() {
        JPanel marble;
        Image img;
        GridBagConstraints c = new GridBagConstraints();
        MarbleMarket market = client.getModel().getGameZone().getMarbleMarket();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                img = market.get(i, j).getImage();
                marble = new MarbleImage(img);
                c.gridx = j;
                c.gridy = i;
                c.insets = new Insets(5, 5, 5, 5);
                marblePanel.add(marble, c);

            }
        }
        img = market.getFreeMarble().getImage();
        marble = new MarbleImage(img);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 300, 210, 0);
        marketTrayPanel.add(marble, c);
        marblePanel.setBorder(new LineBorder(Color.RED));
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

    }

    @Override
    public void clean() {
        SwingUtilities.invokeLater(() -> {
            marketPanel.removeAll();
            loadMarbleMarket();
            marketPanel.revalidate();
            marketPanel.repaint();
        });
    }
}
