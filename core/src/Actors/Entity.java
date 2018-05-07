package Actors;

import Utils.Config;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity extends Circle {

    // health of player
    private float health = 255;

    //Screen Constraints
    final int H = Config.getHeight();
    final int W = Config.getWidth();

    // Speed
    private float speed;

    // Starting color
    public final Color color = new Color().set(0,1,0,1);

    Entity(Vector2 position, float radius){
        super(position, radius);
        movementSpeed();
    }

    public float getSpeed() {
        return speed;
    }

    void initialSpeed(float speed) {
        this.speed = speed;
    }

    public void healthDecrease() {
        health -= 0.3f;
        adjustColor();
    }

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

    public boolean checkIfDead(){
        return health < 0;
    }

    public int getHealth(){
        return (int) health;
    }

    abstract void movementSpeed();


}
