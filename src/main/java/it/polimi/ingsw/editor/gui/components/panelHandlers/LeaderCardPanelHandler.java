package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.TextFieldDocumentListener;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.LeaderCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.LeaderCard;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.*;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.CardSetRequirements;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.RequirementType;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.Requirements;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.ResourcesRequirements;

import javax.swing.*;
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
        int selection = leaderCardsEditor.getCurrentSelection();

        leaderCardSelectionBox.addActionListener(e -> {
            int newSelection = leaderCardSelectionBox.getSelectedIndex();
            if(newSelection == leaderCardsEditor.getLeaderCards().size()) {
                leaderCardsEditor.addNewCard();
                buildComboBox(newSelection);
            } else if(newSelection != -1) {
                leaderCardsEditor.setCurrentSelection(newSelection);
                loadCard(newSelection);
            }
        });

        removeLeaderCardButton.addMouseListener(new ButtonClickEvent((e) -> {
            leaderCardsEditor.removeCard(leaderCardsEditor.getCurrentSelection());
            int newSelection = 0;
            leaderCardsEditor.setCurrentSelection(newSelection);
            buildComboBox(newSelection);
            loadCard(newSelection);
        }));

        leaderCardPointsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(leaderCardPointsTextField, (value) -> {
            leaderCardsEditor.getLeaderCards().get(leaderCardsEditor.getCurrentSelection()).setPoints(value);
        }));

        Enumeration<AbstractButton> requirementsButton = requirementsGroup.getElements();
        for(RequirementType requirementType: RequirementType.values()) {
            requirementsButton.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                int currentSelection = leaderCardsEditor.getCurrentSelection();
                LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(currentSelection);
                if(leaderCard.getRequirements().getRequirementType() != requirementType) {
                    leaderCard.setRequirements(Requirements.build(requirementType));
                    loadCard(currentSelection);
                }
            }));
        }

        Enumeration<AbstractButton> effectButton = effectGroup.getElements();
        for(EffectType effectType: EffectType.values()) {
            effectButton.nextElement().addMouseListener(new ButtonClickEvent((e) -> {
                int currentSelection = leaderCardsEditor.getCurrentSelection();
                LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(currentSelection);
                if(leaderCard.getEffect().getEffectType() != effectType) {
                    leaderCard.setEffect(Effect.build(effectType));
                    loadCard(currentSelection);
                }
            }));
        }

        buildComboBox(selection);
        loadCard(selection);
    }

    private void buildComboBox(int selection) {
        leaderCardSelectionBox.removeAllItems();

        ArrayList<LeaderCard> leaderCards = leaderCardsEditor.getLeaderCards();
        for (int i = 0; i < leaderCards.size(); i++) {
            leaderCardSelectionBox.addItem(i);
        }
        leaderCardSelectionBox.addItem("Add new card");

        leaderCardSelectionBox.setSelectedIndex(selection);
    }

    private void loadCard(int selection) {
        LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(selection);

        leaderCardPointsTextField.setValue(leaderCard.getPoints());

        Enumeration<AbstractButton> requirementsButton = requirementsGroup.getElements();
        for(RequirementType requirementType: RequirementType.values()) {
            requirementsGroup.setSelected(requirementsButton.nextElement().getModel(), leaderCard.getRequirements().getRequirementType() == requirementType);
        }

        Enumeration<AbstractButton> effectButton = effectGroup.getElements();
        for(EffectType effectType: EffectType.values()) {
            effectGroup.setSelected(effectButton.nextElement().getModel(), leaderCard.getEffect().getEffectType() == effectType);
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
