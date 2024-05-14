package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Food;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Task;

import java.util.Objects;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.utils.TiledObjectUtil.*;

public class CollisionListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        player.interactedFixture = fB;

        System.out.println(fA.getUserData() + " Collides with " + fB.getUserData());




        // If the interactedList is not empty or the fixture is null or its userData is null, return
        if (Objects.equals(fB.getUserData(), null) || grabFood != null) {
            System.out.println("Hands is full!");
            if(Objects.equals(fA.getUserData().toString(), "Sensor") && Objects.equals(fB.getUserData(), "Food")) {
                System.out.println("Food dropped to sensor!");
                deleteJoint = true;
                toBeDeleted.add(fB.getBody());
            }
            return;
        } else if(Objects.equals(fA.getUserData().toString(), "Sensor") && Objects.equals(fB.getUserData(), "Food")){
            System.out.println("Food dropped to sensor!");
            toBeDeleted.add(fB.getBody());
        } else if(Objects.equals(fB.getUserData().toString(), "Food") && Objects.equals(fA.getUserData(), "Player")) {
            System.out.println("Interacted with " + fB.getUserData() + "with body: " + fB.getBody());
            System.out.println("Added to list!");
            player.interactedList.add(fB.getBody());
        }
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

        player.interactedFixture = null;
        player.interactedList.clear();
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
