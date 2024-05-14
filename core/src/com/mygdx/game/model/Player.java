package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.TiledObjectUtil;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.utils.TiledObjectUtil.*;
import static com.mygdx.game.utils.Constants.PPM;

public class Player{
    public Body body;
    public Fixture interactedFixture;
    public Body interactedFood;
    public boolean isGrabbing = false;
    public Sprite sprite;

    public ArrayList<Body> interactedList = new ArrayList<>();

    public Player(World world){
        createPlayerBody(world, 200, 200, 32, 32);
        sprite = new Sprite(new Texture("download-compresskaru.com.png"));
    }

    public void inputUpdate(float delta){
        int horizontalForce = 0;
        int verticalForce = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 1;
            body.setTransform(body.getPosition(), (float) Math.PI);
            sprite.setRotation(180);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
            body.setTransform(body.getPosition(), 0f);
            sprite.setRotation(0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            verticalForce += 1;
            body.setTransform(body.getPosition(), (float) (Math.PI / 2));
            sprite.setRotation(90);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            verticalForce -= 1;
            body.setTransform(body.getPosition(), (float) (3 * Math.PI / 2));
            sprite.setRotation(270);
        }

        if(horizontalForce == 0 && verticalForce == 0) {
            body.setLinearVelocity(0, 0);
        } else {
            body.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            isGrabbing = true;
            System.out.println("Now grabbing!");
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
            if (interactedList.isEmpty()) {
                System.out.println("No food");
                return;
            } else {

                for(Body b: interactedList ){
                    System.out.println(b);
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.X)){
            System.out.println("Successfully dropped food!");
            isGrabbing = false;

            interactedList.clear();
            if(grabFood != null) {
                world.destroyJoint(grabFood);
                grabFood = null;
            }
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if (interactedFixture == null) {
                System.out.println("No interaction");
            } else if(Objects.equals(interactedFixture.getUserData(), null)){
                System.out.println("Interacted with " + interactedFixture.getUserData());
            } else {
                for(Task task : TiledObjectUtil.tasks){
                    if(Objects.equals(interactedFixture.getUserData().toString(), task.name)){
                        System.out.println("Interacted with " + interactedFixture.getUserData());
                        task.interactTimer();
                        break;
                    }
                }
            }
        }

    }

    private void createPlayerBody(World world, int x, int y, int w, int h){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = shape;
        playerFixture.density = 1.0f;




        this.body = world.createBody(def);
        this.body.createFixture(playerFixture).setUserData("Player");
    }

    public void draw(Batch batch){
        sprite.setPosition(body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
        sprite.draw(batch);
    }
}
