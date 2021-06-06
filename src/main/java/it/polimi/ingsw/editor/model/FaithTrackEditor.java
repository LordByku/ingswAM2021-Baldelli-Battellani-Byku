package it.polimi.ingsw.editor.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.editor.model.simplifiedModel.VaticanReportSection;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.utility.JsonUtil;

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

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public void addVaticanReportSection(int index, int firstSpace, int popeSpace, int points) {
        vaticanReportSections.add(index, new VaticanReportSection(firstSpace, popeSpace, points));
    }

    public void removeVaticanReportSection(int index) {
        vaticanReportSections.remove(index);
    }

    public void setVRSFirstSpace(int index, int firstSpace) {
        VaticanReportSection vaticanReportSection = vaticanReportSections.get(index);
        int popeSpace = vaticanReportSection.getPopeSpace();
        int points = vaticanReportSection.getPoints();
        vaticanReportSections.set(index, new VaticanReportSection(firstSpace, popeSpace, points));
    }

    public void setVRSPopeSpace(int index, int popeSpace) {
        VaticanReportSection vaticanReportSection = vaticanReportSections.get(index);
        int firstSpace = vaticanReportSection.getFirstSpace();
        int points = vaticanReportSection.getPoints();
        vaticanReportSections.set(index, new VaticanReportSection(firstSpace, popeSpace, points));
    }

    public void setVRSPoints(int index, int points) {
        VaticanReportSection vaticanReportSection = vaticanReportSections.get(index);
        int firstSpace = vaticanReportSection.getFirstSpace();
        int popeSpace = vaticanReportSection.getPopeSpace();
        vaticanReportSections.set(index, new VaticanReportSection(firstSpace, popeSpace, points));
    }

    public ArrayList<VaticanReportSection> getVaticanReportSections() {
        return vaticanReportSections;
    }

    public void addCheckPoint(int index, CheckPoint checkPoint) {
        checkPoints.add(index, checkPoint);
    }

    public void removeCheckPoint(int index) {
        checkPoints.remove(index);
    }

    public void setCheckPointPosition(int index, int position) {
        checkPoints.set(index, new CheckPoint(position, checkPoints.get(index).getPoints()));
    }

    public void setCheckPointPoints(int index, int points) {
        checkPoints.set(index, new CheckPoint(checkPoints.get(index).getPosition(), points));
    }

    public ArrayList<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    private JsonObject buildFaithTrack() {
        JsonObject out = new JsonObject();
        JsonArray checkPointsArray = new JsonArray();
        for(CheckPoint checkPoint: checkPoints) {
            checkPointsArray.add(JsonUtil.getInstance().serialize(checkPoint));
        }
        out.addProperty("finalPosition", finalPosition);
        out.add("checkPoints", checkPointsArray);
        return out;
    }

    private JsonArray buildVaticanReportSections() {
        JsonArray vrsArray = new JsonArray();
        for(VaticanReportSection vaticanReportSection: vaticanReportSections) {
            vrsArray.add(JsonUtil.getInstance().serialize(vaticanReportSection));
        }
        return vrsArray;
    }

    public void write(JsonObject out) {
        if(!out.has("board")) {
            JsonObject board = new JsonObject();
            board.add("faithTrack", buildFaithTrack());
            out.add("board", board);
        } else {
            out.getAsJsonObject("board").add("faithTrack", buildFaithTrack());
        }

        out.add("vaticanReportSections", buildVaticanReportSections());
    }
}
