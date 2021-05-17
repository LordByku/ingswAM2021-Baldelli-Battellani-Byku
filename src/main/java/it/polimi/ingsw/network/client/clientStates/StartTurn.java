package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerDiscardLeaderCard;
import it.polimi.ingsw.network.client.clientStates.singlePlayerStates.SinglePlayerPlayLeaderCard;
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
                    CLI.getInstance().marbleMarket(client.getModel().getGameZone().getMarbleMarket());
                    client.setState(new CollectResources());
                    break;
                }
                case 2: {
                    client.setState(new PurchaseMenu());
                    break;
                }
                case 3: {
                    CLI.getInstance().productionSelection(client.getModel().getPlayer(client.getNickname()).getBoard().getDevCardDecks(), client.getModel().getPlayer(client.getNickname()).getBoard().getPlayedLeaderCards());
                    CLI.getInstance().activateProductionSelection();
                    client.setState(new ProductionSelection());
                    break;
                }
                case 4: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());

                    if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                        client.setState(new SinglePlayerPlayLeaderCard(StartTurn::new));
                    } else {
                        client.setState(new PlayLeaderCard(StartTurn::new));
                    }
                    break;
                }
                case 5: {
                    CLI.getInstance().showLeaderCards(client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards());
                    if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                        client.setState(new SinglePlayerDiscardLeaderCard(StartTurn::new));
                    } else {
                        client.setState(new DiscardLeaderCard(StartTurn::new));
                    }
                    break;
                }
                case 0: {
                    client.setState(new ViewState(StartTurn::new));
                    break;
                }
                default: {
                    CLI.getInstance().startTurn();
                }
            }
        } catch (NumberFormatException e) {
            CLI.getInstance().startTurn();
        }
    }
}