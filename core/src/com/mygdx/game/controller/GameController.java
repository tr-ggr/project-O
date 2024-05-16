package com.mygdx.game.controller;

import com.mygdx.game.model.NPC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GameController {
    ArrayList<NPC> npcs = new ArrayList<>();
    
    public int lives;
    public int moneyEarned;

    public float timeSeconds = 0f;
    public float spawnRate = 30f;

    public float timePassed = 0f;

    public PHASE phase = PHASE.PHASE1;

    private Random random = new Random();

    public boolean firstSpawn = true;

    public enum PHASE {
        PHASE1, PHASE2, PHASE3;
    }

    public GameController(){
        lives = 3;
        moneyEarned = 0;
    }

    public void generateNPC(){
        if(phase == PHASE.PHASE3){
            if(random.nextInt(2) == 1 && !npcs.contains(new NPC("Cherry", 40, 3))) {
                System.out.println("Generated Ma'am Dean NPC!");
                npcs.add(new NPC("Cherry", 40, 3));
            } else {
                System.out.println("Generated a normal NPC!");
                npcs.add(new NPC(npcs.size() + 1 + "", 25, 1));
            }
        } else if (phase == PHASE.PHASE1){
            System.out.println("Generated an NPC!");
            npcs.add(new NPC(npcs.size() + 1 + "", 25, 1));
        } else {
            if(random.nextInt(2) == 1 && !npcs.contains(new NPC("Cherry", 40, 2))) {
                System.out.println("Generated Ma'am Dean NPC!");
                npcs.add(new NPC("Cherry", 40, 3));
            } else {
                System.out.println("Generated a normal NPC!");
                npcs.add(new NPC(npcs.size() + 1 + "", 25, 1));
            }
        }
    }

    public void update(float delta){
        if(timePassed >= 3 && firstSpawn){
            generateNPC();
            firstSpawn = false;
        }

        timeSeconds += delta;
        timePassed += delta;
        if(timeSeconds >= spawnRate){
            generateNPC();
            timeSeconds = 0f;
        }

        if(npcs.isEmpty() && timeSeconds >= 10f){
            generateNPC();
            timeSeconds = 0f;
        }
        checkPhase();
        updateTimer(delta);
    }

    private void checkPhase(){
        if(timePassed >= 70 && phase == PHASE.PHASE1){
            phase = PHASE.PHASE2;
            spawnRate = 20f;
        } else if(timePassed >= 150 && phase == PHASE.PHASE2){
            phase = PHASE.PHASE3;
            spawnRate = 20f;
        }
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
        int phaseMultiplier;
        switch (phase) {
            case PHASE2:
                phaseMultiplier = 2;
                break;
            case PHASE3:
                phaseMultiplier = 3;
                break;
            default:
                phaseMultiplier = 1;
                break;
        }

        for(Iterator<NPC> it = npcs.iterator() ; it.hasNext();){
            NPC npc = it.next();
            if(npc.checkRequirements(food)){
                moneyEarned += (Math.round(npc.currentTime / npc.maxTime) * phaseMultiplier);
                if(npc.getRequirements().isEmpty()){
                    it.remove();
                }
                return true;
            }
        }
        return  false;
    }




}
