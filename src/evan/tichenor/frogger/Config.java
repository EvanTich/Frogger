package evan.tichenor.frogger;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/25/2018
 */
public class Config {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final class App {
        public static final int TILE_SIZE = 18 * 2;

        // future reference: the width is actually x - 1 for whatever x gets multiplied by TILE_SIZE
        public static final int WIDTH = 14 * TILE_SIZE;

        // this is a "backup height"
        public static final int HEIGHT = (int)(16 * TILE_SIZE);

        public static final double START_Y = 1.5 * TILE_SIZE;

        public static final Duration FLY_APPEAR = Duration.seconds(20);
        public static final Duration FLY_APPEARANCE_DURATION = Duration.seconds(10);

        public static final String FILE = "./highscore.dat";

        public static final double TIME_LIMIT = 60; // should be 30 seconds

        public static final double SPEED_MULTIPLIER = (60 / 145f); // ~145f or ~63f **** SHOULD NOT NEED THIS ****

        public static final Sound KONAMI_CODE_JINGLE = FXGL.getAssetLoader().loadSound("secret.wav");

        public static final KeyCode GAME_OVER_KEY = KeyCode.BACK_QUOTE;
    }

    public static final class UI {
        public static final Texture LIFE = FXGL.getAssetLoader().loadTexture("life.png", App.TILE_SIZE / 2, App.TILE_SIZE / 2);
        public static final Texture LEVEL = FXGL.getAssetLoader().loadTexture("level.png", App.TILE_SIZE / 2, App.TILE_SIZE / 2);

        public static final int SCORE_X = 100;
        public static final int HIGHSCORE_X = 200;
        public static final int TOP_UI_Y = 15;
        public static final int LIVES_X = 0;
        public static final int LEVEL_X = App.WIDTH - (int)LEVEL.getWidth();
        public static final int BOTTOM_UI_Y = App.HEIGHT - App.TILE_SIZE + 4;
        public static final int TIME_UI_Y = BOTTOM_UI_Y + App.TILE_SIZE / 2 - 2;

        public static final int MAX_VISIBLE_LIVES = 10;
        public static final int MAX_VISIBLE_LEVEL = 15;
    }

    public static final class Player {
        public static final String PLAYER = "PLAYER";

        public static final double WIDTH = 3 * App.TILE_SIZE / 4;
        public static final double HEIGHT = 3 * App.TILE_SIZE / 4;

        public static final Image JUMP = FXGL.getAssetLoader().loadTexture("frog_jump.png", 3 * WIDTH, HEIGHT).getImage();
        public static final Image DEATH = FXGL.getAssetLoader().loadTexture("frog_death.png", 4 * WIDTH, HEIGHT).getImage();

        public static final Texture VIEW = FXGL.getAssetLoader().loadTexture("turtle.png", WIDTH, HEIGHT);

        public static final KeyCode UP = KeyCode.W;
        public static final KeyCode DOWN = KeyCode.S;
        public static final KeyCode LEFT = KeyCode.A;
        public static final KeyCode RIGHT = KeyCode.D;

        public static final double MOVE_AMOUNT = App.TILE_SIZE;
        public static final Duration MOVE_DURATION = Duration.millis(125); // time it takes to jump from one position to another

        public static final Duration DEATH_DURATION = Duration.millis(500);

        public static final double LADY_FROG_X_OFFSET = 0;
        public static final double LADY_FROG_Y_OFFSET = 1;
    }

    public static final class LilyPad {
        public static final double WIDTH = App.WIDTH / 24;
        public static final double LENGTH = App.TILE_SIZE;
        public static final double PLAYER_AREA = App.TILE_SIZE * 1.2; // 1/12 of app width

        public static final Texture FLY_TEXTURE = FXGL.getAssetLoader().loadTexture("fly.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture FROG_TEXTURE = FXGL.getAssetLoader().loadTexture("static_frog.png", App.TILE_SIZE, App.TILE_SIZE);
    }

    public static final class Lane {
        public static final String TURTLE_LANE = "TURTLE";
        public static final String CAR_LANE = "CAR";
        public static final String LOG_LANE = "LOG";
        public static final String EMPTY_LANE = "EMPTY";

        public static final Duration SNAKE_TIMER = Duration.seconds(30);
        public static final Duration LADY_FROG_WAIT_TIME = Duration.millis(1000 * App.SPEED_MULTIPLIER);

        // this is the distance between the moving entities in the lane
        // honestly, may need to change this to another mechanic
        public static final double HORIZONTAL_DISTANCE = App.TILE_SIZE * 4;

        public static final RenderLayer LANE_LAYER = new RenderLayer() {
            @Override
            public String name() {
                return "LANE";
            }

            @Override
            public int index() {
                return 1001;
            }
        };

        public static final RenderLayer MOVER_LAYER = new RenderLayer() {
            @Override
            public String name() {
                return "MOVER";
            }

            @Override
            public int index() {
                return 2001;
            }
        };
    }

    public static final class SpawnEntity {
        public static final String LILY_PAD = "PAD";
        public static final String FLY = "FLY";
        public static final String STATIC_FROG = "STATIC_FROG";
        public static final String SNAKE = "SNAKE";
        public static final String LADY_FROG = "LADY_FROG";
        public static final String LOG = "LOG_ENTITY";
        public static final String TURTLES = "TURTLE_ENTITY";
        public static final String VEHICLE = "VEHICLE_ENTITY";
    }

    public static final class Vehicles {
        public static final Texture CAR1 = FXGL.getAssetLoader().loadTexture("car1.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture CAR2 = FXGL.getAssetLoader().loadTexture("car2.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture CAR3 = FXGL.getAssetLoader().loadTexture("car4.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture CAR4 = FXGL.getAssetLoader().loadTexture("car5.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture CAR5 = FXGL.getAssetLoader().loadTexture("car3.png", 29f * App.TILE_SIZE / 18, App.TILE_SIZE);
    }

    public static final class Turtles {
        public static final Texture VIEW = FXGL.getAssetLoader().loadTexture("turtle.png", App.TILE_SIZE, App.TILE_SIZE);
    }

    public static final class Logs {
        public static final Texture TEXTURE_LEFT = FXGL.getAssetLoader().loadTexture("log_left.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture TEXTURE_MIDDLE = FXGL.getAssetLoader().loadTexture("log_middle.png", App.TILE_SIZE, App.TILE_SIZE);
        public static final Texture TEXTURE_RIGHT = FXGL.getAssetLoader().loadTexture("log_right.png", App.TILE_SIZE, App.TILE_SIZE);

        public static final double LADY_FROG_SPAWN_CHANCE = 0.10;

        public static final Texture LADY_FROG_VIEW = FXGL.getAssetLoader().loadTexture("lady_frog.png", App.TILE_SIZE, App.TILE_SIZE);
    }
}
