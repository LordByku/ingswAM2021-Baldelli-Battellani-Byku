package it.polimi.ingsw.view.localModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;

public abstract class LocalModelElement {
    protected final transient Gson gson = new Gson();
    private final ArrayList<LocalModelElementObserver> observers = new ArrayList<>();

    public abstract void updateModel(JsonElement jsonElement);

    public void addObserver(LocalModelElementObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LocalModelElementObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        System.out.println(observers.size());
        for (LocalModelElementObserver observer : observers) {
            System.out.println(observer);
            observer.notifyObserver();
        }
    }
}
