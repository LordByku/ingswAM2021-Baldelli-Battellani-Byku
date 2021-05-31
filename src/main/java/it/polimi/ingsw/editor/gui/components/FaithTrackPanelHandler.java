package it.polimi.ingsw.editor.gui.components;

import it.polimi.ingsw.editor.model.Config;
import it.polimi.ingsw.editor.model.FaithTrackEditor;
import it.polimi.ingsw.model.playerBoard.faithTrack.CheckPoint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FaithTrackPanelHandler {
    private final JFrame frame;
    private final JPanel panel;
    private final JFormattedTextField faithTrackLengthTextField;
    private final FaithTrackEditor faithTrackEditor;
    private final GridBagConstraints gbc;

    public FaithTrackPanelHandler(JFrame frame, JPanel panel, JFormattedTextField faithTrackLengthTextField) {
        this.frame = frame;
        this.panel = panel;
        this.faithTrackLengthTextField = faithTrackLengthTextField;
        faithTrackEditor = Config.getInstance().getFaithTrackEditor();

        faithTrackLengthTextField.setValue(faithTrackEditor.getFinalPosition());
        faithTrackLengthTextField.getDocument().addDocumentListener(new TextFieldDocumentListener(
                faithTrackLengthTextField, faithTrackEditor::setFinalPosition
        ));

        gbc = new GridBagConstraints();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    }

    public void build() {
        panel.removeAll();

        ArrayList<CheckPoint> checkPoints = faithTrackEditor.getCheckPoints();

        for(int i = 0; i < checkPoints.size(); ++i) {
            CheckPoint checkPoint = checkPoints.get(i);
            Dimension dimension;

            addAddButtonPanel(i);

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

            JPanel positionPanel = new JPanel(new GridBagLayout());
            JPanel pointsPanel = new JPanel(new GridBagLayout());
            JPanel buttonPanel = new JPanel(new GridBagLayout());

            int finalI = i;

            JFormattedTextField position = new JFormattedTextField();
            position.setValue(checkPoint.getPosition());
            dimension = position.getPreferredSize();
            dimension.width = 30;
            position.setPreferredSize(dimension);
            position.getDocument().addDocumentListener(new TextFieldDocumentListener(
                    position, (value) -> faithTrackEditor.setCheckPointPosition(finalI, value)
            ));

            JFormattedTextField points = new JFormattedTextField();
            points.setValue(checkPoint.getPoints());
            dimension = points.getPreferredSize();
            dimension.width = 30;
            points.setPreferredSize(dimension);
            points.getDocument().addDocumentListener(new TextFieldDocumentListener(
                    points, (value) -> faithTrackEditor.setCheckPointPoints(finalI, value)
            ));

            JButton button = new JButton("-");
            button.setMargin(new Insets(1, 1, 1, 1));
            button.setPreferredSize(new Dimension(25, 25));
            button.setVerticalAlignment(SwingConstants.CENTER);
            button.addMouseListener(new RemoveCheckPointClickEvent(i, this));

            positionPanel.add(position, gbc);
            pointsPanel.add(points, gbc);
            buttonPanel.add(button, gbc);

            dataPanel.add(positionPanel);
            dataPanel.add(pointsPanel);
            dataPanel.add(buttonPanel);

            panel.add(dataPanel);
        }

        addAddButtonPanel(checkPoints.size());

        panel.setVisible(true);
    }

    private void addAddButtonPanel(int buttonIndex) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        JButton button = new JButton("+");
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setPreferredSize(new Dimension(25, 25));
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.addMouseListener(new AddCheckPointClickEvent(buttonIndex, this));

        buttonPanel.add(button, gbc);

        panel.add(buttonPanel);

        frame.setVisible(true);
    }

    public boolean validate() {
        // TODO
        return true;
    }
}
