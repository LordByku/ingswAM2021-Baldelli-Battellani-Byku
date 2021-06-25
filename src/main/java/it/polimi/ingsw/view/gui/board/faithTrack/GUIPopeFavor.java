package it.polimi.ingsw.view.gui.board.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.localModel.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIPopeFavor {
    private final Player player;
    JPanel faithTrack;
    JPanel[][] panels;
    GridBagConstraints c;
    Integer lolloPosition;
    ArrayList<VaticanReportSection> vaticanReportSections;
    ArrayList<String> nicknames;
    Client client;
    int size;
    int numOfPlayers;

    public GUIPopeFavor(Client client, JPanel faithTrack, JPanel[][] panels, GridBagConstraints c, Integer lolloPosition, String nickname, int size) {
        this.faithTrack = faithTrack;
        this.panels = panels;
        this.c = c;
        this.lolloPosition = lolloPosition;
        this.client = client;
        this.size = size;
        vaticanReportSections = LocalConfig.getInstance().getVaticanReportSections();
        nicknames = LocalConfig.getInstance().getTurnOrder();
        numOfPlayers = nicknames.size();

        player = client.getModel().getPlayer(nickname);
    }

    public void loadPopeFavors() {

        ArrayList<Integer> receivedFavors = player.getBoard().getFaithTrack().getReceivedFavors();
        for (VaticanReportSection vaticanReportSection : vaticanReportSections) {

            int initPos = vaticanReportSection.getFirstSpace();
            int finalPos = vaticanReportSection.getPopeSpace();
            int vrsSize = finalPos - initPos + 1;


            panels[1][finalPos].setBackground(Color.decode("#bc5e00"));
            JLabel number = (JLabel) panels[1][finalPos].getComponent(0);
            number.setForeground(Color.BLACK);
            panels[0][initPos] = new JPanel();
            panels[0][initPos].setVisible(true);
            panels[0][initPos].setBorder(new LineBorder(Color.BLACK));
            panels[0][initPos].setBackground(Color.RED);
            String points = Integer.toString(vaticanReportSection.getPoints());
            JLabel popeFavor = new JLabel(points);

            boolean activated = false;
            Integer[] positions = new Integer[numOfPlayers];

            for (int i = 0; i < numOfPlayers; i++) {
                positions[i] = client.getModel().getPlayer(nicknames.get(i)).getBoard().getFaithTrack().getPosition();
                if (positions[i] >= finalPos)
                    activated = true;
            }
            if (lolloPosition != null && lolloPosition >= finalPos)
                activated = true;

            if (activated && !receivedFavors.contains(vaticanReportSection.getId())) {
                popeFavor = new JLabel("X");
                popeFavor.setForeground(Color.RED);
                panels[0][initPos].setBackground(Color.WHITE);
            } else if ((activated && receivedFavors.contains(vaticanReportSection.getId()))) {
                panels[0][initPos].setBackground(Color.GREEN);
            }

            popeFavor.setVisible(true);
            panels[0][initPos].add(popeFavor);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = initPos;
            c.gridy = 2;
            c.weightx = 1.0 / (size);
            c.gridwidth = vrsSize;
            //c.insets = new Insets(20, 0, 0, 0);
            faithTrack.add(panels[0][initPos], c);
        }
    }
}
