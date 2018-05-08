package Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity extends Circle {

    private float health = 255;

    private Vector2 velocity;
    private Vector2 acceleration;

    private Color color;


    public Entity(Vector2 position, float radius) {
        super(position, radius);

        this.color = new Color(0,1,0,1);

        this.acceleration = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
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

    public void setHealth(float health) {
        this.health = health;
    }

    abstract void healthDecrease();

    // TODO: Based on consumable eaten
    public void healthIncrease() {
        health += 5f;
        if(health > 255){
            health = 255;
        }
        adjustColor();
    }

    public void adjustColor(){
        Color newColor = new Color().set(0,health/255,0,1);
        color.set(newColor);
    }
}
