package evan.tichenor.frogger.collision;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/20/2018
 */
public class PlayerFlyHandler extends CollisionHandler {

    public PlayerFlyHandler() {
        super(FroggerType.PLAYER, FroggerType.FLY);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity fly) {
        FXGL.getApp().getGameState().increment("score", 200);
    }
}
