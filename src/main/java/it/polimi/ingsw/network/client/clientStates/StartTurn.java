package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class StartTurn extends ClientState {
    public StartTurn() {
        CLI.getInstance().startTurn();
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int selection = Integer.parseInt(line);

            switch(selection) {
                case 1: {
                    client.setState(new CollectResources());
                    break;
                }
                case 2: {
                    client.setState(new PurchaseDevCard());
                    break;
                }
                case 3: {
                    client.setState(new ActivateProductions());
                    break;
                }
                case 4: {
                    client.setState(new PlayLeaderCard(StartTurn::new));
                    break;
                }
                case 5: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    client.setState(new DiscardLeaderCard(StartTurn::new));
                    break;
                }
                case 0: {
                    client.setState(new ViewState(StartTurn::new));
                    break;
                }
                default: {
                    client.setState(new StartTurn());
                }
            }
        } catch (NumberFormatException e) {
            client.setState(new StartTurn());
        }
    }
}
