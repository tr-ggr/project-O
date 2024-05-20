package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Task;

import java.util.Objects;

import static com.mygdx.game.screens.GameplayScreen.*;

public class CollisionListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        player.interactedFixture = fB;

        System.out.println(fA + " Collides with " + fB);
        System.out.println(fA.getUserData() + " Collides with " + fB.getUserData());

        checkFixtures(fA, fB);
        checkFixtures(fB, fA);

        // If the interactedList is not empty or the fixture is null or its userData is null, return
        if (Objects.equals(fB.getUserData(), null) || grabFood != null) {
//            System.out.println("Hands is full!");
            if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//                System.out.println("Food dropped to sensor!");
                deleteJoint = true;
                toBeDeleted.add(fB.getBody());
            }
        } else if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//            System.out.println("Food dropped to sensor!");
            toBeDeleted.add(fB.getBody());
        } else if (foodController.isFood((String) fB.getUserData()) && Objects.equals(fA.getUserData(), "Player")) {
//            System.out.println("Interacted with " + fB.getUserData() + "with body: " + fB.getBody());
//            System.out.println("Added to list!");
            player.interactedList.add(fB.getBody());
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureB().getUserData() == null) {
//            System.out.println("Removed!");
        } else {
            for (Task task : taskController.getTasks()) {
                if (Objects.equals(contact.getFixtureB().getUserData().toString(), task.name)) {
                    task.pauseTimer();
//                    System.out.println("Removed " + task.name + "!");
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

    private void checkFixtures(Fixture fixture1, Fixture fixture2) {
        if (foodController.isFood((String) fixture1.getUserData()) && taskController.isTask((String) fixture2.getUserData()) && player.interactedFood != null) {
//            System.out.println("Keying in!");
            for (Task task : taskController.getTasks()) {
                if (Objects.equals(fixture2.getUserData().toString(), task.name)) {
                    if (Objects.equals(fixture1.getUserData().toString(), task.key) && !task.isEnabled) {
//                        System.out.println("Key in successful with " + task.key + "!");
                        deleteJoint = true;
                        toBeDeleted.add(fixture1.getBody());
                        task.isEnabled = true;
                        break;
                    } else {
//                        System.out.println("Key in failed with " + task.key + "!");
                        break;
                    }
                }
            }
        }

    }
}
