// Task.java
package com.mygdx.game.model;

public class Task {
    public String name;
    public TaskTimer runnableTimer;
    public Thread thread;
    public int time;

    public Task(String name, int time){
        this.name = name;
        this.runnableTimer = new TaskTimer(time);
        this.thread = new Thread(runnableTimer);
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
}