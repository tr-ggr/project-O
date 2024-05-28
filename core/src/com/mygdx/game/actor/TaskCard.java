package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.utils.TextureAssetManager;

import java.util.ArrayList;

public class TaskCard extends Table {
    TextureRegionDrawable textureRegionDrawableBg;

    public ProgressBar progressBar;

    TextureAssetManager TextureAssetManager = new TextureAssetManager();

    public float maxTime = 0;

    public float currentTime = 0;

    public TaskCard(String requirement, float time){
        // Create a new TaskCard with the given requirements, name, foodName, and time
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.WHITE);
        bgPixmap.fill();
        textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));

        Skin progressskin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        Skin skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        Label taskName = new Label(requirement, skin, "black");
        Image foodImage = new Image(TextureAssetManager.getTexture(requirement));
        taskName.setFontScale(3f);

        progressBar = new ProgressBar(0.0f, time, 1.0f, false, progressskin);
        progressBar.setValue(time);
        progressBar.setAnimateDuration(0);

        this.pad(10);

        this.add(foodImage).size(150,150).space(10);
        this.setBackground(textureRegionDrawableBg);

        Table table = new Table();
        this.add(table).fill();

        table.add(taskName).grow().fill();

        table.row();
        table.add(progressBar).grow().fill();

//        this.add(progressBar).grow();
        this.pack();

        maxTime = time;
        currentTime = time;


    }

    public void updateProgress(float progress) {
        // Update the progress of the TaskCard

        currentTime -= progress;
//        System.out.println(currentTime);

        if(currentTime <= 0){
//            System.out.println("TaskCard has run out of time");
            currentTime = 0;
        }

        progressBar.setValue(currentTime);
//        System.out.println(success);

    }
}
