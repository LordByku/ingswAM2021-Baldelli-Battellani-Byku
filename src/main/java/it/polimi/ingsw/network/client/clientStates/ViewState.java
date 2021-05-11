package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class ViewState extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    public ViewState(Supplier<ClientState> returnStateSupplier) {
        this.returnStateSupplier = returnStateSupplier;
        CLI.getInstance().viewState();
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")) {
            client.setState(returnStateSupplier.get());
        } else {
            try {
                int selection = Integer.parseInt(line);

                switch (selection) {
                    case 1: {
                        CLI.getInstance().marbleMarket(client.getModel().getGameZone().getMarbleMarket());
                        CLI.getInstance().viewState();
                        break;
                    }
                    case 2: {
                        CLI.getInstance().cardMarket(client.getModel().getGameZone().getCardMarket());
                        client.setState(new CardMarketDeckSelection(returnStateSupplier));
                        break;
                    }
                    case 3: {
                        client.setState(new PlayerBoardSelection(returnStateSupplier));
                        break;
                    }
                    default: {
                        CLI.getInstance().viewState();
                    }
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().viewState();
            }
        }
    }
}
