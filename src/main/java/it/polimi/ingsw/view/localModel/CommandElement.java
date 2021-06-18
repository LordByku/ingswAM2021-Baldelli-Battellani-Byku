package it.polimi.ingsw.view.localModel;

import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandElement {
    private final HashMap<CommandType, ArrayList<LocalModelElementObserver>> observers = new HashMap<>();
    private CommandBuffer commandBuffer;

    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void addObserver(LocalModelElementObserver observer, CommandType commandType) {
        synchronized (observers) {
            if (!observers.containsKey(commandType)) {
                observers.put(commandType, new ArrayList<>());
            }
            observers.get(commandType).add(observer);
        }
    }

    public synchronized void removeObserver(LocalModelElementObserver observer, CommandType commandType) {
        synchronized (observers) {
            observers.get(commandType).remove(observer);
        }
    }

    protected synchronized void notifyObservers(CommandType commandType) {
        synchronized (observers) {
            if (observers.containsKey(commandType)) {
                for (LocalModelElementObserver observer : observers.get(commandType)) {
                    observer.notifyObserver();
                }
            }
        }
    }

    public void updateCommand(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
        if (commandBuffer != null) {
            notifyObservers(commandBuffer.getCommandType());
        }
    }
}
