package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class LocalConfig {
    private static LocalConfig instance;
    private JsonObject config;
    private ArrayList<String> turnOrder;

    private LocalConfig() {}

    public static LocalConfig getInstance() {
        if(instance == null) {
            instance = new LocalConfig();
        }
        return instance;
    }

    public void setConfig(JsonObject config) {
        this.config = config;
    }

    public void setTurnOrder(ArrayList<String> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public int getInitialResources(String nickname) {
        switch(turnOrder.indexOf(nickname)) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 0;
        }
    }
}
