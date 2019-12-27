package com.climb.utils;

import com.climb.states.PlayState;

import java.util.List;

import static com.climb.Application.gsm;


public class PositionsHandler extends Thread
{
    List<Frame> positions;

    public boolean running = true;

    public PositionsHandler(List<Frame> positions)
    {
        this.positions = positions;
    }

    @Override
    public void run()
    {
        try
        {
            while(running)
            {
                PlayState.others = (List<Frame>) gsm.posInput.readObject();
            }
        }
        catch (NullPointerException e)
        {
            System.out.println("Error while joining server. You can still play alone");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
