import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PipeManager {
    private final List<Pipe> pipes;
    private double spawnTimer;
    private final Random random;

    public PipeManager() {
        pipes = new ArrayList<>();
        random = new Random();
        spawnTimer = -1.0;
    }

    public void update(double deltaTime) {
        spawnTimer += deltaTime;
        if (spawnTimer >= Constants.SPAWN_INTERVAL) {
            spawnPipe();
            spawnTimer = 0;
        }

        for (Pipe pipe : pipes) {
            pipe.update(deltaTime);
        }

        Iterator<Pipe> it = pipes.iterator();
        while (it.hasNext()) {
            if (it.next().isOffScreen()) {
                it.remove();
            }
        }
    }

    private void spawnPipe() {
        int gapY = Constants.MIN_GAP_Y + random.nextInt(Constants.MAX_GAP_Y - Constants.MIN_GAP_Y + 1);
        pipes.add(new Pipe(Constants.WIDTH, gapY));
    }

    public List<Pipe> getPipes() {
        return pipes;
    }

    public void reset() {
        pipes.clear();
        spawnTimer = -1.0;
    }
}
