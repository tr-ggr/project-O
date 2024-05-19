package com.mygdx.game.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureAssetManager {
    Map<String, Texture> textures = new HashMap<String, Texture>();

    public TextureAssetManager(){
        textures.put("player", new Texture("download-compresskaru.com.png"));
        textures.put("Bread", new Texture("bread.png"));
        textures.put("Spoon", new Texture("spoon.png"));
        textures.put("Coffee", new Texture("coffee.png"));
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }
}
