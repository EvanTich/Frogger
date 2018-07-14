package evan.tichenor.frogger.control.lane;

import com.almasb.fxgl.entity.Entity;
import evan.tichenor.frogger.control.mover.MovableControl;
import evan.tichenor.frogger.control.mover.TurtleControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 2/13/2018
 */
public class TurtleLaneControl extends LaneControl {

    public TurtleLaneControl(Entity mover, double speed, int number) {
        super(mover, speed, number);
    }

    @Override
    protected MovableControl getControl(int index) {
        return getControl(entityPool.get(index));
    }

    @Override
    protected MovableControl getControl(Entity e) {
        return e.getControl(TurtleControl.class);
    }

    @Override
    protected void addTexture(Entity e) {
        e.setView(TurtleControl.createSpecifiedView(e.getControl(TurtleControl.class).getNumber()));
    }
}
