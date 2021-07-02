package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.DepotEffect;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;

public class DepotEffectPanelHandler extends PanelHandler {
    private final DepotEffect depotEffect;
    private ValidatableTextField slotsField;

    public DepotEffectPanelHandler(JFrame frame, JPanel panel, DepotEffect depotEffect) {
        super(frame, panel);
        this.depotEffect = depotEffect;
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel resourcePanel = new JPanel();
        resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.X_AXIS));

        EditorGUIUtil.addLabel("Resource:", resourcePanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        ButtonGroup buttonGroup = new ButtonGroup();

        for (ConcreteResource resource : ConcreteResource.values()) {
            EditorGUIUtil.addRadioButton(resource.getString(), depotEffect.getResource() == resource, buttonGroup, buttonsPanel, new ButtonClickEvent((e) -> {
                depotEffect.setResource(resource);
            }));
        }

        resourcePanel.add(buttonsPanel);

        JPanel slotsPanel = new JPanel();
        slotsPanel.setLayout(new BoxLayout(slotsPanel, BoxLayout.X_AXIS));

        EditorGUIUtil.addLabel("Depot slots:", slotsPanel);

        slotsField = EditorGUIUtil.addValidatableTextField(depotEffect.getSlots(), slotsPanel,
                depotEffect::setSlots, (value) -> value > 0 && value < 5);

        panel.add(resourcePanel);
        panel.add(slotsPanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        return slotsField.validate();
    }
}
