package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Task;

import static com.mygdx.game.screens.GameplayScreen.taskController;
import static com.mygdx.game.utils.Constants.PPM;

public class TiledObjectUtil {

    public static void parseTiledObjectLayer(World world, MapObjects objects) {
        for (MapObject object : objects) {
            Shape shape;

            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object);
            }
            else {
                continue;
            }

            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
            body.createFixture(shape, 1f);







            shape.dispose();
        }
    }

    public static void parseTaskObjects(World world, MapObjects objects) {
        for (MapObject object : objects) {
            Shape shape;

            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object);
            }
            else {
                continue;
            }

            Body body;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bdef);
//            body.createFixture(shape, 1);
            body.createFixture(shape, 1).setUserData(object.getName());
//
//            FixtureDef objectFixture = new FixtureDef();
//            objectFixture.shape = shape;
//            objectFixture.density = 1.0f;
//
//            body = world.createBody(bdef);
//            body.createFixture(objectFixture).setUserData(object.getName());


            Object timeProperty = object.getProperties().get("Time");
            if (timeProperty != null) {
                System.out.println(((RectangleMapObject) object).getRectangle());
                if(object.getProperties().get("Key") == null) {
                    System.out.println("Created Task: " + object.getName() + " with output: " + object.getProperties().get("Output").toString() + " and time: " + Integer.parseInt(timeProperty.toString()));
                    taskController.addTask(new Task(object.getName(), object.getProperties().get("Output").toString(), Integer.parseInt(timeProperty.toString()), ((RectangleMapObject) object).getRectangle()));
                }else{
                    taskController.addTask(new Task(object.getName(), object.getProperties().get("Output").toString(), Integer.parseInt(timeProperty.toString()), ((RectangleMapObject) object).getRectangle(), object.getProperties().get("Key").toString()));
                    System.out.println("Key: " + object.getProperties().get("Key").toString());
                }
            } else {
                System.out.println("Warning: Time property is missing for object " + object.getName());
            }


            shape.dispose();
        }
    }

    public static void parseDropOff(World world, MapObjects objects) {
        for (MapObject object : objects) {
            Shape shape;

            if (object instanceof PolylineMapObject) {
                shape = createPolyline((PolylineMapObject) object);
            } else if (object instanceof RectangleMapObject) {
                shape = createRectangle((RectangleMapObject) object);
            } else {
                continue;
            }

            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.fixedRotation = true;

            // Assuming x, y, w, h are the position and dimensions of the object
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            def.position.set(rect.x / PPM, rect.y / PPM);

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 1.0f;
            fixtureDef.isSensor = true;

            Body body = world.createBody(def);
            body.createFixture(fixtureDef).setUserData("Sensor");

            shape.dispose();
        }
    }

    private static ChainShape createPolyline(PolylineMapObject polyline) {
        float[] vertices = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
        }

        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    private static PolygonShape createRectangle(RectangleMapObject rectangle) {
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(rectangle.getRectangle().width / (2 * PPM), rectangle.getRectangle().height / (2 * PPM));
        Vector2 pos = new Vector2((rectangle.getRectangle().x + rectangle.getRectangle().width / 2) / PPM, (rectangle.getRectangle().y + rectangle.getRectangle().height / 2) / PPM);
        polygon.setAsBox(size.x, size.y, pos, 0);
        return polygon;
    }
}
