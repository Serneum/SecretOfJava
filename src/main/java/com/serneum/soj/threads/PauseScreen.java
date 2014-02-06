package com.serneum.soj.threads;

import java.awt.Graphics;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.core.InputManager;
import com.serneum.soj.map.ScreenManager;


public class PauseScreen implements Runnable
{
    private boolean paused = true;
    private InputManager inputManager;
    private Graphics g = ScreenManager.getGraphics();

    public void checkInput()
    {
        if (GameBase.pause.isPressed())
        {
            inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
            stop();
        }
    }

    public void stop()
    {
        paused = false;
    }


    @Override
    public void run()
    {
        inputManager = new InputManager(ScreenManager.getFullScreenWindow());
        inputManager.setDefaultCursor();

        Menu.r = Menu.gen.nextInt(5);

        while (paused)
        {
            Menu.paintComponent(g);
            checkInput();
        }
    }
}
