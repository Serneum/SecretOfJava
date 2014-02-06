/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: Menu.java
 *Purpose: Creates the basic pause menu with filler text
***********************************************************************/

package com.serneum.soj.threads;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.map.ScreenManager;

public class Menu
{
    private static String[] text = new String[]{"This. Is. JAVA!!!",
                                            "This is your god speaking",
                                            "Reboot.",
                                            "Failsauce",
                                            "Win."};
    public static Random gen = new Random();
    public static int r;

    public static void paintComponent(Graphics g)
    {
        //Draw background
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);

        //Draw top bar
        g.setColor(Color.blue);
        g.drawRect(0, 0, 798, 100);
        g.fillRoundRect(5, 6, 788, 88, 25, 25);

        //Draw left bar
        g.drawRect(0, 102, 100, 494);
        g.fillRoundRect(5, 108, 90, 482, 25, 25);

        //Draw text
        g.setColor(Color.white);
        g.drawString(text[r], 350, 300);

        //Draw Player image/stats to the menu
        g.drawImage(GameBase.player.animation.getImage(), 10, 30, null);
        g.drawString("Name: " + GameBase.player.pName, 40, 35);
        g.drawString("HP: " + GameBase.player.currentHealth, 300, 35);
        g.drawString("Level: " + GameBase.player.level, 40, 75);
        g.drawString("EXP: " + GameBase.player.currentEXP, 300, 75);
        g.drawString("Floor: B" + GameBase.player.floor, 560, 35);

        ScreenManager.update();
    }
}
