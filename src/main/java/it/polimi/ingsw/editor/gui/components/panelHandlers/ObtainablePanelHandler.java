package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.model.resources.ObtainableResource;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;

import javax.swing.*;

public class ObtainablePanelHandler extends PanelHandler {
    private final ObtainableResourceSet obtainable;

    public ObtainablePanelHandler(JFrame frame, JPanel panel, ObtainableResourceSet obtainable) {
        super(frame, panel);
        this.obtainable = obtainable;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));

        for(ObtainableResource resource: ObtainableResource.values()) {
            addLabel(resource.getString(), labelPanel);
            addTextField(obtainable.getQuantity(resource), textFieldPanel, (value) -> obtainable.updateQuantity(resource, value));
        }

        panel.add(labelPanel);
        panel.add(textFieldPanel);

        frame.setVisible(true);
    }
}
