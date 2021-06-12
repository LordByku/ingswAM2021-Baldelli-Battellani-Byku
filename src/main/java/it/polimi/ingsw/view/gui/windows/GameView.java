package it.polimi.ingsw.view.gui.windows;

import it.polimi.ingsw.network.client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.BlockingQueue;

public class GameView {
    private JPanel panel;
    private JLabel nickname3;
    private JLabel nickname1;
    private JLabel nickname4;
    private JLabel nickname2;
    private JButton viewBoard3Button;
    private JButton viewBoard4Button;
    private JButton viewBoard1Button;
    private JButton viewBoard2Button;
    private JButton button1;
    private JPanel strongbox1;
    private JPanel strongbox3;
    private JPanel strongbox4;
    private JPanel strongbox2;

    public GameView(Client client, BlockingQueue<String> buffer) {

        viewBoard1Button.setEnabled(true);
        viewBoard2Button.setEnabled(true);
        viewBoard3Button.setEnabled(true);
        viewBoard4Button.setEnabled(true);
        viewBoard1Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        viewBoard2Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        viewBoard3Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        viewBoard4Button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

    }

    public void refreshWindow(JFrame frame) {
        frame.setVisible(true);
    }

}
