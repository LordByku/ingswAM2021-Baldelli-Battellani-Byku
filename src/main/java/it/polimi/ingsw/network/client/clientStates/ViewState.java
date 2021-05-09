package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.localModel.LocalModel;
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

                    }
                    case 3: {

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
