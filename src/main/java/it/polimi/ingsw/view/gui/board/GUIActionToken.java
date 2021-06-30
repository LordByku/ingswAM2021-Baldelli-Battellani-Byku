package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.game.actionTokens.ActionToken;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.images.ActionTokenImage;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;

public class GUIActionToken implements LocalModelElementObserver {

    private final Client client;
    private final JPanel actionTokensTextPanel;

    public GUIActionToken(Client client, JPanel actionTokensTextPanel){
        this.client = client;
        this.actionTokensTextPanel = actionTokensTextPanel;
        actionTokensTextPanel.setLayout(new BoxLayout(actionTokensTextPanel, BoxLayout.X_AXIS));
        client.getModel().getGameZone().getActionTokenDeck().addObserver(this);
    }

    public void loadActionToken(){
        JPanel imagePanel;
        ActionToken token = client.getModel().getGameZone().getActionToken();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        if(token!=null) {
            JPanel imageContainer = new JPanel(new GridBagLayout());
            Image actionTokenImage = token.getImage();
            imagePanel = new ActionTokenImage(actionTokenImage, 50);
            imageContainer.add(imagePanel);
            actionTokensTextPanel.add(imageContainer, c);
            c.gridx++;
            JPanel labelContainer = new JPanel(new GridBagLayout());
            JPanel textPanel = new JPanel();
            JLabel text = new JLabel(token.getString());
            text.setFont(new Font("Arial", Font.PLAIN, 13));
            textPanel.add(text);
            labelContainer.add(textPanel);
            actionTokensTextPanel.add(labelContainer, c);
        }
    }


    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            synchronized (client.getModel()) {
                actionTokensTextPanel.removeAll();
                loadActionToken();

                actionTokensTextPanel.revalidate();
                actionTokensTextPanel.repaint();
            }
        });
    }

    @Override
    public void clean() {
        client.getModel().getGameZone().getActionTokenDeck().removeObserver(this);
    }
}
