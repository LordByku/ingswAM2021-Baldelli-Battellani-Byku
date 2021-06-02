package it.polimi.ingsw.editor.gui;

import it.polimi.ingsw.editor.gui.components.*;
import it.polimi.ingsw.editor.gui.components.panelHandlers.*;
import it.polimi.ingsw.editor.model.BoardEditor;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.editor.model.InitGameEditor;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JPanel VRSPanel;
    private JFormattedTextField developmentCardSlotsTextField;
    private JPanel defaultProductionInPanel;
    private JPanel defaultProductionOutPanel;
    private JPanel depotsPanel;
    private JComboBox devCardSelectionBox;
    private JRadioButton IRadioButton;
    private JRadioButton IIRadioButton;
    private JRadioButton IIIRadioButton;
    private JRadioButton GREENRadioButton;
    private JRadioButton BLUERadioButton;
    private JRadioButton YELLOWRadioButton;
    private JRadioButton PURPLERadioButton;
    private JFormattedTextField devCardPointsTextField;
    private JPanel devCardRequirementPanel;
    private JPanel devCardProductionInPanel;
    private JPanel devCardProductionOutPanel;
    private JPanel devCardPanel;
    private JButton removeDevCardButton;

    public Window(JFrame frame) {
        FaithTrackEditor faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        faithTrackLengthTextField.setValue(faithTrackEditor.getFinalPosition());
        faithTrackLengthTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                faithTrackLengthTextField, faithTrackEditor::setFinalPosition
        ));

        CheckPointsPanelHandler checkPointsPanelHandler = new CheckPointsPanelHandler(frame, checkPointsPanel);
        checkPointsPanelHandler.build();

        VRSPanelHandler vrsPanelHandler = new VRSPanelHandler(frame, VRSPanel);
        vrsPanelHandler.build();

        BoardEditor boardEditor = Config.getInstance().getBoardEditor();
        SpendablePanelHandler productionInPanelHandler = new SpendablePanelHandler(frame, defaultProductionInPanel, boardEditor.getDefaultProductionIn());
        productionInPanelHandler.build();
        ObtainablePanelHandler productionOutPanelHandler = new ObtainablePanelHandler(frame, defaultProductionOutPanel, boardEditor.getDefaultProductionOut());
        productionOutPanelHandler.build();
        DepotsPanelHandler depotsPanelHandler = new DepotsPanelHandler(frame, depotsPanel);
        depotsPanelHandler.build();
        developmentCardSlotsTextField.setValue(boardEditor.getDevelopmentCardSlots());
        developmentCardSlotsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                developmentCardSlotsTextField, boardEditor::setDevelopmentCardSlots
        ));

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

        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.add(IRadioButton);
        levelGroup.add(IIRadioButton);
        levelGroup.add(IIIRadioButton);

        ButtonGroup colourGroup = new ButtonGroup();
        colourGroup.add(GREENRadioButton);
        colourGroup.add(BLUERadioButton);
        colourGroup.add(YELLOWRadioButton);
        colourGroup.add(PURPLERadioButton);

        DevCardPanelHandler devCardPanelHandler = new DevCardPanelHandler(frame, devCardPanel,
                devCardRequirementPanel, devCardProductionInPanel, devCardProductionOutPanel,
                devCardSelectionBox, devCardPointsTextField, removeDevCardButton, levelGroup, colourGroup);
        devCardPanelHandler.build();

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
