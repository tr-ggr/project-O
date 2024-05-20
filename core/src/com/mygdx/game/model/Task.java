// Task.java
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public Task(String name, String foodName, int time, Rectangle body){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.key = null;
        this.isEnabled = true;
        this.foodName = foodName;
        this.runnableTimer.addPropertyChangeListener(this);

    }

    public Task(String name, String foodName, int time, Rectangle body, String key){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
        this.key = key;
        this.isEnabled = false;
        this.foodName = foodName;
        this.runnableTimer.addPropertyChangeListener(this);
        this.sprite = new Sprite(textureAssetManager.getTexture(key));
        sprite.setPosition((body.x) + (body.getWidth() / 2),
                (body.y));
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
        font.setColor(0f, 0f, 0f, 1.0f);
        float x = this.body.x + this.body.width / 2 - 16;
        float y = this.body.y  + this.body.height ;
//        System.out.println("Drawing task + " + this.body.getPosition().x);

        if(isEnabled && key != null) sprite.draw(batch);

        if(thread.getState().equals(Thread.State.TERMINATED)) {
//            font.draw(batch, "Task completed!", x, y);
            thread = new Thread(runnableTimer);

            batch.draw(textureAssetManager.getTexture("SB_Check"), x, y);
        } else if(thread.getState().equals(Thread.State.WAITING)){
            batch.draw(textureAssetManager.getTexture("SpeechBubbleEmpty"), x, y);
            // Calculate the position for the font
            float fontX = x + (float) textureAssetManager.getTexture("SpeechBubbleEmpty").getWidth() / 2 - 5;
            float fontY = y + (float) textureAssetManager.getTexture("SpeechBubbleEmpty").getHeight() / 2 + 8;

            // Draw the font at the calculated position
            font.draw(batch, runnableTimer.timeLeft + "", fontX, fontY);
        } else if(thread.getState().equals(Thread.State.TIMED_WAITING)){
            batch.draw(textureAssetManager.getTexture("SpeechBubbleEmpty"), x, y);

            // Calculate the position for the font
            float fontX = x + (float) textureAssetManager.getTexture("SpeechBubbleEmpty").getWidth() / 2 - 5;
            float fontY = y + (float) textureAssetManager.getTexture("SpeechBubbleEmpty").getHeight() / 2 + 8;

            // Draw the font at the calculated position
            font.draw(batch, runnableTimer.timeLeft + "", fontX, fontY);
        } else if (isEnabled){
//            font.draw(batch, "Press SPACE to interact", x, y);
            batch.draw(textureAssetManager.getTexture("SB_CKey"), x, y);
        } else {
//            font.draw(batch, "Task is not enabled", x, y);
//            System.out.println(key);
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