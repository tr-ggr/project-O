package com.mygdx.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureAssetManager {
    Map<String, Texture> textures = new HashMap<String, Texture>();

    public TextureAssetManager(){
//        textures.put("player", new Texture("download-compresskaru.com.png"));
        textures.put("Post-it", new Texture("holdable/Post-it.png"));
        textures.put("Booklet", new Texture("holdable/Booklet.png"));
        textures.put("MoM", new Texture("holdable/MoM.png"));
        textures.put("Paper", new Texture("holdable/Paper.png"));
        textures.put("Pencil", new Texture("holdable/Pencil.png"));
        textures.put("Coffee", new Texture("coffee.png"));
        textures.put("LessonDraft", new Texture("holdable/LessonDraft.png"));

        //taskAssets
        textures.put("SpeechBubbleEmpty", new Texture("taskAsset/SpeechBubbleEmpty.png"));
        textures.put("ExclamationMark", new Texture("taskAsset/ExclamationMark.png"));
        textures.put("SB_CKey", new Texture("taskAsset/SB_CKey.png"));
        textures.put("SB_X", new Texture("taskAsset/SB_X.png"));
        textures.put("SB_Check", new Texture("taskAsset/SB_Check.png"));
        textures.put("PencilSB", new Texture("taskAsset/PencilSB.png"));
        textures.put("PaperSB", new Texture("taskAsset/PaperSB.png"));
        textures.put("BookletSB", new Texture("taskAsset/BookletSB.png"));
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }
}
