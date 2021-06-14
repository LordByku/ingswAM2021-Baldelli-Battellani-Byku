package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.ConversionEffect;
import it.polimi.ingsw.view.gui.components.ButtonClickEvent;

import javax.swing.*;

public class ConversionEffectPanelHandler extends PanelHandler {
    private final ConversionEffect conversionEffect;

    public ConversionEffectPanelHandler(JFrame frame, JPanel panel, ConversionEffect conversionEffect) {
        super(frame, panel);
        this.conversionEffect = conversionEffect;
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
            EditorGUIUtil.addRadioButton(resource.getString(), conversionEffect.getResource() == resource, buttonGroup, buttonsPanel, new ButtonClickEvent((e) -> {
                conversionEffect.setResource(resource);
            }));
        }

        resourcePanel.add(buttonsPanel);

        panel.add(resourcePanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
