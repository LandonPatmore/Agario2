package Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity extends Circle {

    // health of player
    private float health = 255;

    // Starting color
    public final Color color = new Color().set(0,1,0,1);

    Entity(Vector2 position, float radius){
        super(position, radius);
        setPosition(position);
    }

    abstract void validateMovement(float x, float y);

    public void healthDecrease() {
        health -= 0.35f;
        adjustColor();
    }

    public void healthIncrease() {
        health += 5f;
        adjustColor();
    }

    private void adjustColor(){
        float inverse = 255 - health;
        color.set(inverse / 255,health/255,0,1);
    }

    public boolean checkIfDead(){
        return health < 0;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getHealth(){
        return (int) health;
    }

    public Vector2 getPosition(){
        return new Vector2(x,y);
    }

}
