package evan.tichenor.frogger.collision;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.control.LilypadControl;
import evan.tichenor.frogger.control.PlayerControl;
import evan.tichenor.frogger.state.EndScoringState;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/21/2018
 */
public class PlayerHomeHandler extends CollisionHandler {

    public PlayerHomeHandler() {
        super(FroggerType.PLAYER, FroggerType.HOME);
    }

    @Override
    protected void onCollision(Entity player, Entity home) {
        PlayerControl pc = player.getControl(PlayerControl.class);

        if(pc.isJumping())
            return; // wait until the player is not jumping

        if(home.getControl(LilypadControl.class).hasFrog())
            pc.die();

        FXGL.getApp().getGameState().increment("score", 50);

        if(pc.hasLadyFrog()) {
            FXGL.getApp().getGameState().increment("score", 200);
            pc.getLadyFrog().removeFromWorld();
        }

        home.getControl(LilypadControl.class).setHasFrog(true);

        FXGL.getApp().getStateMachine().pushState(new EndScoringState());
    }
}
