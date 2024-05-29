package com.mygdx.game.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerXBOX extends Player{
    public PlayerXBOX(World world, TiledMap map, boolean isPlayer1) {
        super(world, map, isPlayer1);

    }
}
