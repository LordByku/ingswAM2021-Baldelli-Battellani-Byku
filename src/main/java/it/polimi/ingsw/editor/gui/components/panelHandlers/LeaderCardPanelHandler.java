package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.LeaderCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.DevCard;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.LeaderCard;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.*;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.*;
import it.polimi.ingsw.model.devCards.CardLevel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;

public class LeaderCardPanelHandler extends PanelHandler {
    private final JPanel leaderCardRequirementsPanel;
    private final JPanel leaderCardEffectPanel;
    private final JComboBox leaderCardSelectionBox;
    private final JFormattedTextField leaderCardPointsTextField;
    private final JButton removeLeaderCardButton;
    private final ButtonGroup requirementsGroup;
    private final ButtonGroup effectGroup;
    private final LeaderCardsEditor leaderCardsEditor;

    public LeaderCardPanelHandler(JFrame frame, JPanel panel, JPanel leaderCardRequirementsPanel,
                                  JPanel leaderCardEffectPanel, JComboBox leaderCardSelectionBox,
                                  JFormattedTextField leaderCardPointsTextField, JButton removeLeaderCardButton,
                                  ButtonGroup requirementsGroup, ButtonGroup effectGroup) {
        super(frame, panel);
        this.leaderCardRequirementsPanel = leaderCardRequirementsPanel;
        this.leaderCardEffectPanel = leaderCardEffectPanel;
        this.leaderCardSelectionBox = leaderCardSelectionBox;
        this.leaderCardPointsTextField = leaderCardPointsTextField;
        this.removeLeaderCardButton = removeLeaderCardButton;
        this.requirementsGroup = requirementsGroup;
        this.effectGroup = effectGroup;
        leaderCardsEditor = Config.getInstance().getLeaderCardsEditor();
    }

    @Override
    public void build() {
        int selection = 0;

        buildComboBox(selection);
        loadCard(selection);
    }

    private void buildComboBox(int selection) {
        leaderCardSelectionBox.removeAllItems();
        ActionListener[] listeners = leaderCardSelectionBox.getActionListeners();
        for(ActionListener listener: listeners) {
            leaderCardSelectionBox.removeActionListener(listener);
        }

        ArrayList<LeaderCard> leaderCards = leaderCardsEditor.getLeaderCards();
        for (int i = 0; i < leaderCards.size(); i++) {
            leaderCardSelectionBox.addItem(i);
        }
        leaderCardSelectionBox.addItem("Add new card");

        leaderCardSelectionBox.setSelectedIndex(selection);

        leaderCardSelectionBox.addActionListener(e -> {
            int newSelection = leaderCardSelectionBox.getSelectedIndex();
            if(newSelection == leaderCards.size()) {
                leaderCardsEditor.addNewCard();
                buildComboBox(newSelection);
            } else if(newSelection != -1) {
                loadCard(newSelection);
            }
        });
    }

    private void loadCard(int selection) {
        LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(selection);

        MouseListener[] mouseListeners = removeLeaderCardButton.getMouseListeners();
        for(MouseListener mouseListener: mouseListeners) {
            if(mouseListener instanceof ButtonClickEvent) { // TODO: no instanceof ?
                removeLeaderCardButton.removeMouseListener(mouseListener);
            }
        }
        removeLeaderCardButton.addMouseListener(new ButtonClickEvent((e) -> {
            leaderCardsEditor.removeCard(selection);
            int newSelection = 0;
            buildComboBox(newSelection);
            loadCard(newSelection);
        }));

        ActionListener[] listeners = leaderCardPointsTextField.getActionListeners();
        for(ActionListener listener: listeners) {
            leaderCardPointsTextField.removeActionListener(listener);
        }
        leaderCardPointsTextField.setValue(leaderCard.getPoints());
        leaderCardPointsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                leaderCardPointsTextField, leaderCard::setPoints
        ));

        Enumeration<AbstractButton> requirementsButton = requirementsGroup.getElements();
        for(RequirementType requirementType: RequirementType.values()) {
            JRadioButton button = (JRadioButton) requirementsButton.nextElement();
            clearListeners(button);
            button.addMouseListener(new ButtonClickEvent((e) -> {
                if(leaderCard.getRequirements().getRequirementType() != requirementType) {
                    leaderCard.setRequirements(Requirements.build(requirementType));
                    loadCard(selection);
                }
            }));

            requirementsGroup.setSelected(button.getModel(), leaderCard.getRequirements().getRequirementType() == requirementType);
        }

        Enumeration<AbstractButton> effectButton = effectGroup.getElements();
        for(EffectType effectType: EffectType.values()) {
            JRadioButton button = (JRadioButton) effectButton.nextElement();
            clearListeners(button);
            button.addMouseListener(new ButtonClickEvent((e) -> {
                if(leaderCard.getEffect().getEffectType() != effectType) {
                    leaderCard.setEffect(Effect.build(effectType));
                    loadCard(selection);
                }
            }));

            effectGroup.setSelected(button.getModel(), leaderCard.getEffect().getEffectType() == effectType);
        }

        switch (leaderCard.getRequirements().getRequirementType()) {
            case cardSet: {
                CardSetPanelHandler cardSetPanelHandler = new CardSetPanelHandler(frame, leaderCardRequirementsPanel, (CardSetRequirements) leaderCard.getRequirements());
                cardSetPanelHandler.build();
                break;
            }
            case resources: {
                ConcretePanelHandler concretePanelHandler = new ConcretePanelHandler(frame, leaderCardRequirementsPanel, ((ResourcesRequirements) leaderCard.getRequirements()).getResources());
                concretePanelHandler.build();
                break;
            }
        }

        switch (leaderCard.getEffect().getEffectType()) {
            case discount: {
                DiscountEffect discountEffect = (DiscountEffect) leaderCard.getEffect();
                DiscountEffectPanelHandler discountEffectPanelHandler = new DiscountEffectPanelHandler(frame, leaderCardEffectPanel, discountEffect);
                discountEffectPanelHandler.build();
                break;
            }
            case depot: {
                DepotEffect depotEffect = (DepotEffect) leaderCard.getEffect();
                DepotEffectPanelHandler depotEffectPanelHandler = new DepotEffectPanelHandler(frame, leaderCardEffectPanel, depotEffect);
                depotEffectPanelHandler.build();
                break;
            }
            case conversion: {
                ConversionEffect conversionEffect = (ConversionEffect) leaderCard.getEffect();
                ConversionEffectPanelHandler conversionEffectPanelHandler = new ConversionEffectPanelHandler(frame, leaderCardEffectPanel, conversionEffect);
                conversionEffectPanelHandler.build();
                break;
            }
            case production: {
                leaderCardEffectPanel.removeAll();
                leaderCardEffectPanel.setLayout(new BoxLayout(leaderCardEffectPanel, BoxLayout.X_AXIS));

                JPanel spendablePanel = new JPanel();
                JPanel obtainablePanel = new JPanel();

                ProductionEffect productionEffect = (ProductionEffect) leaderCard.getEffect();

                leaderCardEffectPanel.add(spendablePanel);
                leaderCardEffectPanel.add(obtainablePanel);

                SpendablePanelHandler spendablePanelHandler = new SpendablePanelHandler(frame, spendablePanel, productionEffect.getProductionIn());
                spendablePanelHandler.build();

                ObtainablePanelHandler obtainablePanelHandler = new ObtainablePanelHandler(frame, obtainablePanel, productionEffect.getProductionOut());
                obtainablePanelHandler.build();

                break;
            }
        }

        frame.setVisible(true);
    }
}
