package com.mygdx.game.screens;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.mygdx.game.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.controller.FoodController;
import com.mygdx.game.controller.GameController;
import com.mygdx.game.controller.TaskController;
import com.mygdx.game.controller.XboxControllerListener;
import com.mygdx.game.database.CurrentUser;
import com.mygdx.game.database.DatabaseHelper;
import com.mygdx.game.model.*;
import com.mygdx.game.utils.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.mygdx.game.controller.GameController.controller;
import static com.mygdx.game.utils.Constants.PPM;


public class GameplayScreen implements Screen {
    private final Application app;

    private Image hudImage;

    private boolean isTransitioning;

    //GAME STUFF
    private boolean DEBUG = false;
    private final float SCALE = 2.0f;

    public static World world;

    private Box2DDebugRenderer b2dr;

    public SpriteBatch batch, hudBatch;
    public OrthographicCamera camera;

    private Texture texture;
    public static Texture breadTexture;
    private OrthogonalTiledMapRenderer tmr;
    private TiledMap map;

    public static boolean isMultiplayer;


    private Food food, food2;

    public static Player player, player2;

    public static ArrayList<Food> foods = new ArrayList<Food>();
    public static ArrayList<Food> foodsToBeAdded = new ArrayList<Food>();

    private DropOff dropOff;
    private JointDef jointDef;

    public static BitmapFont font;

    public static ArrayList<Body> toBeDeleted = new ArrayList<Body>();
    public static ArrayList<Joint> jointDeleted = new ArrayList<Joint>();


    public static FoodController foodController = new FoodController();
    public static TaskController taskController = new TaskController();
    public static GameController gameController;
    public static TextureAssetManager textureAssetManager;

    public GameplayScreen(final Application app, boolean isMultiplayer) {
        this.app = app;
        this.isMultiplayer = isMultiplayer;


        gameController = new GameController(app);


//        Texture hudTexture = new Texture(Gdx.files.internal("overtimeHUD.png"));



//        Controller controller = Controllers.getControllers().first(); // Get the first controller
//        XboxControllerListener listener = new XboxControllerListener();
//        controller.addListener(listener);

//        System.out.println("Controller: " + controller.getName() + " is connected!");
        Gdx.input.setInputProcessor(gameController.stageHUD);

        //GAME STUFF
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        textureAssetManager = new TextureAssetManager();
        breadTexture = textureAssetManager.getTexture("Bread");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w / SCALE, h / SCALE);

        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();

        if (isMultiplayer){
            world.setContactListener(new CollisionListener2Player());
        } else {
            world.setContactListener(new CollisionListener());
        }


        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();

//        map = new TmxMapLoader().load("map.tmx");
        map = new TmxMapLoader().load("map/OvertimeMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);

        player = new Player(world, map, true);
        if(isMultiplayer){
            player2 = new Player(world, map, false);
        }

        dropOff = new DropOff(world, map);

//		Vector3 pos = camera.position;
//		pos.x = 5000 * PPM;
//		pos.y = player.getPosition().y * PPM;
//		camera.position.set(768, 817389123, 0);

        camera.update();

        TiledObjectUtil.parseTaskObjects(world, map.getLayers().get("Tasks").getObjects());
        TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("Border").getObjects());

        jointDef = new WeldJointDef();

        font = new BitmapFont();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        // Clear the screen
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isTransitioning){
            if (gameController.stageHUD.getRoot().getColor().a >= 1){
                isTransitioning = false;
            }
            gameController.stageHUD.getRoot().getColor().a += v;
        }


//        b2dr.render(world, camera.combined.scl(PPM));

        //Execute handleEvent each 1 second
        gameController.update(Gdx.graphics.getDeltaTime());

        update(Gdx.graphics.getDeltaTime());



        //Render Assets
        // Render the game world
        tmr.render();

        // Render the player and foods
        batch.begin();
        player.draw(batch);
        if(isMultiplayer){
            player2.draw(batch);
        }

        for (Food food : foods) {
            if (!food.isDeleted) {
                food.draw(batch);
            }
        }

        batch.end();

        // Render the tasks
        batch.begin();
        for (Task task : taskController.getTasks()) {
            task.drawTask(batch);
        }
        batch.end();

        gameController.renderHUD();

        // Render the game stats
        app.batch.begin();
        app.font.draw(app.batch, gameController.moneyEarned + "", 80 , 60);
        app.font.draw(app.batch, (int)gameController.timePassed + "", 500 , 60);
        app.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if(gameController.lives == 0){
            System.out.println("Game Over!");
            System.out.println("Money earned: " + gameController.moneyEarned);
            System.out.println("Time passed: " + gameController.timePassed);



            app.setScreen(new GameOverScreen(app, (int)gameController.timePassed, gameController.moneyEarned));
        }


//        super.render();
    }



    @Override
    public void resize(int i, int i1) {
        gameController.stageHUD.getViewport().update(i, i1, true);

        camera.setToOrtho(false, i / 2, i1 / 2);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        texture.dispose();
        tmr.dispose();
        map.dispose();
    }

    public void grabFood(){
        if(!player.isGrabbing || player.interactedList.isEmpty()) {
            return;
        } else {
            Body bodyA = player.body, bodyB = player.interactedList.get(0);
            player.interactedFood = bodyB;
            Vector2 worldCoordsAnchorPoint = bodyA.getWorldCenter();

            if(player.facingDirection == FacingDirection.LEFT){
                bodyB.setTransform(bodyA.getPosition().x - 0.5f , bodyA.getPosition().y, 0);
            } else if(player.facingDirection == FacingDirection.RIGHT){
                bodyB.setTransform(bodyA.getPosition().x + 0.5f , bodyA.getPosition().y, 0);
            } else if(player.facingDirection == FacingDirection.UP){
                bodyB.setTransform(bodyA.getPosition().x, bodyA.getPosition().y + 0.3f, 0);
            } else if(player.facingDirection == FacingDirection.DOWN){
                bodyB.setTransform(bodyA.getPosition().x, bodyA.getPosition().y - 0.3f, 0);
            }

            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.bodyA = bodyA;
            weldJointDef.bodyB = bodyB;
            weldJointDef.localAnchorA.set(weldJointDef.bodyA.getLocalPoint(worldCoordsAnchorPoint));
            weldJointDef.localAnchorB.set(weldJointDef.bodyB.getLocalPoint(worldCoordsAnchorPoint));
            weldJointDef.referenceAngle = weldJointDef.bodyB.getAngle() - weldJointDef.bodyA.getAngle();

            player.grabFood = world.createJoint(weldJointDef);
        }


    }

    public void grabFood2(){
        if (isMultiplayer){
            // For player2
            if(!player2.isGrabbing || player2.interactedList.isEmpty()) {
                return; // Don't grab the food if the player is not grabbing
            }

            Body bodyA2 = player2.body, bodyB2 = player2.interactedList.get(0);
            player2.interactedFood = bodyB2;
            Vector2 worldCoordsAnchorPoint2 = bodyA2.getWorldCenter();

            if(player2.facingDirection == FacingDirection.LEFT){
                bodyB2.setTransform(bodyA2.getPosition().x - 0.5f , bodyA2.getPosition().y, 0);
            } else if(player2.facingDirection == FacingDirection.RIGHT){
                bodyB2.setTransform(bodyA2.getPosition().x + 0.5f , bodyA2.getPosition().y, 0);
            } else if(player2.facingDirection == FacingDirection.UP){
                bodyB2.setTransform(bodyA2.getPosition().x, bodyA2.getPosition().y + 0.3f, 0);
            } else if(player2.facingDirection == FacingDirection.DOWN){
                bodyB2.setTransform(bodyA2.getPosition().x, bodyA2.getPosition().y - 0.3f, 0);
            }

            WeldJointDef weldJointDef2 = new WeldJointDef();
            weldJointDef2.bodyA = bodyA2;
            weldJointDef2.bodyB = bodyB2;
            weldJointDef2.localAnchorA.set(weldJointDef2.bodyA.getLocalPoint(worldCoordsAnchorPoint2));
            weldJointDef2.localAnchorB.set(weldJointDef2.bodyB.getLocalPoint(worldCoordsAnchorPoint2));
            weldJointDef2.referenceAngle = weldJointDef2.bodyB.getAngle() - weldJointDef2.bodyA.getAngle();

            player2.grabFood = world.createJoint(weldJointDef2);
        }
    }



    public void update(float delta) {
        world.step(1/60f, 6, 2);
        renderFood();
        grabFood();
        grabFood2();
        removeJoints();
//        destroyJoints();
        removeFood();
        destroyFood();
        gameController.stageHUD.act(delta);
        player.inputUpdate(delta);
        if(isMultiplayer){
            player2.inputUpdate(delta);
        }
        cameraUpdate(delta);
//		camera.update();
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);

    }

    public void cameraUpdate(float delta) {
        if(!isMultiplayer){
            Vector3 pos = camera.position;
            pos.x = player.body.getPosition().x * PPM;
            pos.y = player.body.getPosition().y * PPM;
            camera.position.set(pos);
            camera.zoom = 1f;
        } else {
            // Get the map's width and height in tiles
            int mapWidthInTiles = map.getProperties().get("width", Integer.class);
            int mapHeightInTiles = map.getProperties().get("height", Integer.class);

            // Get the tile size
            int tileSize = map.getProperties().get("tilewidth", Integer.class);

            // Calculate the map's width and height in pixels
            float mapWidthInPixels = mapWidthInTiles * tileSize;
            float mapHeightInPixels = mapHeightInTiles * tileSize;

            camera.position.set(mapWidthInPixels / 2, mapHeightInPixels / 2, 0);
            camera.zoom = 2f;
        }

        camera.update();
    }

    public static void generateFood(Rectangle body, String foodType, Texture texture){
        Gdx.app.log("Food", foodType + " generating...");
        System.out.println("Food: " + body + " is generating " + foodType);
        foodsToBeAdded.add(new Food(foodType, texture, (int) body.x, (int) body.y));
    }

    public static void renderFood(){
        for(Iterator<Food> it = foodsToBeAdded.iterator(); it.hasNext();){
            Food food = it.next();
            if(food.generateToWorld()){
                foods.add(food);
            }
        }
        foodsToBeAdded.clear();
    }

    public static void removeFood(){
        if(toBeDeleted.isEmpty()) return;
        System.out.println("Removing food!");

        for (Body body : toBeDeleted){
            if(!world.isLocked()){
                for(Food foods : foods){
                    if(foods.body == body){
                        foods.isDeleted = true;
                        break;
                    }
                }

                System.out.println("Food: " + body + " is deleted by the world!");
                world.destroyBody(body);
            }
        }

        toBeDeleted.clear();
    }

    public static void removeJoints(){
        if(player.deleteJoint && player.grabFood != null){
            System.out.println("Joint: 1" + player.grabFood + " is being deleted by the world!");

            if(!world.isLocked() && player.grabFood != null){
                System.out.println("Player 1 Joint trying to delete... with body" + player.body);
                if(isMultiplayer) System.out.println("Player 2 pair with body" + player2.body);
                Joint toBeDeleted = player.grabFood;
                player.grabFood = null;
                player.deleteJoint = false;
                player.interactedFood = null;
                player.interactedList.clear();
                System.out.println("Joint 1: " + toBeDeleted + " is being deleted by the world!");
                System.out.println(toBeDeleted.getBodyA() + " " + toBeDeleted.getBodyB());

                if (toBeDeleted.getBodyA() != null && toBeDeleted.getBodyB() != null && toBeDeleted.getBodyA() == player.body && player.interactedFood == null && !world.isLocked()){
                    System.out.println("Current interacted food: " + player.interactedFood);
                    world.destroyJoint(toBeDeleted);
                    System.out.println("Successfully Deleted Joint on Player 1! "  + toBeDeleted);
                }
            }
        }

        if(isMultiplayer){
            if(player2.deleteJoint && player2.grabFood != null){
                System.out.println("Joint 2: " + player2.grabFood + " is being deleted by the world!");

                if(!world.isLocked() && player2.grabFood != null){
                    System.out.println("Player 2 Joint trying to delete... with body" + player2.body);
                    System.out.println("Player 1 pair with body" + player.body);
                    Joint toBeDeleted = player2.grabFood;
                    player2.grabFood = null;
                    player2.deleteJoint = false;
                    player2.interactedFood = null;
                    player2.interactedList.clear();
                    System.out.println("Joint 2: " + toBeDeleted + " is being deleted by the world!");
                    System.out.println(toBeDeleted.getBodyA() + " " + toBeDeleted.getBodyB());
                    if (toBeDeleted.getBodyA() != null && toBeDeleted.getBodyB() != null && toBeDeleted.getBodyA() == player2.body && player2.interactedFood == null && !world.isLocked()){
                        System.out.println("Current interacted food: " + player2.interactedFood);
                        world.destroyJoint(toBeDeleted);
                    }
                }
            }
        }
    }

    public static void destroyFood(){
        for(Iterator<Food> it = foods.iterator(); it.hasNext();){
            Food food = it.next();
            if(food.isDeleted){
                System.out.println(food.name + " is submitted!");
                System.out.println("Food: " + food + " deleted on the world!");
                it.remove();
            }
        }
    }



}
