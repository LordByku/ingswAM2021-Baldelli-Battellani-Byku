package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.gui.EditorGUIUtil;
import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.editor.model.resources.SpendableResource;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;

import javax.swing.*;
import java.util.ArrayList;

public class SpendablePanelHandler extends PanelHandler {
    private final SpendableResourceSet spendable;
    private ArrayList<ValidatableTextField> quantityFields;

    public SpendablePanelHandler(JFrame frame, JPanel panel, SpendableResourceSet spendable) {
        super(frame, panel);
        this.spendable = spendable;
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

        for (SpendableResource resource : SpendableResource.values()) {
            EditorGUIUtil.addLabel(resource.getString(), labelPanel);
            quantityFields.add(EditorGUIUtil.addValidatableTextField(spendable.getQuantity(resource), textFieldPanel, (value) -> {
                spendable.updateQuantity(resource, value);
            }, (value) -> value >= 0 && value < 10));
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

        int totalQuantity = 0;
        for (SpendableResource resource: SpendableResource.values()) {
            totalQuantity += spendable.getQuantity(resource);
        }
        if(totalQuantity >= 10) {
            result = false;
        }

        return result;
    }
}
