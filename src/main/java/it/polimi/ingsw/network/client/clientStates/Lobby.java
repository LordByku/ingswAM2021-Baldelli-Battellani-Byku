package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LoadCards;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.utility.Deserializer;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.LocalModel;

import java.util.ArrayList;

public class Lobby extends ClientState {
    private final ViewInterface viewInterface;

    public Lobby(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        // TODO: use view interface
        CLI.connecting();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = JsonUtil.getInstance().parseLine(line).getAsJsonObject();

        String status = json.get("status").getAsString();

        switch (status) {
            case "fatalError": {
                String message = json.get("message").getAsString();
                viewInterface.onError(client, message);
                client.closeServerCommunication();
                client.setState(new NicknameSelection(viewInterface));
                break;
            }
            case "error": {
                String message = json.get("message").getAsString();
                viewInterface.onError(client, message);
                break;
            }
            case "ok": {
                String type = json.get("type").getAsString();

                switch (type) {
                    case "endGame": {
                        JsonObject message = json.getAsJsonObject("message");
                        viewInterface.onEndGame(client, message);
                        break;
                    }
                    case "playerList": {
                        JsonObject message = json.get("message").getAsJsonObject();

                        JsonArray playerList = message.get("playerList").getAsJsonArray();
                        ArrayList<String> nicknames = new ArrayList<>();
                        String hostNickname = null;

                        for (JsonElement jsonElement : playerList) {
                            JsonObject jsonPlayer = (JsonObject) jsonElement;

                            boolean isHost = jsonPlayer.get("isHost").getAsBoolean();
                            String nickname = jsonPlayer.get("nickname").getAsString();

                            nicknames.add(nickname);
                            if (isHost) {
                                hostNickname = nickname;
                                if (nickname.equals(client.getNickname())) {
                                    LocalConfig.getInstance().setHost();
                                }
                            }
                        }

                        viewInterface.updatePlayerList(client, nicknames, hostNickname);

                        break;
                    }
                    case "config": {
                        viewInterface.loadGame(client);

                        JsonObject message = json.getAsJsonObject("message");

                        JsonArray jsonTurnOrder = message.getAsJsonArray("turnOrder");
                        ArrayList<String> turnOrder = new ArrayList<>();
                        for (JsonElement jsonElement : jsonTurnOrder) {
                            turnOrder.add(jsonElement.getAsString());
                        }

                        LocalConfig.getInstance().setTurnOrder(turnOrder);

                        JsonObject config = message.getAsJsonObject("config");
                        Parser.getInstance().setConfig(config);
                        LocalConfig.getInstance().setConfig(config);

                        LoadCards.getInstance().leaderCardWidth();
                        LoadCards.getInstance().devCardsWidth();
                        break;
                    }
                    case "update": {
                        JsonObject message = json.get("message").getAsJsonObject();

                        LocalModel model = Deserializer.getInstance().getLocalModel(message);
                        client.setModel(model);

                        client.setState(new GameStarted(viewInterface));
                        break;
                    }
                    default: {
                        viewInterface.onUnexpected(client);
                    }
                }

                break;
            }
            default: {
                viewInterface.onUnexpected(client);
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        viewInterface.startGame(client, line);
    }
}
