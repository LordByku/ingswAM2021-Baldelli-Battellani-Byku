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
                    client.setState(new PrePlayLeaderCard());
                    break;
                }
                case 5: {
                    client.setState(new PreDiscardLeaderCard());
                    break;
                }
                case 0: {
                    client.setState(new ViewState());
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
