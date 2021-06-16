package it.polimi.ingsw.view.localModel;

public interface LocalModelElementObserver {
    void notifyObserver();

    void clean();
}
