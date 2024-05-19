package com.mygdx.game.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Application;
import com.mygdx.game.actor.TaskCard;
import com.mygdx.game.utils.TextureAssetManager;
import dev.lyze.flexbox.FlexBox;

public class TestingScreen implements Screen {
    private final Application app;
    private Stage stageHUD;

    private Stage stage;

    private Image hudImage;

    private SpriteBatch batch;

    private TextureRegionDrawable textureRegionDrawableBg;

    private ProgressBar progressBar;

    private TaskCard taskCard;

    private TextureAssetManager TextureAssetManager = new TextureAssetManager();

    public TestingScreen(final Application app) {
        this.app = app;
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Skin skinProgressbar = new Skin(Gdx.files.internal("shadeui/uiskin.json"));

        stage = new Stage(new FitViewport(Application.V_WIDTH, Application.V_HEIGHT , app.camera));
        taskCard = new TaskCard("Bread", 25);

        Image foodImage = new Image(TextureAssetManager.getTexture("Bread"));

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.RED);
        bgPixmap.fill();
        textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));



        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);


        //Life
        Table table = new Table();
        root.add(table).bottom().expand().padBottom(55).space(10);
        table.add(foodImage).size(100, 100).uniform().space(10);
//        table.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).uniform().space(10);
        table.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).uniform().space(10);
        table.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).uniform().space(10);


        Texture hudTexture = new Texture(Gdx.files.internal("overtimeHUD.png"));
        hudImage = new Image(hudTexture);
        stage.addActor(hudImage);


        stage.setDebugAll(true);

        batch = new SpriteBatch();

//        cherryUI.setVisible(true);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        // Clear the screen
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        taskCard.updateProgress(1);

        batch.begin();
        stage.draw();
        batch.end();
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
