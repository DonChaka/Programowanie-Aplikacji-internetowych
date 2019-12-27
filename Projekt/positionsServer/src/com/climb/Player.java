package com.climb;

import com.climb.utils.Frame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Player
{
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    Frame frame;

    public Player(Socket socket)
    {
        try
        {
            this.frame = new Frame();
            this.socket = socket;
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.frame.nickname =  (String) inputStream.readObject();
            System.out.println("Connected: " + this.frame.nickname);
            this.outputStream.writeObject(0);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}