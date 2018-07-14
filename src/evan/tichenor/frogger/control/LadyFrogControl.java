package evan.tichenor.frogger.control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import evan.tichenor.frogger.Config;
import evan.tichenor.frogger.control.mover.MovableControl;
import javafx.geometry.Point2D;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 4/10/2018
 */
public class LadyFrogControl extends Control {

    private Point2D startJump;
    private Point2D endJump;
    private long startJumpTime; // in millis

    private int jumpCounter;
    private Config.Direction jumpDirection;

    private LocalTimer timer;

    private MovableControl platform;

    public static boolean ladyFrogActive = false;

    /**
     * Make the lady frog jump twice right and then twice left.
     */
    public LadyFrogControl(MovableControl platform) {
        this.platform = platform;
    }

    @Override
    public void onAdded(Entity entity) {
        timer = FXGL.newLocalTimer();
        jumpDirection = Config.Direction.RIGHT;
        entity.setOnNotActive(() -> ladyFrogActive = false);

        move(entity);
    }

    @Override
    public void onUpdate(Entity entity, double v) {
        if(isJumping()) {
            // linearly interpolate (lerp) to endJump from startJump in MOVE_DURATION seconds
            entity.setPosition(
                    FXGLMath.lerp(
                            startJump.getX(), startJump.getY(),
                            endJump.getX(), endJump.getY(),
                            Math.min((System.currentTimeMillis() - startJumpTime) / Config.Player.MOVE_DURATION.toMillis(), 1)
                    ));

            // if jump finished
            if(entity.getPosition().equals(endJump)) {
                startJump = null;
                endJump = null;

                timer.capture();
            }
        } else if(timer.elapsed(Config.Lane.LADY_FROG_WAIT_TIME)) {
            move(entity);
        }

        if(platform != null) {
            entity.setX(entity.getX() + platform.getSpeed() * v);
        }
    }

    public boolean isJumping() {
        return startJump != null && endJump != null;
    }

    private void move(Entity entity) {
        startJump = entity.getPosition();
        startJumpTime = System.currentTimeMillis();

        switch(jumpDirection) { // if so: move
            case LEFT:
                rotateTowards(90);
                endJump = startJump.add(-Config.Player.MOVE_AMOUNT, 0);
                jumpCounter--;

                if(jumpCounter <= 0)
                    jumpDirection = Config.Direction.RIGHT;
                break;
            case RIGHT:
                rotateTowards(270);
                endJump = startJump.add(Config.Player.MOVE_AMOUNT, 0);
                jumpCounter++;

                if(jumpCounter >= 2)
                    jumpDirection = Config.Direction.LEFT;
                break;
            default:
                jumpDirection = Config.Direction.RIGHT;
                break;
        }

        endJump = endJump.add(platform.getSpeed() * Config.Player.MOVE_DURATION.toSeconds(), 0);
    }

    public void rotateTowards(double angleDegrees) {
        getEntity().setRotation(angleDegrees);
    }

    public void setPlatform(MovableControl platform) {
        this.platform = platform;
    }
}
