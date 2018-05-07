package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {

    // TODO: Needs to be dynamic @Landon
    public final float FLEE_RADIUS = 50;
    public final float ATTACK_RADIUS = 75;
    public final float EAT_RADIUS = 100;

    public Enemy(Vector2 screenPos, int name) {
        super(screenPos, Config.getNumberProperty("player_size"), name);
    }

    private void reverseX() {
        double[] speed = getSpeed();

        speed[0] = -speed[0];
    }

    private void reverseY() {
        double[] speed = getSpeed();

        speed[1] = -speed[1];
    }

    public void checkGoingOffScreen() {
        if (x > W || x < 0) {
            reverseX();
        }
        if (y > H || y < 0) {
            reverseY();
        }
    }

    @Override
    void movementSpeed() {
        initialSpeed(Config.getNumberProperty("enemy_speed"));
    }
}
