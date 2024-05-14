package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.utils.Constants.PPM;

public class DropOff {
    public Body body;

    public DropOff(World world, int x, int y, int w, int h){
        createDropOff(world, x, y, w, h);
    }

    private void createDropOff(World world, int x, int y, int w, int h){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = shape;
        playerFixture.density = 1.0f;
        playerFixture.isSensor = true;




        this.body = world.createBody(def);
        this.body.createFixture(playerFixture).setUserData("Sensor");
    }
}
