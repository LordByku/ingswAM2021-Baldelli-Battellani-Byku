package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.devCards.*;
import it.polimi.ingsw.model.leaderCards.DepotLeaderCard;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.ProductionLeaderCard;
import it.polimi.ingsw.model.resources.ChoiceResource;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.FullChoiceSet;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.parsing.LeaderCardsParser;
import it.polimi.ingsw.view.gui.images.devCard.DevCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import java.io.IOException;

public class TextFieldExample {
    TextFieldExample() {
        JFrame f = new JFrame();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel outPanel = new JPanel();
        outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        ConcreteResourceSet requirements = new ConcreteResourceSet();
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        for(ConcreteResource resource: ConcreteResource.values()) {
            requirements.addResource(resource, 34);
            for(int i = 0; i < 56; ++i) {
                choiceResourceSet.addResource(resource);
            }
        }
        for(int i = 0; i < 56; ++i) {
            choiceResourceSet.addResource(new ChoiceResource(new FullChoiceSet()));
        }

        SpendableResourceSet productionIn = new SpendableResourceSet(choiceResourceSet);
        ObtainableResourceSet productionOut = new ObtainableResourceSet(choiceResourceSet, 56);

        DevCard devCard = new DevCard(requirements, CardColour.GREEN, CardLevel.I,
                new ProductionDetails(productionIn, productionOut), 25, 3000);

        CardTypeSet cardTypeSet = new CardTypeSet();
        for(CardColour cardColour: CardColour.values()) {
            CardTypeDetails cardTypeDetails = new CardTypeDetails(cardColour, 56, CardLevel.II);
            cardTypeSet.add(cardTypeDetails);
        }

        LeaderCard resourcesLeaderCard = new ProductionLeaderCard(10, requirements, new ProductionDetails(productionIn, productionOut), 3000);
        LeaderCard cardTypeLeaderCard = new DepotLeaderCard(10, cardTypeSet, ConcreteResource.SERVANT, 5, 3001);

        try {
            for(int i = 0; i < 16; i += 5) {
                LeaderCardImage leaderCardImage = LeaderCardsParser.getInstance().getCard(i).getLeaderCardImage(100 + i * 10);
                topPanel.add(leaderCardImage);
            }

            for(int i = 0; i < 4; ++ i) {
                LeaderCardImage leaderCardImage = resourcesLeaderCard.getLeaderCardImage(100 + i * 50);
                //topPanel.add(leaderCardImage);
            }

            for(int i = 0; i < 4; ++i) {
                LeaderCardImage leaderCardImage = cardTypeLeaderCard.getLeaderCardImage(100 + i * 50);
                bottomPanel.add(leaderCardImage);
            }

            int startIndex = 32;
            for(int i = 0; i < 4; ++i) {
                DevCardImage devCardImage = new DevCardImage("" + (startIndex + 5 * i), 200, devCard);
                //bottomPanel.add(devCardImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        outPanel.add(topPanel);
        outPanel.add(bottomPanel);

        f.setContentPane(outPanel);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            for(ResourceImageType resourceImageType: ResourceImageType.values()) {
               resourceImageType.loadImage();
            }
            for(CardColour cardColour: CardColour.values()) {
                cardColour.loadImage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextFieldExample textFieldExample = new TextFieldExample();
    }
}