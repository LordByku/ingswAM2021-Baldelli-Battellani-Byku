package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class TextFieldExample implements ActionListener{
    private JPanel panel1;
    private JFormattedTextField mastersOfRenaissanceFormattedTextField;
    private final JTextField insertYourNicknameToTextField;
    private final JButton b1;

    TextFieldExample(){
        JFrame f= new JFrame();

        insertYourNicknameToTextField = new JTextField();
        insertYourNicknameToTextField.setBounds(50,150,150,20);
        b1=new JButton("Continue");
        b1.setBounds(50,200,100,20);

        b1.addActionListener(this);

        f.add(insertYourNicknameToTextField);f.add(b1);
        f.setSize(300,300);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLayout(null);
        f.setVisible(true);

        insertYourNicknameToTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                source.setText("");
                source.removeFocusListener(this);
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame frame = new JFrame("New Window");
        JTextArea jTextArea = new JTextArea() ;
        String s1= null;

        if(e.getSource()==b1)
            s1=insertYourNicknameToTextField.getText();

        jTextArea.append(s1);
        jTextArea.setEditable(false);
        frame.setSize(235,400);
        frame.add(jTextArea);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
    public static void main(String[] args) {
        new TextFieldExample();
    } }