package it.polimi.ingsw.utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;

public class UserParser {
    private static UserParser instance;
    private final Gson gson;

    private UserParser() {
        gson = new Gson();
    }

    public static UserParser getInstance() {
        if (instance == null) {
            instance = new UserParser();
        }
        return instance;
    }

    public ConcreteResource readUserResource(String word) throws JsonSyntaxException {
        return gson.fromJson(word.toUpperCase(), ConcreteResource.class);
    }

    public ConcreteResourceSet readUserResources(String[] words) throws JsonSyntaxException, InvalidResourceException {
        ConcreteResourceSet concreteResourceSet = new ConcreteResourceSet();

        for (String word : words) {
            concreteResourceSet.addResource(readUserResource(word));
        }

        return concreteResourceSet;
    }

    public int[] readIntArray(String[] words) throws JsonSyntaxException, NumberFormatException {
        int[] result = new int[words.length];

        for (int i = 0; i < words.length; ++i) {
            result[i] = Integer.parseInt(words[i]);
        }

        return result;
    }
}
