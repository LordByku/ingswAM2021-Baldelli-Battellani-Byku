package it.polimi.ingsw.view.cli.windows;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.utility.JsonUtil;
import it.polimi.ingsw.utility.UserParser;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Strings;

public class InitResources extends CommandWindow {
    private final ConcreteResourceSet[] assignment;
    private int selectionsLeft;
    private int currentDepotIndex;

    public InitResources(Client client) {
        selectionsLeft = LocalConfig.getInstance().getInitialResources(client.getNickname());
        System.out.println(selectionsLeft);
        currentDepotIndex = 0;
        assignment = new ConcreteResourceSet[LocalConfig.getInstance().getNumberOfDepots()];
        for (int i = 0; i < assignment.length; ++i) {
            assignment[i] = new ConcreteResourceSet();
        }
    }

    @Override
    public void handleUserMessage(Client client, String line) {
        String[] words = Strings.splitLine(line);
        try {
            ConcreteResourceSet selection = UserParser.getInstance().readUserResources(words);
            assignment[currentDepotIndex].union(selection);

            selectionsLeft -= selection.size();
            if (selectionsLeft > 0) {
                currentDepotIndex = (currentDepotIndex + 1) % assignment.length;
                CLI.getInstance().renderWindow(client);
            } else {
                JsonObject message = buildCommandMessage("resources", JsonUtil.getInstance().serialize(assignment));
                client.write(message.toString());
            }
        } catch (JsonSyntaxException | InvalidResourceException e) {
            CLI.getInstance().renderWindow(client);
        }
    }

    @Override
    public void render(Client client) {
        CLI.getInstance().initResources(selectionsLeft, currentDepotIndex);
    }

    @Override
    public boolean refreshOnUpdate(Client client) {
        return client.getModel().allInitResources();
    }
}
