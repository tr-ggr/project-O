package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.mygdx.game.utils.Constants.PPM;
import static jdk.jfr.internal.consumer.EventLog.update;

public class MyGdxGame extends ApplicationAdapter {
	private boolean DEBUG = false;
	private final float SCALE = 2.0f;

	private OrthographicCamera camera;

	private World world;
	private Body player, platform;

	private Box2DDebugRenderer b2dr;

	private SpriteBatch batch;
	private Texture texture;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w / SCALE, h / SCALE);

		world = new World(new Vector2(0, 0), false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(0, 0,32, 32, false);
		platform = createBox(30, 30,50, 16, true);

		batch = new SpriteBatch();
		texture = new Texture("download-compresskaru.com.png");




	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(texture, player.getPosition().x * PPM - (texture.getWidth() / 2), player.getPosition().y * PPM - (texture.getHeight() / 2));
		batch.end();

		b2dr.render(world, camera.combined.scl(PPM));



		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
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
	}

	public void inputUpdate(float delta){
		int horizontalForce = 0;
		int verticalForce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			horizontalForce -= 1;
			player.setLinearVelocity(horizontalForce * 5, player.getLinearVelocity().y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			horizontalForce += 1;
			player.setLinearVelocity(horizontalForce * 5, player.getLinearVelocity().y);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			verticalForce += 1;
			player.setLinearVelocity(player.getLinearVelocity().x, verticalForce * 5);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			verticalForce -= 1;
			player.setLinearVelocity(player.getLinearVelocity().x, verticalForce * 5);
		}



	}

	public void update(float delta) {
		world.step(1/60f, 6, 2);

		inputUpdate(delta);
		cameraUpdate(delta);
		batch.setProjectionMatrix(camera.combined);
	}

	public void cameraUpdate(float delta) {
		Vector3 pos = camera.position;
		pos.x = player.getPosition().x * PPM;
		pos.y = player.getPosition().y * PPM;
		camera.position.set(pos);

		camera.update();
	}

	public Body createBox(int x, int y, int w, int h, boolean isStatic){
		Body pBody;

		BodyDef def = new BodyDef();
		def.type = (isStatic) ? BodyType.StaticBody : BodyType.DynamicBody;
		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;

		pBody = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

		pBody.createFixture(shape, 1.0f);
		shape.dispose();

		return pBody;
	}
}
