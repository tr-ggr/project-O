package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Application;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MenuScreen implements Screen {
    private final Application app;
    private Stage stage;
    private Skin skin;

    public MenuScreen(Application app) {
        this.app = app;
        skin = new Skin(Gdx.files.internal("level_plane/level-plane-ui.json"));

        stage = new Stage(new FitViewport(Application.V_WIDTH, Application.V_HEIGHT, app.camera));

        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table menu = new Table();
        root.add(menu).left().expandX().padLeft(50);



        menu.row();
        TextButton playButton = new TextButton("Play", skin);
        menu.add(playButton).width(500).height(50);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new GameplayScreen(app, false));
            }
        });

        menu.row().space(10);
        TextButton logoutButton = new TextButton("Log Out", skin);
        menu.add(logoutButton).width(500).height(50);

        logoutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new LoginScreen(app));
            }
        });

        Table leaderboard = new Table();
        leaderboard.setSize(500, 500);
        root.add(leaderboard).right().expandX().padRight(50);

        Label lblLeaderboard = new Label("Leaderboard", skin);
        lblLeaderboard.setFontScale(2f);
        lblLeaderboard.setAlignment(1);

        leaderboard.add(lblLeaderboard).colspan(3).fill();

        leaderboard.row().space(10);
        Label lblRank = new Label("No. ", skin);
        Label lblName = new Label("Username", skin);
        Label lblScore = new Label("Highest Time", skin);

        lblRank.setFontScale(2f);
        lblName.setFontScale(2f);
        lblScore.setFontScale(2f);

        leaderboard.add(lblRank);
        leaderboard.add(lblName).width(250).height(50);
        leaderboard.add(lblScore).width(250).height(50);

        for(int i = 5; i >= 0; i--){
            leaderboard.row().space(10);
            Label rank = new Label(String.valueOf(6 - i), skin);
            Label name = new Label("User " + i, skin);
            Label score = new Label(String.valueOf(i * 100), skin);

            rank.setFontScale(2f);
            name.setFontScale(2f);
            score.setFontScale(2f);

            leaderboard.add(rank);
            leaderboard.add(name).width(250).height(50);
            leaderboard.add(score).width(250).height(50);
        }




        root.pack();
//        stage.setDebugAll(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
        stage.dispose();
        skin.dispose();
    }
}
