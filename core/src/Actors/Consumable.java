package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends Entity {

    public Consumable(Vector2 screenPos, float size) {
        super(screenPos, size);
        setPosition(screenPos);
    }

    @Override
    void validateMovement(float x, float y) {

    }
}
