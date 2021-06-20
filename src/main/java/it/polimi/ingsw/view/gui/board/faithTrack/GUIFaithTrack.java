package it.polimi.ingsw.view.gui.board.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.images.faithTrack.CrossImage;
import it.polimi.ingsw.view.localModel.FaithTrack;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIFaithTrack implements LocalModelElementObserver {
    private JPanel faithTrackPanel;
    private Client client;
    private int currPosition;
    private Integer lolloPosition;
    private FaithTrack faithTrack;
    private Player player;

    public GUIFaithTrack(Client client, JPanel faithTrackPanel, String nickname) {
        this.client = client;
        this.faithTrackPanel = faithTrackPanel;

        player = client.getModel().getPlayer(nickname);

        faithTrack = player.getBoard().getFaithTrack();
        faithTrack.addObserver(this);
    }

    public void loadFaithTrack() {
        currPosition = faithTrack.getPosition();
        lolloPosition = faithTrack.getComputerPosition();

        int size = LocalConfig.getInstance().getFaithTrackFinalPosition() + 1;
        ArrayList<CheckPoint> checkPoints = LocalConfig.getInstance().getFaithTrackCheckPoints();
        GridBagConstraints c = new GridBagConstraints();
        JPanel[][] panels = new JPanel[3][size];
        Dimension cellSizeD = new Dimension(30, 30);

        JPanel cross = new CrossImage("Punchboard/faith_cross.png");
        cross.setPreferredSize(cellSizeD);
        for (int i = 0; i < size; i++) {
            JLabel position = new JLabel();
            position.setForeground(Color.GRAY);
            position.setPreferredSize(cellSizeD);
            position.setHorizontalAlignment(0);
            GridBagConstraints labelC = new GridBagConstraints();
            panels[1][i] = new JPanel(new GridBagLayout());
            panels[1][i].setVisible(true);
            panels[1][i].setBorder(new LineBorder(Color.BLACK));
            panels[1][i].setBackground(Color.decode("#fffcf0"));
            labelC.gridx = 0;
            labelC.gridy = 0;
            panels[1][i].add(position, labelC);
            if (lolloPosition == null) {
                if (currPosition == i) {
                    panels[1][i].add(cross, labelC);
                } else {
                    position.setText(Integer.toString(i));
                }
            } else {
                if (currPosition != i && lolloPosition == i) {
                    JPanel black_cross = new CrossImage("Punchboard/croce.png");
                    cross.setPreferredSize(cellSizeD);
                    panels[1][i].add(black_cross, labelC);
                } else if (currPosition == i && lolloPosition != i) {
                    panels[1][i].add(cross, labelC);
                } else if (currPosition == i) {
                    JPanel black_cross = new CrossImage("Punchboard/croce.png");
                    cross.setPreferredSize(cellSizeD);
                    labelC.insets = new Insets(0, 8, 0, 0);
                    panels[1][i].add(black_cross, labelC);
                    labelC.insets = new Insets(0, 0, 0, 8);
                    panels[1][i].add(cross, labelC);
                } else {
                    position.setText(Integer.toString(i));
                }
            }
            c.fill = GridBagConstraints.BOTH;
            c.gridx = i;
            c.gridy = 1;
            c.weightx = 1.0 / (size);
            faithTrackPanel.add(panels[1][i], c);
        }

        GUICheckPoints guiCheckPoints = new GUICheckPoints(faithTrackPanel, panels, c, checkPoints, size);
        guiCheckPoints.loadCheckPoints();

        GUIPopeFavor guiPopeFavor = new GUIPopeFavor(client, faithTrackPanel, panels, c, lolloPosition, player.getNickname(), size);
        guiPopeFavor.loadPopeFavors();

    }

    @Override
    public void notifyObserver() {
        // TODO : only update positions ?
        SwingUtilities.invokeLater(() -> {
            faithTrackPanel.removeAll();
            loadFaithTrack();
            faithTrackPanel.revalidate();
            faithTrackPanel.repaint();
        });
    }

    @Override
    public void clean() {
        faithTrack.removeObserver(this);
    }
}
