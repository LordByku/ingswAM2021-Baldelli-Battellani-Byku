package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;

import javax.swing.*;
import java.util.ArrayList;

public class VRSPanelHandler extends PanelHandler {
    private final FaithTrackEditor faithTrackEditor;
    private ArrayList<ValidatableTextField> firstSpaceFields;
    private ArrayList<ValidatableTextField> popeSpaceFields;
    private ArrayList<ValidatableTextField> pointsFields;

    public VRSPanelHandler(JFrame frame, JPanel panel) {
        super(frame, panel);
        faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        firstSpaceFields = new ArrayList<>();
        popeSpaceFields = new ArrayList<>();
        pointsFields = new ArrayList<>();

        ArrayList<VaticanReportSection> vaticanReportSections = faithTrackEditor.getVaticanReportSections();

        for(int i = 0; i < vaticanReportSections.size(); ++i) {
            int finalI = i;

            VaticanReportSection vaticanReportSection = vaticanReportSections.get(i);

            EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    faithTrackEditor.addVaticanReportSection(finalI, 1, 1, 1);
                    build();
                }
            }));

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            firstSpaceFields.add(EditorGUIUtil.addValidatableTextField(vaticanReportSection.getFirstSpace(), dataPanel, (value) -> {
                faithTrackEditor.setVRSFirstSpace(finalI, value);
            }, (value) -> faithTrackEditor.validateVRSFirstSpace(finalI)));

            popeSpaceFields.add(EditorGUIUtil.addValidatableTextField(vaticanReportSection.getPopeSpace(), dataPanel, (value) -> {
                faithTrackEditor.setVRSPopeSpace(finalI, value);
            }, (value) -> faithTrackEditor.validateVRSPopeSpace(finalI)));

            pointsFields.add(EditorGUIUtil.addValidatableTextField(vaticanReportSection.getPoints(), dataPanel, (value) -> {
                faithTrackEditor.setVRSPoints(finalI, value);
            }, (value) -> faithTrackEditor.validateVRSPoints(finalI)));

            EditorGUIUtil.addButton("-", dataPanel, new ButtonClickEvent((e) -> {
                faithTrackEditor.removeVaticanReportSection(finalI);
                build();
            }));

            panel.add(dataPanel);
        }

        EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
            if(validate()) {
                faithTrackEditor.addVaticanReportSection(vaticanReportSections.size(), 1, 1, 1);
                build();
            }
        }));

        frame.setVisible(true);
    }

    public boolean validate() {
        // TODO : limit number of vrs
        boolean result = true;
        for(ValidatableTextField validatableTextField: firstSpaceFields) {
            if(!validatableTextField.validate()) {
                result = false;
            }
        }
        for(ValidatableTextField validatableTextField: popeSpaceFields) {
            if(!validatableTextField.validate()) {
                result = false;
            }
        }
        for(ValidatableTextField validatableTextField: pointsFields) {
            if(!validatableTextField.validate()) {
                result = false;
            }
        }
        return result;
    }
}
