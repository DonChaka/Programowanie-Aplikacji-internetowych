package com.climb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.climb.managers.GameStateManager;
import com.climb.utils.Constants;

public class GameOverState extends GameState
{
    float acc = 0f;
    Texture tex;

    public GameOverState(GameStateManager gsm)
    {
        super(gsm);
        tex = new Texture("img/gameOver.png");
    }

    @Override
    public void update(float delta)
    {
        acc += delta;
        if(acc >= 1)
        {
            gsm.setState(GameStateManager.State.PLAY);
        }

        camera.setToOrtho(false, Gdx.graphics.getWidth() / Constants.SCALE, Gdx.graphics.getHeight() / Constants.SCALE);
    }

    @Override
    public void render()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

    }

    @Override
    public void dispose()
    {
        tex.dispose();
    }
}
