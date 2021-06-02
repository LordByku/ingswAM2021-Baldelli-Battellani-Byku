package it.polimi.ingsw.editor;

import it.polimi.ingsw.editor.gui.Window;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import javax.swing.*;
import java.util.ArrayList;

public class EditorApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Parameters Editor");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Window window = new Window(frame);
    }
}
