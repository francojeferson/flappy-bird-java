import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {

    private enum GameState { MENU, PLAYING, GAME_OVER }

    private GameState currentState;
    private final Bird bird;
    private final PipeManager pipeManager;
    private int score;
    private int highScore;
    private final Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Constants.SKY_BLUE);
        setFocusable(true);
        addKeyListener(this);

        bird = new Bird();
        pipeManager = new PipeManager();
        score = 0;
        highScore = 0;
        currentState = GameState.MENU;

        timer = new Timer(Constants.FRAME_TIME, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        if (currentState == GameState.PLAYING) {
            bird.update(Constants.DELTA_TIME);
            pipeManager.update(Constants.DELTA_TIME);
            updateScore();
            if (checkCollision()) {
                currentState = GameState.GAME_OVER;
            }
        }
        repaint();
    }

    private void updateScore() {
        for (Pipe pipe : pipeManager.getPipes()) {
            if (!pipe.isPassed() && Constants.BIRD_X > pipe.getX() + Constants.PIPE_WIDTH) {
                pipe.setPassed(true);
                score++;
            }
        }
    }

    private boolean checkCollision() {
        Rectangle birdBounds = bird.getBounds();

        // Ground
        if (birdBounds.y + birdBounds.height >= Constants.GROUND_Y) {
            return true;
        }

        // Ceiling
        if (birdBounds.y <= 0) {
            return true;
        }

        // Pipes
        for (Pipe pipe : pipeManager.getPipes()) {
            if (birdBounds.intersects(pipe.getTopBounds())) {
                return true;
            }
            if (birdBounds.intersects(pipe.getBottomBounds())) {
                return true;
            }
        }

        return false;
    }

    private void resetGame() {
        bird.reset();
        pipeManager.reset();
        if (score > highScore) {
            highScore = score;
        }
        score = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(Constants.SKY_BLUE);
        g2d.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

        // Pipes
        g2d.setColor(Constants.PIPE_GREEN);
        for (Pipe pipe : pipeManager.getPipes()) {
            g2d.fill(pipe.getTopBounds());
            g2d.fill(pipe.getBottomBounds());
        }

        // Ground
        g2d.setColor(Constants.GROUND_BROWN);
        g2d.fillRect(0, Constants.GROUND_Y, Constants.WIDTH, Constants.HEIGHT - Constants.GROUND_Y);

        // Bird
        g2d.setColor(Constants.BIRD_YELLOW);
        g2d.fillOval(Constants.BIRD_X, (int) bird.getY(), Constants.BIRD_SIZE, Constants.BIRD_SIZE);

        // Score
        drawScoreText(g2d);

        // State UI
        if (currentState == GameState.MENU) {
            drawCenteredText(g2d, "Press SPACE to Start", Constants.HEIGHT / 2);
        } else if (currentState == GameState.GAME_OVER) {
            drawCenteredText(g2d, "Game Over", Constants.HEIGHT / 2 - 40);
            drawCenteredText(g2d, "Score: " + score, Constants.HEIGHT / 2);
            if (highScore > 0) {
                drawCenteredText(g2d, "Best: " + highScore, Constants.HEIGHT / 2 + 40);
            }
            drawCenteredText(g2d, "Press SPACE to Restart", Constants.HEIGHT / 2 + 80);
        }
    }

    private void drawScoreText(Graphics2D g2d) {
        if (currentState != GameState.PLAYING) return;

        String text = String.valueOf(score);
        Font font = new Font("Arial", Font.BOLD, 36);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (Constants.WIDTH - fm.stringWidth(text)) / 2;
        int y = 50;

        // Black outline
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x - 2, y);
        g2d.drawString(text, x + 2, y);
        g2d.drawString(text, x, y - 2);
        g2d.drawString(text, x, y + 2);

        // White text on top
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }

    private void drawCenteredText(Graphics2D g2d, String text, int y) {
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (Constants.WIDTH - fm.stringWidth(text)) / 2;

        // Black outline
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x - 1, y);
        g2d.drawString(text, x + 1, y);
        g2d.drawString(text, x, y - 1);
        g2d.drawString(text, x, y + 1);

        // White text on top
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            switch (currentState) {
                case MENU -> {
                    resetGame();
                    currentState = GameState.PLAYING;
                }
                case PLAYING -> bird.flap();
                case GAME_OVER -> {
                    resetGame();
                    currentState = GameState.PLAYING;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
