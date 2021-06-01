package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;

import javax.swing.*;
import java.util.ArrayList;

public class VRSPanelHandler extends PanelHandler {
    private final FaithTrackEditor faithTrackEditor;

    public VRSPanelHandler(JFrame frame, JPanel panel) {
        super(frame, panel);
        faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        ArrayList<VaticanReportSection> vaticanReportSections = faithTrackEditor.getVaticanReportSections();

        for(int i = 0; i < vaticanReportSections.size(); ++i) {
            int finalI = i;

            VaticanReportSection vaticanReportSection = vaticanReportSections.get(i);

            addButton("+", panel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    faithTrackEditor.addVaticanReportSection(finalI, 1, 1, 1);
                    build();
                }
            }));

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            addTextField(vaticanReportSection.getFirstSpace(), dataPanel, (value) -> faithTrackEditor.setVRSFirstSpace(finalI, value));

            addTextField(vaticanReportSection.getPopeSpace(), dataPanel, (value) -> faithTrackEditor.setVRSPopeSpace(finalI, value));

            addTextField(vaticanReportSection.getPoints(), dataPanel, (value) -> faithTrackEditor.setVRSPoints(finalI, value));

            addButton("-", dataPanel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    faithTrackEditor.removeVaticanReportSection(finalI);
                    build();
                }
            }));

            panel.add(dataPanel);
        }

        addButton("+", panel, new ButtonClickEvent((e) -> {
            if(validate()) {
                faithTrackEditor.addVaticanReportSection(vaticanReportSections.size(), 1, 1, 1);
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
