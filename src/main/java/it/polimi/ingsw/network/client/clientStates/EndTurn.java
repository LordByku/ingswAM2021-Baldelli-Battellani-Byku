package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class EndTurn extends ClientState {
    public EndTurn() {
        CLI.getInstance().endTurn();
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int selection = Integer.parseInt(line);

            switch (selection) {
                case 1: {
                    // TODO
                    break;
                }
                case 2: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    client.setState(new PlayLeaderCard(EndTurn::new));
                    break;
                }
                case 3: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    client.setState(new DiscardLeaderCard(EndTurn::new));
                    break;
                }
                case 0: {
                    client.setState(new ViewState(EndTurn::new));
                    break;
                }
            }
        } catch (NumberFormatException e) {
            CLI.getInstance().endTurn();
        }
    }
}
