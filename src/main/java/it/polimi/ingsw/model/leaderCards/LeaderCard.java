package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.playerBoard.Board;
import it.polimi.ingsw.model.playerBoard.InvalidBoardException;
import it.polimi.ingsw.model.playerBoard.Scoring;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.view.cli.Strings;

import java.util.HashSet;

/**
 * LeaderCard refers to all the leader cards.
 */

public abstract class LeaderCard implements Scoring {

    /**
     * points are the victory points of the card.
     */
    private int points;

    /**
     * requirements needed to play the card.
     */
    private LeaderCardRequirements requirements;

    /**
     * board is the reference to the board.
     */
    protected Board board;

    /**
     * attribute active used to know if the card's been played.
     */
    protected boolean active = false;

    /**
     * attribute discard used to know if the card's been discarded.
     */
    protected boolean discarded = false;

    /**
     * id is the unique id of this card
     */
    private final int id;

    /**
     * usedIds is a container for the ids already used for leader cards
     */
    private final static HashSet<Integer> usedIds = new HashSet<>();

    protected static int width = 0;

    public LeaderCard(int points, LeaderCardRequirements requirements, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidIdException {
        if(points<=0){
            throw new InvalidPointsValueException();
        }
        if(requirements == null){
            throw new InvalidRequirementsException();
        }
        if(id < 0 || usedIds.contains(id)) {
            throw new InvalidIdException();
        }
        this.points = points;
        this.requirements = (LeaderCardRequirements) requirements.clone();
        this.id = id;
        usedIds.add(id);
    }

    /**
     * isPlayable calls the Board to check if we match the requirements
     * @return false if we don't match the requirements or the card's been already played or discarded, true otherwise.
     */
    public boolean isPlayable(){
        if(active || discarded)
            return false;
        try {
            return requirements.isSatisfied(this.board);
        } catch (InvalidBoardException e) {
            return false;
        }
    }

    /**
     * @return if the card's been played.
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Check if the card is playable and plays it.
     */
    public abstract void play() throws InvalidResourceException;

    /**
     * Discard the leader card to get 1 faith point.
     */
    public void discard(){
        if(!active && !discarded) {
            discarded = true;
            board.getFaithTrack().addFaithPoints();
            board.getLeaderCardArea().removeLeaderCard(this);
        }
    }

    /**
     * initDiscard allows to discard the leaderCard without getting faithPoints.
     * Used when the game starts and the leaderCards are hand out.
     */
    public void initDiscard(){
        if(!active && !discarded) {
            discarded = true;
            board.getLeaderCardArea().removeLeaderCard(this);
        }
    }

    /**
     * Assign the LeaderCard to the board.
     * @param board containing the LeaderCard.
     * @throws InvalidBoardException board is null
     */
    public void assignToBoard(Board board) throws InvalidBoardException {
        if(board == null) {
            throw new InvalidBoardException();
        }
        this.board = board.clone();
        board.getLeaderCardArea().addLeaderCard(this);
    }

    /**
     * getPoints returns the amount of points awarded by this LeaderCard
     * @return The amount of points awarded by this LeaderCard
     */
    public int getPoints() {
        return points;
    }

    public abstract String getEffectString();

    public void addCLISupport() {
        width = Math.max(width, Strings.getGraphemesCount(requirements.toString()));
        width = Math.max(width, Strings.getGraphemesCount(getEffectString()));
    }

    @Override
    public String toString() {
        String requirements = this.requirements.toString();
        int requirementsLength = Strings.getGraphemesCount(requirements);
        StringBuilder result = new StringBuilder(" ");
        for(int i = 0; i < width; ++i) {
            result.append("_");
        }
        result.append(" \n|").append(requirements);
        for(int i = requirementsLength; i < width; ++i) {
            result.append(" ");
        }
        result.append("|\n|");
        for(int i = 0; i < width; ++i) {
            result.append(" ");
        }
        result.append("|\n");

        String points = "(" + this.points + ")";
        int pointsLength = Strings.getGraphemesCount(points);
        buildCenteredRow(result, points, pointsLength);

        result.append("|");
        for(int i = 0; i < width; ++i) {
            result.append(" ");
        }
        result.append("|\n");

        String effect = getEffectString();
        int effectLength = Strings.getGraphemesCount(effect);
        buildCenteredRow(result, effect, effectLength);
        result.append("|");
        for(int i = 0; i < width; ++i) {
            result.append("_");
        }
        result.append("|");
        return result.toString();
    }

    public int getId() {
        return id;
    }

    private void buildCenteredRow(StringBuilder result, String str, int strLen) {
        result.append("|");
        for(int i = 0; i < (width - strLen) / 2; ++i) {
            result.append(" ");
        }
        result.append(str);
        for(int i = (width - strLen) / 2 + strLen; i < width; ++i) {
            result.append(" ");
        }
        result.append("|\n");
    }
}
