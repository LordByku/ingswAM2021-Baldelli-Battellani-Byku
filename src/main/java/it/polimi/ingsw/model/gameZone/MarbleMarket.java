package it.polimi.ingsw.model.gameZone;

import it.polimi.ingsw.model.gameZone.marbles.*;
import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.InvalidChoiceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * MarbleMarket represents the resources market
 */
public class MarbleMarket {
    /**
     * marbles is the current state of the market
     */
    private final Marble[][] marbles;
    /**
     * rows is the number of rows in the market
     */
    private final int rows;
    /**
     * columns is the number of columns in the marker
     */
    private final int columns;
    /**
     * freeMarble is the marble outside the current market
     */
    private Marble freeMarble;

    /**
     * The constructor initializes rows and columns to 3 and 4 respectively
     * 13 Marbles are created and randomly placed in marbles and in freeMarble
     */
    public MarbleMarket() {
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
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                marbles[i][j] = tmp.get(tmpIndex++);
            }
        }

        freeMarble = tmp.get(tmpIndex);
    }

    /**
     * selectRow returns the ObtainableResourceSet obtained by selecting a given row of the market
     *
     * @param row       The chosen row
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet representing all resources collected by selecting the given row
     * @throws InvalidMarbleMarketIndexException row is outside the range of marbles
     * @throws InvalidChoiceSetException         choiceSet is null
     */
    public ObtainableResourceSet selectRow(int row, ChoiceSet choiceSet)
            throws InvalidMarbleMarketIndexException, InvalidChoiceSetException {
        if (row < 0 || row >= rows) {
            throw new InvalidMarbleMarketIndexException();
        }

        ObtainableResourceSet result = new ObtainableResourceSet();

        for (int j = 0; j < columns; ++j) {
            result = result.union(marbles[row][j].collect(choiceSet));
        }

        return result;
    }

    /**
     * selectColumn returns the ObtainableResourceSet obtained by selecting a given column of the market
     *
     * @param column    The chosen column
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet representing all resources collected by selecting the given column
     * @throws InvalidMarbleMarketIndexException column is outside the range of marbles
     * @throws InvalidChoiceSetException         choiceSet is null
     */
    public ObtainableResourceSet selectColumn(int column, ChoiceSet choiceSet)
            throws InvalidMarbleMarketIndexException, InvalidChoiceSetException {
        if (column < 0 || column >= columns) {
            throw new InvalidMarbleMarketIndexException();
        }

        ObtainableResourceSet result = new ObtainableResourceSet();

        for (int i = 0; i < rows; ++i) {
            result = result.union(marbles[i][column].collect(choiceSet));
        }

        return result;
    }

    /**
     * getMarketColour returns the MarbleColour of a given Marble in the market
     *
     * @param row    The chosen row
     * @param column The chosen column
     * @return The MarbleColour of the Marble in position row, column in marbles
     * @throws InvalidMarbleMarketIndexException row or column is outside the range of marbles
     */
    public MarbleColour getMarketColour(int row, int column) throws InvalidMarbleMarketIndexException {
        if (row < 0 || row >= rows ||
                column < 0 || column >= columns) {
            throw new InvalidMarbleMarketIndexException();
        }

        return marbles[row][column].getColour();
    }

    /**
     * getFreeMarbleColour returns the MarbleColour of the free marble
     *
     * @return The MarbleColour of freeMarble
     */
    public MarbleColour getFreeMarbleColour() {
        return freeMarble.getColour();
    }

    /**
     * pushRow applies the operation of pushing the free marble into a given row
     *
     * @param row The chosen row
     * @throws InvalidMarbleMarketIndexException row is outside the range of marbles
     */
    public void pushRow(int row) throws InvalidMarbleMarketIndexException {
        if (row < 0 || row >= rows) {
            throw new InvalidMarbleMarketIndexException();
        }
        Marble newFreeMarble = marbles[row][0];
        for (int j = 1; j < columns; ++j) {
            marbles[row][j - 1] = marbles[row][j];
        }
        marbles[row][columns - 1] = freeMarble;
        freeMarble = newFreeMarble;
    }

    /**
     * pushColumn applies the operation of pushing the free marble into a given column
     *
     * @param column The chosen column
     * @throws InvalidMarbleMarketIndexException column is outside the range of marbles
     */
    public void pushColumn(int column) throws InvalidMarbleMarketIndexException {
        if (column < 0 || column >= columns) {
            throw new InvalidMarbleMarketIndexException();
        }
        Marble newFreeMarble = marbles[0][column];
        for (int i = 1; i < rows; ++i) {
            marbles[i - 1][column] = marbles[i][column];
        }
        marbles[rows - 1][column] = freeMarble;
        freeMarble = newFreeMarble;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
