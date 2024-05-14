package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.model.Food;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Task;
import com.mygdx.game.utils.CollisionListener;
import com.mygdx.game.utils.DropOff;
import com.mygdx.game.utils.TiledObjectUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static com.mygdx.game.utils.Constants.PPM;
import static jdk.jfr.internal.consumer.EventLog.update;

public class MyGdxGame extends Game {
	private boolean DEBUG = false;
	private final float SCALE = 2.0f;

	public static World world;
	public static Player player;

	private Box2DDebugRenderer b2dr;

	public SpriteBatch batch;
	public OrthographicCamera camera;

	private Texture texture;
	public static Texture breadTexture;
	private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;

	private WeldJointDef jointDef;
	public static Joint grabFood;

	private Food food, food2;

	public static ArrayList<Food> foods = new ArrayList<Food>();

	private DropOff dropOff;

	public static ArrayList<Body> toBeDeleted = new ArrayList<Body>();
	public static boolean deleteJoint = false;


	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		breadTexture = new Texture("bread.png");

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w / SCALE, h / SCALE);

		world = new World(new Vector2(0, 0), false);
		b2dr = new Box2DDebugRenderer();
		world.setContactListener(new CollisionListener());

		player = new Player(world);
		dropOff = new DropOff(world, 50, 75, 32, 32);

		batch = new SpriteBatch();
		texture = new Texture("download-compresskaru.com.png");

		map = new TmxMapLoader().load("map.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);

		food = new Food("Bread", breadTexture, 100, 100);
		food2 = new Food("Spoon", breadTexture, 150, 170);


//		Vector3 pos = camera.position;
//		pos.x = 5000 * PPM;
//		pos.y = player.getPosition().y * PPM;
//		camera.position.set(768, 817389123, 0);

		camera.update();

		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("Testing").getObjects());

		jointDef = new WeldJointDef();

		foods.add(food);
		foods.add(food2);






	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());
		grabFood();
		removeJoints();
		removeFood();
		destroyFood();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);



		tmr.render();

		batch.begin();

		player.draw(batch);
		for(Food food : foods){
			food.draw(batch);
		}

		// Draw tasks
		for(Task task : TiledObjectUtil.tasks){
			task.drawTask(batch);
		}

		batch.end();

		b2dr.render(world, camera.combined.scl(PPM));

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}

		super.render();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width / 2, height / 2);
	}
	
	@Override
	public void dispose () {
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

		// Set the anchor point to the center of the player's body
		Vector2 worldCoordsAnchorPoint = bodyA.getWorldCenter();

		bodyB.setTransform(bodyA.getPosition().x , bodyA.getPosition().y, 0);

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
		player.inputUpdate(delta);
		cameraUpdate(delta);
//		camera.update();
		tmr.setView(camera);
		batch.setProjectionMatrix(camera.combined);
	}

	public void cameraUpdate(float delta) {
//		Vector3 pos = camera.position;
//		pos.x = player.getPosition().x * PPM;
//		pos.y = player.getPosition().y * PPM;
//		camera.position.set(pos);

		// Get the map's width and height in tiles
		int mapWidthInTiles = map.getProperties().get("width", Integer.class);
		int mapHeightInTiles = map.getProperties().get("height", Integer.class);

		// Get the tile size
		int tileSize = map.getProperties().get("tilewidth", Integer.class);

		// Calculate the map's width and height in pixels
		float mapWidthInPixels = mapWidthInTiles * tileSize;
		float mapHeightInPixels = mapHeightInTiles * tileSize;

		camera.position.set(mapWidthInPixels / 2, mapHeightInPixels / 2, 0);
		camera.zoom = 1.3f;

		camera.update();
	}

	public Body createBox(int x, int y, int w, int h, boolean isStatic){
		Body pBody;

		BodyDef def = new BodyDef();
		def.type = (isStatic) ? BodyType.StaticBody : BodyType.DynamicBody;
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

	public static void generateFood(Rectangle body){
		foods.add(new Food("Food", breadTexture, (int) body.x, (int) ( body.y )));
	}

	public static void removeFood(){
		if(toBeDeleted.isEmpty()) return;
		System.out.println("Removing food!");
		for(Iterator<Body> iterator = toBeDeleted.iterator(); iterator.hasNext();){
			Body body = iterator.next();
			world.destroyBody(body);
			body.setActive(false);
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
		for(Iterator<Food> it = foods.iterator() ; it.hasNext();){
			Food food = it.next();
			if(food.isDeleted){
			System.out.println(food.name + " is submitted!");
//				toBeDeleted.add(food.body);
				it.remove();
			}
		}
	}


}
