package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.DiscountEffect;

import javax.swing.*;

public class DiscountEffectPanelHandler extends PanelHandler {
    private final DiscountEffect discountEffect;

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

        addLabel("Resource:", resourcePanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        ButtonGroup buttonGroup = new ButtonGroup();

        for(ConcreteResource resource: ConcreteResource.values()) {
            addRadioButton(resource.getString(), discountEffect.getResource() == resource, buttonGroup, buttonsPanel, new ButtonClickEvent((e) -> {
                discountEffect.setResource(resource);
            }));
        }

        resourcePanel.add(buttonsPanel);


        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));

        addLabel("Discount:", quantityPanel);

        addTextField(discountEffect.getDiscount(), quantityPanel, discountEffect::setDiscount);

        panel.add(resourcePanel);
        panel.add(quantityPanel);

        frame.setVisible(true);
    }
}
