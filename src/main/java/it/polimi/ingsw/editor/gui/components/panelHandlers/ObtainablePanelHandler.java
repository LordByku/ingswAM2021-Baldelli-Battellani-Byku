package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.resources.ObtainableResource;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;

import javax.swing.*;
import java.util.ArrayList;

public class ObtainablePanelHandler extends PanelHandler {
    private final ObtainableResourceSet obtainable;
    private ArrayList<ValidatableTextField> quantityFields;

    public ObtainablePanelHandler(JFrame frame, JPanel panel, ObtainableResourceSet obtainable) {
        super(frame, panel);
        this.obtainable = obtainable;
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

        for(ObtainableResource resource: ObtainableResource.values()) {
            EditorGUIUtil.addLabel(resource.getString(), labelPanel);
            quantityFields.add(EditorGUIUtil.addValidatableTextField(obtainable.getQuantity(resource), textFieldPanel, (value) -> {
                obtainable.updateQuantity(resource, value);
            }, (value) -> value >= 0 && value < 100));
        }

        panel.add(labelPanel);
        panel.add(textFieldPanel);

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        for(ValidatableTextField validatableTextField: quantityFields) {
            if(!validatableTextField.validate()) {
                result = false;
            }
        }
        return result;
    }
}
