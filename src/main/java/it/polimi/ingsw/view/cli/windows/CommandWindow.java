package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.view.cli.CLI;

public abstract class CommandWindow extends CLIWindow {
    public static CLIWindow build(Client client, CommandType commandType) {
        switch (commandType) {
            case INITDISCARD:
                return new InitDiscard(client);
            case INITRESOURCES:
                return new InitResources(client);
            case DISCARDLEADER:
                return new DiscardLeader(client);
            case PLAYLEADER:
                return new PlayLeader(client);
            case MARKET:
                return new MarketWindow(client);
            case PURCHASE:
                return new PurchaseWindow(client);
            case PRODUCTION:
                return new ProductionWindow(client);
            default:
                return null;
        }
    }

    protected void handleLeaderCard(Client client, String line) {
        if (line.equals("x")) {
            client.write(buildCancelMessage().toString());
            return;
        }

        try {
            int index = Integer.parseInt(line);
            JsonObject message = buildCommandMessage("index", JsonUtil.getInstance().serialize(index));
            client.write(message.toString());
            return;
        } catch (NumberFormatException e) {
        }

        CLI.renderWindow(client);
    }
}
