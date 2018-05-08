package Screens;

import Actors.Player;
import Utils.Config;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

    // Game
    private final Game game;

    // Camera
    private OrthographicCamera camera;

    // Entities
//    private final Array<Consumable> consumables = new Array<>();
//    private final Array<Enemy_Smart> enemies = new Array<>();

    // Entity
    private Player player;

    // Amount of each entity
    private final int C_AMT = 1200;
    private final int E_AMT = 25;

    // Renderer
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Bitmap Font
    private final BitmapFont playerName;
    private final BitmapFont enemyName;
    private final SpriteBatch batch;

    public GameScreen(Game game) {
        this.game = game;
//        generateConsumables();
//        generateEnemies();
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
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    @Override
    public void render(float delta) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        setCamera();
        consumeInput();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        drawEntities();
        generatePlayerNumber();
//        generateEnemyHealth();
//        checkAllPlayersEaten();
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

    private void setCamera(){
//        camera.position.set(player.getPosition(), 0);
        camera.update();
    }

    private void drawEntities() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        placeConsumables();
//        placeEnemies();
        placePlayer();
        shapeRenderer.end();
//        debugRender();
    }

//    private void checkAllPlayersEaten() {
//        if (enemies.size == 0 || consumables.size == 0) {
//            game.setScreen(new EndScreen(game));
//        }
//    }

    private void generatePlayerNumber() {
        batch.begin();
        playerName.draw(batch, "Player", player.x, player.y);
//        playerName.draw(batch, "Health: "+ player.getHealth(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 50);
        batch.end();
    }

//    private void generateEnemyHealth() {
//        batch.begin();
//        for(Enemy_Smart e : enemies) {
//            enemyName.draw(batch, String.valueOf(e.getHealth()) + " | Gen: " + e.getGeneration(), e.x, e.y);
//        }
//        batch.end();
//    }

//    private void enemyChecks(Enemy_Smart e) {
//        e.healthDecrease();
//        checkEnemyCollision(e);
//        e.hunt(consumables, enemies);
//        checkEnemyDead(e);
//        checkEnemyAteConsumable(e);
//    }

//    private void checkEnemyCollision(Enemy_Smart e){
//        for(int i = 0; i < enemies.size; i++){
//            Enemy_Smart enemy = enemies.get(i);
//            if(e != enemy){
//                if(e.overlaps(enemy) && e.getHealth() > enemy.getHealth()){
//                    enemies.removeValue(enemy, false);
//                }
//            }
//        }
//    }

//    private void checkEnemyDead(Enemy_Smart e){
//        if(e.checkIfDead()){
//            consumables.add(new Consumable(new Vector2(e.x,e.y), e.radius));
//            enemies.removeValue(e, false);
//        }
//    }

//    private void checkPlayerDead(){
//        if(player.checkIfDead()){
//            consumables.add(new Consumable(new Vector2(player.x,player.y), player.radius));
//            player = null;
//            game.setScreen(new EndScreen(game));
//        }
//    }

//    private void checkPlayerAteConsumable() {
//        for (Consumable c : consumables) {
//            if (player.overlaps(c)) {
//                player.healthIncrease();
//                consumables.removeValue(c, false);
//            }
//        }
//    }

//    private void checkEnemyAteConsumable(Enemy_Smart e) {
//        for (Consumable c : consumables) {
//            if (e.overlaps(c)) {
//                e.healthIncrease();
//                consumables.removeValue(c, false);
//            }
//        }
//    }

    private void consumeInput() {
        Input g = Gdx.input;
        player.move(g);
    }

//    private void playerChecks() {
//        player.healthDecrease();
////        checkPlayerDead();
//        checkPlayerAteConsumable();
//    }

    private void placePlayer() {
//            playerChecks();
            shapeRenderer.setColor(player.getColor());
            shapeRenderer.circle(player.x, player.y, player.radius);
    }

//    private void placeEnemies() {
//        for (Enemy_Smart e : enemies) {
//            checkNewEnemyGeneration(e);
//            enemyChecks(e);
//            shapeRenderer.setColor(e.color);
//            shapeRenderer.circle(e.x, e.y, e.radius);
//        }
//    }

//    private void debugRender(){
//        if(Config.getDebug()) {
//            for (Enemy_Smart e : enemies) {
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//                shapeRenderer.setColor(1, 0, 0, 1);
//                shapeRenderer.arc(e.x, e.y, e.getDna()[1], e.looking(), e.getDna()[0]);
//                shapeRenderer.setColor(0, 1, 0, 1);
//                shapeRenderer.line(e.getPosition(), e.getClosest());
//                shapeRenderer.setColor(0, 0, 1, 1);
//                shapeRenderer.circle(e.x,e.y, e.getDna()[2]);
//                shapeRenderer.end();
//            }
//        }
//    }

//    private void placeConsumables() {
//        for (Consumable c : consumables) {
//            checkNewConsumableGeneration();
//            shapeRenderer.setColor(c.color);
//            shapeRenderer.circle(c.x, c.y, c.radius);
//        }
//    }

//    private void checkNewEnemyGeneration(Enemy_Smart e) {
//        Enemy_Smart enemy = generateNewEnemy(e);
//        if (enemy != null) {
//            enemies.add(enemy);
//        }
//    }

//    private void checkNewConsumableGeneration() {
//        if (consumables.size < C_AMT / 2 - 200) {
//            Consumable c = generateNewConsumable();
//            consumables.add(c);
//        }
//    }

//    private Enemy_Smart generateNewEnemy(Enemy_Smart e) {
//        float probability = MathUtils.random();
//        if(probability < .0005) {
//            return new Enemy_Smart(new Vector2(randX(), randY()), e.getDna(), e.getGeneration());
//        }
//
//        return null;
//    }

//    private Consumable generateNewConsumable() {
//        return new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size"));
//    }

//    private void generateConsumables() {
//        for (int i = 0; i < C_AMT; i++) {
//            consumables.add(new Consumable(new Vector2(randX(), randY()), Config.getNumberProperty("consumable_size")));
//        }
//    }

//    private void generateEnemies() {
//        for (int i = 0; i < E_AMT; i++) {
//            float x = randX();
//            float y = randY();
//            enemies.add(new Enemy_Smart(new Vector2(x, y), null, 1));
//        }
//    }

    private void generatePlayers() {
        player = new Player(new Vector2(50, 50), 5);
    }

//    private float randX() {
//        return MathUtils.random(5, Config.getWidth() - 5);
//    }
//
//    private float randY() {
//        return MathUtils.random(5, Config.getHeight() - 5);
//    }
}