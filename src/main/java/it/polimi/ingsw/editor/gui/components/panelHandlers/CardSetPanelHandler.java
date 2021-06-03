package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.LeaderCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.CardSetRequirements;
import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;

public class CardSetPanelHandler extends PanelHandler {
    private final LeaderCardsEditor leaderCardsEditor;
    private final CardSetRequirements cardSet;

    public CardSetPanelHandler(JFrame frame, JPanel panel, CardSetRequirements cardSet) {
        super(frame, panel);
        this.cardSet = cardSet;
        leaderCardsEditor = Config.getInstance().getLeaderCardsEditor();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel colourPanel = new JPanel();
        colourPanel.setLayout(new BoxLayout(colourPanel, BoxLayout.Y_AXIS));

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.Y_AXIS));

        // TODO: remove local arrays
        String[] coloursText = {"GREEN", "BLUE", "YELLOW", "PURPLE"};
        String[] levelsText = {"I", "II", "III"};

        for(int i = 0; i < CardColour.values().length; ++i) {
            addLabel(coloursText[i], colourPanel);

            CardColour colour = CardColour.values()[i];

            JPanel checkBoxesPanel = new JPanel();
            checkBoxesPanel.setLayout(new BoxLayout(checkBoxesPanel, BoxLayout.X_AXIS));

            for(int j = 0; j < CardLevel.values().length; ++j) {
                CardLevel level = CardLevel.values()[j];
                addCheckBox(levelsText[j], cardSet.getCardSet(colour).isActive(level), checkBoxesPanel, new ButtonClickEvent((e) -> {
                    cardSet.getCardSet(colour).toggle(level);
                }));
            }

            levelPanel.add(checkBoxesPanel);

            addTextField(cardSet.getCardSet(colour).getQuantity(), quantityPanel, (value) -> {
                cardSet.getCardSet(colour).setQuantity(value);
            });
        }

        panel.add(colourPanel);
        panel.add(levelPanel);
        panel.add(quantityPanel);

        frame.setVisible(true);
    }
}
