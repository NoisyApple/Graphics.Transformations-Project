package com.noisyapple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


@SuppressWarnings("serial")
public class Preview extends JFrame {

    private final int W = 700;
    private final int H = 500;
    private final int UPDATE_RATE = 60;

    private double x, y, prevX, prevY;

    private LayeredFigure lFigure;

    private JPanel mainPanel, toolsPanel, toolsPanelA, toolsPanelB;

    private JMenuBar menuBar;
    private JMenu menuTransormations, menuExit;
    private JMenuItem mItemTranslate, mItemRotate, mItemScale, mItemShear, mItemRestore;

    private JButton btnTransUp, btnTransRight, btnTransDown, btnTransLeft;
    private JButton btnRotateRight, btnRotateLeft;
    private JButton btnScaleUp, btnScaleDown;
    private JButton btnFlipX, btnFlipY;
    private JButton btnShearUp, btnShearDown;
    private JButton btnApplyMatrix, btnRestore;
    private JButton btnInfo;

    private CustomCanvas canvas;

    public Preview(LayeredFigure lFigure) {

        this.lFigure = lFigure;
        x = 80 + (W / 2);
        y = 23 + (H / 2);
        prevX = x;
        prevY = y;

        // Panels.
        mainPanel = new JPanel();
        toolsPanel = new JPanel();
        toolsPanelA = new JPanel();
        toolsPanelB = new JPanel();

        // Menu.
        menuBar = new JMenuBar();
        menuTransormations = new JMenu("Transformations");
        menuExit = new JMenu("Exit");
        mItemTranslate = new JMenuItem("Translate");
        mItemRotate = new JMenuItem("Rotate");
        mItemScale = new JMenuItem("Scale");
        mItemShear = new JMenuItem("Shear");
        mItemRestore = new JMenuItem("Restore");

        // Buttons.
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
        btnFlipX = new JButton();
        btnFlipY = new JButton();
        btnApplyMatrix = new JButton();
        btnRestore = new JButton();
        btnInfo = new JButton();

        // Canvas.
        canvas = new CustomCanvas();

        addListeners();
        setAttributes();
        build();
        launch();
        startLoop();
    }

    public void addListeners() {

        // ### JMenuBar ### +++

        // Menu listener implementation.
        menuExit.addMenuListener(new MenuListener() {
            // Exits the program.
            public void menuSelected(MenuEvent e) {
                System.exit(0);
            }

            public void menuDeselected(MenuEvent e) {
                // Do nothing.
            }

            public void menuCanceled(MenuEvent e) {
                // Do nothing.
            }
        });

        // All of the following prompts a window for inputing data based on its action.
        mItemTranslate
                .addActionListener(new AbstractDialogEventListener(AbstractDialogForm.TRANSLATE));
        mItemRotate.addActionListener(new AbstractDialogEventListener(AbstractDialogForm.ROTATE));
        mItemScale.addActionListener(new AbstractDialogEventListener(AbstractDialogForm.SCALE));
        mItemShear.addActionListener(new AbstractDialogEventListener(AbstractDialogForm.SHEAR));

        // Restores matrix to a default value.
        mItemRestore.addActionListener(e -> {
            x = 80 + (W / 2);
            y = 23 + (H / 2);
            canvas.getTransformMatrix().setTransform(1, 0, 0, 1, x, y);
        });

        // ### JMenuBar ### ---

        // ### JButtons ### +++

        // Translates 10px to the top.
        btnTransUp.addActionListener(e -> y -= 10);

        // Translates 10px to the right.
        btnTransRight.addActionListener(e -> x += 10);

        // Translates 10px to the bottom.
        btnTransDown.addActionListener(e -> y += 10);

        // Translates 10px to the left.
        btnTransLeft.addActionListener(e -> x -= 10);

        // Rotates to the right.
        btnRotateRight.addActionListener(e -> {
            canvas.getTransformMatrix().rotate(Math.PI / 8, lFigure.getWidth() / 2,
                    lFigure.getHeight() / 2);
        });

        // Rotates to the left.
        btnRotateLeft.addActionListener(e -> {
            canvas.getTransformMatrix().rotate(-Math.PI / 8, lFigure.getWidth() / 2,
                    lFigure.getHeight() / 2);
        });

        // Scales up the canvas.
        btnScaleUp.addActionListener(e -> {
            canvas.getTransformMatrix().scale(1.1, 1.1);
        });

        // Scales down the canvas.
        btnScaleDown.addActionListener(e -> {
            canvas.getTransformMatrix().scale(0.9, 0.9);
        });

        // Shears with a factor of 0.1 in both ways.
        btnShearUp.addActionListener(e -> {
            canvas.getTransformMatrix().shear(0.1, 0.1);
        });

        // Shears with a factor of -0.1 in both ways.
        btnShearDown.addActionListener(e -> {
            canvas.getTransformMatrix().shear(-0.1, -0.1);
        });

        // Flips horizontally.
        btnFlipX.addActionListener(e -> {
            canvas.getTransformMatrix().scale(-1, 1);
        });

        // Flips vertically.
        btnFlipY.addActionListener(e -> {
            canvas.getTransformMatrix().scale(1, -1);
        });

        // Prompts a dialog for new transform matrix values.
        btnApplyMatrix.addActionListener(e -> {
            MatrixDialogForm mDialog = new MatrixDialogForm();

            int result = JOptionPane.showConfirmDialog(null, mDialog, "Transform Matrix",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            switch (result) {
                case JOptionPane.OK_OPTION:
                    try {
                        if (mDialog.getConcatenateEnabled()) {
                            x += mDialog.getValues()[4];
                            y += mDialog.getValues()[5];
                            canvas.getTransformMatrix()
                                    .concatenate(new AffineTransform(mDialog.getValues()));
                        } else {
                            x = mDialog.getValues()[4];
                            y = mDialog.getValues()[5];
                            canvas.getTransformMatrix()
                                    .setTransform(new AffineTransform(mDialog.getValues()));
                        }
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(null, "Error! Invalid values.", "Error :(",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        });

        // Restores the matrix to a default value.
        btnRestore.addActionListener(e -> {
            x = 80 + (W / 2);
            y = 23 + (H / 2);
            canvas.getTransformMatrix().setTransform(1, 0, 0, 1, x, y);
        });

        // Prompts a dialog window with information.
        btnInfo.addActionListener(e -> {
            String htmlInfo = "<html>"
                    + "<table><tr><td colspan='2' style='color: #8844ff; font-size: 20px'>KEY SHORTCUTS"
                    + "</td></tr><tr><td align='left'>Translate to the top:</td><td>UP ARROW</td></tr><tr>"
                    + "<td align='left'>Translate to the right:</td><td>RIGHT ARROW</td></tr><tr>"
                    + "<td align='left'>Translate to the bottom:</td><td>DOWN ARROW</td></tr><tr>"
                    + "<td align='left'>Translate to the left:</td><td>LEFT ARROW</td></tr><tr>"
                    + "<td align='left'>Rotate to the left:</td><td>CTRL+R</td></tr><tr><td align='left'>"
                    + "Rotate to the right:</td><td>R</td></tr><tr><td align='left'>Zoom out:</td><td>"
                    + "CTRL+Z</td></tr><tr><td align='left'>Zoom in:</td><td>Z</td></tr><tr><td align='left'>"
                    + "Shear out:</td><td>CTRL+S</td></tr><tr><td align='left'>Shear in:</td><td>S</td></tr>"
                    + "<tr><td align='left'>Flip vertically:</td><td>CTRL+F</td></tr><tr><td align='left'>"
                    + "Flip horizontally:</td><td>F</td></tr><tr><td align='left'>Restore</td><td>C</td>"
                    + "</tr></table><html>";
            JOptionPane.showMessageDialog(null, new JLabel(htmlInfo, JLabel.CENTER),
                    "Key shortcuts", JOptionPane.PLAIN_MESSAGE);
        });
        // ### JButtons ### ---

        // ### Canvas Mouse Events ### +++

        // Zoom on wheel moved.
        canvas.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation != 0) {
                canvas.getTransformMatrix().translate(50, 50);
                canvas.getTransformMatrix().scale((rotation > 0) ? 1.1 : 0.9,
                        (rotation > 0) ? 1.1 : 0.9);
                canvas.getTransformMatrix().translate(-50, -50);
            }
        });

        // Translation on mouse drag.
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                double difX = e.getX() - prevX;
                double difY = e.getY() - prevY;

                x += difX;
                y += difY;

                prevX = e.getX();
                prevY = e.getY();
            };
        });

        canvas.addMouseListener(new MouseAdapter() {
            // Tracks new position for dragging.
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            };

            // Rotation on double click.
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    canvas.getTransformMatrix().rotate(Math.PI / 8, lFigure.getWidth() / 2,
                            lFigure.getHeight() / 2);
                }
            };
        });

        // ### Canvas Mouse Events ### ---

        // ### Frame Key Events ### +++

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                // CTRL DOWN
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_R: // Rotate to the left.
                            canvas.getTransformMatrix().rotate(-Math.PI / 32,
                                    lFigure.getWidth() / 2, lFigure.getHeight() / 2);
                            break;
                        case KeyEvent.VK_Z: // Scale down.
                            canvas.getTransformMatrix().scale(0.975, 0.975);
                            break;
                        case KeyEvent.VK_S: // Shear down.
                            canvas.getTransformMatrix().shear(-0.1, -0.1);
                            break;
                        case KeyEvent.VK_F: // Flip vertically.
                            canvas.getTransformMatrix().scale(1, -1);
                            break;
                    }
                } else {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP: // Translation to top.
                            y -= 2;
                            break;
                        case KeyEvent.VK_RIGHT: // Translation to right.
                            x += 2;
                            break;
                        case KeyEvent.VK_DOWN: // Translation to bottom.
                            y += 2;
                            break;
                        case KeyEvent.VK_LEFT: // Translation to left.
                            x -= 2;
                            break;
                        case KeyEvent.VK_R: // Rotate to the right.
                            canvas.getTransformMatrix().rotate(Math.PI / 32, lFigure.getWidth() / 2,
                                    lFigure.getHeight() / 2);
                            break;
                        case KeyEvent.VK_Z: // Scale up.
                            canvas.getTransformMatrix().scale(1.025, 1.025);
                            break;
                        case KeyEvent.VK_S: // Shear up.
                            canvas.getTransformMatrix().shear(0.1, 0.1);
                            break;
                        case KeyEvent.VK_F: // Flip horizontally.
                            canvas.getTransformMatrix().scale(-1, 1);
                            break;
                        case KeyEvent.VK_C: // Restore.
                            x = 80 + (W / 2);
                            y = 23 + (H / 2);
                            canvas.getTransformMatrix().setTransform(1, 0, 0, 1, x, y);
                            break;
                    }
                }
            }
        });

        // ### Frame Key Events ### ---

    }

    // Sets attributes to elements in the program.
    public void setAttributes() {
        this.setTitle("AffineTransform");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);
        this.setJMenuBar(menuBar);

        mainPanel.setLayout(new BorderLayout());
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.LINE_AXIS));

        toolsPanelA.setLayout(new BoxLayout(toolsPanelA, BoxLayout.PAGE_AXIS));
        toolsPanelB.setLayout(new BoxLayout(toolsPanelB, BoxLayout.PAGE_AXIS));

        toolsPanel.setPreferredSize(new Dimension(80, 0));
        toolsPanelB.setAlignmentY(Component.TOP_ALIGNMENT);
        toolsPanelA.setAlignmentY(Component.TOP_ALIGNMENT);
        toolsPanel.setBackground(Color.decode("#888888"));

        // Sets icons to buttons from resources.
        try {
            btnTransUp.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-up.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            btnTransRight.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-right.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            btnTransDown.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-down.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            btnTransLeft.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-left.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            btnRotateRight.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-rotate-right.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnRotateLeft.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-rotate-left.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnScaleUp.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-scale-up.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnScaleDown.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-scale-down.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnShearUp.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-shear-up.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnShearDown.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-shear-down.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnFlipX.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-flip-x.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnFlipY.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-flip-y.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnApplyMatrix.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-matrix.png"))).getImage()
                                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

            btnRestore.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-clear.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            btnInfo.setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(Thread.currentThread().getContextClassLoader()
                            .getResource("images/icon-info.png"))).getImage().getScaledInstance(30,
                                    30, Image.SCALE_SMOOTH)));

            // All buttons are resized.
            btnTransUp.setPreferredSize(new Dimension(30, 30));
            btnTransRight.setPreferredSize(new Dimension(30, 30));
            btnTransDown.setPreferredSize(new Dimension(30, 30));
            btnTransLeft.setPreferredSize(new Dimension(30, 30));

            btnRotateRight.setPreferredSize(new Dimension(30, 30));
            btnRotateLeft.setPreferredSize(new Dimension(30, 30));

            btnScaleUp.setPreferredSize(new Dimension(30, 30));
            btnScaleDown.setPreferredSize(new Dimension(30, 30));

            btnShearUp.setPreferredSize(new Dimension(30, 30));
            btnShearDown.setPreferredSize(new Dimension(30, 30));

            btnFlipX.setPreferredSize(new Dimension(30, 30));
            btnFlipY.setPreferredSize(new Dimension(30, 30));

            btnApplyMatrix.setPreferredSize(new Dimension(30, 30));
            btnRestore.setPreferredSize(new Dimension(30, 30));

            btnInfo.setPreferredSize(new Dimension(30, 30));

            // Makes every button unfocusable.
            btnTransUp.setFocusable(false);
            btnTransRight.setFocusable(false);
            btnTransDown.setFocusable(false);
            btnTransLeft.setFocusable(false);

            btnRotateRight.setFocusable(false);
            btnRotateLeft.setFocusable(false);

            btnScaleUp.setFocusable(false);
            btnScaleDown.setFocusable(false);

            btnShearUp.setFocusable(false);
            btnShearDown.setFocusable(false);

            btnFlipX.setFocusable(false);
            btnFlipY.setFocusable(false);

            btnApplyMatrix.setFocusable(false);
            btnRestore.setFocusable(false);

            btnInfo.setFocusable(false);

        } catch (Exception e) {
        }
    }

    // Builds the GUI.
    public void build() {

        // Menu.
        menuTransormations.add(mItemTranslate);
        menuTransormations.add(mItemRotate);
        menuTransormations.add(mItemScale);
        menuTransormations.add(mItemShear);
        menuTransormations.add(mItemRestore);
        menuBar.add(menuTransormations);
        menuBar.add(menuExit);

        // Tools Panel A.
        toolsPanelA.add(btnTransUp);
        toolsPanelA.add(btnTransLeft);
        toolsPanelA.add(btnRotateLeft);
        toolsPanelA.add(btnScaleDown);
        toolsPanelA.add(btnShearDown);
        toolsPanelA.add(btnFlipY);
        toolsPanelA.add(btnApplyMatrix);
        toolsPanelA.add(btnInfo);

        // Tools Panel B.
        toolsPanelB.add(btnTransDown);
        toolsPanelB.add(btnTransRight);
        toolsPanelB.add(btnRotateRight);
        toolsPanelB.add(btnScaleUp);
        toolsPanelB.add(btnShearUp);
        toolsPanelB.add(btnFlipX);
        toolsPanelB.add(btnRestore);

        // Tools Panel.
        toolsPanel.add(toolsPanelA);
        toolsPanel.add(toolsPanelB);

        // Main Panel.
        mainPanel.add(toolsPanel, BorderLayout.WEST);
        mainPanel.add(canvas, BorderLayout.EAST);

        // Frame.
        this.add(mainPanel);
    }

    // Launches the program.
    public void launch() {
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Draw Loop.
    public void startLoop() {
        Thread drawLoop = new Thread() {
            public void run() {
                while (true) {
                    repaint();

                    double[] matrix = new double[6];

                    canvas.getTransformMatrix().getMatrix(matrix);
                    matrix[4] = x;
                    matrix[5] = y;

                    // Canvas is translated based on the actual x and y value modified when
                    // dragging.
                    canvas.getTransformMatrix().setTransform(new AffineTransform(matrix));

                    try {
                        Thread.sleep(1000 / UPDATE_RATE); // Desired frame rate.

                    } catch (InterruptedException ex) {
                    }
                }
            }
        };

        drawLoop.start();
    }

    // Custom JPanel for canvas purposes.
    class CustomCanvas extends JPanel {

        private AffineTransform aT;

        public CustomCanvas() {
            aT = new AffineTransform(1, 0, 0, 1, x, y); // Default matrix values.
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.decode("#333333"));
            g2.fillRect(0, 0, W, H);

            g2.setTransform(aT); // AffineTransform is applied to the context.

            lFigure.drawLayeredFigure(g2); // Figure is drawn.
        }

        // Overrides the method to return the desired dimension.
        public Dimension getPreferredSize() {
            return new Dimension(W, H);
        }

        // Returns the aT object.
        public AffineTransform getTransformMatrix() {
            return this.aT;
        }

    }

    // Event handler for menu items.
    class AbstractDialogEventListener implements ActionListener {

        private int action;

        public AbstractDialogEventListener(int action) {
            this.action = action;
        }

        // When the menu is selected, actionPerformed works based on the action of the menu.
        public void actionPerformed(ActionEvent e) {

            String instruction = "", title = "";
            String[] labels = new String[0];

            // Texts are set based on action.
            switch (action) {
                case AbstractDialogForm.TRANSLATE:
                    instruction = "Enter translation values:";
                    labels = new String[] {"X: ", "Y: "};
                    title = "Translation";
                    break;
                case AbstractDialogForm.ROTATE:
                    instruction = "Enter rotation values:";
                    labels = new String[] {"Deg: "};
                    title = "Rotation";
                    break;
                case AbstractDialogForm.SCALE:
                    instruction = "Enter scaling values:";
                    labels = new String[] {"X: ", "Y: "};
                    title = "Scaling";
                    break;
                case AbstractDialogForm.SHEAR:
                    instruction = "Enter shearing values:";
                    labels = new String[] {"X: ", "Y: "};
                    title = "Shearing";
                    break;
            }

            // An instance of AbstractDialogForm is constructed based on the action selected.
            AbstractDialogForm absDialog = new AbstractDialogForm(instruction, labels);

            int result = JOptionPane.showConfirmDialog(null, absDialog, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            switch (result) {
                case JOptionPane.OK_OPTION:

                    // A transformation is applied based on the action selected.
                    try {
                        double[] values = absDialog.getValues();

                        switch (action) {
                            case AbstractDialogForm.TRANSLATE:
                                x += values[0];
                                y += values[1];
                                break;
                            case AbstractDialogForm.ROTATE:
                                canvas.getTransformMatrix().rotate(Math.PI / 180 * values[0],
                                        lFigure.getWidth() / 2, lFigure.getHeight() / 2);
                                break;
                            case AbstractDialogForm.SCALE:
                                canvas.getTransformMatrix().scale(values[0], values[1]);
                                break;
                            case AbstractDialogForm.SHEAR:
                                canvas.getTransformMatrix().shear(values[0], values[1]);
                                break;
                        }

                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(null, "Error! Invalid values.", "Error :(",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        }

    }

}
