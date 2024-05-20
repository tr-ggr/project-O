package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static com.mygdx.game.utils.Constants.PPM;

public class DropOff {
    public Body body;

    public DropOff(World world, TiledMap map){
        createDropOff(world, map);
    }

    public void createDropOff(World world, TiledMap map) {
        MapLayer layer = map.getLayers().get("DropOff");

        if (layer == null) {
            throw new GdxRuntimeException("Layer 'DropOff' not found in the map");
        }

        MapObjects objects = layer.getObjects();

        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();

                BodyDef def = new BodyDef();
                def.type = BodyDef.BodyType.StaticBody;
                def.position.set((rectangle.x + rectangle.width / 2) / PPM, (rectangle.y + rectangle.height / 2) / PPM);
                def.fixedRotation = true;

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rectangle.width / 2 / PPM, rectangle.height / 2 /PPM);

                FixtureDef playerFixture = new FixtureDef();
                playerFixture.shape = shape;
                playerFixture.density = 1.0f;
//                playerFixture.isSensor = true;

                this.body = world.createBody(def);
                this.body.createFixture(playerFixture).setUserData("Sensor");
            }
        }
    }
}
