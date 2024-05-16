// Task.java
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;


import java.util.Observable;
import java.util.Observer;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.utils.Constants.PPM;
import static java.lang.Thread.sleep;

public class Task implements Observer {
    public String name;
    public TaskTimer runnableTimer;
    public Thread thread;
    public int time;
    public Rectangle body;

    public String key;
    public boolean isEnabled;
    public String foodName;

    public Task(String name, String foodName, int time, Rectangle body){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.runnableTimer.addObserver(this);
        this.key = null;
        this.isEnabled = true;
        this.foodName = foodName;
    }

    public Task(String name, String foodName, int time, Rectangle body, String key){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.key = key;
        this.isEnabled = false;
        this.foodName = foodName;
        this.runnableTimer.addObserver(this);
    }

    public void interactTimer(){
        System.out.println(thread.getState());
        if(thread.getState().equals(Thread.State.TERMINATED)) {
            thread = new Thread(runnableTimer);
        }
        if(thread.getState().equals(Thread.State.NEW)) thread.start();
        if(thread.getState().equals(Thread.State.WAITING)){
            synchronized (runnableTimer) {
                runnableTimer.isPaused = false;
                runnableTimer.notify();
            }
        }
    };

    public void pauseTimer() {
        if(thread.isAlive()) {
            synchronized (runnableTimer) {
                runnableTimer.isPaused = true;
            }
            System.out.println("Task is paused!");
        }
    }

    public void cancelTimer() throws InterruptedException {
        if(thread.isAlive()) {
            thread.interrupt();
            System.out.println("Task is abruptly stopped!");
        }
    }

    public void drawTask(SpriteBatch batch){
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        float x = this.body.x + this.body.width / 2 - 20;
        float y = this.body.y  + this.body.height + 20;
//        System.out.println("Drawing task + " + this.body.getPosition().x);

        if(thread.getState().equals(Thread.State.TERMINATED)) {
            font.draw(batch, "Task completed!", x, y);
            thread = new Thread(runnableTimer);
        } else if(thread.getState().equals(Thread.State.WAITING)){
            font.draw(batch, runnableTimer.timeLeft + " left", x, y);
        } else if(thread.getState().equals(Thread.State.TIMED_WAITING)){
            font.draw(batch, runnableTimer.timeLeft + " left", x, y);
        } else if (isEnabled){
            font.draw(batch, "Press SPACE to interact", x, y);
        } else {
            font.draw(batch, "Task is not enabled", x, y);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof TaskTimer) {
            System.out.println("Task " + name + " is done!");
            generateFood(body, foodName, textureAssetManager.getTexture(foodName));
            if(key != null) isEnabled = false;
        }
    }
}