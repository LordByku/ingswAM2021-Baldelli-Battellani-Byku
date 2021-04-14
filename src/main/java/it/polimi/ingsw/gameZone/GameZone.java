package it.polimi.ingsw.gameZone;

public class GameZone {
    private MarbleMarket marbleMarket;
    private CardMarket cardMarket;

    public GameZone() {
        marbleMarket = new MarbleMarket();
        cardMarket = new CardMarket();
    }
}
