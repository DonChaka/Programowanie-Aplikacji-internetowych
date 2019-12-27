package com.climb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.climb.managers.GameStateManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class LoginState extends GameState
{
    private TextField name_field;

    Stage stage;
    Skin skin;

    int chatPort = 2137, posPort = 3000;

    String username;

    public LoginState(GameStateManager gsm)
    {
        super(gsm);

        skin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        try
        {
            gsm.chatSocket = new Socket("localhost", chatPort);
            gsm.outputStream = new ObjectOutputStream(gsm.chatSocket.getOutputStream());
            gsm.inputStream = new ObjectInputStream(gsm.chatSocket.getInputStream());
        }
        catch (ConnectException e)
        {
            System.out.println(chatPort);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Table loginTable = buildLoginTable();

        stage.addActor(loginTable);
    }

    private Table buildLoginTable(){
        final Table table = new Table();
        table.setFillParent(true);

        Window window = new Window("Login", skin);
        window.getTitleLabel().setAlignment(Align.center);

        TextButton join_button = new TextButton("Join", skin);
        name_field = new TextField("", skin);

        window.add(new Label("Enter Your Name", skin));
        window.row();
        window.add(name_field);
        window.row();
        window.add(join_button);

        table.add(window);

        join_button.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {

                username = name_field.getText();

                if(!username.isEmpty())
                {
                    gsm.nickname = username;

                    try
                    {
                        gsm.posSocket = new Socket("localhost", posPort);
                        gsm.posOutput = new ObjectOutputStream(gsm.posSocket.getOutputStream());
                        gsm.posInput = new ObjectInputStream(gsm.posSocket.getInputStream());
                        gsm.posOutput.writeObject(username);
                        System.out.println("Sent nickname");
                        gsm.worldSeed = (int) gsm.posInput.readObject();
                        System.out.println(gsm.worldSeed);
                    }
                    catch (ConnectException e)
                    {
                        System.out.println(posPort);
                    }
                    catch (IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                    gsm.setState(GameStateManager.State.PLAY);
                }
            }
        });

        return table;
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(.3f, .3f, .3f, .5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }
}
