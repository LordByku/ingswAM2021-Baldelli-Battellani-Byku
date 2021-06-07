package it.polimi.ingsw.view.gui.board.faithTrack;

import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUICheckPoints {

    JPanel faithTrack;
    JPanel[][] panels;
    GridBagConstraints c;
    ArrayList<CheckPoint> checkPoints;
    int size;

    public GUICheckPoints(JPanel faithTrack, JPanel[][] panels, GridBagConstraints c, ArrayList<CheckPoint> checkPoints, int size){
        this.faithTrack=faithTrack;
        this.panels=panels;
        this.c = c;
        this.checkPoints=checkPoints;
        this.size=size;

    }

    public void loadCheckPoints(){
        for (CheckPoint checkPoint : checkPoints) {
            int j = checkPoint.getPosition();
            panels[2][j] = new JPanel();
            String points = Integer.toString(checkPoint.getPoints());
            JLabel label = new JLabel(points);
            label.setVisible(true);
            panels[2][j].setBackground(Color.orange);
            panels[2][j].add(label);
            panels[2][j].setVisible(true);
            panels[2][j].setBorder(new LineBorder(Color.BLACK));
            c.gridx=j;
            c.gridy=0;
            c.weightx = 1.0/(size);
            faithTrack.add(panels[2][j],c);
        }
    }
}
