package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Application;
import com.mygdx.game.actor.CherryUI;
import com.mygdx.game.actor.LifeUI;
import com.mygdx.game.actor.TaskCard;
import com.mygdx.game.model.NPC;
import dev.lyze.flexbox.FlexBox;
import io.github.orioncraftmc.meditate.YogaNode;
import io.github.orioncraftmc.meditate.enums.YogaEdge;
import io.github.orioncraftmc.meditate.enums.YogaFlexDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GameController {
    public Stage stageHUD;
    private Image hudImage;

    ArrayList<NPC> npcs = new ArrayList<>();
    private final ArrayList<TaskCard> npcCards = new ArrayList<TaskCard>();

    public int lives;
    public int moneyEarned;

    public float timeSeconds = 0f;
    public float spawnRate = 30f;

    public float timePassed = 0f;

    public PHASE phase = PHASE.PHASE1;

    private Random random = new Random();

    public boolean firstSpawn = true;

    private boolean hasCherry = false;

    private NPC Cherry = null;

    public enum PHASE {
        PHASE1, PHASE2, PHASE3;
    }

    private Table flexBox;

    private CherryUI cherryUI;

    private LifeUI lifeUI;

    private Music dean_spawn;

    public GameController(final Application app){
        lives = 3;
        moneyEarned = 0;
        this.stageHUD = new Stage(new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT , app.camera));

        dean_spawn = Gdx.audio.newMusic(Gdx.files.internal("sfx/dean_spawn.mp3"));

        cherryUI = new CherryUI();
        cherryUI.setVisible(false);
        stageHUD.addActor(cherryUI);

        Texture hudTexture = new Texture(Gdx.files.internal("overtimeHUD.png"));
        hudImage = new Image(hudTexture);
        stageHUD.addActor(hudImage);


        Table root = new Table();
        root.setFillParent(true);
        stageHUD.addActor(root);

        flexBox = new Table();

        root.add(flexBox).top().expand().left().padLeft(50);

        lifeUI = new LifeUI(lives, stageHUD);

//        stageHUD.setDebugAll(true);





    }

    public void generateNPC(){
        NPC curr = null;
        TaskCard card = null;
//
//        if(!hasCherry){
//            dean_spawn.play();
//            Cherry = new NPC("Cherry", 40, 3);
//            System.out.println("Generated Ma'am Dean NPC!");
//            hasCherry = true;
//            cherryUI.setCherry(Cherry);
//            cherryUI.setVisible(true);
//
//        }






//        npcs.add(new NPC("Cherry", 40, 3));
        if(phase == PHASE.PHASE3){
            if(random.nextInt(2) == 1 && !hasCherry) {
                Cherry = new NPC("Cherry", 40, 3);
                System.out.println("Generated Ma'am Dean NPC!");
                hasCherry = true;
                cherryUI.setCherry(Cherry);
                cherryUI.setVisible(true);
                dean_spawn.play();
            } else {
                System.out.println("Generated a normal NPC!");
                curr = new NPC(npcs.size() + 1 + "", 25, 1);
                card = new TaskCard(curr.getRequirements().get(0), curr.maxTime);
                npcs.add(curr);
                npcCards.add(card);
                flexBox.add(card).space(10);
            }
        } else if (phase == PHASE.PHASE1){
            System.out.println("Generated a normal NPC!");
            curr = new NPC(npcs.size() + 1 + "", 25, 1);
            card = new TaskCard(curr.getRequirements().get(0), curr.maxTime);
            npcs.add(curr);
            npcCards.add(card);
            flexBox.add(card).space(10);
        } else {
            if(random.nextInt(2) == 1 && !hasCherry) {
                Cherry = new NPC("Cherry", 40, 2);
                System.out.println("Generated Ma'am Dean NPC!");
                hasCherry = true;
                cherryUI.setCherry(Cherry);
                cherryUI.setVisible(true);
                dean_spawn.play();
            } else {
                System.out.println("Generated a normal NPC!");
                curr = new NPC(npcs.size() + 1 + "", 25, 1);
                card = new TaskCard(curr.getRequirements().get(0), curr.maxTime);
                npcs.add(curr);
                npcCards.add(card);
                flexBox.add(card).space(10);
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

        updateHUD(delta);
        checkPhase();
        updateTimer(delta);
        cherryUI.update();
        lifeUI.update(lives);
    }

    public void updateHUD(float delta){
        for (TaskCard card : npcCards) {
            card.updateProgress(delta);
        }
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
        for(Iterator<NPC> it = npcs.listIterator(); it.hasNext();){
            NPC npc = it.next();
            if(npc.updateTimer(delta)){
                removeActor(flexBox, npcs.indexOf(npc));
                it.remove();
                lives--;
            };
        }

        if(hasCherry && Cherry.updateTimer(delta)){
            hasCherry = false;
            cherryUI.setVisible(false);
            lives--;
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

        if(hasCherry && Cherry.checkRequirements(food)){
            moneyEarned += (Math.round(5 * (Cherry.currentTime / Cherry.maxTime) ) * phaseMultiplier);
            if(Cherry.getRequirements().isEmpty()){
                hasCherry = false;
                cherryUI.setVisible(false);
            }
            return true;
        }

        for(Iterator<NPC> it = npcs.iterator() ; it.hasNext();){
            NPC npc = it.next();
            if(npc.checkRequirements(food)){
                moneyEarned += (Math.round( 5 * (npc.currentTime / npc.maxTime) ) * phaseMultiplier);
                if(npc.getRequirements().isEmpty()){
                    removeActor(flexBox, npcs.indexOf(npc));
                    it.remove();
                }
                return true;
            }
        }

        return  false;
    }

    public void renderHUD(){
        stageHUD.draw();
    }

    public void removeActor(Table container, int position) {
        container.clearChildren();
        npcCards.remove(position);

        for (TaskCard npcCard : npcCards) {
            container.add(npcCard).space(10);
        }
    }




}
