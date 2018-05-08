package Actors;

import Utils.Config;
import Utils.VectorHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy_Smart extends Entity {

    private final float H = Gdx.graphics.getWidth();
    private final float W = Gdx.graphics.getHeight();

    private Vector2 currentTarget = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
    private final Vector2 CENTER = new Vector2(H /2, W / 2);

    private enum STATE{
        ATTACKING,
        FLEEING,
        EATING
    }

    /**
     * 0: Vision Width
     * 1: Vision Length
     * 2: Force
     * 3: Speed
     * 4: Proximity attack Player
     * 5: Proximity flee Player
     * 6: Proximity eat Consumable
     * 7: Proximity flee Trap
     * 8: Consumable Attraction
     * 9: Poison Attraction
     * 10: Health Depreciation
     * 11: How much they hurt another Player
     */
    private float[] dna = new float[12];

    private int generation;

    private float looking = 150;

    private STATE state = STATE.EATING;


    public Enemy_Smart(Vector2 screenPos, float[] dna, int generation) {
        super(screenPos, Config.getNumberProperty("player_size"));
        this.generation = generation;
        setDna(dna);
    }

    private void update(Array<Consumable> consumables, Array<Entity> entities){
        if(state == STATE.FLEEING){
            flee(entities, consumables);
        } else if(state == STATE.ATTACKING){
            attack(entities);
        } else if(state == STATE.EATING){
            eat(consumables);
        }
    }

    public String currentState(){
        if(state == STATE.FLEEING){
            return "FLEEING";
        } else if(state == STATE.ATTACKING){
            return "ATTACKING";
        } else if(state == STATE.EATING){
            return "EATING";
        }
        return null;
    }

    public void determineState(Array<Consumable> consumables, Array<Entity> entities){
        if(checkFlee(entities)){
            state = STATE.FLEEING;
        } else if(checkAttack(entities)) {
            state = STATE.ATTACKING;
        } else if(checkEat(consumables)){
            state = STATE.EATING;
        }

        update(consumables, entities);
    }

    private void flee(Array<Entity> entities, Array<Consumable> consumables){
        Vector2 position = getPosition();
        currentTarget = new Vector2(0, 0);
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            if(e != this) {
                if (position.dst(e.getPosition()) <= fleeProximity()) {
                    for(int j = 0; j < consumables.size; j++){
                        Consumable c = consumables.get(j);
                        if(position.dst(c.getPosition()) >= fleeProximity()) {
                            if(position.dst(c.getPosition()) > position.dst(currentTarget)){
                                currentTarget = c.getPosition();
                            }
                        }
                    }
                }
            }
        }

        steer(currentTarget);
    }

    private void attack(Array<Entity> entities){
        Vector2 position = getPosition();
        Enemy_Smart enemy = null;
        currentTarget = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            if(e != this) {
                if (inSight(e.getPosition()) || position.dst(e.getPosition()) <= playerAttackProximity()) {
                    if (position.dst(e.getPosition()) < position.dst(currentTarget)) {
                        enemy = (Enemy_Smart) e;
                        currentTarget = e.getPosition();
                    }
                }
            }
        }

        if(enemy != null){
            if(this.overlaps(enemy)){
                enemy.setHealth(enemy.getHealth() - lifeSteal());
                setHealth(getHealth() + lifeSteal());
            }
        }

        steer(currentTarget);
    }

    private void eat(Array<Consumable> consumables){
        Vector2 position = getPosition();
        currentTarget = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        for(int i = 0; i < consumables.size; i++){
            Consumable c = consumables.get(i);
            if(inSight(c.getPosition()) || position.dst(c.getPosition()) <= consumableProximity()) {
                if(position.dst(c.getPosition()) < position.dst(currentTarget)){
                    currentTarget = c.getPosition();
                }
            }
        }

        steer(currentTarget);
    }

    private boolean checkFlee(Array<Entity> entities){
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            if(getPosition().dst(e.getPosition()) < fleeProximity()){
                if(getHealth() < e.getHealth()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAttack(Array<Entity> entities){
        for(int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            if(inSight(e.getPosition()) || getPosition().dst(e.getPosition()) < playerAttackProximity()){
                if(getHealth() > e.getHealth()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEat(Array<Consumable> consumables){
        for(int i = 0; i < consumables.size; i++){
            Consumable c = consumables.get(i);
            if(inSight(c.getPosition()) || getPosition().dst(c.getPosition()) <= consumableProximity()){
                return true;
            }
        }
        return false;
    }

    private boolean checkNearEdge(){
        return (x > W - 50) || (x < 50) || (y > H - 50) || (y < 50);
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
            this.dna[0] = MathUtils.random(radius, 100); // Vision width
            this.dna[1] = MathUtils.random(radius, 200); // Vision Length
            this.dna[2] = MathUtils.random(0.01f, 0.03f); // Force
            this.dna[3] = MathUtils.random(1, 4); // Speed
            this.dna[4] = MathUtils.random(radius, 200); // Proximity attack Player
            this.dna[5] = MathUtils.random(radius, 200); // Proximity flee Player
            this.dna[6] = MathUtils.random(radius, 200); // Proximity eat Consumable
            this.dna[7] = MathUtils.random(radius, 200); // Proximity flee Trap
            this.dna[8] = MathUtils.random(0, 10); // Consumable Attraction
            this.dna[9] = MathUtils.random(0, 10); // Poison Attraction
            this.dna[10] = MathUtils.random(0.2f, 0.4f); // Health Depreciation
            this.dna[11] = MathUtils.random(this.dna[10] * 2, this.dna[10] * 4); // How much they life steal from another Player

        }
    }



    public float visionWidth(){
        return this.dna[0];
    }

    public float visionLength(){
        return this.dna[1];
    }

    public float force(){
        return this.dna[2];
    }

    public float speed(){
        return this.dna[3];
    }

    public float playerAttackProximity(){
        return this.dna[4];
    }

    public float fleeProximity(){
        return this.dna[5];
    }

    public float consumableProximity(){
        return this.dna[6];
    }

    public float fleeTrapProximity(){
        return this.dna[7];
    }

    public float consumableAttraction(){
        return this.dna[8];
    }

    public float poisionAttraction(){
        return this.dna[9];
    }

    public float healthDepreciation(){
        return this.dna[10];
    }

    public float lifeSteal(){
        return this.dna[11];
    }

    public float getLooking() {
        return looking;
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

    @Override
    void steer(Vector2 whereTo) {
        heading();

        Vector2 desired = VectorHelper.sub(whereTo, getPosition());

        desired.nor();
        desired.scl(speed());

        Vector2 steer = desired.sub(getVelocity());
        steer.limit(force());

        applyForce(steer);

        updateMovement();
    }

    @Override
    public void updateMovement() {
        Vector2 velocity = getVelocity();
        Vector2 acceleration = getAcceleration();
        Vector2 position = getPosition();

        velocity.add(acceleration);
        velocity.limit(speed());
        position.add(velocity);
        setPosition(position);

        acceleration.scl(0);
    }

    @Override
    public void healthDecrease() {
        float currentHealth = getHealth();
        setHealth(currentHealth - healthDepreciation());
        adjustColor();
    }

    private void heading() {
        Vector2 velocity = getVelocity();
        float angle = MathUtils.atan2(velocity.y, velocity.x);

        looking = (MathUtils.radiansToDegrees * angle) - visionWidth() / 2;
    }

    private boolean inSight(Vector2 target) {
        float pos = Math.abs(looking) + visionWidth() / 2;
        float neg = Math.abs(looking) - visionWidth() / 2;

        float targetPosition = Math.abs(watchTarget(target));
        return (targetPosition <= pos && targetPosition >= neg) && (VectorHelper.distanceToTarget(getPosition(), target) <= visionLength());
    }

    private float watchTarget(Vector2 target) {
        return VectorHelper.angleToTarget(getPosition(), target, (int)visionWidth());
    }

    public Vector2 getClosest() {
        if(currentTarget.x == Float.MAX_VALUE){
            return new Vector2(getPosition().x, getPosition().y);
        }
        return currentTarget;
    }
}
