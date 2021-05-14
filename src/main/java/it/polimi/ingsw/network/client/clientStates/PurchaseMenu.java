package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;

public class PurchaseMenu extends ClientState{

    public PurchaseMenu(){
        CLI.getInstance().purchaseMenu();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")){
            client.setState(new StartTurn());
        }
        else if(line.equals("0")){
            CLI.getInstance().cardMarket(client.getModel().getGameZone().getCardMarket());
            client.setState(new CardMarketDeckSelection(PurchaseMenu::new));
        }
        else if(line.equals("1")){
            client.setState(new PurchaseDevCard());
        }
    }
}
