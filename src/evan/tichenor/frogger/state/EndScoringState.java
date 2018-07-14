package evan.tichenor.frogger.state;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.State;
import com.almasb.fxgl.app.SubState;
import com.almasb.fxgl.time.LocalTimer;
import evan.tichenor.frogger.FroggerType;
import evan.tichenor.frogger.FroggerApp;
import evan.tichenor.frogger.control.LilypadControl;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/21/2018
 */
public class EndScoringState extends SubState {

    private LocalTimer timer;

    private int clockBeatsToAdd;
    private boolean addThisFrame;

    @Override
    protected void onEnter(State prevState) {
        // get the number of clock beats to add
        clockBeatsToAdd = (int) (FXGL.getApp().getGameState().getDouble("timeLeft") * 2);

        timer = FXGL.newLocalTimer();
        timer.capture();
    }

    @Override
    protected void onUpdate(double tpf) {
        if(clockBeatsToAdd > 0 && addThisFrame) {
            timer.capture();
            clockBeatsToAdd--;
            FXGL.getApp().getGameState().increment("score", 10);
            FXGL.getApp().getGameState().increment("timeLeft", -.5);
        } else if(clockBeatsToAdd <= 0) {
            FXGL.getApp().getStateMachine().popState();
        }

        addThisFrame = !addThisFrame;
    }

    @Override
    protected void onExit() {
        // if all frogs have been gathered in lily pad areas
        if(FXGL.getApp().getGameWorld().getEntitiesByType(FroggerType.HOME)
                .stream().allMatch(e -> e.getControl(LilypadControl.class).hasFrog())) {
            FXGL.getApp().getGameState().increment("score", 1000);

            FXGL.<FroggerApp>getAppCast().nextLevel();
        } else
            FXGL.<FroggerApp>getAppCast().resetFrog();
    }
}
