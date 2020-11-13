package com.noisyapple;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MatrixDialogForm extends JPanel {

    private JPanel topPanel, centerPanel, bottomPanel;
    private JPanel m00Panel, m10Panel, m01Panel, m11Panel, m02Panel, m12Panel;
    private JTextField txtM00, txtM10, txtM01, txtM11, txtM02, txtM12;
    private JCheckBox chkBoxConcatenate;

    public MatrixDialogForm() {

        topPanel = new JPanel();
        centerPanel = new JPanel(new GridLayout(2, 3));
        bottomPanel = new JPanel();

        m00Panel = new JPanel();
        m01Panel = new JPanel();
        m02Panel = new JPanel();
        m10Panel = new JPanel();
        m11Panel = new JPanel();
        m12Panel = new JPanel();

        txtM00 = new JTextField(5);
        txtM01 = new JTextField(5);
        txtM02 = new JTextField(5);
        txtM10 = new JTextField(5);
        txtM11 = new JTextField(5);
        txtM12 = new JTextField(5);

        chkBoxConcatenate = new JCheckBox("Concatenate");

        setAttributes();
        build();
    }

    public void setAttributes() {
        this.setLayout(new BorderLayout());
        chkBoxConcatenate.setSelected(true);
    }

    public void build() {
        m00Panel.add(new JLabel("M00: "));
        m00Panel.add(txtM00);
        m01Panel.add(new JLabel("M01: "));
        m01Panel.add(txtM01);
        m02Panel.add(new JLabel("M02: "));
        m02Panel.add(txtM02);
        m10Panel.add(new JLabel("M10: "));
        m10Panel.add(txtM10);
        m11Panel.add(new JLabel("M11: "));
        m11Panel.add(txtM11);
        m12Panel.add(new JLabel("M12: "));
        m12Panel.add(txtM12);

        topPanel.add(new JLabel("Enter a valid transform matrix:"));

        centerPanel.add(m00Panel);
        centerPanel.add(m01Panel);
        centerPanel.add(m02Panel);
        centerPanel.add(m10Panel);
        centerPanel.add(m11Panel);
        centerPanel.add(m12Panel);

        bottomPanel.add(chkBoxConcatenate);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public double[] getValues() {
        double m00 = Double.parseDouble(txtM00.getText());
        double m01 = Double.parseDouble(txtM01.getText());
        double m02 = Double.parseDouble(txtM02.getText());
        double m10 = Double.parseDouble(txtM10.getText());
        double m11 = Double.parseDouble(txtM11.getText());
        double m12 = Double.parseDouble(txtM12.getText());

        return new double[] {m00, m10, m01, m11, m02, m12};
    }

    public boolean getConcatenateEnabled() {
        return chkBoxConcatenate.isSelected();
    }

}
