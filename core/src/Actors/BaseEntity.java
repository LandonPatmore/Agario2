package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseEntity extends Circle {

    //Screen Constraints
    final int H = Config.getHeight();
    final int W = Config.getWidth();

    // Move constants for the circle that can be dynamically changed
    float moveConstantX;
    float moveConstantY;

    // Identify enemy using int, not string for speed
    private final int id;

    // For Enemies and Players
    private int consumables;
    private int enemies;

    // Random color for entity
    public final float R = MathUtils.random();
    public final float G = MathUtils.random();
    public final float B = MathUtils.random();

    BaseEntity(Vector2 position, float radius, int id) {
        super(position, radius);
        this.id = id;
        setMoveConstants();
    }

    public int getId() {
        return id;
    }

    private void setMoveConstants() {
        if (this instanceof Consumable) return;
        moveConstantX = MathUtils.random(-1.5f, 1.5f);
        moveConstantY = MathUtils.random(-1.5f, 1.5f);
    }

    void setMoveConstantX(float moveConstantX) {
        this.moveConstantX = moveConstantX;
    }

    void setMoveConstantY(float moveConstantY) {
        this.moveConstantY = moveConstantY;
    }

    public void changeDirection() {
        float probability = MathUtils.random();
        float innerProbability = MathUtils.random();

        if (probability > 0.99f) {
            if (innerProbability > 0.99f) {
                setMoveConstantX(MathUtils.random(-10.0f, 10.0f));
                setMoveConstantY(MathUtils.random(-10.0f, 10.0f));
            } else {
                setMoveConstants();
            }
        }
    }

    public void depreciate() {
        if (radius > 25) {
            if (radius < 50) {
                radius -= radius * 0.00001f;
            } else {
                radius -= radius * 0.00009f;
            }
        }
    }

    public void radiusIncrease(float amount) {
        if (amount == 1) {
            consumables++;
            if (consumables < 5) {
                radius += Math.log(consumables);
            } else if (consumables < 10) {
                radius++;
            } else if (consumables < 20) {
                radius += 0.5f;
            } else {
                radius += 0.2f;
            }
        } else {
            enemies++;
            radius += (amount * 0.2f);
        }
    }

    public void move() {
        if (this instanceof Consumable) return;
        x += moveConstantX;
        y += moveConstantY;
    }


}
