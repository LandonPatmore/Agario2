package Actors;

import Utils.Config;
import Utils.VectorHelper;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("FieldCanBeLocal")
public class Player extends Entity {

    private final int SPEED = Config.getNumberProperty("player_speed");
    private final float MAX_FORCE = 0.03f;

    // Inputs
    private final int W = Input.Keys.W;
    private final int A = Input.Keys.A;
    private final int S = Input.Keys.S;
    private final int D = Input.Keys.D;

    public Player(Vector2 position, float radius) {
        super(position, radius);
    }

    @Override
    public void steer(Vector2 whereTo) {
        Vector2 desired = VectorHelper.sub(whereTo, getPosition());

        desired.nor();
        desired.scl(SPEED);

        Vector2 steer = desired.sub(getVelocity());
        steer.limit(MAX_FORCE);

        applyForce(steer);

        updateMovement();
    }

    @Override
    public void updateMovement() {
        Vector2 velocity = getVelocity();
        Vector2 acceleration = getAcceleration();
        Vector2 position = getPosition();

        velocity.add(acceleration);
        velocity.limit(SPEED);
        position.add(velocity);
        setPosition(position);


        acceleration.scl(0);
    }

    @Override
    void healthDecrease() {
        float currentHealth = getHealth();
        setHealth(currentHealth - 0.3f);
        adjustColor();
    }

    public void move(Input g){
        Vector2 whereTo = new Vector2();

        float newX = x;
        float newY = y;

        if (g.isKeyPressed(W) && g.isKeyPressed(A)) {
            newY += 1;
            newX -= 1;
        } else if (g.isKeyPressed(W) && g.isKeyPressed(D)) {
            newY += 1;
            newX += 1;
        } else if (g.isKeyPressed(S) && g.isKeyPressed(A)) {
            newY -= 1;
            newX -= 1;
        } else if (g.isKeyPressed(S) && g.isKeyPressed(D)) {
            newY -= 1;
            newX += 1;
        } else if (g.isKeyPressed(W)) {
            newY += 1;
        } else if (g.isKeyPressed(S)) {
            newY -= 1;
        } else if (g.isKeyPressed(D)) {
            newX += 1;
        } else if (g.isKeyPressed(A)) {
            newX -= 1;
        }

        whereTo.set(newX, newY);

        steer(whereTo);
    }
}
