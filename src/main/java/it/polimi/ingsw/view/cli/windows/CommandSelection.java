package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.localModel.Player;

public class CommandSelection extends CLIWindow {
    private boolean endTurnRequested;
    private boolean turnStarted;

    public CommandSelection(Client client) {
        endTurnRequested = false;
        Player self = client.getModel().getPlayer(client.getNickname());
        turnStarted = self.hasInkwell();
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        try {
            int selection = Integer.parseInt(line);
            Player self = client.getModel().getPlayer(client.getNickname());

            if (self.mainAction()) {
                switch (selection) {
                    case 0: {
                        JsonObject message = client.buildEndTurnMessage();
                        client.write(message.toString());
                        endTurnRequested = true;
                        return;
                    }
                    case 1: {
                        JsonObject message = client.buildRequestMessage(CommandType.PLAYLEADER);
                        client.write(message.toString());
                        return;
                    }
                    case 2: {
                        JsonObject message = client.buildRequestMessage(CommandType.DISCARDLEADER);
                        client.write(message.toString());
                        return;
                    }
                }
            } else {
                switch (selection) {
                    case 0: {
                        JsonObject message = client.buildRequestMessage(CommandType.MARKET);
                        client.write(message.toString());
                        return;
                    }
                    case 1: {
                        JsonObject message = client.buildRequestMessage(CommandType.PURCHASE);
                        client.write(message.toString());
                        return;
                    }
                    case 2: {
                        JsonObject message = client.buildRequestMessage(CommandType.PRODUCTION);
                        client.write(message.toString());
                        return;
                    }
                    case 3: {
                        JsonObject message = client.buildRequestMessage(CommandType.PLAYLEADER);
                        client.write(message.toString());
                        return;
                    }
                    case 4: {
                        JsonObject message = client.buildRequestMessage(CommandType.DISCARDLEADER);
                        client.write(message.toString());
                        return;
                    }
                }
            }
        } catch (NumberFormatException e) {
        }

        CLI.renderGameWindow(client);
    }

    @Override
    public void render(Client client) {
        Player self = client.getModel().getPlayer(client.getNickname());
        if (self.hasInkwell()) {
            if (self.mainAction()) {
                CLI.endTurn();
            } else {
                CLI.actionToken(client.getModel());
                CLI.startTurn();
            }
        } else {
            CLI.waitTurn();
        }
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        if (endTurnRequested) {
            endTurnRequested = false;
            return true;
        }
        Player self = client.getModel().getPlayer(client.getNickname());
        if (self.hasInkwell()) {
            if (turnStarted) {
                return false;
            } else {
                turnStarted = true;
                return true;
            }
        } else {
            turnStarted = false;
            return false;
        }
    }
}
