package it.polimi.ingsw.view.localModel;

public interface LocalModelElementObserver {
    void notifyObserver(NotificationSource notificationSource);

    void clean();

    enum NotificationSource {
        ACTIONTOKENDECK,
        BOARD,
        CARDMARKET,
        CARDMARKETDECK,
        COMMANDELEMENT,
        DEVCARDSAREA,
        FAITHTRACK,
        GAMEZONE,
        HANDLEADERCARDSAREA,
        LOCALMODEL,
        MARBLEMARKET,
        PLAYEDLEADERCARDSAREA,
        PLAYER,
        STRONGBOX,
        WAREHOUSE
    }
}
