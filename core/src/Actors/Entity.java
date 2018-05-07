package Actors;

import Utils.Config;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public abstract class Entity extends Circle {

    // For Enemies and Players
    private int consumables;
    private int enemies;

    // id of the entity
    public final int ID;

    //Screen Constraints
    final int H = Config.getHeight();
    final int W = Config.getWidth();

    // Speed
    private double[] speed = new double[2];

    // Random color for entity
    public final float R = MathUtils.random();
    public final float G = MathUtils.random();
    public final float B = MathUtils.random();

    Entity(Vector2 position, float radius, int id){
        super(position, radius);
        this.ID = id;
        movementSpeed();
    }

    double[] getSpeed() {
        return speed;
    }

    void initialSpeed(double speed) {
        Arrays.fill(this.speed, speed);
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
        x += speed[0];
        y += speed[1];
    }

    abstract void movementSpeed();


}
