package evan.tichenor.frogger.component;

import com.almasb.fxgl.entity.Component;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 3/21/2018
 *
 * Unused.
 */
public class StorageComponent<T>  extends Component {

    private T storage;

    public StorageComponent(T store) {
        storage = store;
    }

    public T getStorage() {
        return storage;
    }
}
