package it.polimi.ingsw.view.gui.board;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.controller.Production;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIUtil;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.PlayedLeaderCardsArea;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GUILeaderCardsArea implements LocalModelElementObserver {
    private final GUI gui;
    private final PlayedLeaderCardsArea playedLeaderCards;
    private JPanel leaderCardsArea;
    private final Client client;
    private Player player;

    public GUILeaderCardsArea(GUI gui, Client client, JPanel leaderCardsArea, String nickname) {
        this.gui = gui;
        this.client = client;
        this.leaderCardsArea = leaderCardsArea;
        player = client.getModel().getPlayer(nickname);
        playedLeaderCards = player.getBoard().getPlayedLeaderCards();

        playedLeaderCards.addObserver(this);
        player.getCommandElement().addObserver(this, CommandType.PRODUCTION);
    }

    public void loadLeaderCardsArea() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 5);

        // TODO : leader card depots

        int productionIndex = 0;
        for (int playedLeaderCardID : playedLeaderCards.getLeaderCards()) {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            LeaderCard leaderCard = LeaderCardsParser.getInstance().getCard(playedLeaderCardID);
            JPanel card = leaderCard.getLeaderCardImage(150);

            container.add(card);

            if (leaderCard.isType(LeaderCardType.PRODUCTION)) {
                CommandBuffer commandBuffer = player.getCommandBuffer();
                if (commandBuffer != null && !commandBuffer.isCompleted() && commandBuffer.getCommandType() == CommandType.PRODUCTION) {
                    Production productionCommand = (Production) commandBuffer;

                    int[] currentSelection = productionCommand.getProductionsToActivate();
                    int n = currentSelection != null ? currentSelection.length : 0;

                    boolean selected = false;
                    if (currentSelection != null) {
                        for (int selection : currentSelection) {
                            if (4 + productionIndex == selection) {
                                selected = true;
                            }
                        }
                    }
                    boolean finalSelected = selected;
                    int finalProductionIndex = productionIndex;
                    JButton button = GUIUtil.addButton("select", container, new ButtonClickEvent((e) -> {
                        if (finalSelected) {
                            int[] selection = new int[n - 1];
                            for (int j = 0, k = 0; j < n; ++j) {
                                if (currentSelection[j] != 4 + finalProductionIndex) {
                                    selection[k++] = currentSelection[j];
                                }
                            }
                            JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                            gui.bufferWrite(message.toString());
                        } else {
                            int[] selection = new int[n + 1];
                            for (int j = 0; j < n; ++j) {
                                selection[j] = currentSelection[j];
                            }
                            selection[n] = 4 + finalProductionIndex;
                            JsonObject message = client.buildCommandMessage("selection", JsonUtil.getInstance().serialize(selection));
                            gui.bufferWrite(message.toString());
                        }
                    }));

                    if (selected) {
                        Border redBorder = BorderFactory.createLineBorder(Color.RED);
                        button.setBorder(redBorder);
                    } else {
                        button.setBorder(null);
                    }
                }

                ++productionIndex;
            }

            leaderCardsArea.add(container, c);
            c.gridx++;
        }
    }

    @Override
    public void notifyObserver(NotificationSource notificationSource) {
        SwingUtilities.invokeLater(() -> {
            leaderCardsArea.removeAll();
            loadLeaderCardsArea();
            leaderCardsArea.revalidate();
            leaderCardsArea.repaint();
        });
    }

    @Override
    public void clean() {
        playedLeaderCards.removeObserver(this);
    }
}
