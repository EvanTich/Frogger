package evan.tichenor.frogger.control.mover;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import evan.tichenor.frogger.Config;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 1/26/2018
 */
public class CarControl extends MovableControl<CarControl> {

    private String view;

    public CarControl(String view) {
        super(100);

        this.view = view;
    }

    @Override
    public void onAdded(Entity entity) {
        if(speed < 0)
            entity.setScaleX(-1);
    }

    @Override
    public void onUpdate(Entity car, double v) {
        if(isMoving())
            move(car, v);
    }

    @Override
    public double getTotalWidth() {
        return getEntity().getWidth();
    }

    @Override
    public CarControl copy() {
        return new CarControl(view);
    }

    public Texture getView() {
        return createSpecifiedView(view);
    }

    public static Texture createSpecifiedView(String viewStr) {
        Texture view;
        try {
            view = ((Texture) Config.Vehicles.class.getField(viewStr).get(null)).copy();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            view = Config.Vehicles.CAR1.copy();
        }

        return view;
    }
}
