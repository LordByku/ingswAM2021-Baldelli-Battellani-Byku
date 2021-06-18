package it.polimi.ingsw.view.cli.windows.viewWindows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Board;

import java.util.ArrayList;

public class ViewDevCardDeck extends CLIViewWindow {
    private final Board board;

    public ViewDevCardDeck(Board board) {
        this.board = board;
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if (line.equals("x")) {
            CLI.setViewWindow(new ViewPlayerBoard(board));
        } else {
            try {
                int selection = Integer.parseInt(line);
                ArrayList<ArrayList<Integer>> devCards = board.getDevCardsArea().getDecks();
                if (selection >= 0 && selection < devCards.size()) {
                    CLI.showDevCardDeck(devCards.get(selection));
                }
            } catch (NumberFormatException e) {
            }
        }

        CLI.renderGameWindow(client);
    }

    @Override
    public void render(Client client) {
        CLI.selectDevCardDeck();
    }
}
