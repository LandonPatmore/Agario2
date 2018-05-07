package Actors;

import Utils.Config;
import Utils.VectorHelper;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity {
    
    private float[] dna = new float[1];

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    private final int VISION = 10;
    private final int VISION_RADIUS = 100;

    private final float PROXIMITY = 50;

    private float looking = 150;

    public Enemy(Vector2 screenPos) {
        super(screenPos, Config.getNumberProperty("player_size"));
        dna[0] = MathUtils.random(5, 100); // view

        position = new Vector2(this.x, this.y);
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }

    public void hunt(Array<Consumable> consumables){
        Vector2 currentVec = new Vector2(this.x, this.y);
        Vector2 closest = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for(Consumable c : consumables){
            Vector2 cVec = new Vector2(c.x,c.y);
            if(currentVec.dst(cVec) < currentVec.dst(closest)){
                if(inSight(cVec) || currentVec.dst(cVec) <= PROXIMITY) {
                    closest = cVec;
                }
            }
        }

        heading();
        Vector2 desired = VectorHelper.sub(closest, position);

        desired.nor();
        desired.scl(getSpeed());

        Vector2 steer = desired.sub(velocity);
        float MAX_FORCE = 0.03f;
        steer.limit(MAX_FORCE);

        applyForce(steer);

        update();
    }

    private void update() {
        velocity.add(acceleration);
        velocity.limit(getSpeed());
        position.add(velocity);
        setPosition(position);

        acceleration = acceleration.scl(0);
    }

    private void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    private void heading() {
        float angle = MathUtils.atan2(velocity.y, velocity.x);

        looking = (MathUtils.radiansToDegrees * angle) - VISION / 2;
    }

    private boolean inSight(Vector2 target) {
        float pos = Math.abs(looking) + 60;
        float neg = Math.abs(looking) - 60;

        float targetPosition = Math.abs(watchTarget(target));
        return (targetPosition <= pos && targetPosition >= neg) && (VectorHelper.distanceToTarget(position, target) <= VISION_RADIUS);
    }

    private float watchTarget(Vector2 target) {
        return VectorHelper.angleToTarget(position, target, VISION);
    }

    public float looking() {
        return looking;
    }

    public int getVision() {
        return VISION;
    }

    public int getVisionRadius() {
        return VISION_RADIUS;
    }

    public float getPROXIMITY(){
        return PROXIMITY;
    }

    @Override
    void movementSpeed() {
        initialSpeed(Config.getNumberProperty("enemy_speed"));
    }
}
