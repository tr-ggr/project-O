package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

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
            body.createFixture(shape, 1);

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
