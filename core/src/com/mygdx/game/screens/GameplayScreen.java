package com.mygdx.game.screens;


import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.controller.FoodController;
import com.mygdx.game.controller.GameController;
import com.mygdx.game.controller.TaskController;
import com.mygdx.game.model.*;
import com.mygdx.game.utils.CollisionListener;
import com.mygdx.game.utils.DropOff;
import com.mygdx.game.utils.TextureAssetManager;
import com.mygdx.game.utils.TiledObjectUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mygdx.game.utils.Constants.PPM;


public class GameplayScreen implements Screen {
    private final Application app;

    private Image hudImage;

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

    public static Joint grabFood;

    private Food food, food2;

    public static Player player;

    public static ConcurrentLinkedQueue<Food> foods = new ConcurrentLinkedQueue<Food>();
    public static ArrayList<Food> foodsToBeAdded = new ArrayList<Food>();

    private DropOff dropOff;
    private JointDef jointDef;

    public static BitmapFont font;

    public static ArrayList<Body> toBeDeleted = new ArrayList<Body>();
    public static boolean deleteJoint = false;

    public static FoodController foodController = new FoodController();
    public static TaskController taskController = new TaskController();
    public static GameController gameController;
    public static TextureAssetManager textureAssetManager;

    public GameplayScreen(final Application app) {
        this.app = app;


        gameController = new GameController(app);


//        Texture hudTexture = new Texture(Gdx.files.internal("overtimeHUD.png"));



//        Gdx.input.setInputProcessor(stageHUD);

        //GAME STUFF
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        textureAssetManager = new TextureAssetManager();
        breadTexture = textureAssetManager.getTexture("Bread");

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w / SCALE, h / SCALE);

        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();
        world.setContactListener(new CollisionListener());

        player = new Player(world, 200, 200, false);

        dropOff = new DropOff(world, 50, 75, 32, 32);

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        texture = new Texture("download-compresskaru.com.png");

        map = new TmxMapLoader().load("map.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);


//		Vector3 pos = camera.position;
//		pos.x = 5000 * PPM;
//		pos.y = player.getPosition().y * PPM;
//		camera.position.set(768, 817389123, 0);

        camera.update();

        TiledObjectUtil.parseTaskObjects(world, map.getLayers().get("Testing").getObjects());
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
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the game world
        tmr.render();

        // Render the player and foods
        batch.begin();
        player.draw(batch);
        for (Food food : foods) {
            food.draw(batch);
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

        b2dr.render(world, camera.combined.scl(PPM));

        //Execute handleEvent each 1 second
        gameController.update(Gdx.graphics.getDeltaTime());

        renderFood();
        update(Gdx.graphics.getDeltaTime());

        renderFood();
        grabFood();
        removeJoints();
        removeFood();
        destroyFood();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if(gameController.lives == 0){
            System.out.println("Game Over!");
            System.out.println("Money earned: " + gameController.moneyEarned);
            Gdx.app.exit();
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
//		if(player.interactedList.isEmpty()){
//			if(!player.isGrabbing){
//				if(grabFood != null) {
//					world.destroyJoint(grabFood);
//					grabFood = null;
//				}
//			}
//			return;
//		}

        if(!player.isGrabbing || player.interactedList.isEmpty()) {
            return; // Don't grab the food if the player is not grabbing
        }

//		System.out.println("Grabbing food!");

        Body bodyA = player.body, bodyB = player.interactedList.get(0);

        player.interactedFood = bodyB;

        // Set the anchor point to the center of the player's body
        Vector2 worldCoordsAnchorPoint = bodyA.getWorldCenter();

//		System.out.println(bodyA.getAngle());
        if(player.facingDirection == FacingDirection.LEFT){
            bodyB.setTransform(bodyA.getPosition().x - 0.3f , bodyA.getPosition().y, 0);
        } else if(player.facingDirection == FacingDirection.RIGHT){
            bodyB.setTransform(bodyA.getPosition().x + 0.3f , bodyA.getPosition().y, 0);
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

        grabFood = world.createJoint(weldJointDef);
    }



    public void update(float delta) {
        world.step(1/60f, 6, 2);
        gameController.stageHUD.act(delta);
        player.inputUpdate(delta);
        cameraUpdate(delta);
//		camera.update();
        tmr.setView(camera);
        batch.setProjectionMatrix(camera.combined);

    }

    public void cameraUpdate(float delta) {
        Vector3 pos = camera.position;
        pos.x = player.body.getPosition().x * PPM;
        pos.y = player.body.getPosition().y * PPM;
        camera.position.set(pos);

//		// Get the map's width and height in tiles
//		int mapWidthInTiles = map.getProperties().get("width", Integer.class);
//		int mapHeightInTiles = map.getProperties().get("height", Integer.class);
//
//		// Get the tile size
//		int tileSize = map.getProperties().get("tilewidth", Integer.class);
//
//		// Calculate the map's width and height in pixels
//		float mapWidthInPixels = mapWidthInTiles * tileSize;
//		float mapHeightInPixels = mapHeightInTiles * tileSize;
//
//		camera.position.set(mapWidthInPixels / 2, mapHeightInPixels / 2, 0);
		camera.zoom = 1f;

        camera.update();
    }

    public Body createBox(int x, int y, int w, int h, boolean isStatic){
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = (isStatic) ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;


        PolygonShape shape = new PolygonShape();
        shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = shape;
        playerFixture.friction = 10f;
        playerFixture.restitution = 0.0f;
        playerFixture.density = 1f;

        pBody = world.createBody(def);
        pBody.createFixture(playerFixture).setUserData("Food");
        shape.dispose();

        return pBody;
    }

//	public Body createFood(int x, int y, int radius){
//		Body pBody;
//
//		BodyDef def = new BodyDef();
//		def.type = BodyType.DynamicBody;
//		def.position.set(x / PPM, y / PPM);
//		def.fixedRotation = false;
//
//		CircleShape shape = new CircleShape();
//		shape.setRadius(radius / PPM);
//
//		FixtureDef playerFixture = new FixtureDef();
////		playerFixture.shape = shape;
//		playerFixture.shape = shape;
//		playerFixture.friction = 10f;
//		playerFixture.density = 10f;
////		playerFixture.density = 1f;
//
//		pBody = world.createBody(def);
//		pBody.createFixture(playerFixture).setUserData("Food");
//		shape.dispose();
//
//		return pBody;
//	}

    public static void generateFood(Rectangle body, String foodType, Texture texture){
        foodsToBeAdded.add(new Food(foodType, texture, (int) body.x, (int) body.y));
    }

    public static void renderFood(){
        for(Food food: foodsToBeAdded){
            foods.add(food);
        }
        foodsToBeAdded.clear();
    }

    public static void removeFood(){
        if(toBeDeleted.isEmpty()) return;
        System.out.println("Removing food!");
        for(Iterator<Body> iterator = toBeDeleted.iterator(); iterator.hasNext();){
            Body body = iterator.next();
            world.destroyBody(body);
            body.setActive(false);
//            body = null;
            iterator.remove();
        }
    }

    public static void removeJoints(){
        if(deleteJoint){
            world.destroyJoint(grabFood);
            grabFood = null;
            deleteJoint = false;
        }
    }

    public static void destroyFood(){
        for(Iterator<Food> it = foods.iterator(); it.hasNext();){
            Food food = it.next();
            if(food.isDeleted){
                System.out.println(food.name + " is submitted!");
                it.remove();
            }
        }
    }


}
