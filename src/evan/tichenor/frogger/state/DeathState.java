package evan.tichenor.frogger.state;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.SubState;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import evan.tichenor.frogger.FroggerApp;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


import static evan.tichenor.frogger.Config.*;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/19/2018
 */
public class DeathState extends SubState {

    private double time;

    private Point2D position;

    private AnimatedTexture deathTexture;

    public DeathState() {
        position = FXGL.<FroggerApp>getAppCast().player().getEntity().getCenter();

        time = 3;
        initMask();
        initDeathAnim();
    }

    private void initMask() {
        Shape masker = new Rectangle(App.WIDTH, App.HEIGHT);
        masker = Shape.subtract(masker, new Circle(position.getX(), position.getY(), 2 * App.TILE_SIZE));
        masker.setFill(new Color(0, 0, 0, .5));
        getChildren().add(masker);
    }

    private void initDeathAnim() {
        deathTexture = new AnimatedTexture(
                new AnimationChannel(
                        Player.DEATH, 3, (int) Player.WIDTH, (int) Player.HEIGHT, Player.DEATH_DURATION, 0, 2
        ));

        deathTexture.setX(position.getX() - Player.WIDTH / 2);
        deathTexture.setY(position.getY() - Player.HEIGHT / 2);

        getChildren().add(deathTexture);
        deathTexture.start(this);

        FXGL.getMasterTimer().runOnceAfter(() -> getChildren().remove(deathTexture), Player.DEATH_DURATION);
    }

    @Override
    protected void onUpdate(double tpf) {
        deathTexture.onUpdate(tpf);
        time -= tpf;
        if(time <= 0) {
            FXGL.getApp().getStateMachine().popState();
        }
    }

    @Override
    protected void onExit() {
        FXGL.getApp().getGameState().increment("lives", -1);
        getChildren().removeIf(n -> true);
        FXGL.<FroggerApp>getAppCast().resetFrog();
    }
}
