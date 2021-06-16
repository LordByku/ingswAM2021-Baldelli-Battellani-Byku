package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.board.GUIBottomPanel;
import it.polimi.ingsw.view.gui.board.GUIMarbleMarket;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarbleMarketImage;
import it.polimi.ingsw.view.gui.windows.tokens.MarbleMarketToken;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MarbleMarketView extends GUIWindow {
    private JPanel panel;
    private JPanel marketPanel;
    private JPanel bottomPanel;
    private JPanel marbleMarketPanel;
    private GUIMarbleMarket guiMarbleMarket;

    public MarbleMarketView(GUI gui, Client client) {
        super(gui, client);
        guiMarbleMarket = new GUIMarbleMarket(gui, client, marketPanel);
        guiMarbleMarket.loadMarbleMarket();

        // TODO : load via Class Loader
        Image img = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/plancia_portabiglie.png");
        marbleMarketPanel = new MarbleMarketImage(new ImageIcon(img).getImage());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        marketPanel.add(marbleMarketPanel, c);
        marketPanel.setBorder(new LineBorder(Color.BLACK));

        GUIBottomPanel guiBottomPanel = new GUIBottomPanel(gui, client, bottomPanel, new MarbleMarketToken());
        guiBottomPanel.loadBottomPanel();
    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    protected void clean() {
        guiMarbleMarket.clean();
    }

    @Override
    public void onError(String message) {

    }
}

