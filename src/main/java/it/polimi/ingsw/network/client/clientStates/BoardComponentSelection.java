package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.localModel.Board;
import it.polimi.ingsw.view.cli.CLI;

import java.util.function.Supplier;

public class BoardComponentSelection extends ClientState {
    private final Supplier<ClientState> returnStateSupplier;
    private final String nickname;

    public BoardComponentSelection(Supplier<ClientState> returnStateSupplier, String nickname) {
        this.returnStateSupplier = returnStateSupplier;
        this.nickname = nickname;
        CLI.getInstance().boardComponentSelection();
    }

    @Override
    public void handleServerMessage(Client client, String line) {

    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("x")) {
            if(LocalConfig.getInstance().getTurnOrder().size() == 1) {
                client.setState(new ViewState(returnStateSupplier));
            } else {
                client.setState(new PlayerBoardSelection(returnStateSupplier));
            }
        } else {
            try {
                int selection = Integer.parseInt(line);

                Board playerBoard = client.getModel().getPlayer(nickname).getBoard();

                switch(selection) {
                    case 1:
                        CLI.getInstance().showFaithTrack(playerBoard.getFaithTrack());
                        CLI.getInstance().boardComponentSelection();
                        break;
                    case 2:
                        CLI.getInstance().showWarehouse(playerBoard.getWarehouse(), playerBoard.getPlayedLeaderCards());
                        CLI.getInstance().boardComponentSelection();
                        break;
                    case 3:
                        CLI.getInstance().showStrongbox(playerBoard.getStrongBox());
                        CLI.getInstance().boardComponentSelection();
                        break;
                    case 4:
                        client.setState(new ShowDevCards(returnStateSupplier, nickname));
                        //CLI.getInstance().selectDevCardDeck();
//                        CLI.getInstance().boardComponentSelection();

                        break;
                    case 5:
                        CLI.getInstance().showLeaderCards(playerBoard.getPlayedLeaderCards());
                        CLI.getInstance().boardComponentSelection();
                        break;
                    case 6:
                        CLI.getInstance().showLeaderCards(playerBoard.getHandLeaderCards());
                        CLI.getInstance().boardComponentSelection();
                        break;
                    default:
                        CLI.getInstance().boardComponentSelection();
                }
            } catch (NumberFormatException e) {
                CLI.getInstance().boardComponentSelection();
            }
        }
    }
}
