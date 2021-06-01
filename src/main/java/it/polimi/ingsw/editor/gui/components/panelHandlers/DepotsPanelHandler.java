package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.model.BoardEditor;
import it.polimi.ingsw.editor.model.Config;

import javax.swing.*;
import java.util.ArrayList;

public class DepotsPanelHandler extends PanelHandler {
    private final BoardEditor boardEditor;

    public DepotsPanelHandler(JFrame frame, JPanel panel) {
        super(frame, panel);
        boardEditor = Config.getInstance().getBoardEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        ArrayList<Integer> depotSizes = boardEditor.getDepotSizes();

        for(int i = 0; i < depotSizes.size(); ++i) {
            int finalI = i;

            int size = depotSizes.get(i);

            addButton("+", panel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    boardEditor.addDepot(finalI, 1);
                    build();
                }
            }));

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            addTextField(size, dataPanel, (value) -> boardEditor.setDepot(finalI, value));

            addButton("-", dataPanel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    boardEditor.removeDepot(finalI);
                    build();
                }
            }));

            panel.add(dataPanel);
        }

        addButton("+", panel, new ButtonClickEvent((e) -> {
            if(validate()) {
                boardEditor.addDepot(depotSizes.size(), 1);
                build();
            }
        }));

        frame.setVisible(true);
    }

    public boolean validate() {
        // TODO
        return true;
    }
}
