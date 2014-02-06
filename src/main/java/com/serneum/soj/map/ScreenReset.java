/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: ScreenReset.java
 *Purpose: Allows the screen to change once the player hits an edge.
***********************************************************************/

package com.serneum.soj.map;

import com.serneum.soj.core.GameBase;
import com.serneum.soj.creatures.Player;
import com.serneum.soj.object.Walls;

public class ScreenReset
{
    private static MapDeterminer map = new MapDeterminer();
    public static int lastWall = 0;

    public static void xReset(Player player)
    {
        float x = 0;

        x = player.getX();

        //Remove old GameObjects
        while(GameBase.masterList.size() != 1)
        {
            for(int i = 1; i < GameBase.masterList.size(); i++)
            {
                GameBase.masterList.removeElementAt(i);
            }
             while(GameBase.creatureList.size() != 0)
             {
                 for(int i = 0; i < GameBase.creatureList.size(); i++)
                 {
                     GameBase.creatureList.removeElementAt(i);
                 }
             }
        }

        //sets player's X coordinate to the end of the screen/map
        //if moving left, and sets the view appropriately
        if(x <= 0)
        {
            x = GameBase.screenWidth;
            player.setX(x);
            ScreenCoord.screenMinX = ScreenCoord.mapMaxX - GameBase.screenWidth;
            ScreenCoord.screenMaxX = ScreenCoord.mapMaxX;
            lastWall = 0;
        }

        //sets player's X coordinate to the beginning of the screen/map
        //if moving right, and sets the view appropriately
        else
        {
            x = 0;
            player.setX(x);
            ScreenCoord.screenMinX = (int)x;
            ScreenCoord.screenMaxX = GameBase.screenWidth;
            lastWall = 2;
        }

        Walls.copy(lastWall);
        map.map();
    }

    public static void zReset(Player player)
    {
        float z = 0;

        z = player.getZ();

        //Remove old GameObjects
        while(GameBase.masterList.size() != 1)
        {
            for(int i = 1; i < GameBase.masterList.size(); i++)
            {
                GameBase.masterList.removeElementAt(i);
            }
             while(GameBase.creatureList.size() != 0)
             {
                 for(int i = 0; i < GameBase.creatureList.size(); i++)
                 {
                     GameBase.creatureList.removeElementAt(i);
                 }
             }
        }

        //sets player's Y coordinate to the end of the screen/map
        //if moving up, and sets the view appropriately
        if(z <= 0)
        {
            z = GameBase.screenHeight;
            player.setZ(z);
            ScreenCoord.screenMinY = ScreenCoord.mapMaxY - GameBase.screenHeight;
            ScreenCoord.screenMaxY = ScreenCoord.mapMaxY;
            lastWall = 1;
        }

        //sets player's Y coordinate to the beginning of the screen/map
        //if moving down, and sets the view appropriately
        else
        {
            z = 0;
            player.setZ(z);
            ScreenCoord.screenMinY = (int)z;
            ScreenCoord.screenMaxY = GameBase.screenHeight;
            lastWall = 3;
        }

        Walls.copy(lastWall);
        map.map();
    }

    public static void stairReset(Player player)
    {
        //Remove old GameObjects
        while(GameBase.masterList.size() != 1)
        {
            for(int i = 1; i < GameBase.masterList.size(); i++)
            {
                GameBase.masterList.removeElementAt(i);
            }
             while(GameBase.creatureList.size() != 0)
             {
                 for(int i = 0; i < GameBase.creatureList.size(); i++)
                 {
                     GameBase.creatureList.removeElementAt(i);
                 }
             }
        }

        //Places player in the middle of the map
        player.setX(ScreenCoord.screenMaxX / 2);
        player.setZ(ScreenCoord.screenMaxY / 2);
        player.mapX = (ScreenCoord.mapMaxX / 2);
        player.mapZ = (ScreenCoord.mapMaxY / 2);

        //Reset the min/max coords on screen to focus on the center of the map
        ScreenCoord.screenMinX = (int)((ScreenCoord.mapMaxX / 2) - (GameBase.screenWidth / 2));
        ScreenCoord.screenMaxX = (int)((ScreenCoord.mapMaxX / 2) + (GameBase.screenWidth / 2));
        ScreenCoord.screenMinY = (int)((ScreenCoord.mapMaxY / 2) - (GameBase.screenHeight / 2));
        ScreenCoord.screenMaxY = (int)((ScreenCoord.mapMaxY / 2) + (GameBase.screenHeight / 2));

        player.getLoc();

        map.map();
    }
}