package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.Parser;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class BoardView extends GUIWindow {
    private JPanel panel;
    private JPanel faithTrack;
    private JPanel devCardsArea;
    private JPanel leaderCardsArea;
    private JPanel handLeaderCardsArea;
    private JPanel warehouse;
    private JPanel strongbox;
    private JPanel commands;
    private JButton discardButton;
    private JButton playButton;
    private JButton startProductionButton;
    private JButton purchaseDevCardButton;
    private JButton collectResourcesButton;
    private JButton viewBoard1Button;
    private JButton viewBoard2Button;
    private JButton viewBoard3Button;
    private JButton viewMarketsButton;

    public BoardView(Client client, BlockingQueue<String> buffer){

        ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();
        int i = 0;
        viewBoard1Button.setText(nicknames.get(i).equals(client.getNickname()) ? nicknames.get(++i) : nicknames.get(i++));
        viewBoard2Button.setText(nicknames.get(i).equals(client.getNickname()) ? nicknames.get(++i) : nicknames.get(i++));
        viewBoard3Button.setText(nicknames.get(i).equals(client.getNickname()) ? nicknames.get(++i) : nicknames.get(i));


    }

    public void loadBoard(){

    }

    public void loadFaithTrack(){
        int size = LocalConfig.getInstance().getFaithTrackFinalPosition() + 1;
        ArrayList <CheckPoint> checkPoints = LocalConfig.getInstance().getFaithTrackCheckPoints();
        ArrayList<VaticanReportSection> vaticanReportSections = LocalConfig.getInstance().getVaticanReportSections();
        JPanel faithSquare;

        faithTrack.setLayout(new GridLayout(3,size));
        JPanel[][] panels = new JPanel[3][size];
        for(int i=0; i<size; i++){
            panels[1][i] = new JPanel();
            panels[1][i].setVisible(true);
            panels[1][i].setBorder(new LineBorder(Color.BLACK));
        }
        for (CheckPoint checkPoint : checkPoints) {
            int j = checkPoint.getPosition();
            panels[2][j] = new JPanel();
            JLabel label = new JLabel(checkPoint.getPoints() + "0");
            label.setVisible(true);
            panels[2][j].add(label);
            panels[2][j].setVisible(true);
            panels[2][j].setBorder(new LineBorder(Color.BLACK));
        }
        for(VaticanReportSection vaticanReportSection: vaticanReportSections){
            int initPos = vaticanReportSection.getFirstSpace();
            int finalPos = vaticanReportSection.getPopeSpace();
            int vrsSize = finalPos - initPos + 1;
            //TODO: inserire valore punti, fare il panel grande quanto la size ecc.
            panels[0][initPos] = new JPanel();
            panels[0][initPos].setVisible(true);
            panels[0][initPos].setBorder(new LineBorder(Color.BLACK));
        }


    }

    public void initDiscard(){

    }

    @Override
    protected JPanel getPanel() {
        return null;
    }

    @Override
    public void onError(String message) {

    }
}

