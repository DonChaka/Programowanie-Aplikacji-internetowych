package com.climb;

import com.climb.utils.Frame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PositionServer
{
    private static List<Player> players;
    private static List<PlayerHandler> threads;

    ServerSocket server;

    int port = 3000;

    public PositionServer()
    {
        try
        {
            this.server = new ServerSocket(port);
            System.out.println(server);
            players = new ArrayList<>();
            threads = new ArrayList<>();

            start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        while(true)
        {
            try
            {
                Socket socket = server.accept();
                Player player = new Player(socket);
                players.add(player);

                PlayerHandler thread = new PlayerHandler(players, player);
                threads.add(thread);
                threads.get(threads.size()-1).start();

                System.out.println("Connected");
                System.out.println("Clients size: " + players.size() + " threads size: " + threads.size());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void disconnectPlayer(PlayerHandler thread)
    {
        players.remove(thread.player);
        threads.remove(thread);

        System.out.println("Disconnected");
        System.out.println("Clients size: " + players.size() + " threads size: " + threads.size());
    }

    public static void main(String[] args)
    {
        new PositionServer();
    }
}
