package Actors;

import Utils.Config;
import Utils.VectorHelper;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Entity {

    // 1: vision, 2: vision length, 3: proximity, 4: max force, 5: speed
    private float[] dna = new float[5];

    private int generation;

    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;

    private Vector2 closest;

    private float looking = 150;

    public Enemy(Vector2 screenPos, float[] dna, int generation) {
        super(screenPos, Config.getNumberProperty("player_size"));
        this.generation = generation;
        setDna(dna);
        position = new Vector2(this.x, this.y);
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }

    public int getGeneration() {
        return generation;
    }

    public float[] getDna() {
        return dna;
    }

    private void setDna(float[] dna) {
        if (dna != null) {
            this.generation++;
            this.dna = dna;
            mutate();
        } else {
            this.dna[0] = MathUtils.random(0, 100);
            this.dna[1] = MathUtils.random(0, 200);
            this.dna[2] = MathUtils.random(this.dna[1] - 100, this.dna[1] - 50);
            this.dna[3] = MathUtils.random(0.01f, 0.1f);
            this.dna[4] = MathUtils.random(1, 4);
        }
    }

    private void mutate() {
        for (int i = 0; i < dna.length; i++) {
            float probability = MathUtils.random();
            if (probability < 0.01) {
                if (i < 3) {
                    float mutation = MathUtils.random(-10, 10);
                    dna[i] += mutation;
                } else if (i == 3) {
                    float mutation = MathUtils.random(-0.005f, 0.005f);
                    dna[i] += mutation;
                } else {
                    float mutation = MathUtils.random(-0.5f, 0.5f);
                    dna[i] += mutation;
                }
            }
        }
    }

    public void hunt(Array<Consumable> consumables){
        Vector2 currentVec = new Vector2(this.x, this.y);
        closest = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for(Consumable c : consumables){
            Vector2 cVec = new Vector2(c.x,c.y);
            if(currentVec.dst(cVec) < currentVec.dst(closest)){
                if(inSight(cVec) || currentVec.dst(cVec) <= dna[2]) {
                    closest = cVec;
                }
            }
        }

        heading();
        Vector2 desired = VectorHelper.sub(closest, position);

        desired.nor();
        desired.scl(dna[4]);

        Vector2 steer = desired.sub(velocity);
        steer.limit(dna[3]);

        applyForce(steer);

        update();
    }

    private void update() {
        velocity.add(acceleration);
        velocity.limit(dna[4]);
        position.add(velocity);
        setPosition(position);

        acceleration = acceleration.scl(0);
    }

    private void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    private void heading() {
        float angle = MathUtils.atan2(velocity.y, velocity.x);

        looking = (MathUtils.radiansToDegrees * angle) - dna[0] / 2;
    }

    private boolean inSight(Vector2 target) {
        float pos = Math.abs(looking) + 60;
        float neg = Math.abs(looking) - 60;

        float targetPosition = Math.abs(watchTarget(target));
        return (targetPosition <= pos && targetPosition >= neg) && (VectorHelper.distanceToTarget(position, target) <= dna[1]);
    }

    private float watchTarget(Vector2 target) {
        return VectorHelper.angleToTarget(position, target, (int)dna[0]);
    }

    public float looking() {
        return looking;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getClosest() {
        if(closest.x == Float.MAX_VALUE){
            return new Vector2(position.x, position.y);
        }
        return closest;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    void movementSpeed() {
        initialSpeed(0);
    }
}
