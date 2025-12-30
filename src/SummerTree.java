
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.Serial;

/**
 * Renders the interactive summer tree variant, providing the concrete recursion,
 * keyboard handling, and application entry point layered atop {@link SummerTreeAbstract}.
 */
public class SummerTree extends SummerTreeAbstract {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new windowed tree visualization and prepares the rendering surface.
     */
    public SummerTree() {
        super();
    }

    /**
     * Approximates pi using the arctangent continued fraction to determine the trunk angle.
     *
     * @param n number of refinement steps to execute (must be non-negative)
    * @return approximation of pi after {@code n} iterations
     * @throws IllegalArgumentException if {@code n} is negative
     */
    @Override
    protected double computePi(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid number!");
        }

        return computePiHelper(n, 1);
    }

    /**
    * Recursively evaluates the continued fraction used in {@link #computePi(int)}.
     *
     * @param n    remaining iterations to compute
     * @param last accumulated value from the previous level
    * @return next refinement of the continued fraction
     */
    private double computePiHelper(int n, double last) {
        if (n == 0) {
            return last * 2.0 ;
        } else {
            return computePiHelper(n - 1, 1.0 + (double) n / (n * 2.0 + 1.0) * last);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void drawTreeRec(double x, double y, double alpha, double length, double thickness, Color color, int depth) {
        if ((length <= 1.0) || (depth <= 0)) {
            if (isWinterMode()) {
                //drawGlitterBalls(x, y, thickness);
            }
            return;
        }
        //Using methods for trigonometric functions sin(x) and cos(x)
        double fromX =  x + Math.cos(alpha) * length ;
        double fromY = y +Math.sin(alpha) * length ;

        drawBranch(x, y, fromX, fromY, length, thickness, color);

        double nextLength = length * 0.75;
        double nextThickness = thickness * 0.9;

        drawTreeRec(fromX, fromY, alpha + 0.5 + skewness, nextLength, nextThickness, nextRightColor(color, depth),
            depth - 1);

        drawTreeRec(fromX, fromY, alpha, length * 0.95, nextThickness, nextCenterColor(color, depth), depth - 1);

        drawTreeRec(fromX, fromY, alpha - 0.5 + skewness, nextLength, nextThickness, nextLeftColor(color, depth),
            depth - 1);
    }

    /**
     * Responds to keyboard events by adjusting recursion depth, skewness, or seasonal mode.
     *
     * @param keyCode virtual key code received from the {@link java.awt.event.KeyListener}
     */
    @Override
    public void handleInput(int keyCode) {

        switch (keyCode) {
            case KeyEvent.VK_UP -> // single-step depth growth keeps recursion manageable
                depth += 1;
            case KeyEvent.VK_DOWN -> // mirror decrement for consistent control feel
                depth -= 1;
            case KeyEvent.VK_LEFT -> //decreasing skewness by 0.05
                skewness -= 0.05;
            case KeyEvent.VK_RIGHT -> //increasing skewness by 0.05
                
                skewness += 0.05;
            case KeyEvent.VK_SPACE -> // using setWinterMode( bool) to change into Christmas Tree
                setWinterMode(!isWinterMode());
            default -> {
                return;
            }
        }
        // depth
        enforceDepthBounds();

        if (Math.abs(skewness) >= Math.pow(2.0,0.05)) {
            skewness = Math.signum(skewness) * Math.pow(2.0,0.05);
        }

        refreshTree();
    }

    /**
     * Launches the fractal tree visualization. Intended for quick experimentation and demos.
     *
     * @param args ignored command-line arguments
     */
    public static void main(String[] args) {
        try {
            SummerTree cool = new SummerTree();
            System.out.println("The maximum recursion depth range is [1, " + cool.getMaxDepth() + "] in summer mode");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
