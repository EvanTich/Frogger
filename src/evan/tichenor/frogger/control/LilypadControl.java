package evan.tichenor.frogger.control;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Control;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.LocalTimer;
import evan.tichenor.frogger.Config;
import evan.tichenor.frogger.FroggerType;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/14/2018
 */
public class LilypadControl extends Control {

    private static LocalTimer timer;
    private static boolean flyAlive;

    static {
        timer = FXGL.newLocalTimer();
        flyAlive = false;
    }

    private Entity entity;
    private boolean hasFrog;

    @Override
    public void onUpdate(Entity lilypadEntity, double v) {
        if(!hasFrog) {
            if (!flyAlive && timer.elapsed(Config.App.FLY_APPEAR)) {
                spawnFlySomewhere();
            } else if (flyAlive && this.entity != null && timer.elapsed(Config.App.FLY_APPEARANCE_DURATION)) {
                this.entity.removeFromWorld();
                this.entity = null;
                flyAlive = false;
                timer.capture(); // reset timer
            }
        }
    }

    private static void spawnFlySomewhere() {
        List<LilypadControl> homeList = FXGL.getApp()
                .getGameWorld().getEntitiesByType(FroggerType.HOME)
                .stream().map(e -> e.getControl(LilypadControl.class))
                .collect(Collectors.toList());

        if(homeList.stream().allMatch(LilypadControl::hasFrog)) { // if all lilypads have frogs
            // uhh? not really supposed to happen
            return;
        }

        LilypadControl home;
        do {
            home = homeList.get((int) (Math.random() * homeList.size()));
        } while (home.hasFrog());

        home.spawnFly();
    }

    public void spawnFly() {
        flyAlive = true;
        timer.capture();

        entity = FXGL.getApp().getGameWorld()
                .spawn(Config.SpawnEntity.FLY, getSpawnLocation());
    }

    private void spawnFrog() {
        if(entity != null)
            entity.removeFromWorld();
        entity = FXGL.getApp().getGameWorld()
                .spawn(Config.SpawnEntity.STATIC_FROG, getSpawnLocation());
    }

    public void setHasFrog(boolean hasFrog) {
        if(hasFrog) {
            // spawn frog
            this.hasFrog = true;
            spawnFrog();
        }
    }

    private Point2D getSpawnLocation() {
        return getEntity().getPosition().add(Config.App.TILE_SIZE * .1, 0);
    }

    public boolean hasFrog() {
        return hasFrog;
    }
}
