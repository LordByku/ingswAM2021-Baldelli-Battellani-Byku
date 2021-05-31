package it.polimi.ingsw.editor.gui.components;

import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemoveCheckPointClickEvent extends MouseAdapter {
    private final int buttonIndex;
    private final FaithTrackPanelHandler faithTrackPanelHandler;

    public RemoveCheckPointClickEvent(int buttonIndex, FaithTrackPanelHandler faithTrackPanelHandler) {
        this.buttonIndex = buttonIndex;
        this.faithTrackPanelHandler = faithTrackPanelHandler;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(faithTrackPanelHandler.validate()) {
            Config.getInstance().getFaithTrackEditor().removeCheckPoint(buttonIndex);
            faithTrackPanelHandler.build();
        }
    }
}
