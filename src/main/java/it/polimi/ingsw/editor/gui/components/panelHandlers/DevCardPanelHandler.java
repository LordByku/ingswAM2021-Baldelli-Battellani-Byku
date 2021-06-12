package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.DevCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.DevCard;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class DevCardPanelHandler extends PanelHandler {
    private final JPanel devCardRequirementPanel;
    private final JPanel devCardProductionInPanel;
    private final JPanel devCardProductionOutPanel;
    private final JComboBox devCardSelectionBox;
    private final JPanel devCardPointsPanel;
    private final JButton removeDevCardButton;
    private final ButtonGroup levelGroup;
    private final ButtonGroup colourGroup;
    private final DevCardsEditor devCardsEditor;
    private ValidatableTextField pointsField;
    private ConcretePanelHandler requirementsPanelHandler;
    private SpendablePanelHandler productionInPanelHandler;
    private ObtainablePanelHandler productionOutPanelHandler;

    public DevCardPanelHandler(JFrame frame, JPanel panel, JPanel devCardRequirementPanel,
                               JPanel devCardProductionInPanel, JPanel devCardProductionOutPanel,
                               JComboBox devCardSelectionBox, JPanel devCardPointsPanel,
                               JButton removeDevCardButton, ButtonGroup levelGroup, ButtonGroup colourGroup) {
        super(frame, panel);
        this.devCardRequirementPanel = devCardRequirementPanel;
        this.devCardProductionInPanel = devCardProductionInPanel;
        this.devCardProductionOutPanel = devCardProductionOutPanel;
        this.devCardSelectionBox = devCardSelectionBox;
        this.devCardPointsPanel = devCardPointsPanel;
        this.removeDevCardButton = removeDevCardButton;
        this.levelGroup = levelGroup;
        this.colourGroup = colourGroup;

        devCardsEditor = Config.getInstance().getDevCardsEditor();
        devCardPointsPanel.setLayout(new GridBagLayout());
    }

    @Override
    public void build() {
        int selection = devCardsEditor.getCurrentSelection();

        devCardSelectionBox.addActionListener(e -> {
            int newSelection = devCardSelectionBox.getSelectedIndex();
            if (newSelection == devCardsEditor.getDevCards().size()) {
                devCardsEditor.addNewCard();
                buildComboBox(newSelection);
                loadCard(newSelection);
            } else if (newSelection != -1) {
                devCardsEditor.setCurrentSelection(newSelection);
                loadCard(newSelection);
            }
        });

        removeDevCardButton.addMouseListener(new ButtonClickEvent((e) -> {
            devCardsEditor.removeCard(devCardsEditor.getCurrentSelection());
            int newSelection = 0;
            devCardsEditor.setCurrentSelection(newSelection);
            buildComboBox(newSelection);
            loadCard(newSelection);
        }));

        Enumeration<AbstractButton> levelButtons = levelGroup.getElements();
        for (CardLevel cardLevel : CardLevel.values()) {
            levelButtons.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setLevel(cardLevel);
            }));
        }

        Enumeration<AbstractButton> colourButtons = colourGroup.getElements();
        for (CardColour cardColour : CardColour.values()) {
            colourButtons.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setColour(cardColour);
            }));
        }

        buildComboBox(selection);
        loadCard(selection);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        if (!pointsField.validate()) {
            result = false;
        }
        if (!requirementsPanelHandler.validate()) {
            result = false;
        }
        if (!productionInPanelHandler.validate() || !productionOutPanelHandler.validate()) {
            result = false;
        }
        return result;
    }

    private void buildComboBox(int selection) {
        devCardSelectionBox.removeAllItems();

        ArrayList<DevCard> devCards = devCardsEditor.getDevCards();
        for (int i = 0; i < devCards.size(); i++) {
            devCardSelectionBox.addItem(i);
        }
        devCardSelectionBox.addItem("Add new card");

        devCardSelectionBox.setSelectedIndex(selection);
    }

    private void loadCard(int selection) {
        DevCard devCard = devCardsEditor.getDevCards().get(selection);

        devCardPointsPanel.removeAll();
        pointsField = EditorGUIUtil.addValidatableTextField(devCard.getPoints(), devCardPointsPanel, (value) -> {
            devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setPoints(value);
        }, (value) -> value > 0 && value < 100);

        Enumeration<AbstractButton> levelButtons = levelGroup.getElements();
        for (CardLevel cardLevel : CardLevel.values()) {
            levelGroup.setSelected(levelButtons.nextElement().getModel(), devCard.getLevel() == cardLevel);
        }

        Enumeration<AbstractButton> colourButtons = colourGroup.getElements();
        for (CardColour cardColour : CardColour.values()) {
            colourGroup.setSelected(colourButtons.nextElement().getModel(), devCard.getColour() == cardColour);
        }

        requirementsPanelHandler = new ConcretePanelHandler(frame, devCardRequirementPanel, devCard.getRequirements());
        requirementsPanelHandler.build();

        productionInPanelHandler = new SpendablePanelHandler(frame, devCardProductionInPanel, devCard.getProductionIn());
        productionInPanelHandler.build();

        productionOutPanelHandler = new ObtainablePanelHandler(frame, devCardProductionOutPanel, devCard.getProductionOut());
        productionOutPanelHandler.build();

        frame.setVisible(true);
    }
}
