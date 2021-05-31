package it.polimi.ingsw.editor.gui;

import it.polimi.ingsw.editor.gui.components.FaithTrackPanelHandler;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.InitGameEditor;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class Window {
    private JFormattedTextField leaderCardsToAssignTextField;
    private JFormattedTextField leaderCardsToDiscardTextField;
    private JFormattedTextField initFaithPoints1;
    private JFormattedTextField initResources1;
    private JFormattedTextField initFaithPoints2;
    private JFormattedTextField initResources2;
    private JFormattedTextField initFaithPoints3;
    private JFormattedTextField initResources3;
    private JFormattedTextField initFaithPoints4;
    private JFormattedTextField initResources4;
    private JFormattedTextField faithTrackLengthTextField;
    private JPanel checkPointsPanel;
    private JPanel panel;

    public Window(JFrame frame) {
        FaithTrackPanelHandler checkPointsPanelHandler = new FaithTrackPanelHandler(frame, checkPointsPanel, faithTrackLengthTextField);
        checkPointsPanelHandler.build();

        InitGameEditor initGameEditor = Config.getInstance().getInitGameEditor();
        leaderCardsToAssignTextField.setValue(initGameEditor.getLeaderCardsToAssign());
        leaderCardsToDiscardTextField.setValue(initGameEditor.getLeaderCardsToDiscard());
        initFaithPoints1.setValue(initGameEditor.getFaithPoints(0));
        initFaithPoints2.setValue(initGameEditor.getFaithPoints(1));
        initFaithPoints3.setValue(initGameEditor.getFaithPoints(2));
        initFaithPoints4.setValue(initGameEditor.getFaithPoints(3));
        initResources1.setValue(initGameEditor.getResources(0));
        initResources2.setValue(initGameEditor.getResources(1));
        initResources3.setValue(initGameEditor.getResources(2));
        initResources4.setValue(initGameEditor.getResources(3));

        leaderCardsToAssignTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                leaderCardsToAssignTextField, initGameEditor::setLeaderCardsToAssign
        ));
        leaderCardsToDiscardTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                leaderCardsToDiscardTextField, initGameEditor::setLeaderCardsToDiscard
        ));
        initFaithPoints1.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints1, (value) -> initGameEditor.setFaithPoints(0, value)
        ));
        initFaithPoints2.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints2, (value) -> initGameEditor.setFaithPoints(1, value)
        ));
        initFaithPoints3.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints3, (value) -> initGameEditor.setFaithPoints(2, value)
        ));
        initFaithPoints4.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints4, (value) -> initGameEditor.setFaithPoints(3, value)
        ));
        initResources1.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources1, (value) -> initGameEditor.setResources(0, value)
        ));
        initResources2.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources2, (value) -> initGameEditor.setResources(1, value)
        ));
        initResources3.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources3, (value) -> initGameEditor.setResources(2, value)
        ));
        initResources4.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources4, (value) -> initGameEditor.setResources(3, value)
        ));

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
