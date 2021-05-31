package it.polimi.ingsw.editor.gui.components;

import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AddCheckPointClickEvent extends MouseAdapter {
    private final int buttonIndex;
    private final FaithTrackPanelHandler faithTrackPanelHandler;

    public AddCheckPointClickEvent(int buttonIndex, FaithTrackPanelHandler faithTrackPanelHandler) {
        this.buttonIndex = buttonIndex;
        this.faithTrackPanelHandler = faithTrackPanelHandler;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(faithTrackPanelHandler.validate()) {
            Config.getInstance().getFaithTrackEditor().addCheckPoint(buttonIndex, new CheckPoint(-1, -1));
            faithTrackPanelHandler.build();
        }
    }
}