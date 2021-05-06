package it.polimi.ingsw.network.client.localModel;

import java.util.ArrayList;

public class LocalModel {
    private GameZone gameZone;
    private ArrayList<Player> players;

    public Player getPlayer(String nickname) {
        for(Player player: players) {
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }
}
