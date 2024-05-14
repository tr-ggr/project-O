// TaskTimer.java
package com.mygdx.game.model;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;


import java.util.Observable;

import static com.mygdx.game.MyGdxGame.world;
import static com.mygdx.game.utils.Constants.PPM;

public class TaskTimer extends Observable implements Runnable{
    private final int time;
    public boolean isPaused = false;
    public int timeLeft;

    public TaskTimer(int time){
        this.time = time;
    }

    @Override
    public void run() {
        for(int i = 0; i < time; i++) {
            timeLeft = time - (i);
            synchronized(this){
                while(isPaused){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            try {
                Thread.sleep(1000); // Sleep for one second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println(timeLeft + " seconds left!");
        }
//        System.out.println("Task completed!");
        // Notify observers that the task is completed
        setChanged();
        notifyObservers();
    }


}