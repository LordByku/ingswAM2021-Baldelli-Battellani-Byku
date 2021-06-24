package it.polimi.ingsw.view.localModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.ingsw.view.localModel.LocalModelElementObserver.NotificationSource;

import java.util.ArrayList;

public abstract class LocalModelElement {
    protected final transient Gson gson = new Gson();
    private final ArrayList<LocalModelElementObserver> observers = new ArrayList<>();

    public abstract void updateModel(JsonElement jsonElement);

    public void addObserver(LocalModelElementObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(LocalModelElementObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notifyObservers(NotificationSource notificationSource) {
        synchronized (observers) {
            System.out.println(observers.size());
            for (LocalModelElementObserver observer : observers) {
                System.out.println(observer);
                observer.notifyObserver(notificationSource);
            }
        }
    }
}
