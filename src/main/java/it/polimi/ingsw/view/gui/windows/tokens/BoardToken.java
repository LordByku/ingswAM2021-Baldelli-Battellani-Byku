package it.polimi.ingsw.view.gui.windows.tokens;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.windows.BoardView;
import it.polimi.ingsw.view.gui.windows.GUIWindow;

import java.util.Objects;

public class BoardToken extends WindowToken {
    private final String nickname;

    public BoardToken(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public GUIWindow getWindow(GUI gui, Client client) {
        return new BoardView(gui, client, nickname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardToken that = (BoardToken) o;
        return nickname.equals(that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
