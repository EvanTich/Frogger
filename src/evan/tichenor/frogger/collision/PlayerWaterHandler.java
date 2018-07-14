package evan.tichenor.frogger.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.control.PlayerControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 2/2/2018
 */
public class PlayerWaterHandler extends CollisionHandler {

    public PlayerWaterHandler() {
        super(FroggerType.PLAYER, FroggerType.WATER);
    }

    @Override
    protected synchronized void onCollision(Entity player, Entity water) {
        PlayerControl pc = player.getControl(PlayerControl.class);
        if(!pc.isJumping() && !pc.isOnPlatform()) {
            pc.die();
        }
    }
}
