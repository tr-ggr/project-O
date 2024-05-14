package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.mygdx.game.MyGdxGame.world;
import static com.mygdx.game.utils.Constants.PPM;

public class Food {
    public String name;
    public Sprite sprite;
    public Body body;
    public boolean isDeleted = false;

    public Food(String name, Texture sprite, int x, int y){
        this.name = name;
        this.sprite = new Sprite(sprite);
        this.body = createBox(x, y, 16, 16, false);
    }

    public void draw(Batch batch){
        if(!body.isActive()){
//            sprite.getTexture().dispose();
            System.out.println("Body is null");
            isDeleted = true;
            return;
        }
        sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    public Body createBox(int x, int y, int w, int h, boolean isStatic){
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

        pBody = world.createBody(def);
        pBody.createFixture(playerFixture).setUserData("Food");
        shape.dispose();

        pBody.setActive(true);

        return pBody;
    }
}
