package com.mygdx.game.actor;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.utils.TextureAssetManager;


public class LifeUI extends Table {
    private int lives;
    private int root;
    private TextureAssetManager textureAssetManager;
    public LifeUI(int lives, Stage stage) {
        this.lives = lives;
        textureAssetManager = new TextureAssetManager();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(this).bottom().expand().padBottom(55).space(10);
        for(int i = 0; i < lives; i++){
            this.add(new Image(textureAssetManager.getTexture("Coffee"))).size(100, 100).uniform().space(10);
        }
    }

    public void update(int lives){
        if(this.lives != lives){
            this.clearChildren();

            for(int i = 0; i < lives; i++){
                this.add(new Image(textureAssetManager.getTexture("Coffee"))).size(100, 100).uniform().space(10);
            }

            this.lives = lives;
        }
    }

}
