package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class TextureAssetManager {
    Map<String, Texture> textures = new HashMap<String, Texture>();
    public Animation[] countdownAnimations;

    public TextureAssetManager(){
//        textures.put("player", new Texture("download-compresskaru.com.png"));
        textures.put("Post-it", new Texture("holdable/Post-it.png"));
        textures.put("Booklet", new Texture("holdable/Booklet.png"));
        textures.put("MoM", new Texture("holdable/MoM.png"));
        textures.put("Paper", new Texture("holdable/Paper.png"));
        textures.put("Pencil", new Texture("holdable/Pencil.png"));
        textures.put("Coffee", new Texture("coffee.png"));
        textures.put("LessonDraft", new Texture("holdable/LessonDraft.png"));
        textures.put("GradedPaper", new Texture("holdable/GradedPaper.png"));

        //taskAssets
        textures.put("SpeechBubbleEmpty", new Texture("taskAsset/SpeechBubbleEmpty.png"));
        textures.put("ExclamationMark", new Texture("taskAsset/ExclamationMark.png"));
        textures.put("SB_CKey", new Texture("taskAsset/SB_CKey.png"));
        textures.put("SB_X", new Texture("taskAsset/SB_X.png"));
        textures.put("SB_Check", new Texture("taskAsset/SB_Check.png"));
        textures.put("PencilSB", new Texture("taskAsset/PencilSB.png"));
        textures.put("PaperSB", new Texture("taskAsset/PaperSB.png"));
        textures.put("BookletSB", new Texture("taskAsset/BookletSB.png"));


        createCountdownAnimations();
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }

    private Animation<TextureRegion> createAnimation(String filePath, int frameCols, int frameRows) {
        Texture sheet = new Texture(Gdx.files.internal(filePath));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCols, sheet.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        return new Animation<>(0.25f, frames);
    }

    public void createCountdownAnimations() {
        countdownAnimations = new Animation[3];
        countdownAnimations[0] = createAnimation("countdown/2-0-sheet.png", 3, 1);
        countdownAnimations[1] = createAnimation("countdown/3-0-sheet.png", 4, 1);
        countdownAnimations[2] = createAnimation("countdown/5-0-sheet.png", 6, 1);
    }

    enum Countdown {
        TWO, THREE, FIVE
    }
}
