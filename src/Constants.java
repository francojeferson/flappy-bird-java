import java.awt.Color;

public class Constants {
    // Window
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Flappy Bird";

    // Timing
    public static final int FRAME_TIME = 16;
    public static final double DELTA_TIME = 0.016;

    // Bird
    public static final double GRAVITY = 1200;
    public static final double FLAP_VELOCITY = -400;
    public static final int BIRD_SIZE = 30;
    public static final int BIRD_X = 80;
    public static final int START_Y = HEIGHT / 2;
    public static final int HITBOX_PADDING = 3;

    // Pipes
    public static final int PIPE_WIDTH = 60;
    public static final int GAP_HEIGHT = 150;
    public static final double PIPE_SPEED = 200;
    public static final double SPAWN_INTERVAL = 1.5;
    public static final int MIN_GAP_Y = 100;
    public static final int MAX_GAP_Y = 425;

    // Ground
    public static final int GROUND_Y = HEIGHT - 50;

    // Colors
    public static final Color SKY_BLUE = new Color(135, 206, 235);
    public static final Color PIPE_GREEN = new Color(34, 139, 34);
    public static final Color GROUND_BROWN = new Color(139, 69, 19);
    public static final Color BIRD_YELLOW = new Color(255, 255, 0);
}
