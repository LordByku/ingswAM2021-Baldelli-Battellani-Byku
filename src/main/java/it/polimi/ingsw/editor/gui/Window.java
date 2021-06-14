package it.polimi.ingsw.editor.gui;

import it.polimi.ingsw.editor.EditorApp;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.gui.components.panelHandlers.*;
import it.polimi.ingsw.editor.model.BoardEditor;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.editor.model.InitGameEditor;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private JPanel devCardRequirementPanel;
    private JPanel devCardProductionInPanel;
    private JPanel devCardProductionOutPanel;
    private JPanel devCardPanel;
    private JButton removeDevCardButton;
    private JComboBox leaderCardSelectionBox;
    private JButton removeLeaderCardButton;
    private JRadioButton resourcesRadioButton;
    private JRadioButton devCardsRadioButton;
    private JRadioButton developmentCardDiscountRadioButton;
    private JRadioButton productionPowerRadioButton;
    private JRadioButton whiteMarbleConversionRadioButton;
    private JRadioButton extraDepotRadioButton;
    private JPanel leaderCardRequirementsPanel;
    private JPanel leaderCardEffectPanel;
    private JPanel leaderCardPanel;
    private JButton loadDefaultConfigButton;
    private JButton loadCustomConfigButton;
    private JButton saveCurrentConfigButton;
    private JFormattedTextField saveConfigTextField;
    private JFormattedTextField loadCustomConfigTextField;
    private JLabel fileErrorLabel;
    private JPanel devCardPointsPanel;
    private JPanel leaderCardPointsPanel;

    public Window(JFrame frame) {
        loadDefaultConfigButton.addMouseListener(new ButtonClickEvent((event) -> {
            Config.setDefaultPath();
            Config.reload();
            EditorApp.setWindow(new Window(frame));
        }));

        loadCustomConfigButton.addMouseListener(new ButtonClickEvent((event) -> {
            try {
                Config.setPath(loadCustomConfigTextField.getText());
                Config.reload();
                EditorApp.setWindow(new Window(frame));
            } catch (FileNotFoundException fileNotFoundException) {
                fileErrorLabel.setText("File not found");
            }
        }));

        saveCurrentConfigButton.addMouseListener(new ButtonClickEvent((event) -> {
            try {
                Config.getInstance().save(saveConfigTextField.getText());
            } catch (IOException ioException) {
                fileErrorLabel.setText("An error occurred while saving the file");
            }
        }));

        // TODO : replace with validatable text field
        FaithTrackEditor faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        faithTrackLengthTextField.setValue(faithTrackEditor.getFinalPosition());
        faithTrackLengthTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                faithTrackLengthTextField, (value) -> faithTrackEditor.setFinalPosition(Integer.parseInt(value))
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

        // TODO : replace with validatable text field
        developmentCardSlotsTextField.setValue(boardEditor.getDevelopmentCardSlots());
        developmentCardSlotsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                developmentCardSlotsTextField, (value) -> boardEditor.setDevelopmentCardSlots(Integer.parseInt(value))
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

        // TODO : replace with validatable text fields
        leaderCardsToAssignTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                leaderCardsToAssignTextField, (value) -> initGameEditor.setLeaderCardsToAssign(Integer.parseInt(value))
        ));
        leaderCardsToDiscardTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                leaderCardsToDiscardTextField, (value) -> initGameEditor.setLeaderCardsToDiscard(Integer.parseInt(value))
        ));
        initFaithPoints1.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints1, (value) -> initGameEditor.setFaithPoints(0, Integer.parseInt(value))
        ));
        initFaithPoints2.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints2, (value) -> initGameEditor.setFaithPoints(1, Integer.parseInt(value))
        ));
        initFaithPoints3.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints3, (value) -> initGameEditor.setFaithPoints(2, Integer.parseInt(value))
        ));
        initFaithPoints4.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initFaithPoints4, (value) -> initGameEditor.setFaithPoints(3, Integer.parseInt(value))
        ));
        initResources1.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources1, (value) -> initGameEditor.setResources(0, Integer.parseInt(value))
        ));
        initResources2.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources2, (value) -> initGameEditor.setResources(1, Integer.parseInt(value))
        ));
        initResources3.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources3, (value) -> initGameEditor.setResources(2, Integer.parseInt(value))
        ));
        initResources4.getDocument().addDocumentListener(new TextFieldDocumentListener(
                initResources4, (value) -> initGameEditor.setResources(3, Integer.parseInt(value))
        ));

        // TODO : Radio Buttons: set listener to state change instead of mouse clicked

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
                devCardSelectionBox, devCardPointsPanel, removeDevCardButton, levelGroup, colourGroup);
        devCardPanelHandler.build();

        ButtonGroup requirementsGroup = new ButtonGroup();
        requirementsGroup.add(resourcesRadioButton);
        requirementsGroup.add(devCardsRadioButton);

        ButtonGroup effectGroup = new ButtonGroup();
        effectGroup.add(developmentCardDiscountRadioButton);
        effectGroup.add(extraDepotRadioButton);
        effectGroup.add(whiteMarbleConversionRadioButton);
        effectGroup.add(productionPowerRadioButton);

        LeaderCardPanelHandler leaderCardPanelHandler = new LeaderCardPanelHandler(frame, leaderCardPanel,
                leaderCardRequirementsPanel, leaderCardEffectPanel, leaderCardSelectionBox,
                leaderCardPointsPanel, removeLeaderCardButton, requirementsGroup, effectGroup);
        leaderCardPanelHandler.build();

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
