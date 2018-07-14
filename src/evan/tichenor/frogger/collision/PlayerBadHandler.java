package evan.tichenor.frogger.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.control.PlayerControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/25/2018
 */
public class PlayerBadHandler extends CollisionHandler {

    public PlayerBadHandler() {
        super(FroggerType.PLAYER, FroggerType.BAD);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity bad) {
        player.getControl(PlayerControl.class).die();
    }
}
