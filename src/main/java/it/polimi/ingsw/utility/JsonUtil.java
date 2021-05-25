package it.polimi.ingsw.utility;

import com.google.gson.*;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.game.Person;

public class JsonUtil {
    private static JsonUtil instance;
    private final Gson gson;
    private final JsonParser parser;

    private JsonUtil() {
        gson = new Gson();
        parser = new JsonParser();
    }

    public static JsonUtil getInstance() {
        if (instance == null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public JsonElement parseLine(String line) {
        return parser.parse(line);
    }

    public JsonElement serialize(Object object) {
        return parser.parse(gson.toJson(object));
    }

    public JsonObject serializeCommandBuffer(CommandBuffer commandBuffer, Person person) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("player", person.getNickname());
        if (commandBuffer == null) {
            jsonObject.add("value", JsonNull.INSTANCE);
        } else {
            JsonObject commandObject = JsonUtil.getInstance().serialize(commandBuffer).getAsJsonObject();
            jsonObject.add("value", commandObject);
        }

        return jsonObject;
    }
}
