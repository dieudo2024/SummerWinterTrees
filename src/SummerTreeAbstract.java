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

    protected Color nextCenterColor(Color oldColor, int depth) {
        return new Color(
                Math.min((int) (0.8 * oldColor.getRed() + 0.1 * Color.WHITE.getRed() + 0.04 * depth * 255), 255),
                Math.min((int) (0.8 * oldColor.getGreen() + 0.1 * Color.WHITE.getGreen() + 0.04 * depth * 255), 255),
                Math.min((int) (0.8 * oldColor.getBlue() + 0.1 * Color.WHITE.getBlue() + 0.04 * depth * 255), 255));
    }

    protected void drawBranch(double xStart, double yStart, double xEnd, double yEnd, double length, double thickness,
                              Color color) {
        if (length < 1.f) {
            return;
        }

        g.setStroke(new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.draw(new Line2D.Double(xStart, yStart, xEnd, yEnd));
    }

    protected void drawTree() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        //double baseLength = Math.min(height, width) * 0.3;
        drawTreeRec(width / 2.0, height - 20.0, -computePi(100) / 2.0, 90, 15,
                new Color(155, 100, 60), depth);
    }


    protected void setWinterMode(boolean isWinterMode) {
        this.isWinterMode = isWinterMode;
        enforceDepthBounds();
        updateSeasonalUi();
    }

    protected boolean isWinterMode() {
        return isWinterMode;
    }

    protected void refreshTree() {
        canvas.repaint();
    }

    protected int getMaxDepth() {
        return isWinterMode ? 10 : 30;
    }

    protected void enforceDepthBounds() {
        if (depth > getMaxDepth()) {
            depth = getMaxDepth();
        }
        if (depth < 1) {
            depth = 1;
        }
    }

    private void paintTree(Graphics g) {
        this.g = (Graphics2D) g;
        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTree();
    }

    private void toggleWinterMode() {
        setWinterMode(!isWinterMode);
        canvas.requestFocusInWindow();
    }

    private void updateSeasonalUi() {
        canvas.setBackground(isWinterMode ? WINTER_BACKGROUND : SUMMER_BACKGROUND);
        modeToggleButton.setText(isWinterMode ? "Show Summer Tree" : "Show Winter Tree");
        refreshTree();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleInput(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // We have to implement this method, but don't actually need it
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // We have to implement this method, but don't actually need it
    }

    public abstract void handleInput(int keyCode);

    protected abstract void drawTreeRec(double x, double y, double alpha, double length, double thickness, Color color,
                                        int depth);

    protected abstract double computePi(int n);

}