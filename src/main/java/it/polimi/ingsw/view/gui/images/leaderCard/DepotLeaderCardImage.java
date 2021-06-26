package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCardDepot;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class DepotLeaderCardImage extends LeaderCardImage {
    private final ArrayList<ResourceDepotImage> resourceDepotImages;

    public DepotLeaderCardImage(DepotLeaderCard leaderCard, int width) throws IOException {
        super("4", leaderCard, width);

        JPanel depotsPanel = new JPanel();
        depotsPanel.setLayout(new BoxLayout(depotsPanel, BoxLayout.X_AXIS));
        depotsPanel.setBounds(width / 12, height * 3 / 4, width * 5 / 6, height / 5);

        LeaderCardDepot depot = leaderCard.getDepot();

        resourceDepotImages = new ArrayList<>();
        for (int i = 0; i < depot.getSlots(); ++i) {
            JPanel container = new JPanel();
            container.setLayout(new GridBagLayout());

            ResourceDepotImage resourceDepotImage = new ResourceDepotImage(depot, width / 7);
            container.add(resourceDepotImage);
            resourceDepotImages.add(resourceDepotImage);

            depotsPanel.add(container);
        }

        this.add(depotsPanel);
    }

    public ArrayList<ResourceDepotImage> getResourceDepotImages() {
        return resourceDepotImages;
    }
}
