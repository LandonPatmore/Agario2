package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    public Player(Vector2 position) {
        super(position, Config.getNumberProperty("player_size"));
    }

    public Vector2 getPoisition(){
        return new Vector2(x,y);
    }

    public void validateMovement(float newX, float newY) {
        if (!(newX > W) && !(newX < 0) && !(newY > H) && !(newY < 0)) {
            this.x = newX;
            this.y = newY;
        }
    }

    @Override
    void movementSpeed() {
        initialSpeed(Config.getNumberProperty("player_speed"));
    }
}
