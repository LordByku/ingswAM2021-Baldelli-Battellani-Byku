package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.CardSetRequirements;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;
import java.util.ArrayList;

public class CardSetPanelHandler extends PanelHandler {
    private final CardSetRequirements cardSet;
    private ArrayList<ValidatableTextField> quantityFields;

    public CardSetPanelHandler(JFrame frame, JPanel panel, CardSetRequirements cardSet) {
        super(frame, panel);
        this.cardSet = cardSet;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        quantityFields = new ArrayList<>();

        JPanel colourPanel = new JPanel();
        colourPanel.setLayout(new BoxLayout(colourPanel, BoxLayout.Y_AXIS));

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.Y_AXIS));

        for (CardColour colour : CardColour.values()) {
            EditorGUIUtil.addLabel(colour.toString(), colourPanel);

            JPanel checkBoxesPanel = new JPanel();
            checkBoxesPanel.setLayout(new BoxLayout(checkBoxesPanel, BoxLayout.X_AXIS));

            ButtonGroup checkBoxesButtonGroup = new ButtonGroup();

            for (CardLevel level : CardLevel.values()) {
                EditorGUIUtil.addCheckBox(level.toString(), cardSet.getCardSet(colour).getCardLevel() == level,
                        checkBoxesButtonGroup, checkBoxesPanel, new ButtonClickEvent((e) -> {
                            cardSet.getCardSet(colour).toggle(level);
                        }));
            }

            levelPanel.add(checkBoxesPanel);

            quantityFields.add(EditorGUIUtil.addValidatableTextField(
                    cardSet.getCardSet(colour).getQuantity(), quantityPanel, (value) -> {
                        cardSet.getCardSet(colour).setQuantity(value);
                    }, value -> value >= 0 && value < 100
            ));
        }

        panel.add(colourPanel);
        panel.add(levelPanel);
        panel.add(quantityPanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        for (ValidatableTextField validatableTextField : quantityFields) {
            if (!validatableTextField.validate()) {
                result = false;
            }
        }
        return result;
    }
}
