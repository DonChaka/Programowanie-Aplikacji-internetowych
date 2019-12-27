package com.climb.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.climb.Application;
import com.climb.managers.GameStateManager;

public abstract class GameState
{
    // References
    protected GameStateManager gsm;
    protected Application app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm)
    {
        if(gsm == null)
            return;
        this.gsm = gsm;
        this.app = gsm.application();
        if(this.app == null)
            return;
        batch = app.getBatch();
        camera = app.getCamera();
    }

    public void resize(int w, int h)
    {
        camera.setToOrtho(false, w, h);
    }

    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

}
