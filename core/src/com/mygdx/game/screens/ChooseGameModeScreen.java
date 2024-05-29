package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Application;
import com.mygdx.game.actor.DialogTrial;
import com.mygdx.game.database.CurrentUser;
import com.mygdx.game.database.DatabaseHelper;
import com.mygdx.game.database.User;
import com.mygdx.game.screenutils.ParallaxBackground;
import com.mygdx.game.utils.TextureAssetManager;

import static com.mygdx.game.Application.*;

public class ChooseGameModeScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private Table root;
    private TextButton submit;
    private Batch batch;

    private com.mygdx.game.utils.TextureAssetManager TextureAssetManager = new TextureAssetManager();

    private Texture background;

    private boolean isTransitioning;

    public ChooseGameModeScreen(final Application app) {
        this.app = app;

        this.isTransitioning = true;

        stage = new Stage(new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));
        stage.getRoot().getColor().a = 0f; // Set initial alpha to 0 (transparent)
        stage.getRoot().addAction(Actions.fadeIn(0.5f)); // Add fade-in action to the root actor of the stage

        Gdx.input.setInputProcessor(stage);

        background = TextureAssetManager.getTexture("background");

        batch = new SpriteBatch();

        //Root Table
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable(new TextureRegion(background)));
        stage.addActor(root);


        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background/SelectGameMode.png")))));
        stage.addActor(table);

        Table buttonsLayout = new Table();
        table.add(buttonsLayout).growX().center().padTop(550).padLeft(100).padRight(100);

        TextButton singlePlayerButton = new TextButton("Click Here!", app.skin);
        TextButton multiplayerButton = new TextButton("Click Here!", app.skin);
        buttonsLayout.add(singlePlayerButton).padRight(150).grow().height(100);
        buttonsLayout.add(multiplayerButton).padLeft(150).grow();

        singlePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeMusic(bgm_intro, bgm_game);
                app.setScreen(new GameplayScreen(app, false));
            }
        });

        multiplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fadeMusic(bgm_intro, bgm_game);

                app.setScreen(new GameplayScreen(app, true));
            }
        });









        root.pack();







//        stage.setDebugAll(true);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//        super.render();

        // Clear the screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isTransitioning){
            if (stage.getRoot().getColor().a >= 1){
                isTransitioning = false;
            }
            stage.getRoot().getColor().a += delta;
        }

        stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        app.dispose();
        stage.dispose();
    }
}
