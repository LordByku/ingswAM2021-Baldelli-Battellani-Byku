package it.polimi.ingsw.utility;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {
    private static JsonUtil instance;
    private final Gson gson;
    private final JsonParser parser;

    private JsonUtil() {
        gson = new Gson();
        parser = new JsonParser();
    }

    public static JsonUtil getInstance() {
        if(instance == null) {
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
}
