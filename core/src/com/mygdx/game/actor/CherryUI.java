package com.mygdx.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.model.NPC;
import com.mygdx.game.utils.TextureAssetManager;

public class CherryUI extends Table {
    private NPC cherry;
    private ProgressBar progressBar;
    private TextureAssetManager TextureAssetManager = new TextureAssetManager();
    private int size;
    Skin progressskin;
    Skin skin;

    public CherryUI () {
        progressskin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        skin = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
    }


    public void update() {
        // Update the CherryUI
        if (cherry == null) {
            return;
        }

        if(size != cherry.getRequirements().size()) {
            size = cherry.getRequirements().size();
            this.clearChildren();

            for(String requirement : cherry.getRequirements()) {
                this.add(new Image(TextureAssetManager.getTexture(requirement))).size(100, 100).space(5);
            }

            this.row().pad(20);
            this.add(progressBar).fill().padLeft(100).padRight(100).colspan(cherry.getRequirements().size()).expandY();

            this.row();
            this.add(new Image(new Texture("Cherry.png"))).size(400,400).expand().left().bottom().colspan(cherry.getRequirements().size());
        }

        progressBar.setValue(cherry.getCurrentTime());
        System.out.println(cherry.getCurrentTime());
    }

    public void setCherry(NPC cherry) {
        // Set the CherryUI's cherry
        // Create a new CherryUI
        this.cherry = cherry;
        size = cherry.getRequirements().size();

        progressBar = new ProgressBar(0, 40f, 1f, false, progressskin);
        this.clearChildren();
        this.bottom().left();
//        cherryUI.add(foodImage).size(100, 100).uniform().space(10);
//        table.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).uniform().space(10);
//        this.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).space(5);
//        this.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).space(5);
//        this.add(new Image(TextureAssetManager.getTexture("Bread"))).size(100, 100).space(5);

        for(String requirement : cherry.getRequirements()) {
            this.add(new Image(TextureAssetManager.getTexture(requirement))).size(100, 100).space(5);
        }

        this.row().pad(20);
        this.add(progressBar).fill().padLeft(100).padRight(100).colspan(cherry.getRequirements().size()).expandY();

        this.row();
        this.add(new Image(new Texture("Cherry.png"))).size(400,400).expand().left().bottom().colspan(cherry.getRequirements().size());

        progressBar.setValue(cherry.getCurrentTime());
    }
}
