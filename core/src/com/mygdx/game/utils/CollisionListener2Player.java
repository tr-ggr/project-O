package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Task;

import java.util.Objects;

import static com.mygdx.game.screens.GameplayScreen.*;

public class CollisionListener2Player implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if (fA.getUserData() == null || fB.getUserData() == null) {
            return;
        }

        if (fA.getUserData().equals("Player1")) {
            player.interactedFixture = fB;
        } else {
            player2.interactedFixture = fB;
        }

        System.out.println(fA.getBody() + " Collides with " + fB.getBody());
        System.out.println(fA.getUserData() + " Collides with " + fB.getUserData());
//
        checkFixtures(fA, fB);
        checkFixtures(fB, fA);


        if (player.interactedFood == fB.getBody() || fA.getUserData().equals("Player1")) {
            // If the interactedList is not empty or the fixture is null or its userData is null, return
            if (Objects.equals(fB.getUserData(), null) || player.grabFood != null) {
//            System.out.println("Hands is full!");
                if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//                System.out.println("Food dropped to sensor!");
                    System.out.println("User data: " + fA.getUserData());
                    player.interactedFood = null;
                    player.interactedList.clear();
                    player.deleteJoint = true;
                    player.isGrabbing = false;
                    toBeDeleted.add(fB.getBody());
                }
            } else if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//            System.out.println("Food dropped to sensor!");
                toBeDeleted.add(fB.getBody());
            } else if (foodController.isFood((String) fB.getUserData()) && Objects.equals(fA.getUserData(), "Player1")) {
//            System.out.println("Interacted with " + fB.getUserData() + "with body: " + fB.getBody());
//            System.out.println("Added to list!");
                player.interactedList.add(fB.getBody());
            }
        } else {
            // If the interactedList is not empty or the fixture is null or its userData is null, return
            if (Objects.equals(fB.getUserData(), null) || player2.grabFood != null) {
//            System.out.println("Hands is full!");
                if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//                System.out.println("Food dropped to sensor!");
                    System.out.println("User data: " + fA.getUserData());
                    player2.interactedFood = null;
                    player2.interactedList.clear();
                    player2.deleteJoint = true;
                    player2.isGrabbing = false;
                    toBeDeleted.add(fB.getBody());
                }
            } else if (Objects.equals(fA.getUserData(), "Sensor") && gameController.checkRequirements((String) fB.getUserData())) {
//            System.out.println("Food dropped to sensor!");
                toBeDeleted.add(fB.getBody());
            } else if (foodController.isFood((String) fB.getUserData()) && Objects.equals(fA.getUserData(), "Player2")) {
//            System.out.println("Interacted with " + fB.getUserData() + "with body: " + fB.getBody());
//            System.out.println("Added to list!");
                player2.interactedList.add(fB.getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA().getUserData() == null || contact.getFixtureB().getUserData() == null) {
            return;
        }

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

        if (contact.getFixtureA().getUserData().equals("Player1")) {
            player.interactedFixture = null;
            player.interactedList.clear();
        } else {
            player2.interactedFixture = null;
            player2.interactedList.clear();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

    private void checkFixtures(Fixture fixture1, Fixture fixture2) {
        if (fixture1 == null || fixture2 == null) {
            return;
        }

        String userData1 = (String) fixture1.getUserData();
        String userData2 = (String) fixture2.getUserData();

        if (userData1 == null || userData2 == null) {
            return;
        }




        if (fixture1.getBody() == player.interactedFood || fixture2.getBody() == player.interactedFood) {
            System.out.println("Keying in as Player1");
            System.out.println(fixture1.getUserData() + " + " + fixture2.getUserData());
            System.out.println("Player1 interactedFood: " + player.interactedFood);
            if (foodController.isFood((String) fixture1.getUserData()) && taskController.isTask((String) fixture2.getUserData()) && player.interactedFood != null) {
                System.out.println("Keying in!");
                for (Task task : taskController.getTasks()) {
                    if (Objects.equals(fixture2.getUserData().toString(), task.name)) {
                        if (Objects.equals(fixture1.getUserData().toString(), task.key) && !task.isEnabled) {
                            System.out.println("Key in successful with " + task.key + "!");
                            player.deleteJoint = true;
                            toBeDeleted.add(fixture1.getBody());
                            task.isEnabled = true;
                            player.interactedFood = null;
                            return;
                        } else {
                            System.out.println("Key in failed with " + task.key + "!");
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Keying in as Player2");
            System.out.println(fixture1.getUserData() + " + " + fixture2.getUserData());
            System.out.println("Player2 interactedFood: " + player2.interactedFood);
            if (foodController.isFood((String) fixture1.getUserData()) && taskController.isTask((String) fixture2.getUserData()) && player2.interactedFood != null) {
                System.out.println("Keying in!");
                for (Task task : taskController.getTasks()) {
                    if (Objects.equals(fixture2.getUserData().toString(), task.name)) {
                        if (Objects.equals(fixture1.getUserData().toString(), task.key) && !task.isEnabled) {
                            System.out.println("Key in successful with " + task.key + "!");
                            player2.deleteJoint = true;
                            toBeDeleted.add(fixture1.getBody());
                            task.isEnabled = true;
                            player2.interactedFood = null;
                            break;
                        } else {
                            System.out.println("Key in failed with " + task.key + "!");
                            break;
                        }
                    }
                }
            }

        }
    }

}

