package it.polimi.ingsw.network.client.clientStates;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.network.client.Client;

public abstract class ClientState {
    private final JsonParser parser;

    public ClientState() {
        parser = new JsonParser();
    }

    public abstract void handleServerMessage(Client client, String line);
    public abstract void handleUserMessage(Client client, String line);

    public JsonObject parse(String line) {
        return (JsonObject) parser.parse(line);
    }

    public String getStatus(JsonObject json) {
        return json.get("status").getAsString();
    }

    public JsonObject getMessage(JsonObject json) {
        return json.get("message").getAsJsonObject();
    }

    public String getType(JsonObject json) {
        return json.get("type").getAsString();
    }
}
