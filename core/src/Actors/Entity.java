package Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity extends Circle {

    private float health = 255;

    private Vector2 velocity;
    private Vector2 acceleration;

    private Color color;

    private final String name;

    private final long born = System.currentTimeMillis();
    private long died;


    public Entity(Vector2 position, float radius, String name) {
        super(position, radius);

        this.name = name;

        this.color = new Color(0,1,0,1);

        this.acceleration = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return new Vector2(x,y);
    }

    abstract void steer(Vector2 whereTo);

    abstract void updateMovement();

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    public float getHealth() {
        return health;
    }

    public long getLifeSpan(){
        return died - born;
    }

    public String getName() {
        return name;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    abstract void healthDecrease();

    // TODO: Based on consumable eaten
    public void healthIncrease(float energy) {
        health += energy;
        adjustColor();
    }

    public void adjustColor(){
        float inverse = 255 - health;
        Color newColor = new Color().set(inverse / 255,health/255,0,1);
        color.set(newColor);
    }

    public boolean checkIfDead(){
        if(health < 0) {
            died = System.currentTimeMillis();
            return true;
        }

        return false;
    }
}
