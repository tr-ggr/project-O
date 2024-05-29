package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.screens.LoginScreen;
import com.mygdx.game.screens.SplashScreen;

public class Application extends Game {
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    public Skin skin;

    public Skin progressskin;

    public Skin levelskin;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(2f);

        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        progressskin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        levelskin = new Skin(Gdx.files.internal("level_plane/level-plane-ui.json"));

        this.setScreen(new LoginScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
