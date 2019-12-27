package com.climb.managers;

import com.climb.Application;
import com.climb.states.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Stack;

public class GameStateManager
{
    private final Application app;

    private Stack<GameState> states;

    public String nickname =  "default";

    public Socket chatSocket = null;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;

    public Socket posSocket;
    public ObjectOutputStream posOutput;
    public ObjectInputStream posInput;

    public int worldSeed = 1000;

    public enum State
    {
        SPLASH,
        PLAY,
        GAMEOVER,
        CHAT,
        LOGIN
    }

    public GameStateManager(final Application app)
    {
        this.app = app;
        this.states = new Stack<>();
        this.setState(State.LOGIN);
    }

    public Application application()
    {
        return app;
    }

    public void update(float delta)
    {
        states.peek().update(delta);
    }

    public void render()
    {
        states.peek().render();
    }

    public void dispose()
    {
        for(GameState gs : states)
        {
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h)
    {
        states.peek().resize(w, h);
    }

    public void setState(State state)
    {
        if(states.size() >= 1 && !(states.peek() instanceof PlayState))
            states.pop().dispose();
        if(states.size() >= 1 && state == State.PLAY);
        else
            states.push(getState(state));
    }

    private GameState getState(State state)
    {
        switch (state)
        {
            case SPLASH: return new SplashState(this);
            case PLAY: return new PlayState(this);
            case GAMEOVER: return new GameOverState(this);
            case CHAT: return new ChatState(this);
            case LOGIN: return new LoginState(this);
        }
        return null;
    }
}
