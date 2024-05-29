package com.mygdx.game.controller;

import java.util.ArrayList;

public class FoodController {
    private final ArrayList<String> foodList = new ArrayList<>();

    public FoodController(){
        foodList.add("LessonDraft");
        foodList.add("Pencil");
        foodList.add("Post-it");
        foodList.add("Booklet");
        foodList.add("MoM");
        foodList.add("Paper");
        foodList.add("GradedPaper");
    }

    public boolean isFood(String food){
        if(foodList.contains(food)){
//            System.out.println(food + " is food");
            return true;
        } else {
//            System.out.println(food + " is not food");
            return false;
        }
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }




}
