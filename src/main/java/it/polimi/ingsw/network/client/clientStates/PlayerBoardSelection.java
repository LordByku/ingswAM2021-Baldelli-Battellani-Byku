package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PlayerBoardSelection extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    public PlayerBoardSelection(Supplier<ClientState> returnStateSupplier) {
        this.returnStateSupplier = returnStateSupplier;
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")) {
            client.setState(new ViewState(returnStateSupplier));
        } else {
            try {
                int playerIndex = Integer.parseInt(line);

                ArrayList<String> nicknames = LocalConfig.getInstance().getTurnOrder();

                if(playerIndex < 0 || playerIndex >= nicknames.size()) {
                    CLI.getInstance().selectPlayer(LocalConfig.getInstance().getTurnOrder());
                } else {
                    String nickname = nicknames.get(playerIndex);

                    client.setState(new BoardComponentSelection(nickname));
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().selectPlayer(LocalConfig.getInstance().getTurnOrder());
            }
        }
    }
}
