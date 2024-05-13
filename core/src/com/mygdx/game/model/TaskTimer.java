// TaskTimer.java
package com.mygdx.game.model;

public class TaskTimer implements Runnable{
    private final int time;
    public boolean isPaused = false;

    public TaskTimer(int time){
        this.time = time;
    }

    @Override
    public void run() {
        for(int i = 0; i < time; i++) {
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
            System.out.println((time - (i)) + " seconds left!");
        }
        System.out.println("Task completed!");
    }
}