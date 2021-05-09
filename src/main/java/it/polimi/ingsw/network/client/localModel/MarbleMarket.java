package it.polimi.ingsw.network.client.localModel;

import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;

import java.util.ArrayList;

public class MarbleMarket {
    private MarbleColour[][] market;
    private MarbleColour freeMarble;

    public int getRows() {
        return market.length;
    }

    public int getColumns() {
        if(getRows() == 0) {
            return 0;
        }
        return market[0].length;
    }

    public String getCLIString() {
        StringBuilder result = new StringBuilder(" ");
        for(int i = 0; i < getColumns(); ++i) {
            result.append(" ");
        }
        result.append(freeMarble.getCLIString()).append("\n");
        for (MarbleColour[] marbleColours : market) {
            result.append(" ");
            for (MarbleColour marbleColour : marbleColours) {
                result.append(marbleColour.getCLIString());
            }
            result.append("<\n");
        }
        result.append(" ");
        for(int i = 0; i < getColumns(); ++i) {
            result.append("^");
        }
        result.append(" \n");
        return result.toString();
    }
}
