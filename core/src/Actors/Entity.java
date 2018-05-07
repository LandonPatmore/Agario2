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

    float getSpeed() {
        return speed;
    }

    void initialSpeed(float speed) {
        this.speed = speed;
    }

    public void healthDecrease() {
        health -= 0.15f;
        adjustColor();
    }

    public void healthIncrease() {
        health += 2f;
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

//    public void move() {
//        if (this instanceof Consumable) return;
//        x += speed[0];
//        y += speed[1];
//    }

    abstract void movementSpeed();


}
