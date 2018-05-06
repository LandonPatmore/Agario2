package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends BaseEntity {

    public Consumable(Vector2 screenPos, int name) {
        super(screenPos, Config.getNumberProperty("consumable_size"), name);
    }
}
