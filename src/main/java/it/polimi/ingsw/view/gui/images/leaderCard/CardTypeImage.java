package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.devCards.CardLevel;
import it.polimi.ingsw.model.devCards.CardType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.TreeSet;

public class CardTypeImage extends JPanel {
    private final Image image;
    private final int width;
    private final int height;

    public CardTypeImage(CardType cardType, int width) {
        super(null);
        image = cardType.getColour().getImage();
        this.width = width;
        this.height = width * 6 / 5;

        TreeSet<CardLevel> levels = cardType.getLevelSet();

        this.setLayout(new BorderLayout());

        if(levels.size() < 3) {
            JPanel container = new JPanel();
            container.setLayout(new GridBagLayout());
            container.setOpaque(false);

            for(CardLevel level: levels) {
                JLabel label = new JLabel(level.getCLIString(), SwingConstants.CENTER);
                label.setBorder(new EmptyBorder(1, 1, 1, 1));
                label.setFont(label.getFont().deriveFont((float) height * 2 / 5));
                container.add(label);
            }

            //this.add(container, BorderLayout.NORTH);
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
