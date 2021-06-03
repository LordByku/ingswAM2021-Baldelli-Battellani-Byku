package it.polimi.ingsw.editor;

import it.polimi.ingsw.editor.gui.Window;

import javax.swing.*;

public class EditorApp {
    private static Window window;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parameters Editor");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        setWindow(new Window(frame));
    }

    public static void setWindow(Window window) {
        EditorApp.window = window;
    }
}
