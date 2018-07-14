package evan.tichenor.frogger.control;

import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import evan.tichenor.frogger.control.mover.LogControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 4/10/2018
 */
public class SnakeControl extends Control {

    private LogControl spawnPlatform;

    public SnakeControl(LogControl spawnPlatform) {
        super();
        this.spawnPlatform = spawnPlatform;
    }

    @Override
    public void onAdded(Entity entity) {
        super.onAdded(entity);
    }

    @Override
    public void onUpdate(Entity entity, double v) {
        // move until it hits water or remove if it goes off screen
//        if(spawnPlatform.getEntity().)
    }
}
