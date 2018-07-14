package evan.tichenor.frogger.control.lane;

import com.almasb.fxgl.entity.Entity;
import evan.tichenor.frogger.control.mover.LogControl;
import evan.tichenor.frogger.control.mover.MovableControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 2/13/2018
 */
public class LogLaneControl extends LaneControl {

    private boolean hasGators; // never used as of yet

    public LogLaneControl(Entity mover, double speed, int number, boolean hasGators) {
        super(mover, speed, number);

        this.hasGators = hasGators;
    }

    @Override
    protected MovableControl getControl(int index) {
        return getControl(entityPool.get(index));
    }

    @Override
    protected MovableControl getControl(Entity e) {
        return e.getControl(LogControl.class);
    }

    @Override
    protected void addTexture(Entity e) {
        e.setView(LogControl.createSpecifiedView(e.getControl(LogControl.class).getSize()));
    }
}
