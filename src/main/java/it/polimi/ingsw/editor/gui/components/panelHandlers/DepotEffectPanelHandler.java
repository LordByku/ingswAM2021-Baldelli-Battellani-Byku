package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.DepotEffect;

import javax.swing.*;

public class DepotEffectPanelHandler extends PanelHandler {
    private final DepotEffect depotEffect;

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

        addLabel("Resource:", resourcePanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        ButtonGroup buttonGroup = new ButtonGroup();

        for(ConcreteResource resource: ConcreteResource.values()) {
            addRadioButton(resource.getString(), depotEffect.getResource() == resource, buttonGroup, buttonsPanel, new ButtonClickEvent((e) -> {
                depotEffect.setResource(resource);
            }));
        }

        resourcePanel.add(buttonsPanel);


        JPanel slotsPanel = new JPanel();
        slotsPanel.setLayout(new BoxLayout(slotsPanel, BoxLayout.X_AXIS));

        addLabel("Depot slots:", slotsPanel);

        addTextField(depotEffect.getSlots(), slotsPanel, depotEffect::setSlots);

        panel.add(resourcePanel);
        panel.add(slotsPanel);

        frame.setVisible(true);
    }
}
