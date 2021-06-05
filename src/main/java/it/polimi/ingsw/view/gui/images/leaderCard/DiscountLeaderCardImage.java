package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.leaderCards.DiscountLeaderCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class DiscountLeaderCardImage extends LeaderCardImage {
    public DiscountLeaderCardImage(DiscountLeaderCard leaderCard, int width) throws IOException {
        super(leaderCard.getDiscountEffect().getFilename(), leaderCard, width);

        JPanel discountPanel = new JPanel();
        discountPanel.setLayout(new GridBagLayout());
        discountPanel.setBounds(width * 5 / 9, height * 3 / 4, width / 6, height / 10);

        JLabel discountLabel = new JLabel("-" + leaderCard.getDiscountEffect().getDiscount(), SwingConstants.CENTER);
        discountLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
        discountLabel.setFont(discountLabel.getFont().deriveFont((float) height / 15));
        discountPanel.add(discountLabel);

        this.add(discountPanel);
    }
}
