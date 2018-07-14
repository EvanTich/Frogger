package evan.tichenor.frogger.control.mover;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import evan.tichenor.frogger.Config;
import javafx.geometry.HorizontalDirection;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/26/2018
 */
public class TurtleControl extends MovableControl<TurtleControl> {

    private Entity turtle;
    private int number; // number of turtles

    public TurtleControl(int number) {
        super(100);
        this.number = number;
    }

    @Override
    public void onAdded(Entity entity) {
        turtle = entity;
    }

    @Override
    public void onUpdate(Entity entity, double v) {
        if(isMoving())
            move(turtle, v);
    }

    @Override
    public double getTotalWidth() {
        return turtle.getWidth();
    }

    @Override
    public TurtleControl copy() {
        return new TurtleControl(number);
    }

    public int getNumber() {
        return number;
    }

    /**
     * Used by factory
     * @param number
     * @return
     */
    public static Texture createSpecifiedView(int number) {
        Texture view = Config.Turtles.VIEW.copy();
        for(int i = 1; i < number; i++)
            view = view.superTexture(Config.Turtles.VIEW, HorizontalDirection.RIGHT);
        return view;
    }
}
