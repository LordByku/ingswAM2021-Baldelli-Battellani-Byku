package it.polimi.ingsw.network.clientStates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;

public class Lobby extends ClientState {
    public Lobby() {
        CLI.getInstance().connecting();
    }

    @Override
    public void handleServerMessage(Client client, String line) {
        JsonObject json = parse(line);

        String status = getStatus(json);

        switch (status) {
            case "error": {
                String message = getMessage(json).toString();
                CLI.getInstance().serverError(message);
                client.closeServerCommunication();
                client.setState(new NicknameSelection());
                break;
            }
            case "ok": {
                String type = getType(json);

                switch (type) {
                    case "playerList": {
                        JsonObject message = getMessage(json);

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
                        }

                        break;
                    }
                    case "config": {
                        // TODO
                        client.setState(new LoadMultiPlayer());
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
