package com.mygdx.game.controller;

import com.mygdx.game.model.Task;

import java.util.ArrayList;

public class TaskController {
    private final ArrayList<Task> tasks = new ArrayList<Task>();

    public TaskController(){
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public boolean isTask(String taskName){
        for(Task task : tasks){
            if(task.name.equals(taskName)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
