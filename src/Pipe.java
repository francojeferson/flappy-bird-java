import java.awt.Rectangle;

public class Pipe {
    private double x;
    private final int gapY;
    private boolean passed;

    public Pipe(double x, int gapY) {
        this.x = x;
        this.gapY = gapY;
        this.passed = false;
    }

    public void update(double deltaTime) {
        x -= Constants.PIPE_SPEED * deltaTime;
    }

    public double getX() {
        return x;
    }

    public int getGapY() {
        return gapY;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Rectangle getTopBounds() {
        int topHeight = gapY - Constants.GAP_HEIGHT / 2;
        return new Rectangle((int) x, 0, Constants.PIPE_WIDTH, topHeight);
    }

    public Rectangle getBottomBounds() {
        int bottomY = gapY + Constants.GAP_HEIGHT / 2;
        int bottomHeight = Constants.HEIGHT - bottomY;
        return new Rectangle((int) x, bottomY, Constants.PIPE_WIDTH, bottomHeight);
    }

    public boolean isOffScreen() {
        return x + Constants.PIPE_WIDTH < 0;
    }
}
