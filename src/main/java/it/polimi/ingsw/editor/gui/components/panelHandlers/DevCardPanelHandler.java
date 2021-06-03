package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.DevCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.DevCard;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
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
        int selection = 0;

        buildComboBox(selection);
        loadCard(selection);
    }

    private void buildComboBox(int selection) {
        devCardSelectionBox.removeAllItems();
        ActionListener[] listeners = devCardSelectionBox.getActionListeners();
        for(ActionListener listener: listeners) {
            devCardSelectionBox.removeActionListener(listener);
        }

        ArrayList<DevCard> devCards = devCardsEditor.getDevCards();
        for (int i = 0; i < devCards.size(); i++) {
            devCardSelectionBox.addItem(i);
        }
        devCardSelectionBox.addItem("Add new card");

        devCardSelectionBox.setSelectedIndex(selection);

        devCardSelectionBox.addActionListener(e -> {
            int newSelection = devCardSelectionBox.getSelectedIndex();
            if(newSelection == devCards.size()) {
                devCardsEditor.addNewCard();
                buildComboBox(newSelection);
            } else if(newSelection != -1) {
                loadCard(newSelection);
            }
        });
    }

    private void loadCard(int selection) {
        DevCard devCard = devCardsEditor.getDevCards().get(selection);

        MouseListener[] mouseListeners = removeDevCardButton.getMouseListeners();
        for(MouseListener mouseListener: mouseListeners) {
            if(mouseListener instanceof ButtonClickEvent) { // TODO: no instanceof ?
                removeDevCardButton.removeMouseListener(mouseListener);
            }
        }
        removeDevCardButton.addMouseListener(new ButtonClickEvent((e) -> {
            devCardsEditor.removeCard(selection);
            int newSelection = 0;
            buildComboBox(newSelection);
            loadCard(newSelection);
        }));

        ActionListener[] listeners = devCardPointsTextField.getActionListeners();
        for(ActionListener listener: listeners) {
            devCardPointsTextField.removeActionListener(listener);
        }
        devCardPointsTextField.setValue(devCard.getPoints());
        devCardPointsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                devCardPointsTextField, devCard::setPoints
        ));

        Enumeration<AbstractButton> levelButtons = levelGroup.getElements();
        for(CardLevel cardLevel: CardLevel.values()) {
            JRadioButton button = (JRadioButton) levelButtons.nextElement();
            clearListeners(button);
            button.addMouseListener(new ButtonClickEvent((e) -> {
                devCard.setLevel(cardLevel);
            }));

            levelGroup.setSelected(button.getModel(), devCard.getLevel() == cardLevel);
        }

        Enumeration<AbstractButton> colourButtons = colourGroup.getElements();
        for(CardColour cardColour: CardColour.values()) {
            JRadioButton button = (JRadioButton) colourButtons.nextElement();
            clearListeners(button);
            button.addMouseListener(new ButtonClickEvent((e) -> {
                devCard.setColour(cardColour);
            }));

            colourGroup.setSelected(button.getModel(), devCard.getColour() == cardColour);
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
