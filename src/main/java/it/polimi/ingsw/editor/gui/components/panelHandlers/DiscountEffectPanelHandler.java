package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.DiscountEffect;

import javax.swing.*;

public class DiscountEffectPanelHandler extends PanelHandler {
    private final DiscountEffect discountEffect;
    private ValidatableTextField quantityField;

    public DiscountEffectPanelHandler(JFrame frame, JPanel panel, DiscountEffect discountEffect) {
        super(frame, panel);
        this.discountEffect = discountEffect;
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
            EditorGUIUtil.addRadioButton(resource.getString(), discountEffect.getResource() == resource, buttonGroup, buttonsPanel, new ButtonClickEvent((e) -> {
                discountEffect.setResource(resource);
            }));
        }

        resourcePanel.add(buttonsPanel);


        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));

        EditorGUIUtil.addLabel("Discount:", quantityPanel);

        quantityField = EditorGUIUtil.addValidatableTextField(discountEffect.getDiscount(), quantityPanel,
                discountEffect::setDiscount, (value) -> value > 0 && value < 100);

        panel.add(resourcePanel);
        panel.add(quantityPanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        return quantityField.validate();
    }
}
