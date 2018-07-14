package evan.tichenor.frogger.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.control.LadyFrogControl;
import evan.tichenor.frogger.control.PlayerControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 4/14/2018
 */
public class PlayerLadyHandler extends CollisionHandler {

    public PlayerLadyHandler() {
        super(FroggerType.PLAYER, FroggerType.LADY_FROG);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity lady) {
        lady.removeControl(LadyFrogControl.class);
        player.getControl(PlayerControl.class).setLadyFrog(lady);
    }
}
