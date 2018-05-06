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

    // Players
    private final Array<Player> players = new Array<>();

    // Dimensions
    private final float H = Config.getHeight();
    private final float W = Config.getWidth();

    // Amount of each entity
    private final int C_AMT = 1000;
    private final int E_AMT = 30;

    // Renderer
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Bitmap Font
    private final BitmapFont playerName;
    private final SpriteBatch batch;

    // Track amount of Entities that have existed
    private int enemyCount;
    private int consumableCount;

    // Inputs
    private final int W_ = Input.Keys.W;
    private final int A = Input.Keys.A;
    private final int S = Input.Keys.S;
    private final int D = Input.Keys.D;
    private final int UP = Input.Keys.UP;
    private final int L = Input.Keys.LEFT;
    private final int DN = Input.Keys.DOWN;
    private final int R = Input.Keys.RIGHT;

    public GameScreen(Game game) {
        this.game = game;
        generateConsumables();
        generateEnemies();
        generatePlayers();
        playerName = new BitmapFont();
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        drawEntities();
        generatePlayerNumbers();
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
        consumeInput();
        placeConsumables();
        placeEnemies();
        placePlayers();
        shapeRenderer.end();
    }

    private void checkAllPlayersEaten() {
        if (players.size == 0 || enemies.size == 0 || consumables.size == 0) {
            game.setScreen(new EndScreen(game));
        }
    }

    private void generatePlayerNumbers() {
        batch.begin();
        for (Player p : players) {
            playerName.draw(batch, String.valueOf(p.getId()), p.x, p.y);
        }
        batch.end();
    }

    private void enemyChecks(Enemy e) {
        e.depreciate();
        checkEnemyAtePlayer(e);
        checkEnemyAteEnemy(e);
        checkEnemyAteConsumable(e);
        e.checkGoingOffScreen();
    }

    private void moveFunctions(Enemy e) {
        e.changeDirection();
        e.move();
    }

    private void checkEnemyAtePlayer(Enemy e) {
        for (Player p : players) {
            if (e.overlaps(p) && e.radius > p.radius) {
                players.removeValue(p, false);
                Gdx.app.debug("Enemy at Player", e.getId() + " ate: " + p.getId());
                e.radiusIncrease(p.radius);
            }
        }
    }

    private void checkEnemyAteEnemy(Enemy e) {
        for (int i = 0; i < enemies.size; i++) {
            Enemy ee = enemies.get(i);
            if (e.overlaps(ee) && e.radius > ee.radius) {
                enemies.removeValue(ee, false);
                Gdx.app.debug("Enemy at Enemy", e.getId() + " ate: " + ee.getId());
                e.radiusIncrease(ee.radius);
            }
        }
    }

    private void checkEnemyAteConsumable(Enemy e) {
        for (Consumable c : consumables) {
            if (e.overlaps(c)) {
                consumables.removeValue(c, false);
                Gdx.app.debug("Enemy at Consumable", e.getId() + " ate: " + c.getId());
                e.radiusIncrease(c.radius);
            }
        }
    }

    private void checkPlayerAteConsumable(Player p) {
        for (Consumable c : consumables) {
            if (p.overlaps(c)) {
                consumables.removeValue(c, false);
                Gdx.app.debug("Player at Consumable", p.getId() + " ate: " + c.getId());
                p.radiusIncrease(c.radius);
            }
        }
    }

    private void checkPlayerAteEnemy(Player p) {
        for (Enemy e : enemies) {
            if (p.overlaps(e) && p.radius > e.radius) {
                enemies.removeValue(e, false);
                Gdx.app.debug("Player at Enemy", p.getId() + " ate: " + e.getId());
                p.radiusIncrease(e.radius);
            }
        }
    }

    private void checkPlayerAtePlayer(Player p) {
        if (players.size == 1) return;

        for (int i = 0; i < players.size; i++) {
            Player pp = players.get(i);
            if (p.overlaps(pp) && p.radius > pp.radius) {
                players.removeValue(pp, false);
                Gdx.app.debug("Player at Player", p.getId() + " ate: " + pp.getId());
                p.radiusIncrease(pp.radius);
            }
        }
    }

    private void consumeInput() {
        Input g = Gdx.input;

        for (Player p : players) {
            float newX = p.x;
            float newY = p.y;

            switch (p.getId()) {
                case 1:
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
                    break;
                case 2:
                    if (g.isKeyPressed(UP) && g.isKeyPressed(L)) {
                        newY += 1;
                        newX -= 1;
                    } else if (g.isKeyPressed(UP) && g.isKeyPressed(R)) {
                        newY += 1;
                        newX += 1;
                    } else if (g.isKeyPressed(DN) && g.isKeyPressed(L)) {
                        newY -= 1;
                        newX -= 1;
                    } else if (g.isKeyPressed(DN) && g.isKeyPressed(R)) {
                        newY -= 1;
                        newX += 1;
                    } else if (g.isKeyPressed(UP)) {
                        newY += 1;
                    } else if (g.isKeyPressed(DN)) {
                        newY -= 1;
                    } else if (g.isKeyPressed(R)) {
                        newX += 1;
                    } else if (g.isKeyPressed(L)) {
                        newX -= 1;
                    }
                    break;
                default:
                    break;
            }
            p.validateMovement(newX, newY);
        }
    }

    private void playerChecks(Player p) {
        p.depreciate();
        checkPlayerAtePlayer(p);
        checkPlayerAteEnemy(p);
        checkPlayerAteConsumable(p);
    }

    private void placePlayers() {
        for (Player p : players) {
            playerChecks(p);
            shapeRenderer.setColor(p.R, p.G, p.B, 1);
            shapeRenderer.circle(p.x, p.y, p.radius);
        }
    }

    private void placeEnemies() {
        for (Enemy e : enemies) {
            checkNewEnemyGeneration();
            enemyChecks(e);
            moveFunctions(e);
            shapeRenderer.setColor(e.R, e.G, e.B, 1);
            shapeRenderer.circle(e.x, e.y, e.radius);
        }
    }

    private void placeConsumables() {
        for (Consumable c : consumables) {
            checkNewConsumableGeneration();
            shapeRenderer.setColor(c.R, c.G, c.B, 1);
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
    }

    private void checkNewEnemyGeneration() {
        if (enemies.size < E_AMT) {
            Enemy e = generateNewEnemy();
            if (e != null) {
                enemies.add(e);
            }
        }
    }

    private void checkNewConsumableGeneration() {
        if (consumables.size < C_AMT / 2) {
            Consumable c = generateNewConsumable();
            if (c != null) {
                consumables.add(c);
            }
        }
    }

    private Enemy generateNewEnemy() {
        enemyCount++;
        for (Player p : players) {
            if (enemyCount > E_AMT + 20) {
                return null;
            }
        }
        return new Enemy(new Vector2(randX(), randY()), enemyCount);
    }

    private Consumable generateNewConsumable() {
        consumableCount++;
        for (Player p : players) {
            if (consumableCount > C_AMT + 250) {
                return null;
            }
        }
        return new Consumable(new Vector2(randX(), randY()), consumableCount);
    }

    private void generateConsumables() {
        for (int i = 0; i < C_AMT; i++) {
            consumableCount++;
            consumables.add(new Consumable(new Vector2(randX(), randY()), i));
        }
    }

    private void generateEnemies() {
        for (int i = 0; i < E_AMT; i++) {
            enemyCount++;
            enemies.add(new Enemy(new Vector2(randX(), randY()), i));
        }
    }

    private void generatePlayers() {
        players.add(new Player(new Vector2(50, 50), 1));
        players.add(new Player(new Vector2(W - 50, H - 50), 2));
    }

    private float randX() {
        return MathUtils.random(5, Config.getWidth() - 5);
    }

    private float randY() {
        return MathUtils.random(5, Config.getHeight() - 5);
    }
}