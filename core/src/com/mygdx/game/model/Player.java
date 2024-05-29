package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.utils.ControllerState;
import com.mygdx.game.utils.XboxMapping;

import java.util.ArrayList;
import java.util.Objects;

import static com.mygdx.game.controller.GameController.controller;
import static com.mygdx.game.screens.GameplayScreen.*;
import static com.mygdx.game.utils.Constants.PPM;

public class Player{
    public Body body;
    public Fixture interactedFixture;
    public Body interactedFood;
    public boolean isGrabbing = false;
    public Sprite sprite;
    public FacingDirection facingDirection = FacingDirection.RIGHT;
    public boolean isMoving = false;
    public ArrayList<Body> interactedList = new ArrayList<>();
    public boolean isPlayer1;

    public int moveUp;
    public int moveDown;
    public int moveRight;
    public int moveLeft;

    public int interact;
    public int grab;

    public Joint grabFood;
    public boolean deleteJoint = false;

    private ControllerState grabState;
    private ControllerState interactState;

    // The walk animations for each direction
    private Animation[] walkAnimations; // Must declare frame type (TextureRegion)

    private Animation[] grabAnimations;

    Texture walkSheet;
    // A variable for tracking elapsed time for the animation
    float stateTime;


    public Player(World world, TiledMap map, boolean isPlayer1){
        this.isPlayer1 = isPlayer1;
        createPlayer(map ,world);
        sprite = new Sprite(new Texture("serato-64x64.png"));
//        sprite.setSize(64, 64);
//        sprite.setOriginCenter();


        if(isPlayer1) {
            moveUp = Input.Keys.W;
            moveDown = Input.Keys.S;
            moveRight = Input.Keys.D;
            moveLeft = Input.Keys.A;

            interact = Input.Keys.O;
            grab = Input.Keys.P;
        } else {

//            moveUp = Input.Keys.UP;
//            moveDown = Input.Keys.DOWN;
//            moveRight = Input.Keys.RIGHT;
//            moveLeft = Input.Keys.LEFT;
//
//            interact = Input.Keys.N;
//            grab = Input.Keys.M;



            moveUp = XboxMapping.DPAD_UP; // Assuming up is negative direction
            moveDown = XboxMapping.DPAD_DOWN; // Assuming down is positive direction
            moveRight = XboxMapping.DPAD_RIGHT; // Assuming right is positive direction
            moveLeft = XboxMapping.DPAD_LEFT; // Assuming left is negative direction

            interact = XboxMapping.BUTTON_LS;
            grab = XboxMapping.BUTTON_RS;

            grabState = new ControllerState(grab);
            interactState = new ControllerState(interact);
        }



        if(isPlayer1){
            System.out.println("Generated Player 1");
        } else {
            System.out.println("Generated Player 2");
        }



        createPlayerWalkingAnimations();
        createPlayerGrabbingAnimations();
    }

    public void createPlayerWalkingAnimations() {
        int FRAME_COLS = 16; // Total number of frames in your sprite sheet
        int FRAME_ROWS = 1; // Only one row in the sprite sheet

        // Define the start and end columns for each direction
        int[] startCols = {0, 4, 8, 12}; // Replace with your start columns for each direction
        int[] endCols = {3, 7, 11, 15}; // Replace with your end columns for each direction

        walkSheet = new Texture(Gdx.files.internal("sheets/serato_spritesheet_64x64.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        // Create the walkAnimations array
        walkAnimations = new Animation[4]; // One for each direction

        // Fill the walkAnimations array with the frames from the sprite sheet
        for (int i = 0; i < 4; i++) {
            int startCol = startCols[i];
            int endCol = endCols[i];

            // Create the frames array with the correct size
            TextureRegion[] frames = new TextureRegion[endCol - startCol + 1];
            int index = 0;

            // Fill the frames array with the frames from the startCol to the endCol
            for (int j = startCol; j <= endCol; j++) {
                frames[index++] = tmp[0][j];
            }

            walkAnimations[i] = new Animation<>(0.25f, frames);
        }

        stateTime = 0f;
    }

    public void createPlayerGrabbingAnimations() {
        int FRAME_COLS = 16; // Total number of frames in your sprite sheet
        int FRAME_ROWS = 1; // Only one row in the sprite sheet

        // Define the start and end columns for each direction
        int[] startCols = {0, 4, 8, 12}; // Replace with your start columns for each direction
        int[] endCols = {3, 7, 11, 15}; // Replace with your end columns for each direction

        walkSheet = new Texture(Gdx.files.internal("sheets/serato_spritesheet_grabbing_64x64.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

        // Create the walkAnimations array
        grabAnimations = new Animation[4]; // One for each direction

        // Fill the walkAnimations array with the frames from the sprite sheet
        for (int i = 0; i < 4; i++) {
            int startCol = startCols[i];
            int endCol = endCols[i];

            // Create the frames array with the correct size
            TextureRegion[] frames = new TextureRegion[endCol - startCol + 1];
            int index = 0;

            // Fill the frames array with the frames from the startCol to the endCol
            for (int j = startCol; j <= endCol; j++) {
                frames[index++] = tmp[0][j];
            }

            grabAnimations[i] = new Animation<>(0.25f, frames);
        }

        stateTime = 0f;
    }

    public void inputUpdate(float delta) {
        float horizontalForce = 0;
        float verticalForce = 0;

        isMoving = false;


        if(isPlayer1){
            if (Gdx.input.isKeyPressed(moveLeft)) {
                horizontalForce -= 1.5f;
                body.setTransform(body.getPosition(), (float) Math.PI);
                sprite.setRotation(180);
                facingDirection = FacingDirection.LEFT;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(moveRight)) {
                horizontalForce += 1.5f;
                body.setTransform(body.getPosition(), 0f);
                sprite.setRotation(0);
                facingDirection = FacingDirection.RIGHT;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(moveUp)) {
                verticalForce += 1.5f;
                body.setTransform(body.getPosition(), (float) (Math.PI / 2));
                sprite.setRotation(90);
                facingDirection = FacingDirection.UP;
                isMoving = true;
            }
            if (Gdx.input.isKeyPressed(moveDown)) {
                verticalForce -= 1.5f;
                body.setTransform(body.getPosition(), (float) (3 * Math.PI / 2));
                sprite.setRotation(270);
                facingDirection = FacingDirection.DOWN;
                isMoving = true;
            }



            if (horizontalForce == 0 && verticalForce == 0) {
                body.setLinearVelocity(0, 0);
            } else {
                body.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
            }

            if (Gdx.input.isKeyJustPressed(grab)) {
                if(isGrabbing){
                    System.out.println("Not Grabbing!");
                    isGrabbing = false;
                    interactedFood = null;

                    interactedList.clear();
                    if (grabFood != null) {
//                    world.destroyJoint(grabFood);
//                    jointDeleted.add(grabFood);
//                    grabFood = null;
                        deleteJoint = true;
                    }
                } else {
                    isGrabbing = true;
                    System.out.println("Now grabbing!");
                }
            }



            if (Gdx.input.isKeyJustPressed(interact)) {
                if (interactedFixture == null) {
//                System.out.println("No interaction");
                } else if (Objects.equals(interactedFixture.getUserData(), null)) {
//                System.out.println("Interacted with " + interactedFixture.getUserData());
                } else {
                    for (Task task : taskController.getTasks()) {
                        if (Objects.equals(interactedFixture.getUserData(), task.name)) {
                            if(task.isEnabled){
//                            System.out.println("Interacted with " + interactedFixture.getUserData());
                                task.interactTimer();
                                break;
                            } else {
//                            System.out.println("Task is not enabled!");
                            }

                        }
                    }
                }

            }

        } else {
            if (Controllers.getCurrent().getButton(XboxMapping.DPAD_LEFT)) {
                horizontalForce -= 1.5f;
                body.setTransform(body.getPosition(), (float) Math.PI);
                sprite.setRotation(180);
                facingDirection = FacingDirection.LEFT;
                isMoving = true;
            }
            if (Controllers.getCurrent().getButton(XboxMapping.DPAD_RIGHT)) {
                horizontalForce += 1.5f;
                body.setTransform(body.getPosition(), 0f);
                sprite.setRotation(0);
                facingDirection = FacingDirection.RIGHT;
                isMoving = true;
            }
            if (Controllers.getCurrent().getButton(XboxMapping.DPAD_UP)) {
                verticalForce += 1.5f;
                body.setTransform(body.getPosition(), (float) (Math.PI / 2));
                sprite.setRotation(90);
                facingDirection = FacingDirection.UP;
                isMoving = true;
            }
            if (Controllers.getCurrent().getButton(XboxMapping.DPAD_DOWN)) {
                verticalForce -= 1.5f;
                body.setTransform(body.getPosition(), (float) (3 * Math.PI / 2));
                sprite.setRotation(270);
                facingDirection = FacingDirection.DOWN;
                isMoving = true;
            }



            if (horizontalForce == 0 && verticalForce == 0) {
                body.setLinearVelocity(0, 0);
            } else {
                body.setLinearVelocity(horizontalForce * 5, verticalForce * 5);
            }

            if (grabState.isJustPressed()) {
                if(isGrabbing){
                    System.out.println("Not Grabbing!");
                    isGrabbing = false;
                    interactedFood = null;

                    interactedList.clear();
                    if (grabFood != null) {
//                    world.destroyJoint(grabFood);
//                    jointDeleted.add(grabFood);
//                    grabFood = null;
                        deleteJoint = true;
                    }
                } else {
                    isGrabbing = true;
                    System.out.println("Now grabbing!");
                }
            }



            if (Controllers.getCurrent().getButton(interact)) {
                if (interactedFixture == null) {
//                System.out.println("No interaction");
                } else if (Objects.equals(interactedFixture.getUserData(), null)) {
//                System.out.println("Interacted with " + interactedFixture.getUserData());
                } else {
                    for (Task task : taskController.getTasks()) {
                        if (Objects.equals(interactedFixture.getUserData(), task.name)) {
                            if(task.isEnabled){
//                            System.out.println("Interacted with " + interactedFixture.getUserData());
                                task.interactTimer();
                                break;
                            } else {
//                            System.out.println("Task is not enabled!");
                            }

                        }
                    }
                }

            }

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            System.out.println("Player 1 interacted food: " + player.interactedFood);
            System.out.println("Player 2 interacted food: " + player2.interactedFood);
        }
    }

    private void createPlayerBody(World world, int x, int y, int w, int h){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox( w / 2 / PPM,  h / 2 / PPM);

        FixtureDef playerFixture = new FixtureDef();
        playerFixture.shape = shape;
        playerFixture.density = 1.0f;

        this.body = world.createBody(def);

        if(isPlayer1){
            this.body.createFixture(playerFixture).setUserData("Player1");
        } else {
            this.body.createFixture(playerFixture).setUserData("Player2");
        }
    }

    private void createPlayer(TiledMap map, World world) {
        MapLayer layer = map.getLayers().get("Player");

        if (layer == null) {
            throw new GdxRuntimeException("Layer 'Player' not found in the map");
        }

        MapObjects objects = layer.getObjects();

//        System.out.println("Checking Layer");

        for (MapObject object : objects) {
//            System.out.println(object);
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();

                createPlayerBody(world, (int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);

            }
        }
    }

    public Animation<TextureRegion> getWalkAnimation(int direction) {
        return walkAnimations[direction];
    }

    public Animation<TextureRegion> getGrabAnimation(int direction) {
        return grabAnimations[direction];
    }

    public void draw(Batch batch){
        TextureRegion currentFrame;
        stateTime += Gdx.graphics.getDeltaTime();

        if(!isMoving){
            stateTime = 0;
        }

        if(isGrabbing){
           currentFrame = getGrabAnimation(facingDirection.ordinal()).getKeyFrame(stateTime, true);

        } else {
            currentFrame = getWalkAnimation(facingDirection.ordinal()).getKeyFrame(stateTime, true);
//        batch.draw(currentFrame, body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2);
        }

        batch.draw(currentFrame, body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2, 64, 64);
    }


}

