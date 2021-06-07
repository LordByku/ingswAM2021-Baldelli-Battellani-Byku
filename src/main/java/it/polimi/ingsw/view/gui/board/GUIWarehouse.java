package it.polimi.ingsw.view.gui.board;

import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.resourceSets.ConcreteResourceSet;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.LocalConfig;
import it.polimi.ingsw.view.gui.images.resources.ResourceImage;
import it.polimi.ingsw.view.gui.images.resources.ResourceImageType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class GUIWarehouse {
    JPanel warehousePanel;
    Client client;
    int numOfDepots;
    ArrayList<Integer> depotSizes;
    ArrayList<ConcreteResourceSet> warehouse;

    public GUIWarehouse(Client client, JPanel warehousePanel){
        this.client=client;
        this.warehousePanel =warehousePanel;
        numOfDepots = LocalConfig.getInstance().getNumberOfDepots();
        warehouse = client.getModel().getPlayer(client.getNickname()).getBoard().getWarehouse();
        depotSizes = LocalConfig.getInstance().getDepotSizes();
    }

    public void loadWarehouse(){
        GridBagConstraints c = new GridBagConstraints();
        int count=0;
        int j;
        c.weightx=0.5;
        ConcreteResource concreteResource = null;
        for(int i=0; i<numOfDepots; i++){
            JPanel depotPanel = new JPanel(new GridBagLayout());
            ConcreteResourceSet depot = warehouse.get(i);
            for(ConcreteResource resource: ConcreteResource.values()) {
                count = depot.getCount(resource);
                concreteResource = resource;
            }
            for(j=0; j<depotSizes.get(i); ++j) {
                if (j < count && concreteResource!=null) {
                    JPanel resourcePanel = new ResourceImage(concreteResource.getResourceImageType(), 20);
                    depotPanel.add(resourcePanel, c);
                }else {
                    Image empty = Toolkit.getDefaultToolkit().getImage("src/main/resources/Depots/emptyDepotSlot.png");
                    JLabel emptyResourceLabel = new JLabel();
                    emptyResourceLabel.setIcon(new ImageIcon(empty.getScaledInstance(20,20, Image.SCALE_SMOOTH)));
                    depotPanel.add(emptyResourceLabel,c);
                }
                c.gridx++;
            }
            c.gridx=0;
            c.gridy=i;
            warehousePanel.add(depotPanel, c);
        }
    }
}
