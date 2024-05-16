package com.mygdx.game.controller;

import java.util.ArrayList;

public class FoodController {
    private final ArrayList<String> foodList = new ArrayList<>();

    public FoodController(){
        foodList.add("Bread");
        foodList.add("Spoon");
    }

    public boolean isFood(String food){
        if(foodList.contains(food)){
            System.out.println(food + " is food");
            return true;
        } else {
            System.out.println(food + " is not food");
            return false;
        }
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }




}
