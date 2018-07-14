package evan.tichenor.frogger.control.lane;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import evan.tichenor.frogger.Config;
import evan.tichenor.frogger.control.mover.MovableControl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/25/2018
 */
public abstract class LaneControl extends Control {

    protected List<Entity> entityPool;
    protected double speed;
    protected int number;

    private int lastMovedIndex;

    public LaneControl(Entity mover, double speed, int number) {
        this.speed = speed;
        this.number = number;

        entityPool = new ArrayList<>();
        entityPool.add(mover); // for later use, eventually gets replaced
    }

    @Override
    public void onAdded(Entity background) {
        background.setRenderLayer(Config.Lane.LANE_LAYER);

        // make the other entities with node mover
        populatePool(entityPool.get(0));
        lastMovedIndex = 0;

        entityPool.forEach(FXGL.getApp().getGameWorld()::addEntity);

        getControl(lastMovedIndex).go(); // has to be moving first, else onUpdate won't work
    }

    @Override
    public void onUpdate(Entity background, double v) {
        if(speed > 0) {
            if(entityPool.get(lastMovedIndex).getX() >= Config.Lane.HORIZONTAL_DISTANCE && canMoveNext()) {
                moveNext();
            }
        } else {
            if(FXGL.getAppWidth() - entityPool.get(lastMovedIndex).getRightX() >= Config.Lane.HORIZONTAL_DISTANCE && canMoveNext()) {
                moveNext();
            }
        }
    }

    protected abstract MovableControl getControl(int index);

    protected abstract MovableControl getControl(Entity e);

    protected abstract void addTexture(Entity e);

    private boolean canMoveNext() {
        int index = lastMovedIndex + 1;
        if(index >= entityPool.size())
            index = 0;

        return !getControl(index).isMoving();
    }

    private void moveNext() {
        if(++lastMovedIndex == entityPool.size()) // fun stuff
            lastMovedIndex = 0;

        getControl(lastMovedIndex).go();
    }

    private void populatePool(Entity mover) {
        getControl(mover).setSpeed(speed);
        positionEntity(mover);

        for(int i = 1; i < number; i++) {
            Entity copy = mover.copy();
            getControl(copy).setSpeed(speed);
            addTexture(copy);

            positionEntity(copy);

            entityPool.add(copy);
        }
    }

    private void positionEntity(Entity e) {
        if(speed < 0)
            e.setX(FXGL.getAppWidth());
        else e.setX(-getControl(e).getTotalWidth());
    }
}
