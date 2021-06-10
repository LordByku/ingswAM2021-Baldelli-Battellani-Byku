package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import javax.swing.*;
import java.util.ArrayList;

public class CheckPointsPanelHandler extends PanelHandler {
    private final FaithTrackEditor faithTrackEditor;
    private ArrayList<ValidatableTextField> checkPointFields;
    private ArrayList<ValidatableTextField> pointsFields;

    public CheckPointsPanelHandler(JFrame frame, JPanel panel) {
        super(frame, panel);
        faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        checkPointFields = new ArrayList<>();
        pointsFields = new ArrayList<>();

        ArrayList<CheckPoint> checkPoints = faithTrackEditor.getCheckPoints();

        for(int i = 0; i < checkPoints.size(); ++i) {
            int finalI = i;

            CheckPoint checkPoint = checkPoints.get(i);

            EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
                if(validate()) {
                    faithTrackEditor.addCheckPoint(finalI, new CheckPoint(1, 1));
                    build();
                }
            }));

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            checkPointFields.add(EditorGUIUtil.addValidatableTextField(
                checkPoint.getPosition(), dataPanel, (value) -> {
                    faithTrackEditor.setCheckPointPosition(finalI, value);
                }, (value) -> faithTrackEditor.validatePosition(finalI)
            ));

            pointsFields.add(EditorGUIUtil.addValidatableTextField(
                checkPoint.getPoints(), dataPanel, (value) -> {
                    faithTrackEditor.setCheckPointPoints(finalI, value);
                }, (value) -> faithTrackEditor.validatePoints(finalI)
            ));

            EditorGUIUtil.addButton("-", dataPanel, new ButtonClickEvent((e) -> {
                faithTrackEditor.removeCheckPoint(finalI);
                build();
            }));

            panel.add(dataPanel);
        }

        EditorGUIUtil.addButton("+", panel, new ButtonClickEvent((e) -> {
            if(validate()) {
                faithTrackEditor.addCheckPoint(checkPoints.size(), new CheckPoint(1, 1));
                build();
            }
        }));

        frame.setVisible(true);
    }

    public boolean validate() {
        // TODO : limit number of checkpoints
        boolean result = true;
        for(ValidatableTextField validatableTextField: checkPointFields) {
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
