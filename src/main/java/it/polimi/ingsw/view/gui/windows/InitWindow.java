package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.view.gui.Welcome;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class InitWindow extends GUIWindow {
    private Welcome window;

    public InitWindow() {
        JFrame frame = new JFrame("Masters of Renaissance");
        frame.setContentPane(new Welcome().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
