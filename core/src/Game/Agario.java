package Game;

import Screens.GameScreen;
import com.badlogic.gdx.Game;

public class Agario extends Game {

    private Game game;

    public Agario() {
        this.game = this;
    }

    @Override
    public void create() {
        setScreen(new GameScreen(game));
    }

    @Override
    public void render() {
        super.render();
    }
}
