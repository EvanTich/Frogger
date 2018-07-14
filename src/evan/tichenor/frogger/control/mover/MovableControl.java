package evan.tichenor.frogger.control.mover;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.CopyableControl;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/26/2018
 */
public abstract class MovableControl<T extends Control> extends Control implements CopyableControl<T> {

    protected double speed;
    protected boolean stopped;

    public MovableControl(double speed) {
        setSpeed(speed);
        stop();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;

        // this assumes if the entity is pointing to the right
        if(getEntity() != null) {
            flipEntityIfNeeded(getEntity(), false);
        }
    }

    public void go() {
        stopped = false;
    }

    public void stop() {
        stopped = true;
    }

    public boolean isMoving() {
        return !stopped;
    }

    public void move(Entity entity, double tpf) {
        entity.setX(entity.getX() + getSpeed() * tpf);

        if(entity.getRightX() + getTotalWidth() < 0) {
            stop();
            entity.setX(FXGL.getAppWidth() + getTotalWidth());
        } else if(entity.getX() > FXGL.getAppWidth() + getTotalWidth()) {
            stop();
            entity.setX(-getTotalWidth());
        }
    }

    public abstract double getTotalWidth();

    protected void flipEntityIfNeeded(Entity e, boolean isFlipped) {
        if (speed < 0 && !isFlipped)
            getEntity().setScaleX(-1);
        else getEntity().setScaleX(1);
    }
}
