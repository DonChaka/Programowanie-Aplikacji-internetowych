package com.climb.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.climb.utils.Constants;
import com.climb.utils.Noise;

import java.util.ArrayList;
import java.util.List;

public class Terrain
{
    public static float[] generate(World world, int seed, int start, int length, float steepness)
    {
        Vector2[] worldVertices = new Vector2[length];
        List<Float> ret = new ArrayList<>();
        float res = 50;
        float x, y;
/*        ret.add((float)start);
        ret.add(-10000f);*/
        for(int i = 0; i < length; i++)
        {
            x = (i + start) / 5f;
            y = Noise.generate((i + seed + start) / res) * steepness / Constants.PPM;
            worldVertices[i] = new Vector2(x, y);
            ret.add(x * Constants.PPM);
            ret.add(y * Constants.PPM);
        }
/*        ret.add(x);
        ret.add(-10000f);*/

        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);

        Body body;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        body.createFixture(cs, 1.0f).setUserData(new TerrainContact());
        cs.dispose();

        float[] ver = new float[ret.size()];
        for(int i = 0; i < ret.size(); i++)
            ver[i] = ret.get(i);

        return ver;
    }
}
