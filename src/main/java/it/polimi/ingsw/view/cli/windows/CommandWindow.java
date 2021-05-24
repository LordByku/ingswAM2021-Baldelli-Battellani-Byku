package it.polimi.ingsw.view.cli.windows;

import it.polimi.ingsw.controller.CommandType;
import it.polimi.ingsw.network.client.Client;

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
}
