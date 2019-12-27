package com.climb.managers;

import com.badlogic.gdx.physics.box2d.*;
import com.climb.objects.Car;
import com.climb.objects.TerrainContact;

public class ContactManager implements ContactListener
{

    @Override
    public void beginContact(Contact contact)
    {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        if(fb.getUserData() instanceof Car && fa.getUserData() instanceof TerrainContact)
        {
            Car car = (Car) fb.getUserData();
            car.isKindaDead = true;
        }
    }

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {

    }
}
