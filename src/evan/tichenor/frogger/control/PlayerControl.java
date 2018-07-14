package evan.tichenor.frogger.control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import evan.tichenor.frogger.Config;
import evan.tichenor.frogger.control.mover.MovableControl;
import evan.tichenor.frogger.state.DeathState;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/25/2018
 */
public class PlayerControl extends Control {

    public static Point2D spawnLocation;
    private static double minimumY;

    private Entity player;

    private Entity ladyFrog; // null until we collide with a lady frog, then the ladyFrog is "stuck" to the players back.

    private MovableControl platform;
    private boolean onPlatform;

    private Point2D startJump;
    private Point2D endJump;
    private long startJumpTime; // in millis

    private AnimatedTexture texture;
    private AnimationChannel
            animIdle, // just sitting there
            animJump; // jump


    public PlayerControl() {
        animIdle = new AnimationChannel(Config.Player.JUMP, 3, (int) Config.Player.WIDTH, (int) Config.Player.HEIGHT, Duration.seconds(1), 2, 2);
        animJump = new AnimationChannel(Config.Player.JUMP, 3, (int) Config.Player.WIDTH, (int) Config.Player.HEIGHT, Config.Player.MOVE_DURATION, 0, 1);

        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded(Entity entity) {
        player = entity;
        player.setView(texture);

        if(spawnLocation == null)
            spawnLocation = player.getPosition();
        if(minimumY == 0)
            minimumY = player.getPosition().getY() + Config.App.TILE_SIZE / 2;
    }

    @Override
    public void onUpdate(Entity e, double v) {
        // if jumping
        if(isJumping()) {
            // linearly interpolate (lerp) to endJump from startJump
            player.setPosition(
                    FXGLMath.lerp(
                            startJump.getX(), startJump.getY(),
                            endJump.getX(), endJump.getY(),
                            Math.min((System.currentTimeMillis() - startJumpTime) / Config.Player.MOVE_DURATION.toMillis(), 1)
                    ));

            // if jump finished
            if(player.getPosition().equals(endJump)) {
                texture.setAnimationChannel(animIdle);
                startJump = null;
                endJump = null;
            }
        }

        if(onPlatform && platform != null) {
            player.setX(player.getX() + platform.getSpeed() * v);
        }

        // if lady frog on back
        if(ladyFrog != null) {
            ladyFrog.setX(player.getX() + Config.Player.LADY_FROG_X_OFFSET);
            ladyFrog.setY(player.getY() + Config.Player.LADY_FROG_Y_OFFSET);
        }
    }

    /**
     * Initiates a jump in the specified direction, if possible.
     * @param direction use UP, DOWN, LEFT, or RIGHT
     */
    public void move(Config.Direction direction) {
        if(canMove(direction)) { // can they actually move?
            startJump = player.getPosition(); // I need this to use in the linear interpolation
            startJumpTime = System.currentTimeMillis();

            texture.setAnimationChannel(animJump); // the player is jumping now, so set the correct animation

            switch(direction) { // move in direction: set correct rotation for the sprite && set endJump
                case UP:
                    rotateTowards(0);
                    endJump = startJump.add(0, -Config.Player.MOVE_AMOUNT);
                    break;
                case DOWN:
                    rotateTowards(180);
                    endJump = startJump.add(0, Config.Player.MOVE_AMOUNT);
                    break;
                case LEFT:
                    rotateTowards(270);
                    endJump = startJump.add(-Config.Player.MOVE_AMOUNT, 0);

                    if(onPlatform)
                        endJump = endJump.add(platform.getSpeed() * Config.Player.MOVE_DURATION.toSeconds(), 0);
                    break;
                case RIGHT:
                    rotateTowards(90);
                    endJump = startJump.add(Config.Player.MOVE_AMOUNT, 0);

                    if(onPlatform)
                        endJump = endJump.add(platform.getSpeed() * Config.Player.MOVE_DURATION.toSeconds(), 0);
                    break;
            }
        }
    }

    public boolean isJumping() {
        return startJump != null && endJump != null;
    }

    /**
     * TRUE if can move and is not moving at that instant
     * @param direction
     * @return
     */
    public boolean canMove(Config.Direction direction) {
        return !isJumping()
            && (direction == Config.Direction.UP && player.getY() - Config.Player.MOVE_AMOUNT >= 0
            || direction == Config.Direction.DOWN && (player.getBottomY() + Config.Player.MOVE_AMOUNT <= Config.App.HEIGHT && player.getY() + Config.Player.MOVE_AMOUNT < minimumY)
            || direction == Config.Direction.LEFT && player.getX() - Config.Player.MOVE_AMOUNT >= 0
            || direction == Config.Direction.RIGHT && player.getRightX() + Config.Player.MOVE_AMOUNT <= Config.App.WIDTH);
    }

    public void rotateTowards(double angleDegrees) {
        getEntity().setRotation(angleDegrees);
    }

    public boolean isOnPlatform() {
        return onPlatform || platforms > 0;
    }

    private int platforms;

    public synchronized void setPlatform(MovableControl platform) {
        this.platform = platform;
        onPlatform = true;
        platforms++;
    }

    /**
     * Call when the player jumps off a platform
     */
    public synchronized void removePlatform() {
        platform = null;
        onPlatform = false;
        platforms--;
    }

    /**
     * Called when the player contacts a lady frog
     * @param ladyFrog
     */
    public void setLadyFrog(Entity ladyFrog) {
        this.ladyFrog = ladyFrog;
    }

    public Entity getLadyFrog() {
        return ladyFrog;
    }

    public boolean hasLadyFrog() {
        return ladyFrog != null;
    }

    public void die() {
        if(hasLadyFrog())
            ladyFrog.removeFromWorld();

        if(getEntity() != null && getEntity().isActive())
            FXGL.getApp().getStateMachine().pushState(new DeathState());
    }
}
