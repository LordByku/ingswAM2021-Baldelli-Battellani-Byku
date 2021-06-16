package it.polimi.ingsw.view.gui.board.DevCardsArea;

import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.SpendableResourceSet;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.images.devCardsArea.DefaultProductionImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class GUIDefaultProductionPower {

    JPanel devCardsArea;
    ProductionDetails defaultProductionPower;
    SpendableResourceSet input;
    ObtainableResourceSet output;
    JPanel defProdPower;
    JPanel inputPanel;
    JPanel outputPanel;
    GridBagConstraints inputC;

    public GUIDefaultProductionPower(JPanel devCardsArea) {
        this.devCardsArea = devCardsArea;
        defaultProductionPower = LocalConfig.getInstance().getDefaultProductionPower();
        input = defaultProductionPower.getInput();
        output = defaultProductionPower.getOutput();
        try {
            defProdPower = new DefaultProductionImage("Punchboard/scroll.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputPanel = new JPanel(new GridBagLayout());
        outputPanel = new JPanel(new GridBagLayout());
        inputC = new GridBagConstraints();
    }

    public void loadDefaultProductionPower() {
        int numOfChoiceR;
        numOfChoiceR = input.getResourceSet().getChoiceResources().size();
        loadResources(numOfChoiceR, true); //load input
        numOfChoiceR = output.getResourceSet().getChoiceResources().size();
        loadResources(numOfChoiceR, false); // load output

        defProdPower.setVisible(true);
        defProdPower.setBorder(new LineBorder(Color.BLACK));
        inputC.gridx = 0;
        inputC.gridy = 0;
        inputC.insets = new Insets(0, 0, 0, 5);
        defProdPower.add(inputPanel, inputC);
        inputC.gridx++;
        inputC.insets = new Insets(0, 5, 0, 0);
        defProdPower.add(outputPanel, inputC);
        inputC.gridx = 0;
        inputC.gridy = 0;
        inputC.weightx = 0.25;
        inputC.insets = new Insets(0, 0, 0, 0);
        devCardsArea.add(defProdPower, inputC);
    }

    private void loadResources(int numOfChoiceR, boolean in) {
        JPanel loadingPanel;
        loadingPanel = in ? inputPanel : outputPanel;

        if (numOfChoiceR > 0) {
            JPanel choiceLabelIcon = new ResourceImage(ResourceImageType.CHOICE, 20);
            JLabel choiceLabelQuantity = new JLabel(Integer.toString(numOfChoiceR));
            inputC.gridx = 0;
            inputC.gridy = 0;
            loadingPanel.add(choiceLabelQuantity, inputC);
            inputC.gridx++;
            loadingPanel.add(choiceLabelIcon, inputC);
            inputC.gridy++;
        }

        for (ConcreteResource concreteResource : ConcreteResource.values()) {
            int count = input.getResourceSet().getConcreteResources().getCount(concreteResource);
            if (count > 0) {
                JPanel resourcePanel = new ResourceImage(concreteResource.getResourceImageType(), 20);
                JLabel quantity = new JLabel(Integer.toString(count));
                inputC.gridx = 0;
                loadingPanel.add(quantity, inputC);
                inputC.gridx++;
                loadingPanel.add(resourcePanel, inputC);
                inputC.gridy++;
            }
        }

    }
}
