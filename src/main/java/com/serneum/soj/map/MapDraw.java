/***********************************************************************
 *Author: Chris Rees
 *Date: 4/27/09
 *File Name: MapDraw.java
 *Purpose: Draws the map as the player moves
***********************************************************************/

package com.serneum.soj.map;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class MapDraw
{
    private static Image background;
    private static boolean imageLoaded = false;

    public static void loadMap(String path)
    {
        background = getMapImage(path);
        ScreenCoord.getMapCoord(background);
    }

    private static Image getMapImage(String imageName)
    {
        imageLoaded = true;
        return new ImageIcon(imageName).getImage();
    }

    //Load the map and print it on the screen.
    //Allow for a scroll as coordinates are adjusted
    public static void paint(Graphics g)
    {
        if (imageLoaded)
        {
            ScreenCoord.getMapCoord(background);

            //Always makes the initial image start at 0,0
            if(ScreenCoord.screenMinX == 0 && ScreenCoord.screenMinY == 0)
            {
                g.drawImage(background, 0, 0, null);
            }
            //Draw the adjusted image
            else {
                g.drawImage(background, -1 * ScreenCoord.screenMinX,  -1 * ScreenCoord.screenMinY, null);
            }
        }

        else
        {
            loadMap("images/bg/bg1.png");
        }


    }
}