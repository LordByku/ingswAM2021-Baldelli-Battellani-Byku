package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;
import it.polimi.ingsw.editor.model.resources.ConcreteResourceSet;

import javax.swing.*;
import java.util.ArrayList;

public class ConcretePanelHandler extends PanelHandler {
    private final ConcreteResourceSet concrete;
    private ArrayList<ValidatableTextField> quantityFields;

    protected ConcretePanelHandler(JFrame frame, JPanel panel, ConcreteResourceSet concrete) {
        super(frame, panel);
        this.concrete = concrete;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        quantityFields = new ArrayList<>();

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));

        for (ConcreteResource resource : ConcreteResource.values()) {
            EditorGUIUtil.addLabel(resource.getString(), labelPanel);

            EditorGUIUtil.addValidatableTextField(concrete.getQuantity(resource), textFieldPanel, (value) -> {
                concrete.updateQuantity(resource, value);
            }, (value) -> value >= 0 && value < 100);
        }

        panel.add(labelPanel);
        panel.add(textFieldPanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        for (ValidatableTextField validatableTextField : quantityFields) {
            if (!validatableTextField.validate()) {
                result = false;
            }
        }
        return result;
    }
}
