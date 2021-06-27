package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.LeaderCardsEditor;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.LeaderCard;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.*;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.CardSetRequirements;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.RequirementType;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.Requirements;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.requirements.ResourcesRequirements;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class LeaderCardPanelHandler extends PanelHandler {
    private final JPanel leaderCardRequirementsPanel;
    private final JPanel leaderCardEffectPanel;
    private final JComboBox leaderCardSelectionBox;
    private final JPanel leaderCardPointsPanel;
    private final JButton removeLeaderCardButton;
    private final ButtonGroup requirementsGroup;
    private final ButtonGroup effectGroup;
    private final LeaderCardsEditor leaderCardsEditor;
    private PanelHandler requirementsPanelHandler;
    private PanelHandler effectPanelHandler;
    private ValidatableTextField pointsField;

    public LeaderCardPanelHandler(JFrame frame, JPanel panel, JPanel leaderCardRequirementsPanel,
                                  JPanel leaderCardEffectPanel, JComboBox leaderCardSelectionBox,
                                  JPanel leaderCardPointsPanel, JButton removeLeaderCardButton,
                                  ButtonGroup requirementsGroup, ButtonGroup effectGroup) {
        super(frame, panel);
        this.leaderCardRequirementsPanel = leaderCardRequirementsPanel;
        this.leaderCardEffectPanel = leaderCardEffectPanel;
        this.leaderCardSelectionBox = leaderCardSelectionBox;
        this.leaderCardPointsPanel = leaderCardPointsPanel;
        this.removeLeaderCardButton = removeLeaderCardButton;
        this.requirementsGroup = requirementsGroup;
        this.effectGroup = effectGroup;
        leaderCardsEditor = Config.getInstance().getLeaderCardsEditor();

        leaderCardPointsPanel.setLayout(new GridBagLayout());
    }

    @Override
    public void build() {
        int selection = leaderCardsEditor.getCurrentSelection();

        leaderCardSelectionBox.addActionListener(e -> {
            if (validate()) {
                int newSelection = leaderCardSelectionBox.getSelectedIndex();
                if (newSelection == leaderCardsEditor.getLeaderCards().size()) {
                    leaderCardsEditor.addNewCard();
                    buildComboBox(newSelection);
                    loadCard(newSelection);
                } else if (newSelection != -1) {
                    leaderCardsEditor.setCurrentSelection(newSelection);
                    loadCard(newSelection);
                }
            } else {
                int newSelection = leaderCardsEditor.getCurrentSelection();
                buildComboBox(newSelection);
                loadCard(newSelection);
            }
        });

        removeLeaderCardButton.addActionListener(new ButtonClickEvent((e) -> {
            leaderCardsEditor.removeCard(leaderCardsEditor.getCurrentSelection());
            int newSelection = 0;
            leaderCardsEditor.setCurrentSelection(newSelection);
            buildComboBox(newSelection);
            loadCard(newSelection);
        }));

        Enumeration<AbstractButton> requirementsButton = requirementsGroup.getElements();
        for (RequirementType requirementType : RequirementType.values()) {
            requirementsButton.nextElement().addActionListener((e) -> {
                int currentSelection = leaderCardsEditor.getCurrentSelection();
                LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(currentSelection);
                if (leaderCard.getRequirements().getRequirementType() != requirementType) {
                    leaderCard.setRequirements(Requirements.build(requirementType));
                    loadCard(currentSelection);
                }
            });
        }

        Enumeration<AbstractButton> effectButton = effectGroup.getElements();
        for (EffectType effectType : EffectType.values()) {
            effectButton.nextElement().addActionListener((e) -> {
                int currentSelection = leaderCardsEditor.getCurrentSelection();
                LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(currentSelection);
                if (leaderCard.getEffect().getEffectType() != effectType) {
                    leaderCard.setEffect(Effect.build(effectType));
                    loadCard(currentSelection);
                }
            });
        }

        loadCard(selection);
        buildComboBox(selection);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        if (!requirementsPanelHandler.validate() || !effectPanelHandler.validate()) {
            result = false;
        }
        if (!pointsField.validate()) {
            result = false;
        }
        return result;
    }

    private void buildComboBox(int selection) {
        leaderCardSelectionBox.removeAllItems();

        ArrayList<LeaderCard> leaderCards = leaderCardsEditor.getLeaderCards();
        for (int i = 0; i < leaderCards.size(); i++) {
            leaderCardSelectionBox.addItem(i);
        }
        if (leaderCards.size() < 30) {
            leaderCardSelectionBox.addItem("Add new card");
        }

        leaderCardSelectionBox.setSelectedIndex(selection);
    }

    private void loadCard(int selection) {
        LeaderCard leaderCard = leaderCardsEditor.getLeaderCards().get(selection);

        leaderCardPointsPanel.removeAll();
        pointsField = EditorGUIUtil.addValidatableTextField(leaderCard.getPoints(), leaderCardPointsPanel, (value) -> {
            leaderCardsEditor.getLeaderCards().get(leaderCardsEditor.getCurrentSelection()).setPoints(value);
        }, (value) -> value > 0 && value < 100);

        Enumeration<AbstractButton> requirementsButton = requirementsGroup.getElements();
        for (RequirementType requirementType : RequirementType.values()) {
            requirementsGroup.setSelected(requirementsButton.nextElement().getModel(), leaderCard.getRequirements().getRequirementType() == requirementType);
        }

        Enumeration<AbstractButton> effectButton = effectGroup.getElements();
        for (EffectType effectType : EffectType.values()) {
            effectGroup.setSelected(effectButton.nextElement().getModel(), leaderCard.getEffect().getEffectType() == effectType);
        }

        switch (leaderCard.getRequirements().getRequirementType()) {
            case resources: {
                requirementsPanelHandler = new ConcretePanelHandler(frame, leaderCardRequirementsPanel, ((ResourcesRequirements) leaderCard.getRequirements()).getResources());
                break;
            }
            case cardSet: {
                requirementsPanelHandler = new CardSetPanelHandler(frame, leaderCardRequirementsPanel, (CardSetRequirements) leaderCard.getRequirements());
                break;
            }
        }

        requirementsPanelHandler.build();

        switch (leaderCard.getEffect().getEffectType()) {
            case discount: {
                DiscountEffect discountEffect = (DiscountEffect) leaderCard.getEffect();
                effectPanelHandler = new DiscountEffectPanelHandler(frame, leaderCardEffectPanel, discountEffect);
                break;
            }
            case depot: {
                DepotEffect depotEffect = (DepotEffect) leaderCard.getEffect();
                effectPanelHandler = new DepotEffectPanelHandler(frame, leaderCardEffectPanel, depotEffect);
                break;
            }
            case conversion: {
                ConversionEffect conversionEffect = (ConversionEffect) leaderCard.getEffect();
                effectPanelHandler = new ConversionEffectPanelHandler(frame, leaderCardEffectPanel, conversionEffect);
                break;
            }
            case production: {
                ProductionEffect productionEffect = (ProductionEffect) leaderCard.getEffect();
                effectPanelHandler = new ProductionEffectPanelHandler(frame, leaderCardEffectPanel, productionEffect);
                break;
            }
        }

        effectPanelHandler.build();

        frame.setVisible(true);
    }
}
