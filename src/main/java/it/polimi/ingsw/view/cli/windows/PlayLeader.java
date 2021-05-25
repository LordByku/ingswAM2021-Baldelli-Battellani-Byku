package it.polimi.ingsw.view.cli.windows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Player;

public class PlayLeader extends CommandWindow {
    public PlayLeader(Client client) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        handleLeaderCard(client, line);
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        CLI.showLeaderCards(self.getBoard().getHandLeaderCards());
        CLI.playLeaderCard();
    }
}
