package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.TiledObjectUtil;

import java.util.Objects;

import static com.mygdx.game.utils.TiledObjectUtil.*;
import static com.mygdx.game.utils.Constants.PPM;

public class Player{
    public Body body;
    public static Fixture interactedFixture;

    public Player(World world){
        createPlayerBody(world, 0, 0, 32, 32);
    }

    public void inputUpdate(float delta){
        int horizontalForce = 0;
        int verticalForce = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 1;
//            System.out.println("Facing Left " + (body.getPosition().x - 1));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
//            System.out.println("Facing Right " + (body.getPosition().x + 1));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            verticalForce += 1;
//            System.out.println("Facing Up " + (body.getPosition().y + 1));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            verticalForce -= 1;
//            System.out.println("Facing Down " + (body.getPosition().y - 1));
        }

        if(horizontalForce == 0 && verticalForce == 0) {
            body.setLinearVelocity(0, 0);
        } else {
            body.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
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
}
