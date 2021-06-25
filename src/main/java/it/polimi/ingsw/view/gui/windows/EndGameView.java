package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.localModel.EndGameResult;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EndGameView extends GUIWindow {
    private final ArrayList<EndGameResult> results;
    private final boolean computerWins;
    int textSize = 20;
    private JPanel panel;
    private JPanel rank;
    private JTable rankingTable;
    private JButton backButton;

    public EndGameView(GUI gui, Client client) {
        super(gui, client);
        results = client.getModel().getEndGameResults().getResults();
        computerWins = client.getModel().getEndGameResults().getComputerWins();

        results.sort((r1, r2) -> {
            if (r1.getVPoints() == r2.getVPoints()) {
                return r2.getResources() - r1.getResources();
            }
            return r2.getVPoints() - r1.getVPoints();
        });

        DefaultTableModel model = new DefaultTableModel(new String[]{"Position:", "Nickname:", "VictoryPoints:", "Resources:"}, 0);
        rankingTable.setModel(model);
        rankingTable.setRowHeight(50);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++)
            rankingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);


        for (int i = 0; i < results.size(); ++i) {
            String position = Integer.toString(i + 1);
            String nickname = results.get(i).getPlayer();
            String victoryPoints = Integer.toString(results.get(i).getVPoints());
            String resources = Integer.toString(results.get(i).getResources());

            model.addRow(new String[]{position, nickname, victoryPoints, resources});
        }


    }

    @Override
    protected JPanel getPanel() {
        return panel;
    }

    @Override
    protected void clean() {

    }

    @Override
    public void onError(String message) {

    }
}
