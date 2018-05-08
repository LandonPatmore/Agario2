package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    //Screen Constraints
    private final int H = Config.getHeight();
    private final int W = Config.getWidth();

    public Player(Vector2 position) {
        super(position, Config.getNumberProperty("player_size"));
    }

    @Override
    public void validateMovement(float x, float y) {
        if (!(x > W) && !(x < 0) && !(y > H) && !(y < 0)) {
            setPosition(new Vector2(x,y));
        }
    }


}
