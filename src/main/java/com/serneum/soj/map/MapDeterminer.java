/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: MapDeterminer.java
 *Purpose: Sets all the randomizations for the maps, including boxes,
 *rabites, and whether or not the ManaBeast appears.
***********************************************************************/

package com.serneum.soj.map;

import java.util.Random;

import com.serneum.soj.creatures.ManaBeast;
import com.serneum.soj.creatures.Rabite;
import com.serneum.soj.object.GameObject;
import com.serneum.soj.object.Walls;

public class MapDeterminer
{
    private Random gen = new Random();

    public void map()
    {
        //Gather and send info to redraw
        int r = gen.nextInt(2) + 1;
        MapDraw.loadMap("images/bg/bg" + r + ".png");
        Walls.setImage(r);
        Walls.count = 0;
        Walls.ranOnce = false;

        //Draw mobs on screen
        r = gen.nextInt(15) + 1;
        Rabite[] rab = new Rabite[r];

        int count = 0;
        while(count < r)
        {
            int X = gen.nextInt(ScreenCoord.mapMaxX) + 1;

            if(X > ScreenCoord.mapMaxX - 50) {
                X = ScreenCoord.mapMaxX - 50;
            }
            if(X < ScreenCoord.mapMinX + 50) {
                X = ScreenCoord.mapMaxX + 50;
            }

            int Y = gen.nextInt(ScreenCoord.mapMaxY ) + 1;

            if(Y > ScreenCoord.mapMaxY - 50) {
                Y = ScreenCoord.mapMaxY - 50;
            }
            if(Y < ScreenCoord.mapMinY + 50) {
                Y = ScreenCoord.mapMaxY + 50;
            }

            rab[count] = new Rabite("Rabite", X, Y, 25, 25, 20, 1);
            count++;
        }

        r = gen.nextInt(100) + 1;
        if(r > 75)
        {
            new ManaBeast("ManaBeast", ScreenCoord.mapMaxX - 110, (ScreenCoord.mapMaxY / 2) - 64, 128, 128, 128, 1);
        }

        //Draw boxes on screen
        r = gen.nextInt(5) + 1;
        GameObject[] box = new GameObject[r];
        count = 0;
        while(count < r)
        {
            int X = gen.nextInt(ScreenCoord.mapMaxX) + 1;

            if(X > ScreenCoord.mapMaxX - 50) {
                X = ScreenCoord.mapMaxX - 50;
            }
            if(X < ScreenCoord.mapMinX + 50) {
                X = ScreenCoord.mapMaxX + 50;
            }

            int Y = gen.nextInt(ScreenCoord.mapMaxY ) + 1;

            if(Y > ScreenCoord.mapMaxY - 50) {
                Y = ScreenCoord.mapMaxY - 50;
            }
            if(Y < ScreenCoord.mapMinY + 50) {
                Y = ScreenCoord.mapMaxY + 50;
            }

            box[count] = new GameObject("Box", X, Y, 28, 12, 24);
            count++;
        }
    }
}