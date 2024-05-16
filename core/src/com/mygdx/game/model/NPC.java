package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.MyGdxGame.font;
import static com.mygdx.game.MyGdxGame.foodController;

public class NPC {
    public String id;
    private ArrayList<String> requirements = new ArrayList<>();
    public float maxTime;
    public float currentTime;

    public NPC(String id, float maxTime){
        this.id = id;
        this.maxTime = maxTime;
        this.currentTime = maxTime;
        generateRequirements(1);
        System.out.println("NPC " + id + " has been created with requirements: " + requirements.toString());
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public void generateRequirements(int n){
        Random rand = new Random();
        for(int i = 0; i < n; i++){
            requirements.add(foodController.getFoodList().get(rand.nextInt(foodController.getFoodList().size())));
        }
    }

    public boolean updateTimer(float delta){
        currentTime -= delta;
        if(currentTime <= 0){
            System.out.println("NPC " + id + " has run out of time");
            return true;
        }
        return false;
    }

    public boolean checkRequirements(String food){
        if(requirements.contains(food)){
            System.out.println("NPC " + id + " has been satisfied");
            return true;
        }
        return false;
    }

    public ArrayList<String> getRequirements() {
        return requirements;
    }

    public void draw(Batch batch, float textY){
        font.draw(batch, "NPC ID: " + id + ", Requirements: " + requirements + " Current Time Left: " + currentTime, -200, textY);
    }

}
