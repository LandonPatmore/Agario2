package Actors;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends Circle {

    public Consumable(Vector2 position, float radius) {
        super(position, radius);
    }

    public Vector2 getPosition() {
        return new Vector2(x,y);
    }

}
