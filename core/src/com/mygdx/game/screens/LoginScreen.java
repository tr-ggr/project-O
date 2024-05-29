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

public class LoginScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private Table root;
    private TextButton submit;
    private Batch batch;

    private com.mygdx.game.utils.TextureAssetManager TextureAssetManager = new TextureAssetManager();

    private Texture background;

    private boolean isTransitioning;

    public LoginScreen(final Application app) {
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

        Array<Texture> textures = new Array<Texture>();

        textures.add(new Texture(Gdx.files.internal("background/bg_effect.png")));
        textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);


        ParallaxBackground parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Application.V_WIDTH, Application.V_HEIGHT);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background/login_fg.png")))));
        stage.addActor(table);

        Table formsTable = new Table();
        table.add(formsTable).expand().right().padRight(150).width(500).padTop(200);

        formsTable.row();
        Label titleLabel = new Label("LOGIN", app.skin, "year199x_black");
        titleLabel.setFontScale(3f);
        formsTable.add(titleLabel).padBottom(50).colspan(2);


        formsTable.row();
        Label usernameLabel = new Label("Username", app.skin, "black");
        usernameLabel.setFontScale(2f);
        formsTable.add(usernameLabel).padBottom(10).colspan(2);

        formsTable.row();
        TextField usernameField = new TextField("", app.progressskin);
        formsTable.add(usernameField).growX().height(50).padBottom(20).colspan(2);

        formsTable.row();
        Label passwordLabel = new Label("Password", app.skin, "black");
        passwordLabel.setFontScale(2f);
        formsTable.add(passwordLabel).padBottom(10).colspan(2);

        formsTable.row();
        TextField passwordField = new TextField("", app.progressskin);
        formsTable.add(passwordField).growX().height(50).padBottom(10).colspan(2);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        formsTable.row().space(10);
        submit = new TextButton("Submit", app.levelskin, "c3");
        formsTable.add(submit).height(50).fill();

        TextButton register = new TextButton("Register", app.levelskin);
        formsTable.add(register).height(50).fill();


        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               DatabaseHelper db = DatabaseHelper.getInstance();
               User user = db.login(usernameField.getText(), passwordField.getText());
               if(user != null) {
                   CurrentUser.getInstance().setUser(user);
                   app.setScreen(new MenuScreen(app));
               } else {
                   stage.addActor(new DialogTrial(stage, "Error", "Invalid username or password"));
               }

            }
        });

        register.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                })));
                app.setScreen(new RegisterScreen(app));
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
