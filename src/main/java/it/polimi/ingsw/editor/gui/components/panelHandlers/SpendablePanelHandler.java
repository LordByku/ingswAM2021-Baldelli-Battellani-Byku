package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.model.resources.SpendableResource;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;

import javax.swing.*;

public class SpendablePanelHandler extends PanelHandler {
    private final SpendableResourceSet spendable;

    public SpendablePanelHandler(JFrame frame, JPanel panel, SpendableResourceSet spendable) {
        super(frame, panel);
        this.spendable = spendable;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));

        for(SpendableResource resource: SpendableResource.values()) {
            addLabel(resource.getString(), labelPanel);
            addTextField(spendable.getQuantity(resource), textFieldPanel, (value) -> spendable.updateQuantity(resource, value));
        }

        panel.add(labelPanel);
        panel.add(textFieldPanel);

        frame.setVisible(true);
    }
}
