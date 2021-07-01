package it.polimi.ingsw.network.client.clientStates;

import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.server.GameStateSerializer;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Welcome extends ClientState {
    private final ViewInterface viewInterface;

    public Welcome(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        viewInterface.onUnexpected();
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if (client.getNickname() == null) {
            client.setNickname(line);
            viewInterface.welcome();
        } else {
            try {
                int mode = Integer.parseInt(line);

                switch (mode) {
                    case 0: {
                        try {
                            client.connectToServer();
                        } catch (IOException e) {
                            viewInterface.connectionFailed(0);
                            viewInterface.welcome();
                        }
                        break;
                    }
                    case 1: {
                        viewInterface.loadGame();

                        Game.getInstance().addPlayer(client.getNickname());
                        Game.getInstance().startSinglePlayer();

                        client.setOfflineGame(true);

                        ArrayList<String> turnOrder = new ArrayList<>();
                        turnOrder.add(client.getNickname());
                        LocalConfig.getInstance().setTurnOrder(turnOrder);
                        LocalConfig.getInstance().setConfig(Parser.getInstance().getConfig());

                        GameStateSerializer serializer = new GameStateSerializer(client.getNickname());
                        client.setModel(Deserializer.getInstance().getLocalModel(serializer.gameState()));

                        viewInterface.loadGameInterface();
                        client.setState(new GameStarted(viewInterface));

                        client.startLocalController();
                        break;
                    }
                    default: {
                        viewInterface.welcome();
                    }
                }
            } catch (NumberFormatException | InvalidNicknameException | ExistingNicknameException | GameAlreadyStartedException | FullLobbyException e) {
                viewInterface.welcome();
            }
        }
    }
}
