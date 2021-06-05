package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.model.leaderCards.WhiteConversionLeaderCard;

import java.io.IOException;

public class ConversionLeaderCardImage extends LeaderCardImage {
    public ConversionLeaderCardImage(WhiteConversionLeaderCard leaderCard, int width) throws IOException {
        super(leaderCard.getConversionEffect().getFilename(), leaderCard, width);
    }
}
