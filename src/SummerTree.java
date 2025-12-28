
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.Serial;

public class SummerTree extends SummerTreeAbstract {

    @Serial
    private static final long serialVersionUID = 1L;

    public SummerTree() {
        super();
    }
    //Calculating value of Pi using computeHelper() method
    @Override
    protected double computePi(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid number!");
        }

        return computePiHelper(n, 1);
    }

    private double computePiHelper(int n, double last) {
        if (n == 0) {
            return last * 2.0 ;
        } else {
            return computePiHelper(n - 1, 1.0 + (double) n / (n * 2.0 + 1.0) * last);
        }
    }
    //implementing method drawTreeRec() from superClass
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

    //implementing the tree's event handler handleInput(int keyCode) from superClass
    @Override
    public void handleInput(int keyCode) {

        switch (keyCode) {
            case KeyEvent.VK_UP -> //depth is in range [-0.25,+0.25]
                depth += 2;
            case KeyEvent.VK_DOWN -> //depth is in range [-0.25,+0.25]
                depth -= 2;
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

    public static void main(String[] args) {
        try {
            SummerTree cool = new SummerTree();
            System.out.println( "The Maximum recursion depth is "+cool.depth+ " ranging [1,15]");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
