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
        if (vaticanReportSections == null) {
            vaticanReportSections = new ArrayList<>();
        }
        vaticanReportSections.add(vaticanReportSection);
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(int finalPosition) {
        this.finalPosition = finalPosition;
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
        for (CheckPoint checkPoint : checkPoints) {
            checkPointsArray.add(JsonUtil.getInstance().serialize(checkPoint));
        }
        out.addProperty("finalPosition", finalPosition);
        out.add("checkPoints", checkPointsArray);
        return out;
    }

    private JsonArray buildVaticanReportSections() {
        JsonArray vrsArray = new JsonArray();
        for (VaticanReportSection vaticanReportSection : vaticanReportSections) {
            vrsArray.add(JsonUtil.getInstance().serialize(vaticanReportSection));
        }
        return vrsArray;
    }

    public void write(JsonObject out) {
        if (!out.has("board")) {
            JsonObject board = new JsonObject();
            board.add("faithTrack", buildFaithTrack());
            out.add("board", board);
        } else {
            out.getAsJsonObject("board").add("faithTrack", buildFaithTrack());
        }

        out.add("vaticanReportSections", buildVaticanReportSections());
    }

    public boolean validatePosition(int index) {
        int left = 0;
        int right = finalPosition + 1;

        if (index > 0) {
            left = Math.max(left, checkPoints.get(index - 1).getPosition());
        }
        if (index < checkPoints.size() - 1) {
            right = Math.min(right, checkPoints.get(index + 1).getPosition());
        }

        int position = checkPoints.get(index).getPosition();
        return position > left && position < right;
    }

    public boolean validatePoints(int index) {
        int left = 0;
        int right = 100;

        if (index > 0) {
            left = Math.max(left, checkPoints.get(index - 1).getPoints());
        }
        if (index < checkPoints.size() - 1) {
            right = Math.min(right, checkPoints.get(index + 1).getPoints());
        }

        int points = checkPoints.get(index).getPoints();
        return points > left && points < right;
    }

    public boolean validateVRSFirstSpace(int index) {
        int left = 0;
        int right = Math.min(finalPosition + 1, vaticanReportSections.get(index).getPopeSpace() + 1);

        if (index > 0) {
            left = Math.max(left, vaticanReportSections.get(index - 1).getPopeSpace());
        }
        if (index < vaticanReportSections.size() - 1) {
            right = Math.min(right, vaticanReportSections.get(index + 1).getFirstSpace());
        }

        int firstSpace = vaticanReportSections.get(index).getFirstSpace();
        return firstSpace > left && firstSpace < right;
    }

    public boolean validateVRSPopeSpace(int index) {
        int left = Math.max(0, vaticanReportSections.get(index).getFirstSpace() - 1);
        int right = finalPosition + 1;

        if (index > 0) {
            left = Math.max(left, vaticanReportSections.get(index - 1).getPopeSpace());
        }
        if (index < vaticanReportSections.size() - 1) {
            right = Math.min(right, vaticanReportSections.get(index + 1).getFirstSpace());
        }

        int popeSpace = vaticanReportSections.get(index).getPopeSpace();
        return popeSpace > left && popeSpace < right;
    }

    public boolean validateVRSPoints(int index) {
        int left = 0;
        int right = 100;

        if (index > 0) {
            left = Math.max(left, vaticanReportSections.get(index - 1).getPoints());
        }
        if (index < vaticanReportSections.size() - 1) {
            right = Math.min(right, vaticanReportSections.get(index + 1).getPoints());
        }

        int points = vaticanReportSections.get(index).getPoints();
        return points > left && points < right;
    }
}
