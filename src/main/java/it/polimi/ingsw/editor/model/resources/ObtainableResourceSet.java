package it.polimi.ingsw.editor.model.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class ObtainableResourceSet {
    private final HashMap<ObtainableResource, Integer> resources;

    public ObtainableResourceSet() {
        resources = new HashMap<>();
    }

    public void parse(JsonArray jsonArray) {
        Gson gson = new Gson();

        for(JsonElement jsonElement: jsonArray) {
            JsonObject resource = jsonElement.getAsJsonObject();

            ObtainableResource obtainableResource = gson.fromJson(resource.get("resource"), ObtainableResource.class);
            int quantity = resource.get("quantity").getAsInt();

            resources.put(obtainableResource, quantity);
        }
    }

    public int getQuantity(ObtainableResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    public void updateQuantity(ObtainableResource resource, int value) {
        resources.put(resource, value);
    }
}
