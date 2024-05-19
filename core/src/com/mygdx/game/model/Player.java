package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Objects;

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

    public int moveUp;
    public int moveDown;
    public int moveRight;
    public int moveLeft;

    public int interact;
    public int grab;

    // The walk animations for each direction
    private Animation[] walkAnimations; // Must declare frame type (TextureRegion)

    Texture walkSheet;
    // A variable for tracking elapsed time for the animation
    float stateTime;


    public Player(World world, int x, int y, boolean isPlayer1){
        createPlayerBody(world, x, y, 32, 32);
        sprite = new Sprite(new Texture("serato_down.png"));

        if(isPlayer1){
            moveUp = Input.Keys.UP;
            moveDown = Input.Keys.DOWN;
            moveRight = Input.Keys.RIGHT;
            moveLeft = Input.Keys.LEFT;

            interact = Input.Keys.K;
            grab = Input.Keys.L;
        } else {
            moveUp = Input.Keys.W;
            moveDown = Input.Keys.S;
            moveRight = Input.Keys.D;
            moveLeft = Input.Keys.A;

            interact = Input.Keys.O;
            grab = Input.Keys.P;
        }

        createPlayerAnimations();
    }

    public void createPlayerAnimations() {
        int FRAME_COLS = 16; // Total number of frames in your sprite sheet
        int FRAME_ROWS = 1; // Only one row in the sprite sheet

        // Define the start and end columns for each direction
        int[] startCols = {0, 4, 8, 12}; // Replace with your start columns for each direction
        int[] endCols = {3, 7, 11, 15}; // Replace with your end columns for each direction

        walkSheet = new Texture(Gdx.files.internal("sheets/serato_spritesheet.png"));
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

    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        int verticalForce = 0;

        isMoving = false;

        if (Gdx.input.isKeyPressed(moveLeft)) {
            horizontalForce -= 1;
            body.setTransform(body.getPosition(), (float) Math.PI);
            sprite.setRotation(180);
            facingDirection = FacingDirection.LEFT;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveRight)) {
            horizontalForce += 1;
            body.setTransform(body.getPosition(), 0f);
            sprite.setRotation(0);
            facingDirection = FacingDirection.RIGHT;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveUp)) {
            verticalForce += 1;
            body.setTransform(body.getPosition(), (float) (Math.PI / 2));
            sprite.setRotation(90);
            facingDirection = FacingDirection.UP;
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveDown)) {
            verticalForce -= 1;
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
                    world.destroyJoint(grabFood);
                    grabFood = null;
                }
            } else {
                isGrabbing = true;
                System.out.println("Now grabbing!");
            }
        }


//        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
//            if (interactedFood == null) {
//                System.out.println("No food");
//                return;
//            } else {
////                for(Body b: interactedList ){
////                    System.out.println(b);
////                }
//                System.out.println(interactedFood);
//            }
//        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
//            System.out.println("Successfully dropped food!");
//            isGrabbing = false;
//            interactedFood = null;
//
//            interactedList.clear();
//            if (grabFood != null) {
//                world.destroyJoint(grabFood);
//                grabFood = null;
//            }d
//        }


        if (Gdx.input.isKeyJustPressed(interact)) {
            if (interactedFixture == null) {
                System.out.println("No interaction");
            } else if (Objects.equals(interactedFixture.getUserData(), null)) {
                System.out.println("Interacted with " + interactedFixture.getUserData());
            } else {
                for (Task task : taskController.getTasks()) {
                    if (Objects.equals(interactedFixture.getUserData(), task.name)) {
                        if(task.isEnabled){
                            System.out.println("Interacted with " + interactedFixture.getUserData());
                            task.interactTimer();
                            break;
                        } else {
                            System.out.println("Task is not enabled!");
                        }

                    }
                }
            }

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
        this.body.createFixture(playerFixture).setUserData("Player");
    }

    public Animation<TextureRegion> getAnimation(int direction) {
        return walkAnimations[direction];
    }

    public void draw(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        if(!isMoving){
            stateTime = 0;
        }


        TextureRegion currentFrame = getAnimation(facingDirection.ordinal()).getKeyFrame(stateTime, true);

        batch.draw(currentFrame, body.getPosition().x * PPM - sprite.getWidth() / 2, body.getPosition().y * PPM - sprite.getHeight() / 2, 32, 32);
    }
}

