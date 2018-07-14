package evan.tichenor.frogger.control.mover;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.texture.Texture;
import evan.tichenor.frogger.Config;
import javafx.geometry.HorizontalDirection;

import static evan.tichenor.frogger.Config.Logs.*;
import static evan.tichenor.frogger.control.LadyFrogControl.ladyFrogActive;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 2/13/2018
 */
public class LogControl extends MovableControl<LogControl> {

    private int size;

    private Entity ladyFrog;

    public LogControl(int size) {
        super(100);
        this.size = size;
    }

    @Override
    public void onUpdate(Entity entity, double v) {
        if(isMoving())
            move(entity, v);
    }

    @Override
    public double getTotalWidth() {
        return getEntity().getWidth();
    }

    @Override
    public LogControl copy() {
        return new LogControl(size);
    }

    public int getSize() {
        return size;
    }

    @Override
    public void go() {
        super.go();

        // randomly spawn a lady frog
        if(!ladyFrogActive && size == 3) {
            if(Math.random() <= LADY_FROG_SPAWN_CHANCE) {
                ladyFrogActive = true;

                ladyFrog = FXGL.getApp().getGameWorld()
                        .spawn(Config.SpawnEntity.LADY_FROG,
                            new SpawnData(getEntity().getPosition())
                                .put("platform", this)
                        );
            }
        }
    }

    @Override
    public void stop() {
        super.stop();

        if(ladyFrog != null && (ladyFrog.getX() <= 0 || ladyFrog.getRightX() >= Config.App.WIDTH)) {
            ladyFrog.removeFromWorld();
        }
    }

    /**
     *
     * @param size has to be >2
     * @return
     */
    public static Texture createSpecifiedView(int size) {
        Texture texture = TEXTURE_LEFT.copy();
        for(int i = 1; i < size - 1; i++)
            texture = texture.superTexture(TEXTURE_MIDDLE, HorizontalDirection.RIGHT);
        texture = texture.superTexture(TEXTURE_RIGHT, HorizontalDirection.RIGHT);
        return texture;
    }
}
