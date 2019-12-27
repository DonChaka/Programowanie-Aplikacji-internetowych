package com.climb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.climb.managers.GameStateManager;
import com.climb.utils.InputHandler;

public class ChatState extends GameState
{
    private Stage stage;
    private Skin skin;

    private static InputHandler inputHandler;

    private TextArea message_field;

    public ScrollPane chat_scroll;
    public static Label chat_label;

    public ChatState(GameStateManager gsm)
    {
        super(gsm);
        skin = new Skin(Gdx.files.internal("shadeui/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table chatRoomTable = buildChatRoomTable();
        stage.addActor(chatRoomTable);

        if(inputHandler == null)
        {
            inputHandler = new InputHandler(this);
            inputHandler.start();
        }
    }

    public Table buildChatRoomTable()
    {
        Table table = new Table();
        table.setFillParent(true);

        chat_label = new Label("", skin);
        chat_label.setWrap(true);
        chat_label.setAlignment(Align.center);

        chat_scroll = new ScrollPane(chat_label, skin);
        chat_scroll.setFadeScrollBars(false);

        table.add(chat_scroll).width(300f).height(400f).colspan(2);

        message_field = new TextArea("", skin);
        message_field.setPrefRows(2);

        ScrollPane input_scroll = new ScrollPane(message_field, skin);
        input_scroll.setFadeScrollBars(false);

        table.row();
        table.add(input_scroll).width(300f).colspan(2);

        TextButton send_button = new TextButton("Send", skin);
        table.add(send_button).colspan(2);

        table.setVisible(true);

        send_button.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                    sendMess();
            }
        });

        return table;
    }

    @Override
    public void update(float delta)
    {
        stage.act(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            sendMess();

        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB))
            gsm.setState(GameStateManager.State.PLAY);
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

    public void sendMess()
    {
        String text = message_field.getText();
        text = text.trim();
        if(!text.equals("") && gsm.outputStream != null)
        {
            String message = gsm.nickname + ": " + text;
            try
            {
                gsm.outputStream.writeObject(message);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        message_field.setText("");
    }
}
