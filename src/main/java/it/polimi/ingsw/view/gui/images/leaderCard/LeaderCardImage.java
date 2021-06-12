package it.polimi.ingsw.view.gui.images.leaderCard;

import it.polimi.ingsw.editor.gui.components.ButtonClickEvent;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.view.gui.GUIUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class LeaderCardImage extends JPanel {
    protected final int width;
    protected final int height;
    private final Image image;

    protected LeaderCardImage(String filename, LeaderCard leaderCard, int width) throws IOException {
        super(null);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("LeaderCardPNGs/" + filename + ".png");
        image = ImageIO.read(resource);
        this.width = width;
        this.height = (int) (1.5 * width);

        this.setPreferredSize(new Dimension(width, height));

        this.add(leaderCard.getRequirements().getRequirementsPanel(width, height));

        JPanel pointsPanel = new JPanel();
        pointsPanel.setLayout(new GridBagLayout());
        pointsPanel.setBounds(width * 4 / 9, height * 5 / 9, width / 9, height / 12);

        JLabel pointsLabel = new JLabel("" + leaderCard.getPoints(), SwingConstants.CENTER);
        pointsLabel.setBorder(new EmptyBorder(1, 1, 1, 1));
        pointsLabel.setFont(pointsLabel.getFont().deriveFont((float) height / 20));
        pointsPanel.add(pointsLabel);

        this.add(pointsPanel);

        this.addMouseListener(new ButtonClickEvent((e) -> {
            PopupFactory popupFactory = PopupFactory.getSharedInstance();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int cardWidth = 500;
            int cardHeight = 750;

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            Popup popup = popupFactory.getPopup(LeaderCardImage.this, container, (screenSize.width - cardWidth) / 2, (screenSize.height - cardHeight) / 2);

            LeaderCardImage zoomed = leaderCard.getLeaderCardImage(cardWidth);
            zoomed.addButton("x", new ButtonClickEvent((event) -> popup.hide()), new Rectangle(cardWidth - 27, 0, 27, 27));
            container.add(zoomed);

            popup.show();
        }));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }

    private void addButton(String text, ButtonClickEvent clickEvent, Rectangle bounds) {
        JPanel container = new JPanel();
        container.setBounds(bounds);
        GUIUtil.addButton(text, container, clickEvent);
        this.add(container);
    }
}
