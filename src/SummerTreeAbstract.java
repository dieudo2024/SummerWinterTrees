import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.io.Serial;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Abstract Swing frame that wires up the rendering surface, keyboard input, and seasonal UI for
 * fractal tree visualizations.
 */
public abstract class SummerTreeAbstract extends JFrame implements KeyListener {

    @Serial
    private static final long serialVersionUID = -3088085457878787186L;

    protected int depth = 5;
    protected double skewness = 0.0;
    protected boolean isWinterMode = false;

    private Graphics2D g;
    private final JPanel canvas;
    private final JButton modeToggleButton;

    private static final Color SUMMER_BACKGROUND = new Color(164, 236, 213);
    private static final Color WINTER_BACKGROUND = new Color(164, 236, 213);

    /**
     * Builds the frame, installs the drawing surface, and primes focus handling.
     */
    public SummerTreeAbstract() {
        super();
        setSize(500, 700);
        setLayout(new BorderLayout());

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintTree(g);
            }
        };
        canvas.setBackground(SUMMER_BACKGROUND);
        canvas.setFocusable(true);
        add(canvas, BorderLayout.CENTER);

        modeToggleButton = new JButton("Show Winter Tree");
        modeToggleButton.addActionListener(e -> toggleWinterMode());
        add(modeToggleButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        SwingUtilities.invokeLater(() -> {
            canvas.addKeyListener(this);
            canvas.requestFocusInWindow();
        });
    }

    /**
     * Derives the color palette for the left child branch based on the current theme.
     *
     * @param oldColor the parent branch color
     * @param depth    remaining recursion depth used to modulate winter highlights
     * @return color to apply to the next left branch
     */
    protected Color nextLeftColor(Color oldColor, int depth) {
        if (!isWinterMode) {
            return new Color((int) (0.9 * oldColor.getRed() + 0.1 * Color.GREEN.getRed()),
                    (int) (0.9 * oldColor.getGreen() + 0.1 * Color.GREEN.getGreen()),
                    (int) (0.9 * oldColor.getBlue() + 0.1 * Color.GREEN.getBlue()));
        } else {
            return new Color(
                    Math.min((int) (0.8 * oldColor.getRed() + 0.1 * Color.GREEN.getRed() + 0.04 * depth * 255), 255),
                    Math.min((int) (0.8 * oldColor.getGreen() + 0.1 * Color.GREEN.getGreen() + 0.04 * depth * 255),
                            255),
                    Math.min((int) (0.8 * oldColor.getBlue() + 0.1 * Color.GREEN.getBlue() + 0.04 * depth * 255), 255));
        }
    }

    /**
     * Derives the color palette for the right child branch while respecting the active season.
     *
     * @param oldColor the parent branch color
     * @param depth    remaining recursion depth used to adjust winter bloom brightness
     * @return color to apply to the right branch
     */
    protected Color nextRightColor(Color oldColor, int depth) {
        if (!isWinterMode) {
            return new Color((int) (0.9 * oldColor.getRed() + 0.1 * Color.YELLOW.getRed()),
                    (int) (0.9 * oldColor.getGreen() + 0.1 * Color.YELLOW.getGreen()),
                    (int) (0.9 * oldColor.getBlue() + 0.1 * Color.YELLOW.getBlue()));
        } else {
            return new Color(
                    Math.min((int) (0.8 * oldColor.getRed() + 0.1 * Color.YELLOW.getRed() + 0.04 * depth * 255), 255),
                    Math.min((int) (0.8 * oldColor.getGreen() + 0.1 * Color.YELLOW.getGreen() + 0.04 * depth * 255),
                            255),
                    Math.min((int) (0.8 * oldColor.getBlue() + 0.1 * Color.YELLOW.getBlue() + 0.04 * depth * 255),
                            255));
        }
    }

    /**
     * Provides the color for the center branch that retains the main hue across iterations.
     *
     * @param oldColor the parent branch color
     * @param depth    remaining recursion depth for winter tint calculations
     * @return color to apply to the central branch
     */
    protected Color nextCenterColor(Color oldColor, int depth) {
        if (!isWinterMode) {
            return new Color(
                Math.min((int) (0.8 * oldColor.getRed() + 0.2 * Color.GREEN.getRed()), 255),
                Math.min((int) (0.8 * oldColor.getGreen() + 0.2 * Color.GREEN.getGreen()), 255),
                Math.min((int) (0.8 * oldColor.getBlue() + 0.2 * Color.GREEN.getBlue()), 255));
        }

        return new Color(
            Math.min((int) (0.8 * oldColor.getRed() + 0.1 * Color.WHITE.getRed() + 0.04 * depth * 255), 255),
            Math.min((int) (0.8 * oldColor.getGreen() + 0.1 * Color.WHITE.getGreen() + 0.04 * depth * 255), 255),
            Math.min((int) (0.8 * oldColor.getBlue() + 0.1 * Color.WHITE.getBlue() + 0.04 * depth * 255), 255));
    }

    /**
     * Renders a single branch segment with anti-aliased stroke styling.
     *
     * @param xStart    starting x coordinate
     * @param yStart    starting y coordinate
     * @param xEnd      ending x coordinate
     * @param yEnd      ending y coordinate
     * @param length    branch length used to short-circuit tiny segments
     * @param thickness stroke width in pixels
     * @param color     branch color
     */
    protected void drawBranch(double xStart, double yStart, double xEnd, double yEnd, double length, double thickness,
                              Color color) {
        if (length < 1.f) {
            return;
        }

        g.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.draw(new Line2D.Double(xStart, yStart, xEnd, yEnd));
    }

    /**
     * Calculates the root branch geometry and initiates recursive drawing.
     */
    protected void drawTree() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        drawTreeRec(width / 2.0, height - 20.0, -computePi(100) / 2.0, 90, 15,
                new Color(155, 100, 60), depth);
    }


    /**
     * Updates the seasonal mode and refreshes the UI accordingly.
     *
     * @param isWinterMode {@code true} to enable winter theming
     */
    protected void setWinterMode(boolean isWinterMode) {
        this.isWinterMode = isWinterMode;
        enforceDepthBounds();
        updateSeasonalUi();
    }

    /**
     * Indicates whether the tree currently renders in winter mode.
     *
     * @return {@code true} when winter mode is active
     */
    protected boolean isWinterMode() {
        return isWinterMode;
    }

    /**
     * Requests a repaint of the drawing canvas.
     */
    protected void refreshTree() {
        canvas.repaint();
    }

    /**
     * Reports the current recursion depth ceiling, tightening limits in winter mode to protect
     * performance.
     *
     * @return maximum allowed recursion depth
     */
    protected int getMaxDepth() {
        return isWinterMode ? 10 : 10;
    }

    /**
     * Clamps the current recursion depth to the permitted range.
     */
    protected void enforceDepthBounds() {
        if (depth > getMaxDepth()) {
            depth = getMaxDepth();
        }
        if (depth < 1) {
            depth = 1;
        }
    }

    /**
     * Configures rendering hints and delegates to {@link #drawTree()} during repaint cycles.
     *
     * @param g graphics context supplied by Swing
     */
    private void paintTree(Graphics g) {
        this.g = (Graphics2D) g;
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTree();
    }

    /**
     * Flips the seasonal mode and restores keyboard focus to the canvas.
     */
    private void toggleWinterMode() {
        setWinterMode(!isWinterMode);
        canvas.requestFocusInWindow();
    }

    /**
     * Syncs UI affordances such as background color and toggle button labeling with the
     * activated season.
     */
    private void updateSeasonalUi() {
        canvas.setBackground(isWinterMode ? WINTER_BACKGROUND : SUMMER_BACKGROUND);
        modeToggleButton.setText(isWinterMode ? "Show Summer Tree" : "Show Winter Tree");
        refreshTree();
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        handleInput(e.getKeyCode());
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
        // We have to implement this method, but don't actually need it
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
        // We have to implement this method, but don't actually need it
    }

    /**
     * Handles translated key events raised by the hosted canvas.
     *
     * @param keyCode Swing virtual key code forwarded from {@link #keyPressed(KeyEvent)}
     */
    public abstract void handleInput(int keyCode);

    /**
     * Recursively renders the tree structure starting at the supplied coordinates.
     *
     * @param x         starting x coordinate
     * @param y         starting y coordinate
     * @param alpha     branch angle in radians
     * @param length    branch length in pixels
     * @param thickness stroke width in pixels
     * @param color     branch color
     * @param depth     remaining recursion depth
     */
    protected abstract void drawTreeRec(double x, double y, double alpha, double length, double thickness, Color color,
                                        int depth);

    /**
     * Computes an approximation of pi suitable for deriving trunk orientation.
     *
     * @param n refinement steps to include in the approximation
    * @return approximation of pi
     */
    protected abstract double computePi(int n);

}