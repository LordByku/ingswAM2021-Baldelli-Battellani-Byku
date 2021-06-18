package it.polimi.ingsw.view.gui.board.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.localModel.FaithTrack;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver;

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

    public GUIFaithTrack(Client client, JPanel faithTrackPanel, String nickname) {
        this.client = client;
        this.faithTrackPanel = faithTrackPanel;

        faithTrack = client.getModel().getPlayer(nickname).getBoard().getFaithTrack();
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
        //To add the right cross path.
        Image redCross = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/calamaio.png");
        Image blackCross = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/croce.png");

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
            if (lolloPosition == null) {
                if (currPosition == i) {
                    position.setIcon(new ImageIcon(redCross.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                } else {
                    position.setText(Integer.toString(i));
                }
            } else {
                if (currPosition != i && lolloPosition == i) {
                    position.setIcon(new ImageIcon(blackCross.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                } else if (currPosition == i && lolloPosition != i) {
                    position.setIcon(new ImageIcon(redCross.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                } else if (currPosition == i) {
                    JLabel magnificoPosition = new JLabel();
                    magnificoPosition.setIcon(new ImageIcon(blackCross.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    position.setIcon(new ImageIcon(redCross.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                    labelC.gridx = 0;
                    labelC.gridy = 0;
                    labelC.insets = new Insets(0, 5, 0, 0);
                    panels[1][i].add(magnificoPosition, labelC);
                    labelC.insets = new Insets(0, 0, 0, 5);
                } else {
                    position.setText(Integer.toString(i));
                }
            }
            labelC.gridx = 0;
            labelC.gridy = 0;
            panels[1][i].add(position, labelC);
            c.fill = GridBagConstraints.BOTH;
            c.gridx = i;
            c.gridy = 1;
            c.weightx = 1.0 / (size);
            faithTrackPanel.add(panels[1][i], c);
        }

        GUICheckPoints guiCheckPoints = new GUICheckPoints(faithTrackPanel, panels, c, checkPoints, size);
        guiCheckPoints.loadCheckPoints();

        GUIPopeFavor guiPopeFavor = new GUIPopeFavor(client, faithTrackPanel, panels, c, lolloPosition, size);
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
