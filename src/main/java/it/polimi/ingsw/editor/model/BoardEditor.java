package it.polimi.ingsw.editor.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.resources.ObtainableResourceSet;
import it.polimi.ingsw.editor.model.resources.SpendableResourceSet;
import it.polimi.ingsw.utility.JsonUtil;

import java.util.ArrayList;

public class BoardEditor {
    private final SpendableResourceSet defaultProductionIn;
    private final ObtainableResourceSet defaultProductionOut;
    private final ArrayList<Integer> depotSizes;
    private int developmentCardSlots;

    public BoardEditor(JsonObject json) {
        defaultProductionIn = new SpendableResourceSet();
        defaultProductionIn.parse(json.getAsJsonObject("defaultProductionPower").getAsJsonArray("productionIn"));

        defaultProductionOut = new ObtainableResourceSet();
        defaultProductionOut.parse(json.getAsJsonObject("defaultProductionPower").getAsJsonArray("productionOut"));

        depotSizes = new ArrayList<>();
        for (JsonElement jsonElement : json.getAsJsonArray("depotSizes")) {
            depotSizes.add(jsonElement.getAsInt());
        }

        developmentCardSlots = json.get("developmentCardsSlots").getAsInt();
    }

    public SpendableResourceSet getDefaultProductionIn() {
        return defaultProductionIn;
    }

    public ObtainableResourceSet getDefaultProductionOut() {
        return defaultProductionOut;
    }

    public ArrayList<Integer> getDepotSizes() {
        return depotSizes;
    }

    public void addDepot(int index, int depotSize) {
        depotSizes.add(index, depotSize);
    }

    public void setDepot(int index, int depotSize) {
        depotSizes.set(index, depotSize);
    }

    public void removeDepot(int index) {
        depotSizes.remove(index);
    }

    public int getDevelopmentCardSlots() {
        return developmentCardSlots;
    }

    public void setDevelopmentCardSlots(int developmentCardSlots) {
        this.developmentCardSlots = developmentCardSlots;
    }

    private JsonArray buildDepotSizes() {
        return JsonUtil.getInstance().serialize(depotSizes).getAsJsonArray();
    }

    public void write(JsonObject out) {
        if (!out.has("board")) {
            JsonObject board = new JsonObject();
            board.add("defaultProductionPower", Config.writeProductionPower(defaultProductionIn, defaultProductionOut));
            board.add("depotSizes", buildDepotSizes());
            board.addProperty("developmentCardsSlots", developmentCardSlots);
            out.add("board", board);
        } else {
            out.getAsJsonObject("board").add("defaultProductionPower", Config.writeProductionPower(defaultProductionIn, defaultProductionOut));
            out.getAsJsonObject("board").add("depotSizes", buildDepotSizes());
            out.getAsJsonObject("board").addProperty("developmentCardsSlots", developmentCardSlots);
        }
    }
}
