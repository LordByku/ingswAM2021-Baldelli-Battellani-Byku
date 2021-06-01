package it.polimi.ingsw.editor.model.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class SpendableResourceSet {
    private final HashMap<SpendableResource, Integer> resources;

    public SpendableResourceSet() {
        resources = new HashMap<>();
    }

    public void parse(JsonArray jsonArray) {
        Gson gson = new Gson();

        for(JsonElement jsonElement: jsonArray) {
            JsonObject resource = jsonElement.getAsJsonObject();

            SpendableResource spendableResource = gson.fromJson(resource.get("resource"), SpendableResource.class);
            int quantity = resource.get("quantity").getAsInt();

            resources.put(spendableResource, quantity);
        }
    }

    public int getQuantity(SpendableResource resource) {
        return resources.getOrDefault(resource, 0);
    }

    public void updateQuantity(SpendableResource resource, int value) {
        resources.put(resource, value);
    }
}
