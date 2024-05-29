package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.screens.*;

public class Application extends Game {
    public static final int V_WIDTH = 1920;
    public static final int V_HEIGHT = 1080;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    public Skin skin;

    public Skin progressskin;

    public Skin levelskin;

    //Music Render
    public static Music bgm_intro;
    public static Music bgm_game;
    public static Music bgm_dean;

    //SFX Render
    public static Music sfx_drop;
    public static Music sfx_points;

    private static float maxVolume = 0.5f;

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

        bgm_intro = Gdx.audio.newMusic(Gdx.files.internal("bgm/bgm_intro.mp3"));
        bgm_dean = Gdx.audio.newMusic(Gdx.files.internal("bgm/bgm_dean.mp3"));
        bgm_game = Gdx.audio.newMusic(Gdx.files.internal("bgm/bgm_game.mp3"));

        sfx_drop = Gdx.audio.newMusic(Gdx.files.internal("sfx/sfx_drop.mp3"));
        sfx_points = Gdx.audio.newMusic(Gdx.files.internal("sfx/sfx_points.mp3"));

        bgm_intro.setLooping(true);
        bgm_intro.play();

        bgm_dean.setLooping(true);
        bgm_game.setLooping(true);

        bgm_intro.setVolume(maxVolume);
        bgm_dean.setVolume(maxVolume);
        bgm_game.setVolume(maxVolume);



        this.setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        skin.dispose();
        progressskin.dispose();
        levelskin.dispose();
    }

    public static void fadeMusic(Music music1, Music music2){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Fade out
                for (float i = maxVolume; i >= 0; i -= 0.01f) {
                    music1.setVolume(i);
                    try {
                        Thread.sleep(10); // Pause for 10 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                music1.stop();

                // Start the next track
                music2.play();

                // Fade in
                for (float i = 0.0f; i <= maxVolume; i += 0.01f) {
                    music2.setVolume(i);
                    try {
                        Thread.sleep(10); // Pause for 10 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
