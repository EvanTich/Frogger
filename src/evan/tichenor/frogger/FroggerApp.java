package evan.tichenor.frogger;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.io.FS;
import com.almasb.fxgl.settings.GameSettings;
import evan.tichenor.frogger.collision.*;
import evan.tichenor.frogger.ui.LivesUI;
import evan.tichenor.frogger.ui.TimeBarUI;
import evan.tichenor.frogger.control.PlayerControl;
import evan.tichenor.frogger.ui.LevelUI;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/25/2018
 */
public class FroggerApp extends GameApplication {

    private SaveData saveData = null;

    private JSONArray levels;

    private boolean awardedLife;

    private double maxY;

    public PlayerControl player() {
        return getGameWorld().getSingleton(FroggerType.PLAYER).get().getControl(PlayerControl.class);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Frogger");
        settings.setVersion("1");

        settings.setWidth(Config.App.WIDTH);
        settings.setHeight(Config.App.HEIGHT);

        settings.setCloseConfirmation(false);
        settings.setProfilingEnabled(false);
        settings.setIntroEnabled(false);
        settings.setMenuEnabled(false);

        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        FXGL.getAudioPlayer().globalSoundVolumeProperty().set(.1);
    }

    @Override
    protected void initUI() {
        Text score = FXGL.getUIFactory().newText("SCORE");
        score.textProperty()
                .bind(getGameState().intProperty("score")
                        .asString("SCORE\n%s"));
        score.setTextAlignment(TextAlignment.CENTER);
        score.setX(Config.UI.SCORE_X);
        score.setY(Config.UI.TOP_UI_Y);

        Text highscore = FXGL.getUIFactory().newText("HI-SCORE");
        highscore.textProperty()
                .bind(getGameState().intProperty("highscore")
                        .asString("HI-SCORE\n%s"));
        highscore.setTextAlignment(TextAlignment.CENTER);
        highscore.setX(Config.UI.HIGHSCORE_X);
        highscore.setY(Config.UI.TOP_UI_Y);

        // player lives
        LivesUI lives = new LivesUI();
        lives.livesProperty().bind(getGameState().intProperty("lives"));
        lives.setTranslateX(Config.UI.LIVES_X);
        lives.setTranslateY(Config.UI.BOTTOM_UI_Y);

        // current level
        LevelUI level = new LevelUI();
        level.levelProperty().bind(getGameState().intProperty("level"));
        level.setTranslateX(Config.UI.LEVEL_X);
        level.setTranslateY(Config.UI.BOTTOM_UI_Y);

        // time
        TimeBarUI timeLeft = new TimeBarUI(Color.GREEN);
        timeLeft.setMaxValue(Config.App.TIME_LIMIT);
        timeLeft.setMinValue(0);
        timeLeft.setHeight(Config.App.TILE_SIZE / 2 - 2);
        timeLeft.setWidth(Config.App.WIDTH - 1.5 * Config.App.TILE_SIZE);
        timeLeft.currentValueProperty().bind(getGameState().doubleProperty("timeLeft"));
        timeLeft.setScaleX(-1);

        Text justTime = FXGL.getUIFactory().newText("TIME", Config.App.TILE_SIZE * .45);
        justTime.setTextAlignment(TextAlignment.RIGHT);

        HBox timeBox = new HBox(timeLeft, justTime);
        timeBox.setTranslateX(4);
        timeBox.setTranslateY(Config.UI.TIME_UI_Y);

        getGameScene().addUINodes(score, highscore, lives, level, timeBox);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new PlayerBadHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerPlatformHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerWaterHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerHomeHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerFlyHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerLadyHandler());
    }

    @Override
    protected void initGame() {
        awardedLife = false;

        getGameWorld().setEntityFactory(new FroggerFactory());

        getGameState().intProperty("score").addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > getGameState().getInt("highscore"))
                getGameState().setValue("highscore", newValue);
        });

        FS.<SaveData>readDataTask(Config.App.FILE)
                .onSuccess(data -> saveData = data)
                .onFailure(ignore -> {})
                .execute();

        if(saveData == null)
            saveData = new SaveData("Best Frog", 0);

        getGameState().setValue("highscore", saveData.getHighScore());

        initLevel();
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player().move(Config.Direction.UP);
            }
        }, Config.Player.UP);
        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player().move(Config.Direction.DOWN);
            }
        }, Config.Player.DOWN);
        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player().move(Config.Direction.LEFT);
            }
        }, Config.Player.LEFT);
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player().move(Config.Direction.RIGHT);
            }
        }, Config.Player.RIGHT);
        input.addAction(new UserAction("Game Over") {
            @Override
            protected void onAction() {
                gameOver();
            }
        }, Config.App.GAME_OVER_KEY);

        input.addEventHandler(KeyEvent.KEY_PRESSED, new KonamiCode());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("lives", 3); // # of lives the frog starts with
        vars.put("level", 0);

        vars.put("timeLeft", Config.App.TIME_LIMIT);

        vars.put("score", 0);
        vars.put("highscore", 0);
    }

    // purely for debugging purposes
    private int frames;
    private long lastTime;

    @Override
    protected void onUpdate(double tpf) {

        // figuring out fps, debug and developer only
        if(getSettings().getApplicationMode() != ApplicationMode.RELEASE) {
            frames++;
            double dt = (System.nanoTime() - lastTime) / 1e9;

            if (frames == 1) {
                lastTime = System.nanoTime();
            } else if (dt >= 1) {
                System.out.println("delta time: " + dt);
                System.out.println("given fps:  " + 1 / tpf());
                System.out.println("actual fps: " + ((frames / dt) /*- frames * (dt - 1)*/));
                System.out.println();

                frames = 0;
            }
        }

        // end of fps figuring

        if(!awardedLife && getGameState().getInt("score") >= 20000) {
            awardedLife = true;
            getGameState().increment("lives", 1);
        }

        if(!player().isJumping() && maxY > player().getEntity().getY()) {
            maxY = player().getEntity().getY();
            getGameState().increment("score", 10);
        }

        if(getGameState().getDouble("timeLeft") <= 0) {
            player().die();
        }

        if(getGameState().getInt("lives") <= 0) {
            gameOver();
        }
    }

    @Override
    protected void preInit() {
        String level = "";
        for(String s : getAssetLoader().loadJSON("levels.json"))
            level += s;

        levels = new JSONObject(level).getJSONArray("levels");
    }



    private void initLevel() {
        getGameWorld().clear();
        getMasterTimer().clear();
        getGameState().setValue("timeLeft", Config.App.TIME_LIMIT);
        getMasterTimer().runAtInterval(() ->
                getGameState().increment("timeLeft", -.5), Duration.millis(500));

        // spawns black background
        Entities.builder()
                .at(-50, -50) // give leeway for the background
                .viewFromNode(new Rectangle(Config.App.WIDTH + 100, Config.App.HEIGHT + 100, Color.BLACK))
                .renderLayer(RenderLayer.BACKGROUND)
//                .with(new IrremovableComponent()) // ultimately not needed
                .buildAndAttach();

        JSONObject currentLevel = levels.getJSONObject(getGameState().getInt("level") % 5);

        double y = Config.App.START_Y;
        // yeah...
        Function<Rectangle, Rectangle> withColor = (rect) -> {
            rect.setFill(Color.LIGHTGREEN);
            rect.setStroke(Color.LIGHTGREEN);

            return rect;
        };

        // spawn top
        Entities.builder()
                .at(0, y)
                .viewFromNodeWithBBox(withColor.apply(new Rectangle(0, 0, Config.App.WIDTH, Config.LilyPad.WIDTH)))
                .buildAndAttach();

        y += Config.LilyPad.WIDTH;
        // left-top
        Entities.builder()
                .at(0, y)
                .type(FroggerType.BAD)
                .viewFromNodeWithBBox(withColor.apply(new Rectangle(0, 0, Config.LilyPad.WIDTH, Config.LilyPad.LENGTH)))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        boolean gators = currentLevel.getBoolean("end_gators");
        for(int i = 0; i < 5; i++) {
            double x = i * (Config.LilyPad.PLAYER_AREA + 3 * Config.LilyPad.WIDTH) + Config.LilyPad.WIDTH;
            getGameWorld().spawn(Config.SpawnEntity.LILY_PAD, new SpawnData(x, y).put("gators", gators));
            Entities.builder()
                    .at(x + Config.LilyPad.PLAYER_AREA, y)
                    .type(FroggerType.BAD)
                    .viewFromNodeWithBBox(
                            withColor.apply(new Rectangle(0, 0, Config.LilyPad.WIDTH * 3, Config.LilyPad.LENGTH)))
                    .with(new CollidableComponent(true))
                    .buildAndAttach();
        }

        y += Config.LilyPad.LENGTH;

        // spawn waterways
        JSONArray water = currentLevel.getJSONArray("water");

        for(int i = 0; i < water.length(); i++) {
            JSONObject lane = water.getJSONObject(water.length() - i - 1);
            SpawnData data = new SpawnData(0, y);

            data.put("speed", lane.getDouble("speed") * Config.App.SPEED_MULTIPLIER); // the speed of the group
            data.put("size", lane.getInt("size")); // how many in a group
            data.put("number", lane.getInt("number")); // how many groups

            String type = lane.getString("type").toUpperCase();
            if(type.equals(Config.Lane.LOG_LANE)) {
                if(lane.has("gators"))
                    data.put("gators", lane.getBoolean("gators"));
                else data.put("gators", false);
            }

            getGameWorld().spawn(type, data);

            y += Config.App.TILE_SIZE;
        }

        // spawn the divider
        boolean hasSnakes = currentLevel.getBoolean("snakes");
        spawnDivider(y, hasSnakes);

        y += Config.App.TILE_SIZE;

        // spawn the road
        JSONArray road = currentLevel.getJSONArray("road");
        for(int i = 0; i < road.length(); i++) {
            JSONObject lane = road.getJSONObject(road.length() - i - 1);
            double speed = lane.getDouble("speed") * Config.App.SPEED_MULTIPLIER;
            String view = lane.getString("view");
            int number = lane.getInt("number");

            spawnRoad(y, speed, view, number);
            y += Config.App.TILE_SIZE;
        }

        // spawn the starting area
        spawnDivider(y, false);

        // spawn the player
        y += Config.App.TILE_SIZE / 2 - Config.Player.HEIGHT / 2;
        getGameWorld().spawn(Config.Player.PLAYER, (Config.App.TILE_SIZE - Config.Player.WIDTH) / 2 + 7 * Config.App.TILE_SIZE, y);

        maxY = y;
    }

    private void spawnRoad(double y, double speed, String view, int number) {
        getGameWorld().spawn(Config.Lane.CAR_LANE,
                new SpawnData(0, y).put("speed", speed).put("view", view).put("number", number)
        );
    }

    private void spawnDivider(double y, boolean snakes) {
        getGameWorld().spawn(Config.Lane.EMPTY_LANE,
                new SpawnData(0, y)
                        .put("speed", 50d)
                        .put("snakes", snakes)
        );
    }

    public void resetFrog() {
        player().getEntity().removeFromWorld();
        FXGL.getApp().getGameWorld().spawn(Config.Player.PLAYER, PlayerControl.spawnLocation);
        getGameState().setValue("timeLeft", Config.App.TIME_LIMIT);

        maxY = PlayerControl.spawnLocation.getY();
    }

    public void nextLevel() {
        getGameState().increment("level", 1);
        initLevel();
    }

    public void gameOver() {
        getDisplay().showConfirmationBox("GAME OVER!\n Play Again?", yes -> {
            if(yes)
                startNewGame();
            else
                exit();
        });

        int score = getGameState().getInt("score");
        if(score > saveData.getHighScore()) {
            getDisplay().showInputBox("HIGH SCORE!\n Please enter your name: ", playerName ->
                FS.writeDataTask(new SaveData(playerName, score), Config.App.FILE).execute()
            );
        }
    }
}
