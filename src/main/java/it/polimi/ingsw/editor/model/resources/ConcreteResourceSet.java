package it.polimi.ingsw.editor.model.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.resources.ConcreteResource;

import java.util.HashMap;

public class ConcreteResourceSet {
    private final HashMap<ConcreteResource, Integer> resources;

    public ConcreteResourceSet() {
        resources = new HashMap<>();
    }

    public void parse(JsonArray jsonArray) {
        Gson gson = new Gson();

        for(JsonElement jsonElement: jsonArray) {
            JsonObject resource = jsonElement.getAsJsonObject();

            ConcreteResource concreteResource = gson.fromJson(resource.get("resource"), ConcreteResource.class);
            int quantity = resource.get("quantity").getAsInt();

            resources.put(concreteResource, quantity);
        }
    }

    public int getQuantity(ConcreteResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    public void updateQuantity(ConcreteResource resource, int value) {
        resources.put(resource, value);
    }
}
