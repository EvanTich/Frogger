package evan.tichenor.frogger.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.HBox;

import static evan.tichenor.frogger.Config.UI.MAX_VISIBLE_LIVES;
import static evan.tichenor.frogger.Config.UI.LIFE;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 4/2/2018
 */
public class LivesUI extends HBox {

    private IntegerProperty lives;

    public LivesUI() {
        lives = new SimpleIntegerProperty();
        lives.addListener((observable, oldValue, newValue) -> updateChildren());
    }

    private void updateChildren() {
        getChildren().removeIf(x -> true);
        for(int i = 0; i < Math.min(lives.get(), MAX_VISIBLE_LIVES); i++)
            getChildren().add(LIFE.copy());
    }

    public IntegerProperty livesProperty() {
        return lives;
    }
}
