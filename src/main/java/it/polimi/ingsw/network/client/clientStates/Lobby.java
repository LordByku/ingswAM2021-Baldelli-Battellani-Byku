package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientParser;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.network.client.localModel.LocalModel;
import it.polimi.ingsw.parsing.Parser;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;

public class Lobby extends ClientState {
    public Lobby() {
        CLI.getInstance().connecting();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = ClientParser.getInstance().parse(line).getAsJsonObject();

        String status = ClientParser.getInstance().getStatus(json);

        switch (status) {
            case "fatalError": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                client.closeServerCommunication();
                client.setState(new NicknameSelection());
                break;
            }
            case "error": {
                String message = ClientParser.getInstance().getMessage(json).getAsString();
                CLI.getInstance().serverError(message);
                break;
            }
            case "ok": {
                String type = ClientParser.getInstance().getType(json);

                switch (type) {
                    case "playerList": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

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
                            }
                        }

                        CLI.getInstance().playerList(nicknames, hostNickname);
                        if (client.getNickname().equals(hostNickname)) {
                            CLI.getInstance().host();
                        } else {
                            CLI.getInstance().waitStart();
                        }

                        break;
                    }
                    case "config": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();

                        ArrayList<String> turnOrder = ClientParser.getInstance().getTurnOrder(message);
                        LocalConfig.getInstance().setTurnOrder(turnOrder);

                        JsonObject config = ClientParser.getInstance().getConfig(message);
                        Parser.getInstance().setConfig(config);
                        LocalConfig.getInstance().setConfig(config);

                        client.setState(new LoadMultiPlayer());
                        break;
                    }
                    case "update": {
                        JsonObject message = ClientParser.getInstance().getMessage(json).getAsJsonObject();
                        LocalModel model = ClientParser.getInstance().getLocalModel(message);
                        client.setModel(model);
                        break;
                    }
                    case "state": {
                        String message = ClientParser.getInstance().getMessage(json).getAsString();

                        switch (message) {
                            case "initDiscard": {
                                ArrayList<Integer> leaderCardsIDs = client.getModel().getPlayer(client.getNickname()).getBoard().getHandLeaderCards();
                                CLI.getInstance().showLeaderCards(leaderCardsIDs);
                                client.setState(new InitDiscard(leaderCardsIDs.size()));
                                break;
                            }
                            case "initResources": {
                                client.setState(new InitResources(LocalConfig.getInstance().getInitialResources(client.getNickname())));
                                break;
                            }
                            case "startTurn": {
                                if (client.getModel().getPlayer(client.getNickname()).hasInkwell()) {
                                    client.setState(new StartTurn());
                                } else {
                                    client.setState(new WaitTurn());
                                }
                                break;
                            }
                            default: {
                                CLI.getInstance().unexpected();
                            }
                        }

                        break;
                    }
                    default: {
                        CLI.getInstance().unexpected();
                    }
                }

                break;
            }
            default: {
                CLI.getInstance().unexpected();
            }
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        if(line.equals("")) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("command", "startGame");

            client.write(jsonObject.toString());
        }
    }
}
