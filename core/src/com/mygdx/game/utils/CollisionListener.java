package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Task;

import java.util.Objects;

import static com.mygdx.game.utils.TiledObjectUtil.*;
import static com.mygdx.game.model.Player.interactedFixture;

public class CollisionListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        interactedFixture = fB;

        System.out.println("Colliding!");

    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getUserData() == null) {
            System.out.println("Removed!");
        } else {
            for(Task task : TiledObjectUtil.tasks){
                if(Objects.equals(contact.getFixtureB().getUserData().toString(), task.name)){
                    task.pauseTimer();
                    System.out.println("Removed " + task.name + "!");
                    break;
                }
            }
        }

        interactedFixture = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
