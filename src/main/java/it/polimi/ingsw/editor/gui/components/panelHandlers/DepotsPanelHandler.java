package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.BoardEditor;
import it.polimi.ingsw.editor.model.Config;

import javax.swing.*;
import java.util.ArrayList;

public class DepotsPanelHandler extends PanelHandler {
    private final BoardEditor boardEditor;
    private ArrayList<ValidatableTextField> slotsFields;

    public DepotsPanelHandler(JFrame frame, JPanel panel) {
        super(frame, panel);
        boardEditor = Config.getInstance().getBoardEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        slotsFields = new ArrayList<>();

        ArrayList<Integer> depotSizes = boardEditor.getDepotSizes();

        for (int i = 0; i < depotSizes.size(); ++i) {
            int finalI = i;

            int size = depotSizes.get(i);

            EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
                if (validate()) {
                    boardEditor.addDepot(finalI, 1);
                    build();
                }
            }));

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            slotsFields.add(EditorGUIUtil.addValidatableTextField(size, dataPanel, (value) -> {
                boardEditor.setDepot(finalI, value);
            }, (value) -> value > 0 && value < 100));

            EditorGUIUtil.addButton("-", dataPanel, new ButtonClickEvent((e) -> {
                boardEditor.removeDepot(finalI);
                build();
            }));

            panel.add(dataPanel);
        }

        EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
            if (validate()) {
                boardEditor.addDepot(depotSizes.size(), 1);
                build();
            }
        }));

        frame.setVisible(true);
    }

    public boolean validate() {
        // TODO : limit number of depots
        boolean result = true;
        for (ValidatableTextField validatableTextField : slotsFields) {
            if (!validatableTextField.validate()) {
                result = false;
            }
        }
        return result;
    }
}
