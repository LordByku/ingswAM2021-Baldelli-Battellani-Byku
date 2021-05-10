package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.localModel.CardMarket;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

import java.util.function.Supplier;

public class CardMarketDeckSelection extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;

    public CardMarketDeckSelection(Supplier<ClientState> returnStateSupplier) {
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
            CardMarket cardMarket = client.getModel().getGameZone().getCardMarket();

            String[] words = Strings.splitLine(line);
            if(words.length != 2) {
                CLI.getInstance().cardMarket(cardMarket);
            } else {
                try {
                    int row = Integer.parseInt(words[0]), column = Integer.parseInt(words[1]);

                    if(row < 0 || row >= CardLevel.values().length || column < 0 || column >= CardColour.values().length) {
                        CLI.getInstance().cardMarket(cardMarket);
                    } else {
                        CLI.getInstance().showDevCard(cardMarket.getDevCard(row, column));
                        CLI.getInstance().cardMarket(cardMarket);
                    }
                } catch (NumberFormatException e) {
                    CLI.getInstance().cardMarket(cardMarket);
                }
            }
        }
    }
}
