package com.climb.utils;

import java.io.Serializable;

public class Frame implements Serializable
{
    public String nickname = "";
    public float x = 0, y = 0, angle = 0;

    private static final long serialVersionUID = 2137213721372137210L;

    public Frame(String nickname, float x, float y, float angle)
    {
        this.nickname = nickname;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }


    public Frame()
    {
        nickname = "";
        x = 0;
        y = 0;
        angle = 0;
    }

    public Frame(Frame temp)
    {
        this.nickname = temp.nickname;
        this.angle = temp.angle;
        this.x = temp.x;
        this.y = temp.y;
    }

    public void update(Frame data)
    {
        if(data.nickname.equals(this.nickname))
        {
            this.x = data.x;
            this.y = data.y;
            this.angle = data.angle;
        }
    }
}