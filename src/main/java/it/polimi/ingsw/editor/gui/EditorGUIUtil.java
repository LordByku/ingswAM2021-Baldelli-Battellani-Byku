package it.polimi.ingsw.editor.gui;

import it.polimi.ingsw.editor.gui.components.ValidatableTextField;
import it.polimi.ingsw.view.gui.GUIUtil;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EditorGUIUtil extends GUIUtil {
    public static ValidatableTextField addValidatableTextField(int value, JPanel outerPanel, Consumer<Integer> lambda, Predicate<Integer> validator) {
        ValidatableTextField validatableTextField = new ValidatableTextField(value, lambda, validator);
        outerPanel.add(validatableTextField.getPanel());
        return validatableTextField;
    }
}
