package evan.tichenor.frogger.control.lane;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import evan.tichenor.frogger.Config;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/1/2018
 */
public class EmptyLaneControl extends Control {

    private Entity snake;
    private double speed;
    private boolean hasSnakes;

    private LocalTimer timer;

    public EmptyLaneControl(Entity snake, double speed, boolean hasSnakes) {
        this.snake = snake;
        this.speed = speed;
        this.hasSnakes = hasSnakes;

        timer = FXGL.newLocalTimer();
    }

    @Override
    public void onAdded(Entity entity) {
        timer.capture();
    }

    public boolean hasSnakes() {
        return hasSnakes;
    }

    @Override
    public void onUpdate(Entity entity, double v) {
        if(hasSnakes && timer.elapsed(Config.Lane.SNAKE_TIMER) && !inBounds(snake)) {
            timer.capture();
            snake.setX(snake.getX() + speed * v);
        }
    }

    private boolean inBounds(Entity e) {
        return e.getX() >= 0 || e.getRightX() <= Config.App.WIDTH;
    }
}
