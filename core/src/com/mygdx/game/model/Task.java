// Task.java
package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

import static com.mygdx.game.utils.Constants.PPM;

public class Task {
    public String name;
    public TaskTimer runnableTimer;
    public Thread thread;
    public int time;
    public Rectangle body;
    public BitmapFont font = new BitmapFont();


    public Task(String name, int time, Rectangle body){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
        this.body = body;
    }

    public void interactTimer(){
        if(thread.getState().equals(Thread.State.TERMINATED)) {
            runnableTimer = new TaskTimer(time);
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
        } else if(thread.getState().equals(Thread.State.WAITING)){
            font.draw(batch, runnableTimer.timeLeft + " left", x, y);
        } else if(thread.getState().equals(Thread.State.TIMED_WAITING)){
            font.draw(batch, runnableTimer.timeLeft + " left", x, y);
        }
    }
}