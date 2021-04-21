package it.polimi.ingsw;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class DevCardsParser {
    private static DevCardsParser instance;
    private static final String path = "src/resources/devCards.json";
    private final JsonArray developmentCards;

    private DevCardsParser() throws FileNotFoundException {
        JsonParser parser = new JsonParser();

        FileReader reader = new FileReader(path);

        JsonObject obj = (JsonObject) parser.parse(reader);

        developmentCards = (JsonArray) obj.get("developmentCards");
    }

    public static DevCardsParser getInstance() {
        if(instance == null) {
            try {
                instance = new DevCardsParser();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public JsonArray getDevelopmentCards() {
        return developmentCards;
    }
}
