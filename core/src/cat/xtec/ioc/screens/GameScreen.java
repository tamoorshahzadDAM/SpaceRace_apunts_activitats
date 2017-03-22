package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Bullet;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Settings;

import static cat.xtec.ioc.SpaceRace.puntos;

public class GameScreen implements Screen {

    // Els estats del joc
    public enum GameState {

        READY, RUNNING, GAMEOVER

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;
    private ScrollHandler scrollHandler;
    //private scrollDisparo scrollDisparoo;
    private Asteroid asteroid;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    private Bullet bullet;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;
    private GlyphLayout text;

    //Variable para contar puntos
    private int puntos;

    /**
     * Constructor
     * @param prevBatch
     * @param prevViewport
     */
    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);
        batch = stage.getBatch();

        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT);
        scrollHandler = new ScrollHandler();
        //Se crea el bullet
        bullet = new Bullet(Settings.SPACECRAFT_STARTX+40, Settings.SPACECRAFT_STARTY+5, 50, 20);


        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);
        stage.addActor(bullet);

        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");
        bullet.setName("bullet");
        //asteroid.setName("asteroid");



        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");

        //Estado actual
        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    //Getters y setters
    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    /**
     * Dibuja los elementos
     */
    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());

        //Pintem bullet
        shapeRenderer.rect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());

        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;

        //Bucle para tamaño de asteroides
        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }
        shapeRenderer.end();
    }


    //Methodos override
    @Override
    public void show() {

    }

    /**
     * Methodo override de render
     * @param delta
     */
    @Override
    public void render(float delta) {

        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;

        }

        //drawElements();

    }

    /**
     * Methodo update, que se actualiza y prepara los objetos
     */
    private void updateReady() {

        // Dibuixem el text al centre de la pantalla

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //stage.addActor(textLbl);



        batch.end();

    }

    /**
     * MethodoupdateRunnimg, va actualizado  ejecucion de juego
     * @param delta
     */
    private void updateRunning(float delta) {

        text= new GlyphLayout();
        //Muestra puntos al final
        text.setText(AssetManager.font, "Puntos:");

        stage.act(delta);
        //Va sumando los puntos
        puntos = puntos + 1;

        //textStyle = new Label.LabelStyle(AssetManager.font, null);

        String cadena = String.valueOf(puntos/60);
        //textLayout.setText(AssetManager.font, "puntos: "+cadena);

        //si hay colecion entre spacecraft y asteroides entonces se acaba el juego y muestra un mensajey los puntos
        if (scrollHandler.collides(spacecraft)) {

            //textLbl = new Label("Puntos: " + cadena, textStyle);
            text.setText(AssetManager.font, "puntos: "+cadena);
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            textLayout.setText(AssetManager.font, "GAME OVER \n puntos: "+cadena);
            currentState = GameState.GAMEOVER;
        }
/**
        //Si hay colecion entre bullet y asteroides mientras el bullet esta visible .
        if (scrollHandler.collidesBullet(bullet) && bullet.getEstado()==0) {

            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("bullet").remove();
            stage.getRoot().findActor("asteroid").remove();

        }
*/
    }

    /**
     * Methodo que actualiza el fin de juego.
     * @param delta
     */
    private void updateGameOver(float delta) {
        stage.act(delta);

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);

        //batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (bullet.getX() + bullet.getWidth() / 2) - 32, bullet.getY() + bullet.getHeight() / 2 - 32, 64, 64);

        batch.end();

        explosionTime += delta;

    }

    /**
     * Methodo reset
     */
    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");
        puntos = 0;
        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();
        bullet.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);
        stage.addActor(bullet);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }


    /**
     * Methodos override para resize
     * @param width
     * @param height
     */
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

    }

    //Getter y setters
    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }
}
