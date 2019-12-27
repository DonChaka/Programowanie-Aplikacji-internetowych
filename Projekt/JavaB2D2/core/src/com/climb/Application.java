package com.climb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.climb.utils.Constants;
import com.climb.managers.GameStateManager;

public class Application extends ApplicationAdapter
{

	private OrthographicCamera camera;
	private SpriteBatch batch;

	public static GameStateManager gsm;

	@Override
	public void create ()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / Constants.SCALE, h / Constants.SCALE);

		gsm = new GameStateManager(this);
	}

	@Override
	public void render ()
	{
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height)
	{
		gsm.resize((int)(width/Constants.SCALE), (int)(height / Constants.SCALE));
	}

	@Override
	public void dispose ()
	{
		gsm.dispose();
		batch.dispose();
	}

	public OrthographicCamera getCamera()
	{
		return camera;
	}

	public SpriteBatch getBatch()
	{
		return batch;
	}
}