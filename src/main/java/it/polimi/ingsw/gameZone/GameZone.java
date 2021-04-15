package it.polimi.ingsw.gameZone;

/**
 * GameZone is a container for markets shared between players
 */
public class GameZone {
    /**
     * marbleMarket is the resources market
     */
    private MarbleMarket marbleMarket;
    /**
     * cardMarket is the card market
     */
    private CardMarket cardMarket;

    /**
     * The constructor initializes marbleMarket and cardMarket
     */
    public GameZone() {
        marbleMarket = new MarbleMarket();
        cardMarket = new CardMarket();
    }
}
