package it.polimi.ingsw.gameZone;

import it.polimi.ingsw.gameZone.marbles.*;
import it.polimi.ingsw.playerBoard.Board;
import it.polimi.ingsw.playerBoard.InvalidBoardException;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

import java.util.ArrayList;
import java.util.Collections;

public class MarbleMarket {
    private Marble[][] marbles;
    private Marble freeMarble;
    private final int rows;
    private final int columns;

    MarbleMarket() {
        rows = 3;
        columns = 4;
        ArrayList<Marble> tmp = new ArrayList<>();
        tmp.add(new WhiteMarble());
        tmp.add(new WhiteMarble());
        tmp.add(new WhiteMarble());
        tmp.add(new WhiteMarble());
        tmp.add(new BlueMarble());
        tmp.add(new BlueMarble());
        tmp.add(new GreyMarble());
        tmp.add(new GreyMarble());
        tmp.add(new YellowMarble());
        tmp.add(new YellowMarble());
        tmp.add(new PurpleMarble());
        tmp.add(new PurpleMarble());
        tmp.add(new RedMarble());

        Collections.shuffle(tmp);

        marbles = new Marble[rows][columns];

        int tmpIndex = 0;
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < columns; ++j) {
                marbles[i][j] = tmp.get(tmpIndex++);
            }
        }

        freeMarble = tmp.get(tmpIndex);
    }

    public ObtainableResourceSet selectRow(int row, Board board) throws InvalidMarbleMarketIndexException, InvalidBoardException {
        if(row < 0 || row >= rows) {
            throw new InvalidMarbleMarketIndexException();
        }

        ObtainableResourceSet result = new ObtainableResourceSet();

        for(int j = 0; j < columns; ++j) {
            result.union(marbles[row][j].collect(board));
        }

        return result;
    }

    public ObtainableResourceSet selectColumn(int column, Board board) throws InvalidMarbleMarketIndexException, InvalidBoardException {
        if(column < 0 || column >= columns) {
            throw new InvalidMarbleMarketIndexException();
        }

        ObtainableResourceSet result = new ObtainableResourceSet();

        for(int i = 0; i < rows; ++i) {
            result.union(marbles[i][column].collect(board));
        }

        return result;
    }

    public MarbleColour getMarketColour(int row, int column) throws InvalidMarbleMarketIndexException {
        if(row < 0 || row >= rows ||
           column < 0 || column >= columns) {
            throw new InvalidMarbleMarketIndexException();
        }

        return marbles[row][column].getColour();
    }

    public MarbleColour getFreeMarbleColour() {
        return freeMarble.getColour();
    }

    public void pushRow(int row) throws InvalidMarbleMarketIndexException {
        Marble newFreeMarble = marbles[row][0];
        for(int j = 1; j < columns; ++j) {
            marbles[row][j - 1] = marbles[row][j];
        }
        marbles[row][columns - 1] = freeMarble;
        freeMarble = newFreeMarble;
    }

    public void pushColumn(int column) throws InvalidMarbleMarketIndexException {
        Marble newFreeMarble = marbles[0][column];
        for(int i = 1; i < rows; ++i) {
            marbles[i - 1][column] = marbles[i][column];
        }
        marbles[rows - 1][column] = freeMarble;
        freeMarble = newFreeMarble;
    }
}
