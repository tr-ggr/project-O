package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Application;
import com.mygdx.game.actor.TaskCard;
import com.mygdx.game.screenutils.ParallaxBackground;
import com.mygdx.game.utils.TextureAssetManager;

public class SplashScreen implements Screen {
    private final Application app;

    private Stage stageHUD;

    private Stage stage;

    private Image hudImage;

    private SpriteBatch batch;

    private TextureRegionDrawable textureRegionDrawableBg;

    private ProgressBar progressBar;

    private TaskCard taskCard;

    private TextureAssetManager TextureAssetManager = new TextureAssetManager();

    private Texture background;


    public SplashScreen(final Application app) {
        this.app = app;


        stage = new Stage(new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));

        Gdx.input.setInputProcessor(stage);

        background = TextureAssetManager.getTexture("background");

        batch = new SpriteBatch();

        //Root Table
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        stage.addActor(root);

        Array<Texture> textures = new Array<Texture>();

        textures.add(new Texture(Gdx.files.internal("background/bg_effect.png")));
        textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);


        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Application.V_WIDTH, Application.V_HEIGHT);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background/foreground.png")))));
        stage.addActor(table);

        table.row();
        Label label = new Label("Click anywhere to start!", app.skin, "year199x");
        label.setFontScale(5f);
        table.add(label).bottom().expand().padBottom(50);

        float fadeDuration = 0.5f; // The duration of the fade in and fade out in seconds
        label.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(fadeDuration), Actions.fadeIn(fadeDuration))));

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        app.setScreen(new LoginScreen(app));
                    }
                })));
                return true;
            }
        });

//        stage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        // Clear the screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batch.begin();
//        batch.draw(background, 0, 0,background.getWidth(), background.getHeight());
//        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
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
