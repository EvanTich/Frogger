package evan.tichenor.frogger;

import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.control.KeepOnScreenControl;
import evan.tichenor.frogger.control.lane.LogLaneControl;
import evan.tichenor.frogger.control.lane.TurtleLaneControl;
import evan.tichenor.frogger.control.mover.LogControl;
import evan.tichenor.frogger.control.LadyFrogControl;
import evan.tichenor.frogger.control.LilypadControl;
import evan.tichenor.frogger.control.SnakeControl;
import evan.tichenor.frogger.control.lane.*;
import evan.tichenor.frogger.control.mover.CarControl;
import evan.tichenor.frogger.control.PlayerControl;
import evan.tichenor.frogger.control.mover.MovableControl;
import evan.tichenor.frogger.control.mover.TurtleControl;
import evan.tichenor.frogger.control.lane.CarLaneControl;
import evan.tichenor.frogger.control.lane.EmptyLaneControl;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.5, 1/25/2018
 */
public class FroggerFactory implements EntityFactory {

    @Spawns(Config.Player.PLAYER)
    public Entity newPlayer(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(FroggerType.PLAYER)
                .renderLayer(RenderLayer.TOP)
                .viewFromNodeWithBBox(Config.Player.VIEW.copy())
                .with(new PlayerControl())
                .with(new KeepOnScreenControl(true, true))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.TURTLES)
    public Entity newTurtles(SpawnData data) {
        int number = data.get("size");

        return Entities.builder()
                .from(data)
                .type(FroggerType.PLATFORM)
                .renderLayer(Config.Lane.MOVER_LAYER)
                .viewFromNodeWithBBox(TurtleControl.createSpecifiedView(number))
                .with(new TurtleControl(number))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.VEHICLE)
    public Entity newVehicle(SpawnData data) {
        String view = data.get("view");

        return Entities.builder()
                .from(data)
                .type(FroggerType.BAD)
                .renderLayer(Config.Lane.MOVER_LAYER)
                .viewFromNodeWithBBox(CarControl.createSpecifiedView(view))
                .with(new CarControl(view))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.LOG)
    public Entity newLog(SpawnData data) {
        int size = data.get("size");

        return Entities.builder()
                .from(data)
                .type(FroggerType.PLATFORM)
                .renderLayer(Config.Lane.MOVER_LAYER)
                .viewFromNodeWithBBox(LogControl.createSpecifiedView(size))
                .with(new LogControl(size))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.Lane.TURTLE_LANE)
    public Entity newTurtleLane(SpawnData data) {
        double speed = data.get("speed");
        int number = data.get("number");
        Entity mover = newTurtles(data);

        return Entities.builder()
                .from(data)
                .type(FroggerType.WATER)
                .renderLayer(Config.Lane.LANE_LAYER)
                .viewFromNodeWithBBox(new Rectangle(Config.App.WIDTH, Config.App.TILE_SIZE, Color.BLUE))
                .with(new TurtleLaneControl(mover, speed, number))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.Lane.CAR_LANE)
    public Entity newCarLane(SpawnData data) {
        double speed = data.get("speed");
        Entity mover = newVehicle(data);
        int number = data.get("number");

        return Entities.builder()
                .from(data)
                .type(FroggerType.PLATFORM) // platform because we want the frog to die when it isn't on a platform, i.e. water is deadly
                .renderLayer(Config.Lane.LANE_LAYER)
                .viewFromNodeWithBBox(new Rectangle(Config.App.WIDTH, Config.App.TILE_SIZE))
                .with(new CarLaneControl(mover, speed, number))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.Lane.LOG_LANE)
    public Entity newLogLane(SpawnData data) {
        double speed = data.get("speed");
        int number = data.get("number");
        Entity mover = newLog(data);
        boolean hasGators = data.get("gators");

        return Entities.builder()
                .from(data)
                .type(FroggerType.WATER)
                .renderLayer(Config.Lane.LANE_LAYER)
                .viewFromNodeWithBBox(new Rectangle(Config.App.WIDTH, Config.App.TILE_SIZE, Color.BLUE))
                .with(new LogLaneControl(mover, speed, number, hasGators))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.Lane.EMPTY_LANE)
    public Entity newEmptyLane(SpawnData data) {
        double speed = data.get("speed");
        boolean hasSnakes = data.get("snakes");

        Entity mover = null;

        return Entities.builder()
                .from(data)
                .type(FroggerType.PLATFORM)
                .renderLayer(Config.Lane.LANE_LAYER)
                .viewFromNodeWithBBox(new Rectangle(Config.App.WIDTH, Config.App.TILE_SIZE, Color.GRAY))
                .with(new EmptyLaneControl(mover, speed, hasSnakes))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.LILY_PAD)
    public Entity newLilyPad(SpawnData data) {
        boolean gators = data.get("gators");

        return Entities.builder()
                .from(data)
                .type(FroggerType.HOME)
                .renderLayer(Config.Lane.LANE_LAYER)
                .viewFromNodeWithBBox(new Rectangle(Config.LilyPad.PLAYER_AREA, Config.LilyPad.LENGTH, new Color(0, 0, 0, 0)))
                .with(new LilypadControl())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.FLY)
    public Entity newFly(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(FroggerType.FLY)
                .viewFromNodeWithBBox(Config.LilyPad.FLY_TEXTURE.copy())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.STATIC_FROG)
    public Entity newStaticFrog(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(FroggerType.BAD)
                .viewFromNodeWithBBox(Config.LilyPad.FROG_TEXTURE.copy())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns(Config.SpawnEntity.SNAKE)
    public Entity newSnake(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(FroggerType.BAD)
                .viewFromNodeWithBBox(new Rectangle(Config.App.TILE_SIZE, Config.App.TILE_SIZE, Color.LAWNGREEN))
                .with(new CollidableComponent(true))
                .with(new SnakeControl(null))
                .build();
    }

    @Spawns(Config.SpawnEntity.LADY_FROG)
    public Entity newLadyFrog(SpawnData data) {
        MovableControl platform;
        try {
            platform = data.get("platform");
        } catch (IllegalArgumentException e) {
            platform = null;
        }

        return Entities.builder()
                .from(data)
                .type(FroggerType.LADY_FROG)
                .viewFromNodeWithBBox(Config.Logs.LADY_FROG_VIEW.copy())
                .with(new CollidableComponent(true))
                .with(new LadyFrogControl(platform))
                .build();
    }
}
