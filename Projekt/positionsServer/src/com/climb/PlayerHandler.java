package com.climb;

import com.climb.utils.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerHandler extends Thread
{
    public Player player;
    public List<Player> players;

    private boolean shouldRun = true;

    public PlayerHandler(List<Player> players, Player player)
    {
        this.player = player;
        this.players = players;
    }

    @Override
    public void run()
    {
        while(shouldRun)
        {
            try
            {
                Frame data = new Frame((Frame) player.inputStream.readObject());
                player.frame.update(data);

                List<Frame> positions = new ArrayList<>();

                for(Player iterator: players)
                {
                    if(!iterator.frame.nickname.equals(player.frame.nickname))
                        positions.add(new Frame(iterator.frame));
                }
                player.outputStream.writeObject(positions);
            }
            catch (IOException e)
            {
                shouldRun = false;
                PositionServer.disconnectPlayer(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
