package Actors;

import Utils.Config;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy_Dumb extends Entity {

    // Move constants for the circle that can be dynamically changed
    private float moveConstantX;
    private float moveConstantY;

    //Screen Constraints
    private final int H = Config.getHeight();
    private final int W = Config.getWidth();

    public Enemy_Dumb(Vector2 screenPos, int id) {
        super(screenPos, Config.getNumberProperty("player_size"), String.valueOf(id));
        setColor(new Color(0,0,1,1));
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

    private void setMoveConstants() {
        moveConstantX = MathUtils.random(-5.5f, 5.5f);
        moveConstantY = MathUtils.random(-5.5f, 5.5f);
    }

    void setMoveConstantX(float moveConstantX) {
        this.moveConstantX = moveConstantX;
    }

    void setMoveConstantY(float moveConstantY) {
        this.moveConstantY = moveConstantY;
    }

    public void move() {
        x += moveConstantX;
        y += moveConstantY;
    }

    @Override
    void steer(Vector2 whereTo) {

    }

    @Override
    void updateMovement() {

    }

    @Override
    void healthDecrease() {

    }
}
