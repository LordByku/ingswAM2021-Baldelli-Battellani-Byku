package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.CardTypeDetails;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CardTypeImage extends JPanel {
    private final Image image;
    private final int width;
    private final int height;

    public CardTypeImage(CardTypeDetails cardTypeDetails, int width) {
        super(null);
        image = cardTypeDetails.getCardColour().getImage();
        this.width = width;
        this.height = width * 6 / 5;

        CardLevel level = cardTypeDetails.getCardLevel();

        this.setLayout(new BorderLayout());

        if(level != null) {
            JPanel container = new JPanel();
            container.setLayout(new GridBagLayout());
            container.setOpaque(false);

            JLabel label = new JLabel(level.getCLIString(), SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(1, 1, 1, 1));
            label.setFont(label.getFont().deriveFont((float) height / 2));
            container.add(label);

            this.add(container);
        }

        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }
}
