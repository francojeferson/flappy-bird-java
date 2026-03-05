import java.awt.Rectangle;

public class Bird {
    private double y;
    private double velocityY;

    public Bird() {
        reset();
    }

    public void update(double deltaTime) {
        velocityY += Constants.GRAVITY * deltaTime;
        y += velocityY * deltaTime;
    }

    public void flap() {
        velocityY = Constants.FLAP_VELOCITY;
    }

    public void reset() {
        y = Constants.START_Y;
        velocityY = 0;
    }

    public double getY() {
        return y;
    }

    public Rectangle getBounds() {
        int pad = Constants.HITBOX_PADDING;
        return new Rectangle(
            Constants.BIRD_X + pad,
            (int) y + pad,
            Constants.BIRD_SIZE - 2 * pad,
            Constants.BIRD_SIZE - 2 * pad
        );
    }
}
