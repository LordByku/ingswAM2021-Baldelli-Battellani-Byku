package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.DevCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.DevCard;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class DevCardPanelHandler extends PanelHandler {
    private final JPanel devCardRequirementPanel;
    private final JPanel devCardProductionInPanel;
    private final JPanel devCardProductionOutPanel;
    private final JComboBox devCardSelectionBox;
    private final JFormattedTextField devCardPointsTextField;
    private final JButton removeDevCardButton;
    private final ButtonGroup levelGroup;
    private final ButtonGroup colourGroup;
    private final DevCardsEditor devCardsEditor;

    public DevCardPanelHandler(JFrame frame, JPanel panel, JPanel devCardRequirementPanel,
                               JPanel devCardProductionInPanel, JPanel devCardProductionOutPanel,
                               JComboBox devCardSelectionBox, JFormattedTextField devCardPointsTextField,
                               JButton removeDevCardButton, ButtonGroup levelGroup, ButtonGroup colourGroup) {
        super(frame, panel);
        this.devCardRequirementPanel = devCardRequirementPanel;
        this.devCardProductionInPanel = devCardProductionInPanel;
        this.devCardProductionOutPanel = devCardProductionOutPanel;
        this.devCardSelectionBox = devCardSelectionBox;
        this.devCardPointsTextField = devCardPointsTextField;
        this.removeDevCardButton = removeDevCardButton;
        this.levelGroup = levelGroup;
        this.colourGroup = colourGroup;

        devCardsEditor = Config.getInstance().getDevCardsEditor();
    }

    @Override
    public void build() {
        int selection = devCardsEditor.getCurrentSelection();

        devCardSelectionBox.addActionListener(e -> {
            int newSelection = devCardSelectionBox.getSelectedIndex();
            if(newSelection == devCardsEditor.getDevCards().size()) {
                devCardsEditor.addNewCard();
                buildComboBox(newSelection);
                loadCard(newSelection);
            } else if(newSelection != -1) {
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

        devCardPointsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(devCardPointsTextField, (value) -> {
            devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setPoints(value);
        }));

        Enumeration<AbstractButton> levelButtons = levelGroup.getElements();
        for(CardLevel cardLevel: CardLevel.values()) {
            levelButtons.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setLevel(cardLevel);
            }));
        }

        Enumeration<AbstractButton> colourButtons = colourGroup.getElements();
        for(CardColour cardColour: CardColour.values()) {
            colourButtons.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                devCardsEditor.getDevCards().get(devCardsEditor.getCurrentSelection()).setColour(cardColour);
            }));
        }

        buildComboBox(selection);
        loadCard(selection);
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

        devCardPointsTextField.setValue(devCard.getPoints());

        Enumeration<AbstractButton> levelButtons = levelGroup.getElements();
        for(CardLevel cardLevel: CardLevel.values()) {
            levelGroup.setSelected(levelButtons.nextElement().getModel(), devCard.getLevel() == cardLevel);
        }

        Enumeration<AbstractButton> colourButtons = colourGroup.getElements();
        for(CardColour cardColour: CardColour.values()) {
            colourGroup.setSelected(colourButtons.nextElement().getModel(), devCard.getColour() == cardColour);
        }

        ConcretePanelHandler concretePanelHandler = new ConcretePanelHandler(frame, devCardRequirementPanel, devCard.getRequirements());
        concretePanelHandler.build();

        SpendablePanelHandler spendablePanelHandler = new SpendablePanelHandler(frame, devCardProductionInPanel, devCard.getProductionIn());
        spendablePanelHandler.build();

        ObtainablePanelHandler obtainablePanelHandler = new ObtainablePanelHandler(frame, devCardProductionOutPanel, devCard.getProductionOut());
        obtainablePanelHandler.build();

        frame.setVisible(true);
    }
}
