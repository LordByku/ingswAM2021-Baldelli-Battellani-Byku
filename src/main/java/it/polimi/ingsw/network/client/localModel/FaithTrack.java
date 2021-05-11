package it.polimi.ingsw.network.client.localModel;

import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;
import it.polimi.ingsw.model.playerBoard.faithTrack.VaticanReportSection;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.parsing.VRSParser;
import it.polimi.ingsw.view.cli.BackGroundColor;
import it.polimi.ingsw.view.cli.CLIPrintable;
import it.polimi.ingsw.view.cli.Strings;
import it.polimi.ingsw.view.cli.TextColour;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class FaithTrack implements CLIPrintable {
    private int position;
    private ArrayList<Integer> receivedFavors;

    @Override
    public String getCLIString() {
        int faithTrackLength = LocalConfig.getInstance().getFaithTrackFinalPosition();
        ArrayList<CheckPoint> checkPoints = LocalConfig.getInstance().getFaithTrackCheckPoints();
        ArrayList<VaticanReportSection> vaticanReportSections = new ArrayList<>();

        VaticanReportSection vaticanReportSection;
        while((vaticanReportSection = VRSParser.getInstance().getNextVRS()) != null) {
            vaticanReportSections.add(vaticanReportSection);
        }

        StringBuilder result = new StringBuilder();
        for(int i = 0, checkPointIndex = 0; i <= faithTrackLength; ++i) {
            result.append(" ");
            if(checkPointIndex < checkPoints.size() && checkPoints.get(checkPointIndex).getPosition() == i) {
                result.append(TextColour.YELLOW.escape()).append(Strings.format(checkPoints.get(checkPointIndex).getPoints(), 2)).append(TextColour.RESET);
                checkPointIndex++;
            } else {
                result.append("  ");
            }
        }

        result.append(" \n");

        for(int i = 0, vrsIndex = 0; i <= faithTrackLength; ++i) {
            result.append("|");

            if(vrsIndex < vaticanReportSections.size() && vaticanReportSections.get(vrsIndex).isInsideSection(i)) {
                if(vaticanReportSections.get(vrsIndex).getPopeSpace() == i) {
                    result.append(BackGroundColor.RED.escape());
                    vrsIndex++;
                } else {
                    result.append(BackGroundColor.YELLOW.escape());
                }
            }

            if(position == i) {
                result.append(TextColour.WHITE.escape()).append(" \u25cf").append(TextColour.RESET);
            } else {
                result.append(Strings.format(i, 2));
            }

            result.append(BackGroundColor.RESET);
        }

        result.append("|\n");

        boolean prevPopeSpace = false;
        for(int i = 0, vrsIndex = 0; i <= faithTrackLength; ++i) {

            if(vrsIndex < vaticanReportSections.size() && vaticanReportSections.get(vrsIndex).getPopeSpace() == i) {
                result.append("|");
                if(receivedFavors.contains(vaticanReportSections.get(vrsIndex).getId())) {
                    result.append(TextColour.RED.escape()).append(Strings.format(vaticanReportSections.get(vrsIndex).getPoints(), 2)).append(TextColour.RESET);
                } else {
                    result.append(TextColour.RED.escape()).append(" x").append(TextColour.RESET);
                }
                vrsIndex++;
                prevPopeSpace = true;
            } else {
                if(prevPopeSpace) {
                    result.append("|  ");
                } else {
                    result.append("   ");
                }
                prevPopeSpace = false;
            }
        }

        if(prevPopeSpace) {
            result.append("|");
        } else {
            result.append(" ");
        }

        return result.toString();
    }
}
