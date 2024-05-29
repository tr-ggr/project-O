package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.mygdx.game.screens.GameplayScreen.world;
import static com.mygdx.game.utils.Constants.PPM;

public class Food {
    public String name;
    public Sprite sprite;
    public Body body;
    public boolean isDeleted = false;
    public int x, y;

    public Food(String name, Texture sprite, int x, int y){
        this.name = name;
        this.sprite = new Sprite(sprite);
        this.body = null;
        this.x = x;
        this.y = y;
        System.out.println("Generated new Food!");
    }

    public void draw(Batch batch){
        sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    public boolean generateToWorld(){
        System.out.println("Generating " + this + " to the world...");
        if(!world.isLocked()){
            this.body = createBox(x, y, 16, 16, false);
            return true;
        } else {
            System.out.println("World is locked! Try again later...");
            return false;
        }
    }


    public Body createBox(int x, int y, int w, int h, boolean isStatic){
        if (world.isLocked()) {
            return null;
        }

        Body pBody;

        BodyDef def = new BodyDef();
        def.type = (isStatic) ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = shape;
        playerFixture.friction = 10f;
        playerFixture.restitution = 0.0f;
        playerFixture.density = 1f;

        Gdx.app.debug("Food",  " generating body of " + this);
        System.out.println("Food: generating body of " + this);
        pBody = world.createBody(def);

        System.out.println("Food: generating fixture of " + this + " with body of : " + pBody);
        pBody.createFixture(playerFixture).setUserData(name);

        System.out.println("Food: disposing shape of " + this + " with body of : " + pBody);
        shape.dispose();

        pBody.setActive(true);

        pBody.setUserData(this);

        return pBody;
    }
}
