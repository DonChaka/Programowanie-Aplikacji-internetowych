package com.climb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.climb.managers.ContactManager;
import com.climb.objects.Car;
import com.climb.managers.GameStateManager;
import com.climb.objects.Terrain;
import com.climb.utils.Constants;
import com.climb.utils.Frame;
import com.climb.utils.PositionsHandler;

import java.util.ArrayList;
import java.util.List;

public class PlayState extends GameState
{
    /*debugging*/
    private Box2DDebugRenderer b2dr;

    /* Important*/
    private World world;
    private Car car;

    private Texture carTex, wheelTex, bodyTex, headTex, otherTex;
    private Sprite carSprite, frontSprite, rearSprite, bodySprite, headSprite;
    private List<float[]> rideTerrain;
    private ShapeRenderer shapeRenderer;
    private float terrainSteepness = 100;

    private float previousX = 200 / 1000, previousY = 250 / 1000;

    private int lastGenerated;
    private final int chunkSize = 100;
    private int seed;

    public static List<Frame> others;
    private List<Sprite> otherSprites;
    private Frame frame;

    private int wheelSize = 14;

    public PlayState(GameStateManager gsm)
    {
        super(gsm);
        seed = gsm.worldSeed;

        others = new ArrayList<>();
        PositionsHandler positionsHandler = new PositionsHandler(others);
        otherSprites = new ArrayList<>(others.size());
        frame = new Frame();
        frame.nickname = gsm.nickname;

        positionsHandler.start();

        rideTerrain = new ArrayList<>();
        Texture terrainTex = new Texture("Images/bodySprite.png");
        shapeRenderer = new ShapeRenderer();

        otherTex = new Texture("Images/multiSprite.png");

        create();
    }

    public void create()
    {
        world = new World(new Vector2(0f, -9.8f), false);
        this.world.setContactListener(new ContactManager());

        terrainSteepness = 100;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        world.createBody(bdef);

        rideTerrain.add(Terrain.generate(world, seed,-50, 51, terrainSteepness));

        rideTerrain.add(Terrain.generate(world, seed,0, chunkSize + 1, terrainSteepness));

        lastGenerated = chunkSize;

        car = new Car(world, 200, 250, 90,30, wheelSize);

        carTex = new Texture("Images/carSprite.png");
        carSprite = new Sprite(carTex, 0,0, carTex.getWidth(), carTex.getHeight());
        carSprite.setSize(90, 50);
        carSprite.setOrigin(45,   15);
        carSprite.setPosition(car.getPositionX() - car.dlugoscAuta / 2, car.getPositionY() - car.wysokoscAuta / 2);

        wheelTex = new Texture("Images/wheelSprite.png");
        frontSprite = new Sprite(wheelTex, 0, 0, wheelTex.getWidth(), wheelTex.getHeight());
        frontSprite.setSize(wheelSize * 2, wheelSize * 2);
        frontSprite.setOrigin(wheelSize, wheelSize);
        frontSprite.setPosition(car.frontWheelBody.getPosition().x * Constants.PPM - wheelSize, car.frontWheelBody.getPosition().y * Constants.PPM - wheelSize);

        rearSprite = new Sprite(wheelTex, 0, 0, wheelTex.getWidth(), wheelTex.getHeight());
        rearSprite.setSize(wheelSize * 2 , wheelSize * 2);
        rearSprite.setOrigin(wheelSize, wheelSize);
        rearSprite.setPosition(car.rearWheelBody.getPosition().x * Constants.PPM - wheelSize, car.rearWheelBody.getPosition().y * Constants.PPM - wheelSize);

        bodyTex = new Texture("Images/bodySprite.png");
        bodySprite = new Sprite(bodyTex, 0, 0, bodyTex.getWidth(), bodyTex.getHeight());
        bodySprite.setSize(10, 24);
        bodySprite.setOrigin(5, 12);
        bodySprite.setPosition(car.torso.getPosition().x * Constants.PPM - 5, car.torso.getPosition().y * Constants.PPM - 12);

        headTex = new Texture("Images/headSprite.png");
        headSprite = new Sprite(headTex, 0, 0, headTex.getWidth(), headTex.getHeight());
        headSprite.setSize(12, 14);
        headSprite.setOrigin(6, 7);
        headSprite.setPosition(car.head.getPosition().x * Constants.PPM - 6, car.head.getPosition().y * Constants.PPM - 6);

        b2dr = new Box2DDebugRenderer();
    }

    private void restart()
    {
        rideTerrain.clear();
        b2dr.dispose();
        world.dispose();
        headTex.dispose();
        bodyTex.dispose();
        carTex.dispose();
        wheelTex.dispose();
        create();
    }

    private void sendPosition()
    {
        try
        {
            if(gsm.posSocket != null)
            {
                gsm.posOutput.flush();
                gsm.posOutput.writeObject(new Frame(frame));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void update(float delta)
    {

        world.step(1/ 60f, 6, 2);

        controls();
        cameraUpdate();

        frame.x = car.getPositionX() - car.dlugoscAuta / 2 - 3;
        frame.y = car.getPositionY() - car.wysokoscAuta / 2 - 20;
        frame.angle = car.car.getAngle();
        sendPosition();

        if(car.getPositionX() >= lastGenerated * 3)
        {
            rideTerrain.add(Terrain.generate(world, seed, lastGenerated, chunkSize + 1 , terrainSteepness));
            lastGenerated += chunkSize;
            terrainSteepness *= 1.05f;
        }

        float actualX = car.getPositionX() / 1000, actualY = car.getPositionY() / 1000;
        float deltaX = previousX - actualX, deltaY = previousY - actualY;
        for(float[] vertices : rideTerrain)
        {
            for(int i = 0; i < vertices.length ; i+=2)
            {
                vertices[i] = vertices[i] - deltaX / Constants.PPM;
                vertices[i+1] = vertices[i+1] - deltaY / Constants.PPM;
            }
        }
        previousX = actualX;
        previousY = actualY;

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        carSprite.setPosition(car.getPositionX() - car.dlugoscAuta / 2, car.getPositionY() - car.wysokoscAuta / 2);
        carSprite.setRotation((float)Math.toDegrees(car.car.getAngle()));

        frontSprite.setPosition(car.frontWheelBody.getPosition().x * Constants.PPM - wheelSize, car.frontWheelBody.getPosition().y * Constants.PPM - wheelSize);
        frontSprite.setRotation((float)Math.toDegrees(car.frontWheelBody.getAngle()));

        rearSprite.setPosition(car.rearWheelBody.getPosition().x * Constants.PPM - wheelSize, car.rearWheelBody.getPosition().y * Constants.PPM - wheelSize);
        rearSprite.setRotation((float)Math.toDegrees(car.rearWheelBody.getAngle()));

        bodySprite.setPosition(car.torso.getPosition().x * Constants.PPM - 5, car.torso.getPosition().y * Constants.PPM - 12);
        bodySprite.setRotation((float)Math.toDegrees(car.torso.getAngle()));

        headSprite.setPosition(car.head.getPosition().x * Constants.PPM - 6, car.head.getPosition().y * Constants.PPM - 6);
        headSprite.setRotation((float)Math.toDegrees(car.head.getAngle()));

        while(otherSprites.size() < others.size())
        {
            Sprite newSprite = new Sprite(otherTex, 0, 0, otherTex.getWidth(), otherTex.getHeight());
            newSprite.setSize(97, 78);
            newSprite.setOrigin(49, 35);

            otherSprites.add(newSprite);
        }

        for(int i = 0; i < others.size(); i++)
        {
            Frame temp = others.get(i);
            otherSprites.get(i).setPosition(temp.x, temp.y);
            otherSprites.get(i).setRotation((float)Math.toDegrees(temp.angle));
        }

        car.killIfDead();
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(.25f, .25f, .25f, .25f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for(float[] vertices : rideTerrain)
        {
            shapeRenderer.setColor(0, 0.5f, 0 , 1);
            shapeRenderer.polyline(vertices);
        }

        shapeRenderer.end();

        batch.begin();

        if(others.size() == otherSprites.size())
            for(int i = 0; i < others.size(); i++)
                otherSprites.get(i).draw(batch);

        headSprite.draw(batch);
        bodySprite.draw(batch);
        carSprite.draw(batch);
        frontSprite.draw(batch);
        rearSprite.draw(batch);

        batch.end();
    }

    @Override
    public void dispose()
    {
        b2dr.dispose();
        world.dispose();
        headTex.dispose();
        bodyTex.dispose();
        carTex.dispose();
        wheelTex.dispose();
        otherTex.dispose();
        shapeRenderer.dispose();
    }

    private void cameraUpdate()
    {
        Vector3 pos = camera.position;
        pos.x = camera.position.x + (car.getPositionX() - camera.position.x) * .25f;
        pos.y = camera.position.y + (car.getPositionY() - camera.position.y) * .25f;

        camera.position.set(pos);

        camera.update();
    }

    private void controls()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            car.move(1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            car.move(-1);
        }
        else
            car.stop();

        if(Gdx.input.isKeyJustPressed(Input.Keys.K))
        {
            restart();
            gsm.setState(GameStateManager.State.GAMEOVER);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            restart();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.T))
            gsm.setState(GameStateManager.State.CHAT);
    }

}
