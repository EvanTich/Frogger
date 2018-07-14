package evan.tichenor.frogger.control.lane;

import com.almasb.fxgl.entity.Entity;
import evan.tichenor.frogger.control.mover.CarControl;
import evan.tichenor.frogger.control.mover.MovableControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 2/13/2018
 */
public class CarLaneControl extends LaneControl {

    public CarLaneControl(Entity mover, double speed, int number) {
        super(mover, speed, number);
    }

    @Override
    protected MovableControl getControl(int index) {
        return getControl(entityPool.get(index));
    }

    @Override
    protected MovableControl getControl(Entity e) {
        return e.getControl(CarControl.class);
    }

    @Override
    protected void addTexture(Entity e) {
        e.setView(e.getControl(CarControl.class).getView());
    }
}
