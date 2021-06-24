package it.polimi.ingsw.view.localModel;

public class EndGameResult {

    private String player;
    private int basePoints;
    private int VPoints;
    private int resources;

    public void computeVictoryPoints() {
        VPoints = basePoints + resources / 5;
    }

    public String getPlayer(){
        return player;
    }

    public int getVPoints() {
        return VPoints;
    }

    public int getResources() {
        return resources;
    }
}
