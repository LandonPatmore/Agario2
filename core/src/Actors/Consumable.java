package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends Entity {

    public Consumable(Vector2 screenPos, int size,int name) {
        super(screenPos, size, name);
    }

    @Override
    void movementSpeed() {
        initialSpeed(0);
    }
}
