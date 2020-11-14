package com.noisyapple;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AbstractDialogForm extends JPanel {

    public static final int TRANSLATE = 0, ROTATE = 1, SCALE = 2, SHEAR = 3;

    private JPanel topPanel, centerPanel;
    private JPanel[] optPanels;
    private JTextField[] txtOpts;
    private String instruction;
    private String[] labels;

    public AbstractDialogForm(String instruction, String[] labels) {

        topPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout(1, 2));

        optPanels = new JPanel[labels.length];
        txtOpts = new JTextField[labels.length];


        for (int i = 0; i < labels.length; i++) {
            optPanels[i] = new JPanel();
            txtOpts[i] = new JTextField(5);
        }

        this.instruction = instruction;
        this.labels = labels;

        setAttributes();
        build();
    }

    public void setAttributes() {
        this.setLayout(new BorderLayout());
    }

    public void build() {

        for (int i = 0; i < optPanels.length; i++) {
            optPanels[i].add(new JLabel(labels[i]));
            optPanels[i].add(txtOpts[i]);
            centerPanel.add(optPanels[i]);
        }

        topPanel.add(new JLabel(instruction));

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    public double[] getValues() {
        return Arrays.stream(txtOpts).mapToDouble(e -> Double.parseDouble(e.getText())).toArray();
    }
}
