package Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Consumable extends Circle {

    private boolean isPoison;
    private float energy;
    private Color color;

    public Consumable(Vector2 position, float radius, boolean isPoison) {
        super(position, radius);
        this.isPoison = isPoison;
        setEnergy(isPoison);
    }

    public Color getColor() {
        return color;
    }

    public boolean isPoison() {
        return isPoison;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy){
        this.energy = energy;
    }


    public void setEnergy(boolean isPoison){
        if(isPoison){
            energy = -2;
            color = new Color(1,0,0,1);
        } else {
            energy = 5;
            color = new Color(0,1,0,1);
        }
    }

    public Vector2 getPosition() {
        return new Vector2(x,y);
    }

}
