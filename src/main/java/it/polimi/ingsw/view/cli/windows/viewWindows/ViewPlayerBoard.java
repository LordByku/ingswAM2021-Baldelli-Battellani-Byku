package it.polimi.ingsw.view.cli.windows.viewWindows;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Board;

public class ViewPlayerBoard extends CLIViewWindow {
    private final Board board;

    public ViewPlayerBoard(Board board) {
        this.board = board;
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if (line.equals("x")) {
            CLI.getInstance().setViewWindow(new ViewModel());
        } else {
            try {
                int selection = Integer.parseInt(line);

                switch (selection) {
                    case 0: {
                        CLI.getInstance().showFaithTrack(board.getFaithTrack());
                        break;
                    }
                    case 1: {
                        CLI.getInstance().showWarehouse(board.getWarehouse(), board.getPlayedLeaderCards());
                        break;
                    }
                    case 2: {
                        CLI.getInstance().showStrongbox(board.getStrongBox());
                        break;
                    }
                    case 3: {
                        CLI.getInstance().setViewWindow(new ViewDevCardDeck(board));
                        break;
                    }
                    case 4: {
                        CLI.getInstance().showLeaderCards(board.getPlayedLeaderCards());
                        break;
                    }
                    case 5: {
                        CLI.getInstance().showLeaderCards(board.getHandLeaderCards());
                        break;
                    }
                }
            } catch (NumberFormatException e) {
            }
        }

        CLI.getInstance().renderWindow(client);
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().boardComponentSelection(board);
    }
}
