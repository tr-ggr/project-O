package com.mygdx.game.controller;

import com.mygdx.game.model.NPC;

import java.util.ArrayList;
import java.util.Iterator;


public class GameController {
    ArrayList<NPC> npcs = new ArrayList<>();
    public int lives;
    public int moneyEarned;

    public GameController(){
        lives = 3;
        moneyEarned = 0;
    }

    public void generateNPC(){
        System.out.println("Generated an NPC!");
        npcs.add(new NPC(npcs.size() + 1 + "", 20));
    }

    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public void updateTimer(float delta){
        for(Iterator<NPC> it = npcs.iterator() ; it.hasNext();){
            NPC npc = it.next();
            if(npc.updateTimer(delta)){
                lives--;

                it.remove();
            };
        }
    }

    public boolean checkRequirements(String food){
        for(Iterator<NPC> it = npcs.iterator() ; it.hasNext();){
            NPC npc = it.next();
            if(npc.checkRequirements(food)){
                System.out.println("NPC " + npc.id + " has been satisfied");
                moneyEarned += (int) (10 * (npc.currentTime / npc.maxTime));
                it.remove();
                return  true;
            }
        }
        return  false;
    }




}
