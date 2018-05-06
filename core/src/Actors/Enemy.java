package Actors;

import Utils.Config;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends BaseEntity {

    public Enemy(Vector2 screenPos, int name) {
        super(screenPos, Config.getNumberProperty("player_size"), name);
        generateInitialMoveConstants();
    }

    private void generateInitialMoveConstants() {
        moveConstantX = MathUtils.random(-1.5f, 1.5f);
        moveConstantY = MathUtils.random(-1.5f, 1.5f);
    }

    private void reverseX() {
        setMoveConstantX(-moveConstantX);
    }

    private void reverseY() {
        setMoveConstantY(-moveConstantY);
    }

    public void checkGoingOffScreen() {
        if (x > W || x < 0) {
            reverseX();
        }
        if (y > H || y < 0) {
            reverseY();
        }
    }
}
