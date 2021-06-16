package it.polimi.ingsw.view.localModel;

import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.controller.CommandType;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandElement {
    private CommandBuffer commandBuffer;
    private HashMap<CommandType, ArrayList<LocalModelElementObserver>> observers = new HashMap<>();

    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void addObserver(LocalModelElementObserver observer, CommandType commandType) {
        if (!observers.containsKey(commandType)) {
            observers.put(commandType, new ArrayList<>());
        }
        observers.get(commandType).add(observer);
    }

    public void removeObserver(LocalModelElementObserver observer, CommandType commandType) {
        observers.get(commandType).remove(observer);
    }

    protected void notifyObservers(CommandType commandType) {
        if (observers.containsKey(commandType)) {
            for (LocalModelElementObserver observer : observers.get(commandType)) {
                observer.notifyObserver();
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
