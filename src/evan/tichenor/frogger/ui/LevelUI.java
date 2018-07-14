package evan.tichenor.frogger.ui;

import evan.tichenor.frogger.Config;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.HBox;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 4/3/2018
 */
public class LevelUI extends HBox {

    private IntegerProperty level;

    public LevelUI() {
        setSpacing(-1);
        level = new SimpleIntegerProperty();
        level.addListener((observable, oldValue, newValue) -> update((int)oldValue));
    }

    private void update(int oldValue) {
        getChildren().removeIf(x -> true);
        int num = Math.min(level.get(), Config.UI.MAX_VISIBLE_LEVEL);
        for(int i = 0; i < num; i++)
            getChildren().add(Config.UI.LEVEL.copy());

        if(oldValue < Config.UI.MAX_VISIBLE_LEVEL) {
            double newX = getTranslateX() - Config.UI.LEVEL.getWidth() * (num - oldValue) - getSpacing();
            setTranslateX(newX);
        }
    }

    public IntegerProperty levelProperty() {
        return level;
    }
}
