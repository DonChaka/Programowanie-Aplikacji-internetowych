package com.climb.utils;

import com.climb.states.ChatState;

import static com.climb.Application.gsm;

public class InputHandler extends Thread
{
    ChatState chat;
    public boolean running = true;

    public InputHandler(ChatState chat)
    {
        this.chat = chat;
    }

    @Override
    public synchronized void run()
    {
        try
        {
            while (running)
            {
                String inText = (String) gsm.inputStream.readObject();
                chat.chat_label.setText(inText + "\n" + chat.chat_label.getText());
            }
            System.out.println("Exit");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            chat.chat_label.setText("Error while connecting to chat server \nPlease restart the game and make sure the server is running");
            System.out.println("flaga x");
        }
    }
}
