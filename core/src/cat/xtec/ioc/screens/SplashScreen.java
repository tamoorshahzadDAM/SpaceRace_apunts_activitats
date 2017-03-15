package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;


public class SplashScreen implements Screen {

    private Stage stage;
    private SpaceRace game;

    private Label.LabelStyle textStyle;
    private Label textLbl1;
    private Label textLbl2;
    private Label textLbl3;
    private Container contFacil, contNormal, contDificil;


    public SplashScreen(SpaceRace game) {

        this.game = game;

        // Creem la càmera de les dimensions del joc
        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        // Posant el paràmetre a true configurem la càmera per a
        // que faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(true);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        // Creem l'estil de l'etiqueta i l'etiqueta
        textStyle = new Label.LabelStyle(AssetManager.font, null);
       // textStyle2 = new Label.LabelStyle(AssetManager.font, null);
        //textStyle3 = new Label.LabelStyle(AssetManager.font, null);
        textLbl1 = new Label("Facil", textStyle);
        textLbl2 = new Label("Normal", textStyle);
        textLbl3 = new Label("Dificil", textStyle);

        // Creem el contenidor necessari per aplicar-li les accions
        contFacil = new Container(textLbl1);
        contNormal = new Container(textLbl2);
        contDificil = new Container(textLbl3);

        contFacil.setTransform(true);
        contFacil.center();
        contFacil.setPosition(Settings.GAME_WIDTH / 6,20);

        contNormal.setTransform(true);
        contNormal.center();
        contNormal.setPosition(Settings.GAME_WIDTH /2 ,20);

        contDificil.setTransform(true);
        contDificil.center();
        contDificil.setPosition(Settings.GAME_WIDTH*5/6,20);



        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament
       // contFacil.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        contFacil.setName("Facil");
        stage.addActor(contFacil);

      //  contNormal.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        contNormal.setName("Normal");
        stage.addActor(contNormal);

      //  contDificil.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        contDificil.setName("Dificil");
        stage.addActor(contDificil);

        // Creem la imatge de la nau i li assignem el moviment en horitzontal
        Image spacecraft = new Image(AssetManager.spacecraft);
        float y = Settings.GAME_HEIGHT / 2 + textLbl1.getHeight();
        spacecraft.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveTo(0 - spacecraft.getWidth(), y), Actions.moveTo(Settings.GAME_WIDTH, y, 5))));

        stage.addActor(spacecraft);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        stage.draw();
        stage.act(delta);
        Vector3 vector;

        // Si es fa clic en la pantalla, canviem la pantalla
        if (Gdx.input.isTouched()) {
                   // vector = new Vector3 (Gdx.input.getX(), Gdx.input.getY(), 0);
                   // stage.getCamera().unproject(vector);
            vector = new Vector3 (Gdx.input.getX(), Gdx.input.getY(), 0);

            if(vector.x >= contFacil.getRight()-55 && vector.x <= contFacil.getRight()+80 && vector.y >= contFacil.getTop()&& vector.y <= contFacil.getTop()+35) {
                Gdx.app.log("coordenada Facil has clickado en " + Float.toString(vector.x), Float.toString(contFacil.getRight()));

                Settings.setSpacecraftVelocity(80);
                Settings.setAsteroidSpeed(-80);
                Settings.setMaxAsteroid(1f);
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
                dispose();
            }

            if(vector.x >= contNormal.getRight() +50 && vector.x <= contNormal.getRight()+180 && vector.y >= contNormal.getTop() && vector.y <= contNormal.getTop()+35){
                Gdx.app.log ("coordenada Normal has clickado en " + Float.toString (vector.x), Float.toString(contNormal.getRight()));
                Settings.setSpacecraftVelocity(90);
                Settings.setAsteroidSpeed(-100);
                Settings.setMaxAsteroid(2f);
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
                dispose();
            }

            if(vector.x >= contDificil.getRight()+150 && vector.x <= contDificil.getRight()+250 && vector.y >= contDificil.getTop() && vector.y <= contDificil.getTop()+35){
                Gdx.app.log ("coordenada Dificil has clickado en " + Float.toString (vector.x), Float.toString(contDificil.getRight()));
                Settings.setSpacecraftVelocity(160);
                Settings.setAsteroidSpeed(-230);
                Settings.setMaxAsteroid(2f);
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
                dispose();
            }



        }


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

    }
}
