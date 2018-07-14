package evan.tichenor.frogger;

import com.almasb.fxgl.app.FXGL;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.1, 4/4/2018
 */
public class KonamiCode implements EventHandler<KeyEvent> {

    private enum State {
        UP {
            public State update(KeyCode key) {
                if(key == KeyCode.UP) {
                    counter++;

                    if(counter == 2)
                        return DOWN;
                } else {
                    return resetAll();
                }

                return this;
            }
        },
        DOWN {
            public State update(KeyCode key) {
                if(key == KeyCode.DOWN) {
                    counter++;

                    if(counter == 2)
                        return LEFT;
                } else {
                    return resetAll();
                }

                return this;
            }
        },
        LEFT {
            public State update(KeyCode key) {
                if(key == KeyCode.LEFT) {
                    return RIGHT;
                }

                return resetAll();
            }
        },
        RIGHT {
            public State update(KeyCode key) {
                if(key == KeyCode.RIGHT) {
                    counter++;
                    if(counter == 2)
                        return B;
                    return LEFT;
                }

                return resetAll();
            }
        },
        B {
            public State update(KeyCode key) {
                if(key == KeyCode.B) {
                    return A;
                }

                return resetAll();
            }
        },
        A {
            public State update(KeyCode key) {
                if(key == KeyCode.A) {
                    return END;
                }

                return resetAll();
            }
        },
        END {
            public State update(KeyCode key) {
                return resetAll(); // reset :)
            }
        };

        protected int counter;

        State() {
            counter = 0;
        }

        public abstract State update(KeyCode key);

        private static State resetAll() {
            for(State s : values())
                s.reset();

            return UP;
        }

        private State reset() {
            counter = 0;
            return this;
        }
    }

    private State state = State.UP;

    @Override
    public void handle(KeyEvent event) {
        state = state.update(event.getCode());

        if(state == State.END) {
            FXGL.getAudioPlayer().playSound(Config.App.KONAMI_CODE_JINGLE);
            FXGL.getApp().getGameState().setValue("lives", 30);
        }
    }
}
