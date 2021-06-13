package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarbleImage;
import it.polimi.ingsw.view.gui.images.marbleMarket.MarbleMarketImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.MarbleMarket;

import javax.swing.*;
import java.awt.*;

public class GUIMarbleMarket implements LocalModelElementObserver {

    JPanel marketPanel;
    JPanel marblePanel;
    Client client;

    public GUIMarbleMarket(Client client, JPanel marketPanel){
        this.client=client;
        Image img = Toolkit.getDefaultToolkit().getImage("/resources/Punchboard/plancia_portabiglie.png\"");
        this.marketPanel= new MarbleMarketImage(img);
        marblePanel = new JPanel();
    }

    public void loadMarbleMarket(){
        JPanel marble;
        Image img = null;
        GridBagConstraints c = new GridBagConstraints();
        for(int i=0; i<3;i++){
            for(int j=0;j<4; j++) {
                MarbleMarket market = client.getModel().getGameZone().getMarbleMarket();
                MarbleColour colour = market.get(i,j);
                switch (colour){
                    case RED:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/red.png");
                        break;
                    case BLUE:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/blue.png");
                        break;
                    case GREY:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/grey.png");
                        break;
                    case WHITE:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/white.png");
                        break;
                    case PURPLE:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/purple.png");
                        break;
                    case YELLOW:
                        img = Toolkit.getDefaultToolkit().getImage("/resources/Biglie/yellow.png");
                        break;
                }
                marble = new MarbleImage(img);
                c.gridx=i;
                c.gridy=j;
                marblePanel.add(marble, c);
                c.gridx=0;
                c.gridy=0;
                c.insets =  new Insets(20,20, 200, 20);
                marketPanel.add(marblePanel, c);
            }
        }
    }
    @Override
    public void notifyObserver() {

    }
}
