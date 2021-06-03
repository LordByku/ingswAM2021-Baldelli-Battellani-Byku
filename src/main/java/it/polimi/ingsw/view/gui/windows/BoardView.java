package it.polimi.ingsw.view.gui.windows;


import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;

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
    private JPanel bottomPanel;
    ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();

    public BoardView(Client client, BlockingQueue<String> buffer){

        nicknames.remove(client.getNickname());
        int numOfPlayers = nicknames.size();

        GridBagConstraints c = new GridBagConstraints();
        JPanel viewMarketsPanel = new JPanel();
        viewMarketsPanel.setVisible(true);
        //viewMarketsPanel.setBorder(new LineBorder(Color.BLACK));
        viewMarketsPanel.add(new JButton("View Markets"));
        c.gridx=0;
        c.gridy=0;
        c.weightx = 1.0/(numOfPlayers+1);
        c.insets = new Insets(10,10,10,10);
        bottomPanel.add(viewMarketsPanel, c);

        JPanel[] panels = new JPanel[3];

        for (int i=0; i<numOfPlayers; i++) {
            panels[i] = new JPanel();
            panels[i].setVisible(true);
            //panels[i].setBorder(new LineBorder(Color.BLACK));
            panels[i].add(new JButton("View " + nicknames.get(i) + " board"));
            c.gridx=i + 1;
            c.gridy=0;
            c.weightx = 1.0/(numOfPlayers+1);
            c.insets = new Insets(10,10,10,10);
            bottomPanel.add(panels[i], c);
        }


        loadFaithTrack(client);
    }

    public void loadBoard(){

    }

    public void loadFaithTrack(Client client){
        int size = LocalConfig.getInstance().getFaithTrackFinalPosition() + 1;
        ArrayList <CheckPoint> checkPoints = LocalConfig.getInstance().getFaithTrackCheckPoints();
        ArrayList<VaticanReportSection> vaticanReportSections = LocalConfig.getInstance().getVaticanReportSections();

        GridBagConstraints c = new GridBagConstraints();

        JPanel[][] panels = new JPanel[3][size];

        Dimension cellSizeD = new Dimension(30, 30);

        Image cross = Toolkit.getDefaultToolkit().getImage("src/main/resources/Punchboard/croce.png");
        //ImageIcon crossIcon = new ImageIcon(cross);
        int currPosition = 0;
        if(client.getModel()!=null)
            currPosition = client.getModel().getPlayer(client.getNickname()).getBoard().getFaithTrack().getPosition();

        for(int i=0; i<size; i++){
            JLabel position = new JLabel();
            position.setForeground(Color.GRAY);
            position.setPreferredSize(cellSizeD);
            position.setHorizontalAlignment(0);
            panels[1][i] = new JPanel();
            panels[1][i].setVisible(true);
            panels[1][i].setBorder(new LineBorder(Color.BLACK));
            panels[1][i].setBackground(Color.decode("#fffcf0"));
            if(currPosition==i){
                position.setIcon(new ImageIcon(cross.getScaledInstance(30,30, Image.SCALE_SMOOTH)));

            }
            else{
                position.setText(Integer.toString(i));
            }
            panels[1][i].add(position);
            c.fill = GridBagConstraints.BOTH;
            c.gridx=i;
            c.gridy=1;
            c.weightx = 1.0/(size);
            //c.insets = new Insets(10,10,10,10);
            faithTrack.add(panels[1][i], c);
        }
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
            //c.insets = new Insets(0, 0, 20, 0);
            faithTrack.add(panels[2][j],c);
        }
        for(VaticanReportSection vaticanReportSection: vaticanReportSections){

            int initPos = vaticanReportSection.getFirstSpace();
            int finalPos = vaticanReportSection.getPopeSpace();
            int vrsSize = finalPos - initPos + 1;

            int numOfPlayers = nicknames.size();
            Integer[] playerPosition = new Integer[nicknames.size()];
            for(int i=0; i< numOfPlayers; i++){
                playerPosition[i] = client.getModel().getPlayer(nicknames.get(i)).getBoard().getFaithTrack().getPosition();
            }

            //TODO: Handle pope favor discoveries;

            panels[1][finalPos].setBackground(Color.decode("#bc5e00"));
            JLabel number = (JLabel) panels[1][finalPos].getComponent(0);
            number.setForeground(Color.BLACK);
            panels[0][initPos] = new JPanel();
            panels[0][initPos].setVisible(true);
            panels[0][initPos].setBorder(new LineBorder(Color.BLACK));
            Dimension d = panels[1][0].getSize();
            //panels[0][initPos].setPreferredSize(new Dimension((int)d.getWidth()*vrsSize, (int)d.getHeight()));
            panels[0][initPos].setBackground(Color.RED);
            String points = Integer.toString(vaticanReportSection.getPoints());
            JLabel popeFavor = new JLabel(points);
            popeFavor.setVisible(true);
            panels[0][initPos].add(popeFavor);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx=initPos;
            c.gridy=2;
            c.weightx = 1.0/(size);
            c.gridwidth = vrsSize;
            //c.insets = new Insets(20, 0, 0, 0);
            faithTrack.add(panels[0][initPos],c);
        }
    }
    public void loadDevCardsArea(){
        devCardsArea.setLayout(new GridLayout());
        ProductionDetails defaultProductionPower = LocalConfig.getInstance().getDefaultProductionPower();
        JPanel defProdPower = new JPanel();
        //int inputSize = defaultProductionPower.getInput();
        int outputSize;

        


    }

    public void initDiscard(){

    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    public void onError(String message) {

    }
}

