package it.polimi.ingsw.editor.gui;

import it.polimi.ingsw.editor.EditorApp;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.gui.components.panelHandlers.*;
import it.polimi.ingsw.editor.model.*;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Window {
    private JPanel checkPointsPanel;
    private JPanel panel;
    private JPanel VRSPanel;
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
    private JPanel faithTrackLengthPanel;
    private JPanel developmentCardsSlotsPanel;
    private JPanel leaderCardsToAssignPanel;
    private JPanel leaderCardsToDiscardPanel;
    private JPanel initResources1Panel;
    private JPanel initResources2Panel;
    private JPanel initResources3Panel;
    private JPanel initResources4Panel;
    private JPanel initFaithPoints1Panel;
    private JPanel initFaithPoints2Panel;
    private JPanel initFaithPoints3Panel;
    private JPanel initFaithPoints4Panel;
    private final ArrayList<ValidatableTextField> validatableTextFields;
    private final ArrayList<PanelHandler> panelHandlers;

    public Window(JFrame frame) {
        loadDefaultConfigButton.addActionListener(new ButtonClickEvent((event) -> {
            Config.setDefaultPath();
            Config.reload();
            EditorApp.setWindow(new Window(frame));
        }));

        loadCustomConfigButton.addActionListener(new ButtonClickEvent((event) -> {
            try {
                Config.setPath(loadCustomConfigTextField.getText());
                Config.reload();
                EditorApp.setWindow(new Window(frame));
            } catch (FileNotFoundException fileNotFoundException) {
                fileErrorLabel.setText("File not found");
            }
        }));

        saveCurrentConfigButton.addActionListener(new ButtonClickEvent((event) -> {
            try {
                if (validate()) {
                    Config.getInstance().save(saveConfigTextField.getText());
                    fileErrorLabel.setText("Config file saved successfully");
                } else {
                    fileErrorLabel.setText("Some parameters are invalid, fix them first");
                }
            } catch (IOException ioException) {
                fileErrorLabel.setText("An error occurred while saving the file");
            }
        }));

        validatableTextFields = new ArrayList<>();
        panelHandlers = new ArrayList<>();

        FaithTrackEditor faithTrackEditor = Config.getInstance().getFaithTrackEditor();
        faithTrackLengthPanel.setLayout(new GridBagLayout());
        validatableTextFields.add(EditorGUIUtil.addValidatableTextField(faithTrackEditor.getFinalPosition(),
                faithTrackLengthPanel,
                faithTrackEditor::setFinalPosition,
                (value) -> value > 0 && value < 30));

        CheckPointsPanelHandler checkPointsPanelHandler = new CheckPointsPanelHandler(frame, checkPointsPanel);
        checkPointsPanelHandler.build();
        panelHandlers.add(checkPointsPanelHandler);

        VRSPanelHandler vrsPanelHandler = new VRSPanelHandler(frame, VRSPanel);
        vrsPanelHandler.build();
        panelHandlers.add(vrsPanelHandler);

        BoardEditor boardEditor = Config.getInstance().getBoardEditor();
        SpendablePanelHandler productionInPanelHandler = new SpendablePanelHandler(frame, defaultProductionInPanel, boardEditor.getDefaultProductionIn());
        productionInPanelHandler.build();
        ObtainablePanelHandler productionOutPanelHandler = new ObtainablePanelHandler(frame, defaultProductionOutPanel, boardEditor.getDefaultProductionOut());
        productionOutPanelHandler.build();
        DepotsPanelHandler depotsPanelHandler = new DepotsPanelHandler(frame, depotsPanel);
        depotsPanelHandler.build();
        panelHandlers.add(productionInPanelHandler);
        panelHandlers.add(productionOutPanelHandler);
        panelHandlers.add(depotsPanelHandler);

        developmentCardsSlotsPanel.setLayout(new GridBagLayout());
        validatableTextFields.add(EditorGUIUtil.addValidatableTextField(boardEditor.getDevelopmentCardSlots(),
                developmentCardsSlotsPanel,
                boardEditor::setDevelopmentCardSlots,
                (value) -> value > 0 && value <= 5));

        InitGameEditor initGameEditor = Config.getInstance().getInitGameEditor();
        LeaderCardsEditor leaderCardsEditor = Config.getInstance().getLeaderCardsEditor();

        leaderCardsToAssignPanel.setLayout(new GridBagLayout());
        leaderCardsToDiscardPanel.setLayout(new GridBagLayout());
        validatableTextFields.add(EditorGUIUtil.addValidatableTextField(initGameEditor.getLeaderCardsToAssign(),
                leaderCardsToAssignPanel,
                initGameEditor::setLeaderCardsToAssign,
                (value) -> value > 0 && 4 * value <= leaderCardsEditor.getLeaderCards().size()));
        validatableTextFields.add(EditorGUIUtil.addValidatableTextField(initGameEditor.getLeaderCardsToDiscard(),
                leaderCardsToDiscardPanel,
                initGameEditor::setLeaderCardsToDiscard,
                (value) -> value >= 0 && value < initGameEditor.getLeaderCardsToAssign()));

        JPanel[] initFaithPointsPanels = new JPanel[4];
        initFaithPointsPanels[0] = initFaithPoints1Panel;
        initFaithPointsPanels[1] = initFaithPoints2Panel;
        initFaithPointsPanels[2] = initFaithPoints3Panel;
        initFaithPointsPanels[3] = initFaithPoints4Panel;

        JPanel[] initResourcesPanels = new JPanel[4];
        initResourcesPanels[0] = initResources1Panel;
        initResourcesPanels[1] = initResources2Panel;
        initResourcesPanels[2] = initResources3Panel;
        initResourcesPanels[3] = initResources4Panel;

        for(int i = 0; i < 4; ++i) {
            int finalI = i;
            initFaithPointsPanels[i].setLayout(new GridBagLayout());
            initResourcesPanels[i].setLayout(new GridBagLayout());
            validatableTextFields.add(EditorGUIUtil.addValidatableTextField(initGameEditor.getFaithPoints(i),
                    initFaithPointsPanels[i],
                    (value) -> initGameEditor.setFaithPoints(finalI, value),
                    (value) -> value >= 0 && value < faithTrackEditor.getFinalPosition()));
            validatableTextFields.add(EditorGUIUtil.addValidatableTextField(initGameEditor.getResources(i),
                    initResourcesPanels[i],
                    (value) -> initGameEditor.setResources(finalI, value),
                    (value) -> {
                        int warehouseSize = 0;
                        for(int depotSize: boardEditor.getDepotSizes()) {
                            warehouseSize += depotSize;
                        }
                        return value >= 0 && value <= warehouseSize;
                    }));
        }

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
        panelHandlers.add(devCardPanelHandler);

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
        panelHandlers.add(leaderCardPanelHandler);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private boolean validate() {
        boolean result = true;
        for(ValidatableTextField validatableTextField: validatableTextFields) {
            if(!validatableTextField.validate()) {
                result = false;
            }
        }
        for(PanelHandler panelHandler: panelHandlers) {
            if(!panelHandler.validate()) {
                result = false;
            }
        }
        return result;
    }
}
