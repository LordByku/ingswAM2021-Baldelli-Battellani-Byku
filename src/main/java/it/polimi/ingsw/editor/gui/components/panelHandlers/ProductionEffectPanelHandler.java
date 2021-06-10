package it.polimi.ingsw.editor.gui.components.panelHandlers;

import it.polimi.ingsw.editor.model.simplifiedModel.leaderCards.effects.ProductionEffect;

import javax.swing.*;

public class ProductionEffectPanelHandler extends PanelHandler {
    private final ProductionEffect productionEffect;

    protected ProductionEffectPanelHandler(JFrame frame, JPanel panel, ProductionEffect productionEffect) {
        super(frame, panel);
        this.productionEffect = productionEffect;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    @Override
    public void build() {
        panel.removeAll();

        JPanel spendablePanel = new JPanel();
        JPanel obtainablePanel = new JPanel();

        panel.add(spendablePanel);
        panel.add(obtainablePanel);

        SpendablePanelHandler spendablePanelHandler = new SpendablePanelHandler(frame, spendablePanel, productionEffect.getProductionIn());
        spendablePanelHandler.build();

        ObtainablePanelHandler obtainablePanelHandler = new ObtainablePanelHandler(frame, obtainablePanel, productionEffect.getProductionOut());
        obtainablePanelHandler.build();

        frame.setVisible(true);
    }

    @Override
    public boolean validate() {
        return true;
    }
}
