package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by ALUMNEDAM on 15/03/2017.
 */

public class Bullet extends Actor {


    // visibilidad de bullet
    public static final int BULLET_VISIBILE = 0;
    public static final int BULLET_INVISIBILE = 1;

    // Paràmetros para el bullet
    private Vector2 position;
    private int width, height;
    private int estado;

    private Rectangle bcollisionRect;
    private ScrollHandler scrollHandler = new ScrollHandler();

    /**
     * Constructor
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Bullet(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem la spacecraft a l'estat normal
        estado = BULLET_INVISIBILE;

        // Creem el rectangle de col·lisions
        bcollisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    /**
     * MEethodo para mostrar bullet
     */
    public void BulletVisible() {
        estado = BULLET_VISIBILE;
    }

    /**
     * Methodo para ocultar bullet
     */
    public void BulletInVisible() {
        estado = BULLET_INVISIBILE;
        reset();
    }


    /**
     * MEthodo act
     * @param delta
     */
    public void act(float delta) {

        //estados de la bala, visible o invisible seun el caso
        switch (estado) {
            case BULLET_VISIBILE:
                if (this.position.x + width + Settings.SPACECRAFT_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x += Settings.SPACECRAFT_VELOCITY * delta;

                   //Rodondea el posicion de bullet, su punto fina
                    float num = Math.round(this.position.x + width);
                    //Gdx.app.log("num: ", Float.toString(num));
                    //Si es igual que el final de game with, se pone a invisible
                    if (num == Settings.GAME_WIDTH){
                        estado = BULLET_INVISIBILE;
                    }
                }
                break;

        }

        bcollisionRect.set(position.x, position.y + 3, width, 10);
        setBounds(position.x, position.y, width, height);


    }


    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getCollisionRect() {
        return bcollisionRect;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    // Obtenim el TextureRegion depenent de la posició de la bala
    public TextureRegion getSpacecraftTexture() {

        switch (estado) {

            case BULLET_INVISIBILE:
                return AssetManager.disparoBullet;
            case BULLET_VISIBILE:
                return AssetManager.disparo;

            default:
                return AssetManager.disparoBullet;
        }
    }

    /**
     * Methodo reset
     */
    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.SPACECRAFT_STARTX + 36;
        position.y = Settings.SPACECRAFT_STARTY + 5;

        estado = BULLET_INVISIBILE;

        bcollisionRect = new Rectangle();
    }

    /**
     * Methodo override para digujar la bala
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getSpacecraftTexture(), position.x, position.y, width, height);
    }




}
