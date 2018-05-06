package Actors;

import Utils.Globals;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends BaseEntity {

    public Consumable(Vector2 screenPos, int name) {
        super(screenPos, Globals.C_SIZE, name);
    }
}
