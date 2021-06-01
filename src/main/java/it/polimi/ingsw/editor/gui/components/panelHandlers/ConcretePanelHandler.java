package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;

import javax.swing.*;

public class ConcretePanelHandler extends PanelHandler {
    private final ConcreteResourceSet concrete;

    protected ConcretePanelHandler(JFrame frame, JPanel panel, ConcreteResourceSet concrete) {
        super(frame, panel);
        this.concrete = concrete;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));

        for(ConcreteResource resource: ConcreteResource.values()) {
            addLabel(resource.getString(), labelPanel);
            addTextField(concrete.getQuantity(resource), textFieldPanel, (value) -> concrete.updateQuantity(resource, value));
        }

        panel.add(labelPanel);
        panel.add(textFieldPanel);

        frame.setVisible(true);
    }
}
