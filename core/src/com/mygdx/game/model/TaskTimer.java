// TaskTimer.java
package com.mygdx.game.model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TaskTimer implements Runnable{
    private final int time;
    public boolean isPaused = false;
    public int timeLeft;
    private PropertyChangeSupport support;

    public TaskTimer(int time){
        this.time = time;
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
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
//            System.out.println(timeLeft + " seconds left!");
        }
//        System.out.println("Task completed!");
        // Notify observers that the task is completed
        support.firePropertyChange("completed", null, this);
    }


}