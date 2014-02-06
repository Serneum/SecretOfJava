/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: ScreenCoord.java
 *Purpose: Manages the two sets of coordinates inside the game and
 *allows for easy access of the player's coordinates.
***********************************************************************/

package com.serneum.soj.map;

import java.awt.Image;
import java.awt.Point;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Player;


public class ScreenCoord
{
    public static int mapMinX = 0, mapMaxX = 0, mapMinY = 0, mapMaxY = 0;
    public static int screenMinX, screenMaxX, screenMinY, screenMaxY;
    public static float pX, pZ;

    //Initializes the variables to the "max" size (max on screen)
    public static void init()
    {
        screenMaxX = GameBase.screenWidth;
        screenMaxY = GameBase.screenHeight;
        screenMinX = 0;
        screenMinY = 0;
    }

    //Sets the max coordinates on a per-map basis
    public static void getMapCoord(Image background)
    {
        mapMaxX = background.getWidth(null);
        mapMaxY = background.getHeight(null);
    }

    //Sets min/max X values (needs work)
    public static void setScreenX(int x)
    {
        screenMaxX -= x;
        screenMinX = screenMaxX - GameBase.screenWidth;
    }

    //Sets min/max Y values (needs work)
    public static void setScreenY(int y)
    {
        screenMaxY -= y;
        screenMinY = screenMaxY - GameBase.screenHeight;
    }

    public static void playerCoord(Player player)
    {
        pX = screenMinX + player.getX();
        pZ = screenMinY + player.getZ();
    }

    public static Point objectCoord(Point point)
    {
        return (new Point(screenMinX + point.x, screenMinY + point.y));
    }
}