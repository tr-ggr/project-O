// Task.java
package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.mygdx.game.screens.GameplayScreen.*;
import static com.mygdx.game.utils.Constants.PPM;
import static java.lang.Thread.sleep;

public class Task implements PropertyChangeListener {
    public String name;
    public TaskTimer runnableTimer;
    public Thread thread;
    public int time;
    public Rectangle body;

    public String key;
    public boolean isEnabled;
    public String foodName;

    public Sprite sprite;

    public Animation<TextureRegion>  countdownAnimation;

    float stateTime = 0f;

    public Task(String name, String foodName, int time, Rectangle body){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.time = time;
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.key = null;
        this.isEnabled = true;
        this.foodName = foodName;
        this.runnableTimer.addPropertyChangeListener(this);

        if(time == 5) {
            countdownAnimation = textureAssetManager.countdownAnimations[2];
        } else if(time == 3) {
            countdownAnimation = textureAssetManager.countdownAnimations[1];
        } else if(time == 2) {
            countdownAnimation = textureAssetManager.countdownAnimations[0];
        }
    }

    public Task(String name, String foodName, int time, Rectangle body, String key){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.time = time;
        this.key = key;
        this.isEnabled = false;
        this.foodName = foodName;
        this.runnableTimer.addPropertyChangeListener(this);
        this.sprite = new Sprite(textureAssetManager.getTexture(key));
        sprite.setPosition((body.x) + (body.getWidth() / 2),
                (body.y));

        if(time == 5) {
            countdownAnimation = textureAssetManager.countdownAnimations[2];
        } else if(time == 3) {
            countdownAnimation = textureAssetManager.countdownAnimations[1];
        } else if(time == 2) {
            countdownAnimation = textureAssetManager.countdownAnimations[0];
        }


    }



    public void interactTimer(){
//        System.out.println(thread.getState());
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
//            System.out.println("Task is paused!");
        }
    }

    public void cancelTimer() throws InterruptedException {
        if(thread.isAlive()) {
            thread.interrupt();
//            System.out.println("Task is abruptly stopped!");
        }
    }



    public void drawTask(SpriteBatch batch){
        TextureRegion currentFrame;

        float x = this.body.x + this.body.width / 2 - 16;
        float y = this.body.y  + this.body.height ;

        if(isEnabled && key != null) sprite.draw(batch);

        int frameIndex = (int) (runnableTimer.timeLeft / (float) time * countdownAnimation.getKeyFrames().length);
        frameIndex = Math.min(frameIndex, countdownAnimation.getKeyFrames().length - 1); // Ensure the index is within bounds


        if(thread.getState().equals(Thread.State.TERMINATED)) {
            thread = new Thread(runnableTimer);

            currentFrame = countdownAnimation.getKeyFrames()[(time - runnableTimer.timeLeft)];
            batch.draw(currentFrame, x, y);
        } else if(thread.getState().equals(Thread.State.WAITING) || thread.getState().equals(Thread.State.TIMED_WAITING)){
            currentFrame = countdownAnimation.getKeyFrames()[(time - runnableTimer.timeLeft)];
            batch.draw(currentFrame, x, y);
        } else if (isEnabled){
            batch.draw(textureAssetManager.getTexture("SB_CKey"), x, y);
        } else {
            batch.draw(textureAssetManager.getTexture(key + "SB"), x, y);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("completed".equals(evt.getPropertyName())) {
            System.out.println("Task " + name + " is done!");
            generateFood(body, foodName, textureAssetManager.getTexture(foodName));
            System.out.println("Successfully generated food!");
            if(key != null) isEnabled = false;
        }
    }
}