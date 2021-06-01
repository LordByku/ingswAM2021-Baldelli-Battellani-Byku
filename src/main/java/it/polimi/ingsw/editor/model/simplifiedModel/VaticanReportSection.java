package it.polimi.ingsw.editor.model.simplifiedModel;

public class VaticanReportSection {
    private final int firstSpace;
    private final int popeSpace;
    private final int points;

    public VaticanReportSection(int firstSpace, int popeSpace, int points) {
        this.firstSpace = firstSpace;
        this.popeSpace = popeSpace;
        this.points = points;
    }

    public int getFirstSpace() {
        return firstSpace;
    }

    public int getPopeSpace() {
        return popeSpace;
    }

    public int getPoints() {
        return points;
    }
}
