package Screens;

import Actors.Consumable;
import Actors.Enemy;
import Actors.Player;
import Utils.Config;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    // Game
    private final Game game;

    // Entities
    private final Array<Consumable> consumables = new Array<>();
    private final Array<Enemy> enemies = new Array<>();

    // Player
    private Player player;

    // Amount of each entity
    private final int C_AMT = 250;
    private final int E_AMT = 50;

    // Renderer
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Bitmap Font
    private final BitmapFont playerName;
    private final BitmapFont enemyName;
    private final SpriteBatch batch;

    // Track amount of Entities that have existed
    private int enemyCount;
    private int consumableCount;

    // Inputs
    private final int W_ = Input.Keys.W;
    private final int A = Input.Keys.A;
    private final int S = Input.Keys.S;
    private final int D = Input.Keys.D;

    public GameScreen(Game game) {
        this.game = game;
        generateConsumables();
        generateEnemies();
        generatePlayers();
        playerName = new BitmapFont();
        enemyName = new BitmapFont();
        batch = new SpriteBatch();

        if(Config.getDebug()){
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        consumeInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        drawEntities();
        generatePlayerNumber();
        generateEnemyHealth();
        checkAllPlayersEaten();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerName.dispose();
        shapeRenderer.dispose();
        batch.dispose();
    }

    private void drawEntities() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        placeConsumables();
        placeEnemies();
        placePlayers();
        shapeRenderer.end();
        debugRender();
    }

    private void checkAllPlayersEaten() {
        if (enemies.size == 0 || consumables.size == 0) {
            game.setScreen(new EndScreen(game));
        }
    }

    private void generatePlayerNumber() {
        batch.begin();
        playerName.draw(batch, "1", player.x, player.y);
        batch.end();
    }

    private void generateEnemyHealth() {
        batch.begin();
        for(Enemy e : enemies) {
            enemyName.draw(batch, String.valueOf(e.getHealth()), e.x, e.y);
        }
        batch.end();
    }

    private void enemyChecks(Enemy e) {
        e.healthDecrease();
        e.hunt(consumables);
        checkDead(e);
        checkEnemyAteConsumable(e);
    }

    private void checkDead(Enemy e){
        if(e.checkIfDead()){
            enemies.removeValue(e, false);
        }
    }

    private void checkPlayerAteConsumable() {
        for (Consumable c : consumables) {
            if (player.overlaps(c)) {
                player.healthIncrease();
                consumables.removeValue(c, false);
            }
        }
    }

    private void checkEnemyAteConsumable(Enemy e) {
        for (Consumable c : consumables) {
            if (e.overlaps(c)) {
                e.healthIncrease();
                consumables.removeValue(c, false);
            }
        }
    }

    private void consumeInput() {
        Input g = Gdx.input;

            float newX = player.x;
            float newY = player.y;

            if (g.isKeyPressed(W_) && g.isKeyPressed(A)) {
                newY += 1;
                newX -= 1;
            } else if (g.isKeyPressed(W_) && g.isKeyPressed(D)) {
                newY += 1;
                newX += 1;
            } else if (g.isKeyPressed(S) && g.isKeyPressed(A)) {
                newY -= 1;
                newX -= 1;
            } else if (g.isKeyPressed(S) && g.isKeyPressed(D)) {
                newY -= 1;
                newX += 1;
            } else if (g.isKeyPressed(W_)) {
                newY += 1;
            } else if (g.isKeyPressed(S)) {
                newY -= 1;
            } else if (g.isKeyPressed(D)) {
                newX += 1;
            } else if (g.isKeyPressed(A)) {
                newX -= 1;
            }
            player.validateMovement(newX, newY);
    }

    private void playerChecks() {
        player.healthDecrease();
        checkPlayerAteConsumable();
    }

    private void placePlayers() {
            playerChecks();
            shapeRenderer.setColor(player.color);
            shapeRenderer.circle(player.x, player.y, player.radius);
    }

    private void placeEnemies() {
        for (Enemy e : enemies) {
//            checkNewEnemyGeneration();
            enemyChecks(e);
            shapeRenderer.setColor(e.color);
            shapeRenderer.circle(e.x, e.y, e.radius);
        }
    }

    private void debugRender(){
        if(Config.getDebug()) {
            for (Enemy e : enemies) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(1, 0, 0, 1);
                shapeRenderer.arc(e.x, e.y, e.getVisionRadius(), e.looking(), e.getVision());
                shapeRenderer.circle(e.x,e.y, e.getPROXIMITY());
                shapeRenderer.end();
            }
        }
    }

    private void placeConsumables() {
        for (Consumable c : consumables) {
            checkNewConsumableGeneration();
            shapeRenderer.setColor(c.color);
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
    }

//    private void checkNewEnemyGeneration() {
//        if (enemies.size < E_AMT) {
//            Enemy e = generateNewEnemy();
//            if (e != null) {
//                enemies.add(e);
//            }
//        }
//    }
//
    private void checkNewConsumableGeneration() {
        if (consumables.size < C_AMT - 10) {
            Consumable c = generateNewConsumable();
            consumables.add(c);
        }
    }

//    private Enemy generateNewEnemy() {
//        enemyCount++;
//        for (Player p : players) {
//            if (enemyCount > E_AMT + 20) {
//                return null;
//            }
//        }
//        return new Enemy(new Vector2(randX(), randY()), enemyCount);
//    }

    private Consumable generateNewConsumable() {
        return new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size"));
    }

    private void generateConsumables() {
        for (int i = 0; i < C_AMT; i++) {
            consumableCount++;
            consumables.add(new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size")));
        }
    }

    private void generateEnemies() {
        for (int i = 0; i < E_AMT; i++) {
            enemyCount++;
            enemies.add(new Enemy(new Vector2(randX(), randY())));
        }
    }

    private void generatePlayers() {
        player = new Player(new Vector2(50, 50), 1);
    }

    private float randX() {
        return MathUtils.random(5, Config.getWidth() - 5);
    }

    private float randY() {
        return MathUtils.random(5, Config.getHeight() - 5);
    }
}