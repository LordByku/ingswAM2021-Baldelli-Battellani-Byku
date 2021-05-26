package it.polimi.ingsw.view.cli.windows.viewWindows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.MarbleMarket;
import it.polimi.ingsw.view.localModel.Player;

import java.util.ArrayList;

public class ViewModel extends CLIViewWindow {
    @Override
    public void handleUserMessage(Client client, String line) {
        if (line.equals("x")) {
            CLI.setViewWindow(null);
        } else {
            try {
                int selection = Integer.parseInt(line);
                switch (selection) {
                    case 0: {
                        MarbleMarket marbleMarket = client.getModel().getGameZone().getMarbleMarket();
                        CLI.marbleMarket(marbleMarket);
                        break;
                    }
                    case 1: {
                        CLI.setViewWindow(new ViewCardMarket());
                        break;
                    }
                    default: {
                        int index = selection - 2;
                        ArrayList<Player> players = client.getModel().getPlayers();
                        if (index >= 0 && index < players.size()) {
                            CLI.setViewWindow(new ViewPlayerBoard(players.get(index).getBoard()));
                        }
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
        CLI.renderGameWindow(client);
    }

    @Override
    public void render(Client client) {
        CLI.showModel(client.getModel());
    }
}
