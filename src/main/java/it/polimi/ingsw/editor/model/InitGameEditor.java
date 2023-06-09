package it.polimi.ingsw.editor.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utility.JsonUtil;

public class InitGameEditor {
    private int leaderCardsToAssign;
    private int leaderCardsToDiscard;
    private int[] faithPoints;
    private int[] resources;

    public int getLeaderCardsToAssign() {
        return leaderCardsToAssign;
    }

    public void setLeaderCardsToAssign(int leaderCardsToAssign) {
        this.leaderCardsToAssign = leaderCardsToAssign;
    }

    public int getLeaderCardsToDiscard() {
        return leaderCardsToDiscard;
    }

    public void setLeaderCardsToDiscard(int leaderCardsToDiscard) {
        this.leaderCardsToDiscard = leaderCardsToDiscard;
    }

    public int getFaithPoints(int index) {
        return faithPoints[index];
    }

    public int getResources(int index) {
        return resources[index];
    }

    public void setFaithPoints(int index, int faithPoints) {
        this.faithPoints[index] = faithPoints;
    }

    public void setResources(int index, int resources) {
        this.resources[index] = resources;
    }

    public void write(JsonObject out) {
        out.add("initGame", JsonUtil.getInstance().serialize(this));
    }
}
