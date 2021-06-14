package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonPrimitive;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;
import it.polimi.ingsw.view.localModel.HandLeaderCardsArea;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class GUIHandLeaderCards implements LocalModelElementObserver {
    private final Client client;
    private final GUI gui;
    private JPanel handLeaderCardsPanel;
    private int numOfCardsToDiscard;
    private ArrayList<Integer> initDiscardSelection;
    private ArrayList<Integer> handLeaderCards;

    public GUIHandLeaderCards(GUI gui, Client client, JPanel handLeaderCardsPanel) {
        this.client = client;
        this.gui = gui;
        this.handLeaderCardsPanel = handLeaderCardsPanel;
        numOfCardsToDiscard = LocalConfig.getInstance().getInitialDiscards();
        initDiscardSelection = new ArrayList<>();

        HandLeaderCardsArea handLeaderCardsArea = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards();
        handLeaderCardsArea.addObserver(this);

        handLeaderCards = handLeaderCardsArea.getLeaderCards();
    }

    public void loadHandLeaderCards() {
        GridBagConstraints c = new GridBagConstraints();
        ArrayList<LeaderCardImage> leaderCardImages = new ArrayList<>();

        c.gridx = 0;
        for (int i = 0; i < handLeaderCards.size(); i++) {
            int handLeaderCard = handLeaderCards.get(i);
            JPanel cardPanel = new JPanel(new GridBagLayout());
            LeaderCard card = LeaderCardsParser.getInstance().getCard(handLeaderCard);
            LeaderCardImage cardImage = card.getLeaderCardImage(150);
            JButton discardButton = new JButton("discard");
            JButton playButton = new JButton("play");

            leaderCardImages.add(cardImage);

            Player self = client.getModel().getPlayer(client.getNickname());
            discardButton.setEnabled(self.canDiscard(client.getModel()));
            playButton.setEnabled(self.canPlay(client.getModel()));

            int finalI = i;
            discardButton.addMouseListener(new ButtonClickEvent((event) -> {
                if (!self.initDiscard()) {
                    if (!initDiscardSelection.contains(finalI)) {
                        Border redBorder = BorderFactory.createLineBorder(Color.RED);
                        cardImage.setBorder(redBorder);
                        initDiscardSelection.add(finalI);
                        if (initDiscardSelection.size() == numOfCardsToDiscard) {
                            for (LeaderCardImage leaderCardImage : leaderCardImages) {
                                leaderCardImage.setBorder(null);
                            }
                            gui.bufferWrite(client.buildRequestMessage(CommandType.INITDISCARD).toString());
                            gui.bufferWrite(client.buildCommandMessage("indices", JsonUtil.getInstance().serialize(initDiscardSelection)).toString());
                            initDiscardSelection.clear();
                        }
                    } else {
                        cardImage.setBorder(null);
                        initDiscardSelection.remove((Integer) finalI);
                    }
                } else {
                    gui.bufferWrite(client.buildRequestMessage(CommandType.DISCARDLEADER).toString());
                    gui.bufferWrite(client.buildCommandMessage("index", new JsonPrimitive(finalI)).toString());
                }
            }));
            playButton.addMouseListener(new ButtonClickEvent((event) -> {
                gui.bufferWrite(client.buildRequestMessage(CommandType.PLAYLEADER).toString());
                gui.bufferWrite(client.buildCommandMessage("index", new JsonPrimitive(finalI)).toString());
            }));

            c.gridy = 0;
            cardPanel.add(cardImage, c);
            c.gridy++;
            cardPanel.add(discardButton, c);
            c.gridy++;
            cardPanel.add(playButton, c);

            c.gridy = 0;
            c.insets = new Insets(0, 5, 0, 5);
            handLeaderCardsPanel.add(cardPanel, c);
            c.gridx++;
        }
    }

    @Override
    public void notifyObserver() {
        System.out.println("notification");
        SwingUtilities.invokeLater(() -> {
            handLeaderCardsPanel.removeAll();
            loadHandLeaderCards();
            handLeaderCardsPanel.revalidate();
            handLeaderCardsPanel.repaint();
        });
    }
}
