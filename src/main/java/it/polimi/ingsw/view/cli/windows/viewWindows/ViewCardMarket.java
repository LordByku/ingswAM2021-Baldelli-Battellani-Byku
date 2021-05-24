package it.polimi.ingsw.view.cli.windows.viewWindows;

import it.polimi.ingsw.model.devCards.CardColour;
import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.localModel.CardMarket;

public class ViewCardMarket extends CLIViewWindow {
    @Override
    public void handleUserMessage(Client client, String line) {
        if (line.equals("x")) {
            CLI.getInstance().setViewWindow(new ViewModel());
        } else {
            String[] words = Strings.splitLine(line);
            if (words.length == 2) {
                try {
                    int row = Integer.parseInt(words[0]);
                    int col = Integer.parseInt(words[1]);

                    if (row >= 0 && row < CardLevel.values().length &&
                            col >= 0 && col < CardColour.values().length) {
                        CardMarket cardMarket = client.getModel().getGameZone().getCardMarket();
                        Integer cardID = cardMarket.getDevCard(row, col);
                        CLI.getInstance().showDevCard(cardID);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        CLI.getInstance().renderWindow(client);
    }

    @Override
    public void render(Client client) {
        CardMarket cardMarket = client.getModel().getGameZone().getCardMarket();
        CLI.getInstance().cardMarket(cardMarket);
    }
}
