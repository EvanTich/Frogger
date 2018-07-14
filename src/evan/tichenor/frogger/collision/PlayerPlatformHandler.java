package evan.tichenor.frogger.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.control.mover.LogControl;
import evan.tichenor.frogger.control.mover.TurtleControl;
import evan.tichenor.frogger.control.PlayerControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/19/2018
 */
public class PlayerPlatformHandler extends CollisionHandler {

    public PlayerPlatformHandler() {
        super(FroggerType.PLAYER, FroggerType.PLATFORM);
    }

    @Override
    protected synchronized void onCollision(Entity player, Entity platform) {
        if(platform.hasControl(TurtleControl.class))
            player.getControl(PlayerControl.class)
                    .setPlatform(platform.getControl(TurtleControl.class));
        else if(platform.hasControl(LogControl.class))
            player.getControl(PlayerControl.class)
                    .setPlatform(platform.getControl(LogControl.class));
    }

    @Override
    protected synchronized void onCollisionEnd(Entity player, Entity platform) {
        player.getControl(PlayerControl.class).removePlatform();
    }
}
