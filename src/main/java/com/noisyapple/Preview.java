package com.noisyapple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Preview extends JFrame {

    private final int W = 700;
    private final int H = 500;
    private final int UPDATE_RATE = 60;

    private LayeredFigure lFigure;

    private JPanel mainPanel, toolsPanel;
    private CustomCanvas canvas;
    private JButton btnTransUp, btnTransRight, btnTransDown, btnTransLeft;
    private JButton btnRotateRight, btnRotateLeft;
    private JButton btnScaleUp, btnScaleDown;
    private JButton btnShearUp, btnShearDown;
    private JButton btnApplyMatrix, btnClear;

    public Preview(LayeredFigure lFigure) {

        this.lFigure = lFigure;

        mainPanel = new JPanel();
        toolsPanel = new JPanel();

        canvas = new CustomCanvas();

        btnTransUp = new JButton();
        btnTransRight = new JButton();
        btnTransDown = new JButton();
        btnTransLeft = new JButton();

        btnRotateRight = new JButton();
        btnRotateLeft = new JButton();

        btnScaleUp = new JButton();
        btnScaleDown = new JButton();

        btnShearUp = new JButton();
        btnShearDown = new JButton();

        btnApplyMatrix = new JButton();
        btnClear = new JButton();

        addListeners();
        setAttributes();
        build();
        launch();
        startLoop();

    }

    public void addListeners() {
        btnTransUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().translate(0, -10);
            }
        });

        btnTransRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().translate(10, 0);
            }
        });

        btnTransDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().translate(0, 10);
            }
        });

        btnTransLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().translate(-10, 0);
            }
        });

        btnRotateRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().rotate(Math.PI / 8, lFigure.getWidth() / 2,
                        lFigure.getHeight() / 2);
            }
        });

        btnRotateLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().rotate(-Math.PI / 8, lFigure.getWidth() / 2,
                        lFigure.getHeight() / 2);
            }
        });

        btnScaleUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().scale(1.1, 1.1);
            }
        });

        btnScaleDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().scale(0.9, 0.9);
            }
        });

        btnShearUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().shear(0.1, 0.1);
            }
        });

        btnShearDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().shear(-0.1, -0.1);
            }
        });

        btnApplyMatrix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double m00 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m00 value:"));
                double m01 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m01 value:"));
                double m02 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m02 value:"));
                double m10 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m10 value:"));
                double m11 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m11 value:"));
                double m12 =
                        Double.parseDouble(JOptionPane.showInputDialog(null, "Input m12 value:"));

                canvas.getTransformMatrix()
                        .concatenate(new AffineTransform(m00, m10, m01, m11, m02, m12));
            }
        });


        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.getTransformMatrix().setTransform(1, 0, 0, 1,
                        80 + (W / 2) - (lFigure.getHeight() / 2),
                        (H / 2) - (lFigure.getWidth() / 2));
            }
        });
    }

    public void setAttributes() {
        this.setTitle("AffineTransform");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setLayout(new BorderLayout());
        // toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.PAGE_AXIS));
        toolsPanel.setLayout(new GridLayout(0, 2));

        toolsPanel.setPreferredSize(new Dimension(80, 0));
        toolsPanel.setBackground(Color.decode("#888888"));

        try {
            btnTransUp.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-up.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnTransRight.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-right.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnTransDown.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-down.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnTransLeft.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-left.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnRotateRight.setIcon(new ImageIcon(
                    new ImageIcon(ImageIO.read(new File("./assets/icon-rotate-right.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnRotateLeft.setIcon(new ImageIcon(
                    new ImageIcon(ImageIO.read(new File("./assets/icon-rotate-left.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnScaleUp.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-scale.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnScaleDown.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-scale.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnShearUp.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-shear.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnShearDown.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-shear.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnApplyMatrix.setIcon(new ImageIcon(
                    new ImageIcon(ImageIO.read(new File("./assets/icon-apply-matrix.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnClear.setIcon(
                    new ImageIcon(new ImageIcon(ImageIO.read(new File("./assets/icon-clear.png")))
                            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnTransUp.setPreferredSize(new Dimension(30, 30));
            btnTransRight.setPreferredSize(new Dimension(30, 30));
            btnTransDown.setPreferredSize(new Dimension(30, 30));
            btnTransLeft.setPreferredSize(new Dimension(30, 30));

            btnRotateRight.setPreferredSize(new Dimension(30, 30));
            btnRotateLeft.setPreferredSize(new Dimension(30, 30));

            btnScaleUp.setPreferredSize(new Dimension(30, 30));
            btnScaleDown.setPreferredSize(new Dimension(30, 30));

            btnScaleUp.setBackground(Color.decode("#8888ff"));
            btnScaleDown.setBackground(Color.decode("#ff8888"));

            btnShearUp.setPreferredSize(new Dimension(30, 30));
            btnShearDown.setPreferredSize(new Dimension(30, 30));

            btnShearUp.setBackground(Color.decode("#8888ff"));
            btnShearDown.setBackground(Color.decode("#ff8888"));

            btnApplyMatrix.setPreferredSize(new Dimension(30, 30));
            btnClear.setPreferredSize(new Dimension(30, 30));


        } catch (Exception e) {


        }


    }

    public void build() {
        toolsPanel.add(btnTransUp);
        toolsPanel.add(btnTransRight);
        toolsPanel.add(btnTransDown);
        toolsPanel.add(btnTransLeft);

        toolsPanel.add(btnRotateRight);
        toolsPanel.add(btnRotateLeft);

        toolsPanel.add(btnScaleUp);
        toolsPanel.add(btnScaleDown);

        toolsPanel.add(btnShearUp);
        toolsPanel.add(btnShearDown);

        toolsPanel.add(btnApplyMatrix);
        toolsPanel.add(btnClear);

        mainPanel.add(toolsPanel, BorderLayout.WEST);
        mainPanel.add(canvas, BorderLayout.EAST);

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

        drawLoop.start();
    }

    class CustomCanvas extends JPanel {

        private AffineTransform aT;

        public CustomCanvas() {
            aT = new AffineTransform(1, 0, 0, 1, 80 + (W / 2) - (lFigure.getHeight() / 2),
                    (H / 2) - (lFigure.getWidth() / 2));
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.decode("#333333"));
            g2.fillRect(0, 0, W, H);

            g2.setTransform(aT);

            lFigure.drawLayeredFigure(g2);
        }

        public Dimension getPreferredSize() {
            return new Dimension(W, H);
        }

        public AffineTransform getTransformMatrix() {
            return this.aT;
        }

    }

}
