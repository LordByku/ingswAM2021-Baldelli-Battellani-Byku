package it.polimi.ingsw.editor.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;

import java.util.ArrayList;

public class FaithTrackEditor {
    private int finalPosition;
    private ArrayList<CheckPoint> checkPoints;
    private ArrayList<VaticanReportSection> vaticanReportSections;

    public void addVRS(VaticanReportSection vaticanReportSection) {
        if(vaticanReportSections == null) {
            vaticanReportSections = new ArrayList<>();
        }
        vaticanReportSections.add(vaticanReportSection);
    }

    public void addCheckPoint(int index, CheckPoint checkPoint) {
        checkPoints.add(index, checkPoint);
    }

    public void removeCheckPoint(int index) {
        checkPoints.remove(index);
    }

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }

    public void setCheckPointPosition(int index, int position) {
        checkPoints.set(index, new CheckPoint(position, checkPoints.get(index).getPoints()));
    }

    public void setCheckPointPoints(int index, int points) {
        checkPoints.set(index, new CheckPoint(checkPoints.get(index).getPosition(), points));
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public ArrayList<CheckPoint> getCheckPoints() {
        return checkPoints;
    }
}
