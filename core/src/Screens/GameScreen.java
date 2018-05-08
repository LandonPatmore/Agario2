package Screens;

import Actors.Consumable;
import Actors.Enemy_Smart;
import Actors.Entity;
import Actors.Player;
import Utils.Config;
import Utils.Score;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {

    // Game
    private final Game game;

    // Camera
    private OrthographicCamera camera;

    private Array<Player> players = new Array<>();
    private Array<Entity> entities = new Array<>();
    private Array<Consumable> consumables = new Array<>();

    private Array<Score> leaderboard = new Array<>();

    private boolean debug = Config.getDebug();

    // Amount of each entity
    private final int C_AMT = 1000;
    private final int E_AMT = 10;

    // Renderer
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Bitmap Font
    private final BitmapFont playerName;
    private final BitmapFont enemyName;
    private final BitmapFont lb;
    private final SpriteBatch batch;

    public GameScreen(Game game) {
        this.game = game;
        generateConsumables();
        generateEnemies();
        generatePlayer();
        playerName = new BitmapFont();
        enemyName = new BitmapFont();
        lb = new BitmapFont();
        batch = new SpriteBatch();

        if(Config.getDebug()){
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_NONE);
        }
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        setCamera();
        consumeInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        drawEntities();
        generatePlayerNumber();
        generateEnemyHealth();
        generateLeaderboard();
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

    private void generateLeaderboard(){
        leaderboard.sort();
        batch.begin();
        if(leaderboard.size > 5) {
            for (int i = 0; i < 5; i++) {
                playerName.draw(batch, i + 1 + " : " + leaderboard.get(i).toString(), Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() - (40 * i));
            }
        } else {
            for (int i = 0; i < leaderboard.size; i++) {
                playerName.draw(batch, i + 1 + " : " + leaderboard.get(i).toString(), Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() - (40 * i));
            }
        }
        batch.end();
    }

    private void setCamera(){
        camera.update();
        camera.update();
    }

    private void drawEntities() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        placeConsumables();
        placeEnemies();
        placePlayer();
        shapeRenderer.end();
        debugRender();
    }

    private void checkAllPlayersEaten() {
        if (entities.size == 0 || consumables.size == 0) {
            game.setScreen(new EndScreen(game));
        }
    }

    private void generatePlayerNumber() {
        batch.begin();
        for(Player p : players) {
            playerName.draw(batch, "Player", p.x, p.y);
        playerName.draw(batch, "Health: "+ (int) p.getHealth(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 50);
        }
        batch.end();
    }

    private void generateEnemyHealth() {
        batch.begin();
        for(Entity e : entities) {
            if(!(e instanceof Player) && e != null) {
                Enemy_Smart enemy = (Enemy_Smart) e;
                enemyName.draw(batch, "Name: " + e.getName() + " | " + String.valueOf((int) e.getHealth()) + " | Gen: " + enemy.getGeneration() + " | " + ((Enemy_Smart) e).currentState(), e.x, e.y);

            }
        }
        batch.end();
    }

    private void runEnemy(Enemy_Smart e) {
        e.healthDecrease();
        e.determineState(consumables, entities);
        checkEnemyDead(e);
        checkEnemyAteConsumable(e);
    }

    private void checkEnemyDead(Enemy_Smart e){
        if(e.checkIfDead()){
            leaderboard.add(new Score(e.getLifeSpan(), e.getName()));
            Consumable c = new Consumable(new Vector2(e.x,e.y), e.radius / 2, false);
            c.setEnergy(10);

            consumables.add(c);
            entities.removeValue(e, false);
        }
    }

    private void checkPlayerDead(Player p){
            if (p.checkIfDead()) {
                leaderboard.add(new Score(p.getLifeSpan(), p.getName()));
                Consumable c = new Consumable(new Vector2(p.x,p.y), p.radius / 2, false);
                c.setEnergy(10);

                players.removeValue(p, false);
                entities.removeValue(p, false);
                consumables.add(c);
            }

    }

    private void checkPlayerAteConsumable() {
        for(Player p : players) {
            for (Consumable c : consumables) {
                if (p.overlaps(c)) {
                    p.healthIncrease(c.getEnergy());
                    consumables.removeValue(c, false);
                }
            }
        }
    }

    private void checkEnemyAteConsumable(Enemy_Smart e) {
        for (Consumable c : consumables) {
            if (e.overlaps(c)) {
                e.healthIncrease(c.getEnergy());
                consumables.removeValue(c, false);
            }
        }
    }

    private void consumeInput() {
        Input g = Gdx.input;
        if(g.isKeyJustPressed(Input.Keys.SPACE)){
            entities.add(new Enemy_Smart(new Vector2(randX(), randY()), null, 1, entities.size++));
        } else if(g.isKeyJustPressed(Input.Keys.P)){
            generatePlayer();
        } else if(g.isKeyJustPressed(Input.Keys.Y)){
            debug = true;
        } else if(g.isKeyJustPressed(Input.Keys.U)){
            debug = false;
        }
        for(Player p : players) {
            p.move(g);
        }
    }

    private void playerChecks(Player p) {
            p.healthDecrease();
            checkPlayerDead(p);
            checkPlayerAteConsumable();
    }

    private void placePlayer() {
        for(int i = 0; i < players.size; i++) {
            Player p = players.get(i);
            playerChecks(p);
            shapeRenderer.setColor(p.getColor());
            shapeRenderer.circle(p.x, p.y, p.radius);
            shapeRenderer.circle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 2);
        }
    }

    private void placeEnemies() {
        for (Entity e : entities) {
            if(!(e instanceof Player) && e != null) {
                checkNewEnemyGeneration((Enemy_Smart) e);
                runEnemy((Enemy_Smart) e);
                shapeRenderer.setColor(e.getColor());
                shapeRenderer.circle(e.x, e.y, e.radius);
            }
        }
    }

    private void debugRender(){
        if(debug) {
            for (Entity e : entities) {
                if(!(e instanceof Player) && e != null) {
                    Enemy_Smart enemy = (Enemy_Smart) e;
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(1, 51 / 255f, 153 / 255f, 1);
                    shapeRenderer.arc(e.x, e.y, enemy.visionLength(), enemy.getLooking(), enemy.visionWidth());
                    shapeRenderer.setColor(0, 1, 1, 1);
                    shapeRenderer.line(e.getPosition(), enemy.getClosest());
                    shapeRenderer.setColor(1, 0, 0, 1);
                    shapeRenderer.circle(e.x, e.y, enemy.playerAttackProximity());
                    shapeRenderer.setColor(0, 1, 0, 1);
                    shapeRenderer.circle(e.x, e.y, enemy.consumableProximity());
                    shapeRenderer.setColor(0, 0, 1, 1);
                    shapeRenderer.circle(e.x, e.y, enemy.fleeProximity());
                    shapeRenderer.end();
                }
            }
        }
    }

    private void placeConsumables() {
        for (Consumable c : consumables) {
            checkNewConsumableGeneration();
            shapeRenderer.setColor(c.getColor());
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
    }

    private void checkNewEnemyGeneration(Enemy_Smart e) {
        Enemy_Smart enemy = generateNewEnemy(e);
        if (enemy != null) {
            entities.add(enemy);
        }
    }

    private Enemy_Smart generateNewEnemy(Enemy_Smart e) {
        float probability = MathUtils.random();
        if(probability < .0003) {
            return new Enemy_Smart(new Vector2(randX(), randY()), e.getDna(), e.getGeneration(), entities.size++);
        }

        return null;
    }

    private void checkNewConsumableGeneration() {
        if (consumables.size < C_AMT / 2) {
            Consumable c = generateNewConsumable();
            consumables.add(c);
        }
    }

    private Consumable generateNewConsumable() {
        float probability = MathUtils.random();
        return new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size"), probability > .5);
    }

    private void generateConsumables() {
        for (int i = 0; i < C_AMT; i++) {
            consumables.add(new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size"), false));
        }

        for(int i = 0; i < C_AMT; i++){
            consumables.add(new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size"), true));
        }
    }

    private void generateEnemies() {
        for (int i = 0; i < E_AMT; i++) {
            float x = randX();
            float y = randY();
            entities.add(new Enemy_Smart(new Vector2(x, y), null, 1, i));
        }
    }

    private void generatePlayer() {
        if(players.size < 1) {
            Player player = new Player(new Vector2(50, 50));
            entities.add(player);
            players.add(player);
        }
    }

    private float randX() {
        return MathUtils.random(5, Config.getWidth() - 5);
    }

    private float randY() {
        return MathUtils.random(5, Config.getHeight() - 5);
    }
}