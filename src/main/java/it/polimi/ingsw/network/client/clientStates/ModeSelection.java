package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LoadCards;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.util.ArrayList;

public class ModeSelection extends ClientState {
    public ModeSelection() {
        CLI.getInstance().selectMode();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        CLI.getInstance().unexpected();
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int mode = Integer.parseInt(line);

            switch(mode) {
                case 0: {
                    try {
                        client.connectToServer();
                    } catch (IOException e) {
                        CLI.getInstance().connectionError();
                        CLI.getInstance().selectMode();
                    }
                    break;
                }
                case 1: {
                    CLI.getInstance().loadGame();

                    Game.getInstance().addPlayer(client.getNickname());
                    Game.getInstance().startSinglePlayer();

                    ArrayList<String> turnOrder = new ArrayList<>();
                    turnOrder.add(client.getNickname());
                    LocalConfig.getInstance().setTurnOrder(turnOrder);
                    LocalConfig.getInstance().setConfig(Parser.getInstance().getConfig());

                    LoadCards.getInstance().leaderCardWidth();
                    LoadCards.getInstance().devCardsWidth();

                    GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
                    client.setModel(Deserializer.getInstance().getLocalModel(serializer.gameState()));

                    ArrayList<Integer> leaderCardsIDs = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards();
                    CLI.getInstance().showLeaderCards(leaderCardsIDs);

                    //client.setState(new SinglePlayerInitDiscard(leaderCardsIDs.size()));
                    break;
                }
                default: {
                    CLI.getInstance().selectMode();
                }
            }
        } catch (NumberFormatException | InvalidNicknameException | ExistingNicknameException | GameAlreadyStartedException | FullLobbyException e) {
            CLI.getInstance().selectMode();
        }
    }
}
