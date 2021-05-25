package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class prova {
    private JPanel panel1;
    private JFormattedTextField mastersOfRenaissanceFormattedTextField;
    private JTextField insertYourNicknameToTextField;

    public prova() {
        insertYourNicknameToTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
        insertYourNicknameToTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField)e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });
    }

    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("prova");
        frame.setContentPane(new prova().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        //Display the window.
        frame.setSize(1024,512);
        frame.setVisible(true);
    }

}
