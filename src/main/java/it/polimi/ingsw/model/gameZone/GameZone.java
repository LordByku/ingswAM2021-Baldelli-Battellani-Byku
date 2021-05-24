package it.polimi.ingsw.model.gameZone;

/**
 * GameZone is a container for markets shared between players
 */
public class GameZone {
    /**
     * marbleMarket is the resources market
     */
    private final MarbleMarket marbleMarket;
    /**
     * cardMarket is the card market
     */
    private final CardMarket cardMarket;
    /**
     * leaderCardsDeck is the deck of leader cards
     */
    private final LeaderCardsDeck leaderCardsDeck;

    /**
     * The constructor initializes marbleMarket and cardMarket
     */
    public GameZone() {
        marbleMarket = new MarbleMarket();
        cardMarket = new CardMarket();
        leaderCardsDeck = new LeaderCardsDeck();
    }

    /**
     * getMarbleMarket returns the marble market
     *
     * @return marbleMarket
     */
    public MarbleMarket getMarbleMarket() {
        return marbleMarket;
    }

    /**
     * getCardMarket returns the card market
     *
     * @return cardMarket
     */
    public CardMarket getCardMarket() {
        return cardMarket;
    }

    /**
     * getLeaderCardsDeck returns the leader cards deck
     *
     * @return leaderCardsDeck
     */
    public LeaderCardsDeck getLeaderCardsDeck() {
        return leaderCardsDeck;
    }
}
