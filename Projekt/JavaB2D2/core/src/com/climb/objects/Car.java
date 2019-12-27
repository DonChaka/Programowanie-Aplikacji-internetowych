package com.climb.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;

import static com.climb.utils.Constants.PPM;

public class Car
{
    public Body car, frontWheelBody, rearWheelBody, head, torso;
    private WheelJoint frontWheelJoint, rearWheelJoint;
    private RevoluteJoint seat, neck;
    private DistanceJoint belt, distanceNeck;

    public int dlugoscAuta, wysokoscAuta;

    private boolean isAlive = true;
    public boolean isKindaDead = false;

    private World world;

    public Car(World world, int x, int y, int dlugoscAuta, int wysokoscAuta, int rozmiarKola)
    {
        this.world = world;
        this.dlugoscAuta = dlugoscAuta;
        this.wysokoscAuta = wysokoscAuta;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = false;
        car = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[6];

        /* Glowne "body" auta */
        vertices[0] = new Vector2(-dlugoscAuta / 2 / PPM, wysokoscAuta / 2 / PPM);
        vertices[1] = new Vector2((dlugoscAuta / 4) / PPM, wysokoscAuta / 2 / PPM);
        vertices[2] = new Vector2(dlugoscAuta / 2 / PPM, (wysokoscAuta / 2 - 5) / PPM);
        vertices[3] = new Vector2(dlugoscAuta / 2 / PPM, (-wysokoscAuta / 2 + 5)  / PPM);
        vertices[4] = new Vector2((dlugoscAuta / 4 + 5) / PPM, -wysokoscAuta / 2 / PPM);
        vertices[5] = new Vector2(-dlugoscAuta / 2 / PPM, -wysokoscAuta / 2 / PPM);

        shape.set(vertices);
        car.createFixture(shape, 1.0f);


        /* Windshield */
        Vector2[] vertices2 = new Vector2[4];

        vertices2[0] = new Vector2(dlugoscAuta / 4 / PPM, wysokoscAuta / 2 / PPM);
        vertices2[1] = new Vector2((dlugoscAuta / 4 - 10) / PPM, (wysokoscAuta / 2 + 20) / PPM );
        vertices2[2] = new Vector2((dlugoscAuta / 4 - 5) / PPM, (wysokoscAuta / 2 + 20) / PPM);
        vertices2[3] = new Vector2((dlugoscAuta / 4 + 5) / PPM, wysokoscAuta / 2 / PPM);

        shape.set(vertices2);
        car.createFixture(shape, .0001f);


        /* Koła i zamocowanie ich do auta z własnościami zawieszenia*/
        frontWheelBody = createCircle(x + dlugoscAuta / 2 - rozmiarKola * 1.2f, y - wysokoscAuta / 2 - rozmiarKola / 4, rozmiarKola);
        rearWheelBody = createCircle(x - dlugoscAuta / 2 + rozmiarKola * 1.2f, y - wysokoscAuta / 2 - rozmiarKola / 4, rozmiarKola);

        WheelJointDef wheel = new WheelJointDef();
        wheel.bodyA = car;
        wheel.bodyB = rearWheelBody;
        wheel.localAnchorA.set((-dlugoscAuta / 2 + rozmiarKola) / PPM, -(wysokoscAuta / 2 + rozmiarKola / 4) / PPM);
        wheel.localAxisA.set(0, 1);
        wheel.dampingRatio = .1f;
        wheel.frequencyHz = 5;
        wheel.maxMotorTorque = 10;
        rearWheelJoint = (WheelJoint) world.createJoint(wheel);

        wheel.bodyB = frontWheelBody;
        wheel.localAnchorA.set((dlugoscAuta / 2 - rozmiarKola) / PPM, -(wysokoscAuta / 2 + rozmiarKola / 4) / PPM);
        frontWheelJoint = (WheelJoint) world.createJoint(wheel);

        /*Cialo kierowcy oraz "poszdzenie go i zapiecie pasami"*/

        torso = createTorso(x, y + 5, 5f, 12);

        RevoluteJointDef seatDef = new RevoluteJointDef();
        seatDef.bodyA = car;
        seatDef.bodyB = torso;
        seatDef.localAnchorA.set(0 , 0);
        seatDef.localAnchorB.set(0, -15 / PPM);
        seatDef.enableLimit = true;
        seatDef.lowerAngle = -45;
        seatDef.upperAngle = 45;
        seat = (RevoluteJoint) world.createJoint(seatDef);

        DistanceJointDef beltDef = new DistanceJointDef();
        beltDef.bodyA = car;
        beltDef.bodyB = torso;
        beltDef.localAnchorA.set(dlugoscAuta / 3 / PPM, wysokoscAuta / 2 / PPM);
        beltDef.localAnchorB.set(7 / PPM, 15 / PPM);
        beltDef.dampingRatio = .2f;
        beltDef.frequencyHz = 3;
        belt = (DistanceJoint) world.createJoint(beltDef);


        /*Glowa oraz przytwordzenie jej do ciala*/
        head = createHead(x, y + 27, 6f);

        RevoluteJointDef neckDef = new RevoluteJointDef();
        neckDef.bodyA = torso;
        neckDef.bodyB = head;
        neckDef.localAnchorA.set(0, 12 / PPM);
        neckDef.localAnchorB.set(0, -6 / PPM);

        neck = (RevoluteJoint) world.createJoint(neckDef);

        DistanceJointDef disNeckDef = new DistanceJointDef();
        disNeckDef.bodyA = torso;
        disNeckDef.bodyB = head;
        disNeckDef.frequencyHz = 3;
        disNeckDef.dampingRatio = .3f;

        distanceNeck = (DistanceJoint) world.createJoint(disNeckDef);

    }

    private Body createCircle(float x, float y, float radius)
    {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fixdef = new FixtureDef();
        fixdef.shape = shape;
        fixdef.density = 1.0f;
        fixdef.friction = 1f;

        pBody.createFixture(fixdef);

        shape.dispose();
        return pBody;
    }

    private Body createTorso(float x, float y, float width, float height)
    {
        BodyDef bdef = new BodyDef();

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x / PPM, y / PPM);

        Body pbody = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM, height / PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.075f;

        pbody.createFixture(fixDef);
        shape.dispose();

        return pbody;
    }

    private Body createHead(float x, float y, float radius)
    {
        Body pBody;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fixdef = new FixtureDef();
        fixdef.shape = shape;
        fixdef.density = 0.05f;

        pBody.createFixture(fixdef).setUserData(this);

        shape.dispose();
        return pBody;
    }

    public float getPositionX()
    {
        return car.getPosition().x * PPM;
    }

    public float getPositionY()
    {
        return car.getPosition().y * PPM;
    }

    public void move(int direction)
    {
        if(isAlive)
        {
            frontWheelJoint.enableMotor(true);
            int carSpeed = 50;
            frontWheelJoint.setMotorSpeed(-carSpeed * direction);

            rearWheelJoint.enableMotor(true);
            rearWheelJoint.setMotorSpeed(-carSpeed * direction);

            float rotationTorque = 0.2f;
            car.applyAngularImpulse(rotationTorque * direction, true);
        }
    }

    public void stop()
    {
        frontWheelJoint.enableMotor(false);
        rearWheelJoint.enableMotor(false);
        car.setAngularDamping(1.5f);
        car.applyTorque(0, true);
    }

    public void killIfDead()
    {
        if(isKindaDead && isAlive)
        {
            world.destroyJoint(seat);
            world.destroyJoint(distanceNeck);
            world.destroyJoint(neck);
            world.destroyJoint(belt);
            isAlive = false;
        }
    }
}
