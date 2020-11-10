package com.noisyapple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Preview extends JFrame {

    private final int W = 700;
    private final int H = 500;
    private final int UPDATE_RATE = 60;

    private LayeredFigure lFigure;

    private JPanel mainPanel, toolsPanel;
    private JButton btnTranslate, btnRotate, btnScale, btnShear, btnApplyMatrix, btnClear;

    public Preview(LayeredFigure lFigure) {

        this.lFigure = lFigure;

        mainPanel = new JPanel();
        toolsPanel = new JPanel();

        btnTranslate = new JButton();
        btnRotate = new JButton();
        btnScale = new JButton();
        btnShear = new JButton();
        btnApplyMatrix = new JButton();
        btnClear = new JButton();

        setAttributes();
        build();
        launch();
        // startLoop();

    }

    public void setAttributes() {
        this.setTitle("AffineTransform");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setLayout(new BorderLayout());
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.PAGE_AXIS));

        toolsPanel.setPreferredSize(new Dimension(40, 0));
        toolsPanel.setBackground(Color.decode("#888888"));

        try {
            btnTranslate.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-move.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnRotate.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-rotate.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnScale.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-scale.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnShear.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-shear.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnApplyMatrix.setIcon(new ImageIcon(
                    new ImageIcon(ImageIO.read(new File("./assets/icon-apply-matrix.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnClear.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-clear.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnTranslate.setPreferredSize(new Dimension(30, 30));
            btnRotate.setPreferredSize(new Dimension(30, 30));
            btnScale.setPreferredSize(new Dimension(30, 30));
            btnShear.setPreferredSize(new Dimension(30, 30));
            btnApplyMatrix.setPreferredSize(new Dimension(30, 30));
            btnClear.setPreferredSize(new Dimension(30, 30));


        } catch (Exception e) {


        }


    }

    public void build() {
        toolsPanel.add(btnTranslate);
        toolsPanel.add(btnRotate);
        toolsPanel.add(btnScale);
        toolsPanel.add(btnShear);
        toolsPanel.add(btnApplyMatrix);
        toolsPanel.add(btnClear);

        mainPanel.add(toolsPanel, BorderLayout.WEST);
        mainPanel.add(new CustomCanvas(), BorderLayout.EAST);

        this.add(mainPanel);
    }

    public void launch() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void startLoop() {
        Thread drawLoop = new Thread() {
            public void run() {
                while (true) {
                    repaint();

                    try {
                        Thread.sleep(1000 / UPDATE_RATE); // Desired frame rate.

                    } catch (InterruptedException ex) {
                    }
                }
            }
        };

        // Thread execution.
        drawLoop.start();
    }

    class CustomCanvas extends JPanel {

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.decode("#333333"));
            g2.fillRect(0, 0, W, H);

            g2.setTransform(new AffineTransform(1, 0, 0, 1,
                    40 + (W / 2) - (lFigure.getHeight() / 2), (H / 2) - (lFigure.getWidth() / 2)));

            lFigure.drawLayeredFigure(g2);
        }

        public Dimension getPreferredSize() {
            return new Dimension(W, H);
        }

    }

}
