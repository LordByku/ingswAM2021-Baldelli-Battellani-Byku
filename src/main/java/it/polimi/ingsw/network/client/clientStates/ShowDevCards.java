package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class ShowDevCards extends ClientState{

    private final Supplier<ClientState> returnStateSupplier;
    private final String nickname;

    public ShowDevCards(Supplier<ClientState> returnStateSupplier, String nickname) {
        CLI.getInstance().selectDevCardDeck();
        this.returnStateSupplier = returnStateSupplier;
        this.nickname = nickname;
    }


    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")) {
            //client.setState(returnStateSupplier.get());
            client.setState(new BoardComponentSelection(returnStateSupplier,nickname));
        } else {
            try {
                int deckIndex = Integer.parseInt(line);

                if(deckIndex >= 0 && deckIndex<=2) {
                    CLI.getInstance().showDevCardDeck(client.getModel().getPlayer(nickname).getBoard().getDevCardDeck(deckIndex));
                }
                CLI.getInstance().selectDevCardDeck();
            } catch (NumberFormatException e) {
                CLI.getInstance().selectDevCardDeck();
            }

        }
    }
}
