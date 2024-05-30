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

import static com.mygdx.game.Application.bgm_game;
import static com.mygdx.game.Application.bgm_intro;
import static com.mygdx.game.screens.GameplayScreen.gameController;

public class GameOverScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private Table root;
    private TextButton submit;
    private Batch batch;

    private com.mygdx.game.utils.TextureAssetManager TextureAssetManager = new TextureAssetManager();

    private Texture background;

    private boolean isTransitioning;

    public GameOverScreen(final Application app, int score, int money) {
        this.app = app;

        this.isTransitioning = true;

        String HighScore = "";
        if(CurrentUser.getInstance().getHighscore() < gameController.timePassed){
            DatabaseHelper.getInstance().saveUser(CurrentUser.getInstance().getId(), (int)gameController.timePassed, gameController.moneyEarned);
            HighScore = "New Highscore: " + (int) gameController.timePassed;
        } else {
            DatabaseHelper.getInstance().saveUser(CurrentUser.getInstance().getId(), CurrentUser.getInstance().getHighscore(), gameController.moneyEarned);
            HighScore = "Highscore: " + CurrentUser.getInstance().getHighscore();
        }

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



        Table buttonsLayout = new Table();
        root.add(buttonsLayout).growX().center().padLeft(100).padRight(100);

        Label gameOverLabel = new Label("Game Over", app.skin, "year199x");
        gameOverLabel.setFontScale(5f);
        gameOverLabel.setAlignment(1);
        buttonsLayout.add(gameOverLabel).center().padTop(100).padBottom(100).growX().colspan(2);

        buttonsLayout.row();
        Label highScore = new Label(HighScore, app.skin);
        highScore.setFontScale(3f);
        highScore.setAlignment(1);
        buttonsLayout.add(highScore).center().padTop(100).padBottom(100).growX().colspan(2);


        buttonsLayout.row();
        Label scoreLabel = new Label("Score: " + score, app.skin);

        scoreLabel.setFontScale(3f);
        scoreLabel.setAlignment(1);
        buttonsLayout.add(scoreLabel).center().padTop(100).padBottom(100).growX();

        Label moneyLabel = new Label("Money: " + money, app.skin);
        moneyLabel.setFontScale(3f);
        moneyLabel.setAlignment(1);
        buttonsLayout.add(moneyLabel).center().padTop(100).padBottom(100).growX();





        buttonsLayout.row();
        TextButton playerAgainButton = new TextButton("Play Again!", app.progressskin);
        TextButton backButton = new TextButton("Return to Menu", app.progressskin);
        buttonsLayout.add(playerAgainButton).padRight(150).grow().height(100);
        buttonsLayout.add(backButton).padLeft(150).grow();

        playerAgainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                app.setScreen(new ChooseGameModeScreen(app));
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MenuScreen(app));
            }
        });









        root.pack();







        stage.setDebugAll(true);

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
