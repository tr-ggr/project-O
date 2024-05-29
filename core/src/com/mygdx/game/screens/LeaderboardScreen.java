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

import java.util.ArrayList;

public class LeaderboardScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;
    private Table root;
    private TextButton submit;
    private Batch batch;

    private com.mygdx.game.utils.TextureAssetManager TextureAssetManager = new TextureAssetManager();

    private Texture background;

    private boolean isTransitioning;

    public LeaderboardScreen(final Application app) {
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

        // Create a table to hold the leaderboard
        Table leaderboard = new Table();
        root.add(leaderboard).expand().fill().pad(100);

        // Create a label for the title
        Label title = new Label("LEADERBOARD", app.levelskin, "title-1");
        title.setFontScale(5f);
        leaderboard.add(title).expandX().top().padBottom(50);
        leaderboard.row();

        // Create a table to hold the leaderboard entries
        Table entries = new Table();
        leaderboard.add(entries).top().expandX().center();

        // Create a label for the headers
        Label rank = new Label("RANK", app.skin, "year199x");
        rank.setFontScale(3f);
        entries.add(rank).expandX().left().padRight(50);

        Label name = new Label("NAME", app.skin, "year199x");
        name.setFontScale(3f);
        entries.add(name).expandX().left().padRight(50);

        Label score = new Label("SCORE", app.skin, "year199x");
        score.setFontScale(3f);
        entries.add(score).expandX().left().padRight(50);

        entries.row().space(10);

        // Get the top 5 users from the database
        ArrayList<User> Users = DatabaseHelper.getInstance().TopUsers();

        for (int i = 0; i < Users.size(); i++) {
            User user = Users.get(i);
            Label user_rank = new Label(Integer.toString(i + 1), app.skin, "year199x");
            user_rank.setFontScale(3f);
            user_rank.setAlignment(1);
            entries.add(user_rank).expandX().left().padRight(50).growX();

            Label user_name = new Label(user.getUsername(), app.skin, "year199x");
            user_name.setFontScale(3f);
            user_name.setAlignment(1);
            entries.add(user_name).expandX().left().padRight(50).growX();

            Label user_score = new Label(Integer.toString(user.getHighscore()), app.skin, "year199x");
            user_score.setFontScale(3f);
            user_score.setAlignment(1);
            entries.add(user_score).expandX().left().padRight(50).growX();
            entries.row().space(10);
        }

//        Label user1_rank = new Label("1", app.skin, "year199x");
//        user1_rank.setFontScale(3f);
//        user1_rank.setAlignment(1);
//        entries.add(user1_rank).expandX().left().padRight(50).growX();
//
//        Label user1_name = new Label("Ungart", app.skin, "year199x");
//        user1_name.setFontScale(3f);
//        user1_name.setAlignment(1);
//        entries.add(user1_name).expandX().left().padRight(50).growX();
//
//        Label user1_score = new Label("100", app.skin, "year199x");
//        user1_score.setFontScale(3f);
//        user1_score.setAlignment(1);
//        entries.add(user1_score).expandX().left().padRight(50).growX();


        leaderboard.row();
        Button backButton = new TextButton("Back", app.levelskin);
        leaderboard.add(backButton).expand().bottom().height(100).growX();


        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MenuScreen(app));
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
