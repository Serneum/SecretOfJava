package com.serneum.soj.threads;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.serneum.soj.core.GameAction;
import com.serneum.soj.core.InputManager;
import com.serneum.soj.map.ScreenManager;


public class IntroScreen implements Runnable
{
    private static boolean paused = true;
    private InputManager inputManager;
    private static GameAction enter;

    private Graphics g = ScreenManager.getGraphics();

    public static void checkInput()
    {
        if (enter.isPressed()) {
            stop();
        }
    }

    public static void stop()
    {
        paused = false;
    }


    @Override
    public void run()
    {
        enter = new GameAction("enter");

        inputManager = new InputManager(ScreenManager.getFullScreenWindow());
        inputManager.mapToKey(enter, KeyEvent.VK_ENTER);

        while (paused)
        {
            paint(g);
            checkInput();
        }
    }

    public static void paint(Graphics g)
    {
        try
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 600);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 72));
            g.setColor(Color.white);
            g.drawString("Secret of Java", 200, 100);

            ScreenManager.update();
            Thread.sleep(375);
            (new Thread(new Flash())).start();
            Thread.sleep(375);
            g.dispose();
        }
        catch(InterruptedException IE)
        {
        }
    }


}
