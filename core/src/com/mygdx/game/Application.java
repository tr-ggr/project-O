package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.controller.GameController;
import com.mygdx.game.screens.GameplayScreen;
import com.mygdx.game.screens.LoginScreen;
import com.mygdx.game.screens.TestingScreen;
import jdk.jpackage.internal.Log;

public class Application extends Game {
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(2f);

        this.setScreen(new GameplayScreen(this, true));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
