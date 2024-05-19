package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Application;

public class LoginScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private Table root;
    private TextButton submit;

    public LoginScreen(final Application app) {
        this.app = app;

        skin = new Skin(Gdx.files.internal("level_plane/level-plane-ui.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label lblUsername = new Label("Username", skin);
        lblUsername.setFontScale(2f);
        root.add(lblUsername).width(500).height(50);

        root.row();
        TextField username = new TextField("", skin);
        root.add(username).width(500).height(50);

        root.row();
        Label lblPassword = new Label("Password", skin);
        lblPassword.setFontScale(2f);
        root.add(lblPassword).width(500).height(50);

        root.row().space(10);
        TextField password = new TextField("", skin);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        root.add(password).width(500).height(50);

        root.row();
        submit = new TextButton("Submit", skin);
        root.add(submit).width(500).height(50);

        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MenuScreen(app));
            }
        });

//        stage.setDebugAll(true);
        root.pack();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//        super.render();

        // Clear the screen
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
